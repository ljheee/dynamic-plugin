package com.ljheee.plugin.core;

import java.util.List;

/**
 * 基于spring容器的创建工厂
 */
public interface SpringPluginFactory {


    void installPlugin(PluginConfig config, Boolean active);

    void activePlugin(String pluginId);

    void disablePlugin(String pluginId);

    void unstallPlugin(String pluginId);


    List<PluginConfig> getInstalledPlugins();


}
