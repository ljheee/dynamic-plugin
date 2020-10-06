package com.ljheee.test;


import com.alibaba.fastjson.JSON;
import com.ljheee.plugin.core.DefaultPluginFactory;
import com.ljheee.plugin.core.PluginConfig;
import com.ljheee.plugin.core.PluginSite;
import com.ljheee.plugin.core.SpringPluginFactory;
import com.ljheee.test.service.UserService;
import org.aopalliance.aop.Advice;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.springframework.aop.framework.Advised;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;


public class DefaultSpringPluginFactoryTest {
    private DefaultPluginFactory factory;

    @Before
    public void init() {
        factory = new DefaultPluginFactory();
    }

    // 构建通知测试
    @Test
    public void testBuildAdvice() throws Exception {
        PluginConfig config = new PluginConfig();
        config.setActive(true);
        config.setId("1");
        config.setJаrRеmоtеUrl(
                "file:/Users/lijianhua/Documents/IdeaProjects/dynamic-plugin/plugins/dynamic-plugin.jar");
        config.setClassName("com.ljheee.test.RequestLogPlugin");
        config.setName("服务参数日志打印");
        Advice advice = factory.buildAdvice(config);
        Assert.assertNotNull(advice);
    }

    // 本地安装测试
    // 1 下载jar包
    // 2 下载配置
    @Test
    public void testIntall() {
        PluginConfig config = new PluginConfig();
        config.setActive(false);
        config.setId("3");
        config.setVersion("V1.0.0");
        config.setJаrRеmоtеUrl(
                "file:/Users/lijianhua/Documents/IdeaProjects/dynamic-plugin/plugins/dynamic-plugin.jar");
        config.setClassName("com.ljheee.test.RequestLogPlugin");
        config.setName("服务参数日志打印");
        factory.installPlugin(config, false);
        System.out.println(factory.getInstalledPlugins());
        Map<String, List<PluginConfig>> map = factory.getInstalledPlugins().stream().collect(Collectors.groupingBy(PluginConfig::getId));

        List<PluginConfig> pluginConfigs = map.get("3");
        Assert.assertTrue(pluginConfigs.size() > 0);
    }

    // 卸载插件
    @Test
    public void uninstallTest() {
        ClassPathXmlApplicationContext context = new ClassPathXmlApplicationContext("/spring-aop.xml");
        context.start();
        UserService service = context.getBean(UserService.class);
        service.addUser("hmm");
        SpringPluginFactory pluginFactory = context.getBean(SpringPluginFactory.class);
        pluginFactory.unstallPlugin("2");
    }

    public void getPluginList() {
        factory.getInstalledPlugins();
    }

    // 启动时装载本地插件
    @Test
    public void loaderLocalTest() {
        ClassPathXmlApplicationContext context = new ClassPathXmlApplicationContext("/spring-aop.xml");
        context.start();
        UserService service = context.getBean(UserService.class);
        service.addUser("hmm");
    }

    // 动态激活插件测试
    @Test
    public void activeTest() {
        ClassPathXmlApplicationContext context = new ClassPathXmlApplicationContext("/spring-aop.xml");
        context.start();
        SpringPluginFactory pluginFactory = context.getBean(SpringPluginFactory.class);
        UserService service = context.getBean(UserService.class);
        service.addUser("hmm");
        pluginFactory.activePlugin("3");
        service.addUser("hmm");
    }

    @Test
    public void disableTest() {
        ClassPathXmlApplicationContext context = new ClassPathXmlApplicationContext("/spring-aop.xml");
        context.start();
        SpringPluginFactory pluginFactory = context.getBean(SpringPluginFactory.class);
        UserService service = context.getBean(UserService.class);
        service.addUser("hmm");
        pluginFactory.disablePlugin("3");
        service.addUser("hmm");
    }

    @Test
    public void buildSiteTest() {
        PluginSite site = new PluginSite();
        site.setName("我的插件小仓库");
        List<PluginConfig> configs = new ArrayList<>();
        {
            PluginConfig config = new PluginConfig();
            config.setActive(true);
            config.setId("1");
            config.setJаrRеmоtеUrl(
                    "file:/Users/lijianhua/Documents/IdeaProjects/dynamic-plugin/plugins/dynamic-plugin.jar");
            config.setClassName("com.ljheee.plugin.sample.RequestLogPlugin");
            config.setName("服务参数日志打印");
            configs.add(config);
        }
        {
            PluginConfig config = new PluginConfig();
            config.setActive(true);
            config.setId("2");
            config.setJаrRеmоtеUrl(
                    "file:/Users/lijianhua/Documents/IdeaProjects/dynamic-plugin/plugins/dynamic-plugin.jar");
            config.setClassName("com.ljheee.plugin.sample.CountingPlugin");
            config.setName("方法执行统计");
            configs.add(config);
        }
        site.setPlugins(configs);
        String json = JSON.toJSONString(site);
        System.out.println(json);
    }

    // 动态添加通知
    @Test
    public void addAdviceTest() {
        ClassPathXmlApplicationContext context = new ClassPathXmlApplicationContext("/spring-aop.xml");
        context.start();
        UserService userService = context.getBean(UserService.class);
        Advised advised = (Advised) userService;
        CountingBeforeAdvice counting = new CountingBeforeAdvice();
        advised.addAdvice(counting);

        userService.getUser();
        userService.getUser();
        userService.getUser();
        advised.removeAdvice(counting);
        userService.getUser();
    }


}
