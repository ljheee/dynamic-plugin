package com.ljheee.plugin.core;

import java.util.Date;
import java.util.List;

/**
 * 插件 本地存储
 * PluginConfigs.dat
 */
public class PluginStore {
    String name;
    List<PluginConfig> plugins;
    Date lastModify;


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<PluginConfig> getPlugins() {
        return plugins;
    }

    public void setPlugins(List<PluginConfig> plugins) {
        this.plugins = plugins;
    }

    public Date getLastModify() {
        return lastModify;
    }

    public void setLastModify(Date lastModify) {
        this.lastModify = lastModify;
    }
}
