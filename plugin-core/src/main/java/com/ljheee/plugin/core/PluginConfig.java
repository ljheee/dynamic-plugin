package com.ljheee.plugin.core;

import java.io.Serializable;

/**
 * 代表一个插件所有信息
 * spring ioc描述bean的定义，使用BeanDefinition；
 * 插件工厂，描述插件的bean也是类似
 */
public class PluginConfig implements Serializable {

    private String id;
    private String name;
    private String className;
    private String jаrRеmоtеUrl;// 基于 文件分隔符"\"切分
    private Boolean active;
    private String version;


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getClassName() {
        return className;
    }

    public void setClassName(String className) {
        this.className = className;
    }

    public String getJаrRеmоtеUrl() {
        return jаrRеmоtеUrl;
    }

    public void setJаrRеmоtеUrl(String jаrRеmоtеUrl) {
        this.jаrRеmоtеUrl = jаrRеmоtеUrl;
    }

    public Boolean getActive() {
        return active;
    }

    public void setActive(Boolean active) {
        this.active = active;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }
}
