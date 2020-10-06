package com.ljheee.plugin.core;

import java.io.Serializable;
import java.util.List;

/**
 * 浏览器打开站点，展示该站点有插件信息，就是解析这个json对象，然后在页面展示列表。
 *
 */
public class PluginSite implements Serializable{

    String name;//站点名称
    List<PluginConfig> plugins;//站点 拥有的插件

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
}
