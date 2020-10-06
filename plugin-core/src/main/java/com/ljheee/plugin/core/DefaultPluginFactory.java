package com.ljheee.plugin.core;

import com.alibaba.fastjson.JSON;
import org.aopalliance.aop.Advice;
import org.springframework.aop.Advisor;
import org.springframework.aop.framework.Advised;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

import java.io.*;
import java.lang.reflect.Method;
import java.net.URL;
import java.net.URLClassLoader;
import java.nio.file.Files;
import java.util.*;

import static org.springframework.util.StreamUtils.copy;


/**
 * 插件工厂实现类
 */
@Component
public class DefaultPluginFactory implements SpringPluginFactory, ApplicationContextAware, InitializingBean {


    private ApplicationContext applicationContext;
    private Map<String, PluginConfig> configs = new HashMap<>();
    private Map<String, Advice> adviceCache = new HashMap<>();

    private static final String BASE_DIR;

    static {
        BASE_DIR = System.getProperty("user.home") + "/.plugins/";
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }

    /**
     * spring 容器启动时，读取配置文件，加载所有插件信息
     *
     * @throws Exception
     */
    @Override
    public void afterPropertiesSet() throws Exception {
        loaderLocals();
    }

    /**
     * 装载本地已安装插件
     *
     * @throws Exception
     */
    private void loaderLocals() throws Exception {

        Map<String, PluginConfig> localConfig = readerLocalConfigs();
        if (localConfig == null) {
            return;
        }
        configs.putAll(localConfig);
        for (PluginConfig config : localConfig.values()) {
            if (config.getActive()) {
                activePlugin(config.getId());
            }
        }
    }

    /**
     * 读取本地配置
     *
     * @return
     * @throws Exception
     */
    private Map<String, PluginConfig> readerLocalConfigs() throws Exception {
        String baseDir = BASE_DIR;
        File configFile = new File(baseDir + "PluginConfigs.dat");
        if (!configFile.exists()) {
            return new HashMap<>();
        }
        InputStream in = new FileInputStream(configFile);
        Map<String, PluginConfig> result = new HashMap<>();

        try {
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            copy(in, out);
            PluginStore store = JSON.parseObject(out.toString("UTF-8"), PluginStore.class);
            for (PluginConfig config : store.getPlugins()) {
                result.put(config.getId(), config);
            }
        } finally {
            in.close();
        }

        return result;
    }


    /**
     * 安装插件
     *
     * @param plugin
     * @param active
     */
    @Override
    public void installPlugin(PluginConfig plugin, Boolean active) {

        if (configs.containsKey(plugin.getId())) {
            throw new RuntimeException(String.format("已存在指定的插件,id=%s", plugin.getId()));
        }
        //填充至插件库
        configs.put(plugin.getId(), plugin);

        //下载远程插件
        try {
            buildAdvice(plugin);
        } catch (Exception e) {
            configs.remove(plugin.getId());
            throw new RuntimeException(String.format("插件枸建失敗id=%s", plugin.getId()));
        }

        //持久化至本地庠
        try {
            storeConfigs();
        } catch (IOException e) {
            configs.remove(plugin.getId());
            throw new RuntimeException(String.format("插件安装失敗id=%s", plugin.getId()));
        }

        if (active != null && active) {
            //激活当前插件
            activePlugin(plugin.getId());
        }

    }

    /**
     * 激活插件
     * 1、获取plugin配置
     * 2、获取spring ioc当中所有的bean
     * 3、安装条件验证
     * 4、构建切面通知对象
     * 5、添加至aop切面
     *
     * @param pluginId
     */
    @Override
    public void activePlugin(String pluginId) {

        if (!configs.containsKey(pluginId)) {
            throw new RuntimeException(String.format("指定插件不存在id=%s", pluginId));
        }

        PluginConfig config = configs.get(pluginId);

        // 扫描，controller service都会被扫描出来
        for (String name : applicationContext.getBeanDefinitionNames()) {
            Object bean = applicationContext.getBean(name);
            if (bean == this)
                continue;

            // 是否是切面
            // bean instanceof Advised的目的就是判断是否已经是被动态代理的类
            // applicationContext.getBeanNamesForType(Advised.class)
            if (!(bean instanceof Advised))
                continue;

            //是否重复安装
            if (findAdvice(config.getClassName(), (Advised) bean) != null)
                continue;

            Advice advice = null;
            try {
                advice = buildAdvice(config);//实例化插件
                ((Advised) bean).addAdvice(advice);// 动态增加aop 通知
            } catch (Exception e) {
                throw new RuntimeException("安装失敗", e);
            }

            try {
                config.setActive(true);
                storeConfigs();
            } catch (IOException e) {
                // TODO需要回滾己添加的切面
                throw new RuntimeException("激活失敗", e);
            }
        }
    }

    //保存配置至本地
    // TODO序列化方式改为json更为合理
    private void storeConfigs() throws IOException {

        String baseDir = BASE_DIR;
        File configFile = new File(baseDir + "PluginConfigs.dat");
        if (!configFile.exists()) {
            configFile.getParentFile().mkdirs();
            configFile.createNewFile();
        }

        PluginStore store = new PluginStore();
        store.setPlugins(new ArrayList<>(configs.values()));
        store.setLastModify(new Date());
        OutputStream out = new FileOutputStream(configFile);
        String jsonString = JSON.toJSONString(store, true);
        out.write(jsonString.getBytes("UTF-8"));
        out.flush();
        out.close();
    }

    /**
     * 实例化插件 (将插件jar实例化成Advice，前提是它实现了Advice接口)
     * @param config
     * @return
     * @throws Exception
     */
    public Advice buildAdvice(PluginConfig config) throws Exception {
        if (adviceCache.containsKey(config.getClassName())) {
            return adviceCache.get(config.getClassName());
        }

        // 先从本地获取
        File jarFile = new File(getLocalJarFile(config));

        //从远程下载
        if (!jarFile.exists()) {
            URL url = new URL(config.getJаrRеmоtеUrl());
            InputStream stream = url.openStream();
            jarFile.getParentFile().mkdirs();
            try {
                Files.copy(stream, jarFile.toPath());
            } catch (Exception e) {
                jarFile.deleteOnExit();
                throw new RuntimeException(e);
            }
            stream.close();
        }

        // 插件的jar是否 已被classloader加载
        URLClassLoader loader = (URLClassLoader) getClass().getClassLoader();
        URL targetUrl = jarFile.toURI().toURL();
        boolean isLoader = false;
        for (URL url : loader.getURLs()) {
            if (url.equals(targetUrl)) {
                isLoader = true;
                break;
            }
        }

        if (!isLoader) {
            Method add = URLClassLoader.class.getDeclaredMethod("addURL", new Class[]{URL.class});
            add.setAccessible(true);
            add.invoke(loader, targetUrl);
        }

        // 初始化 Plugin Advice 实例
        Class<?> adviceClass = loader.loadClass(config.getClassName());
        if (!Advice.class.isAssignableFrom(adviceClass)) {
            throw new RuntimeException(
                    String.format("plugin 配置错误 %s非Advice的实现类 ", config.getClassName()));

        }

        adviceCache.put(adviceClass.getName(), (Advice) adviceClass.newInstance());
        return adviceCache.get(adviceClass.getName());
    }

    private Advice findAdvice(String className, Advised advised) {
        for (Advisor a : advised.getAdvisors()) {
            if (a.getAdvice().getClass().getName().equals(className)) {
                return a.getAdvice();
            }
        }
        return null;
    }

    private String getLocalJarFile(PluginConfig config) {
        String jarName = config.getJаrRеmоtеUrl().substring(config.getJаrRеmоtеUrl().lastIndexOf("/"));
        return BASE_DIR + jarName;
    }


    @Override
    public void disablePlugin(String pluginId) {
        PluginConfig config = configs.get(pluginId);
        if (config == null) {
            throw new RuntimeException(String.format("指定插件不存在 id=%s", pluginId));
        }

        for (String name : applicationContext.getBeanDefinitionNames()) {
            Object bean = applicationContext.getBean(name);
            if (bean instanceof Advised) {
                Advice advice = findAdvice(config.getClassName(), (Advised) bean);
                if (advice != null)
                    ((Advised) bean).removeAdvice(advice);
            }
        }
        config.setActive(false);
        try {
            storeConfigs();
        } catch (IOException e) {
            // TODO 保存失败需要回滚
            throw new RuntimeException("禁用失败", e);
        }

    }

    @Override
    public void unstallPlugin(String pluginId) {

        disablePlugin(pluginId); //禁用指定插件
        configs.remove(pluginId); //缓存中删除
        try {
            storeConfigs();//更新配置
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void doBefore() {
        // do nothing, just to being aop
    }

    @Override
    public List<PluginConfig> getInstalledPlugins() {
        try {
            return new ArrayList<>(readerLocalConfigs().values());
        } catch (Exception e) {
            throw new RuntimeException("插件获取失败", e);
        }
    }
}
