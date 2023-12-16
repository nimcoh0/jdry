package org.softauto.analyzer.core.system.plugin.spi;


import org.softauto.analyzer.core.system.plugin.api.Provider;

public interface PluginProvider {

    /**
     * load the plugin impl class
     * @return
     */
    Provider create(Object...obj);

    String getProtocol();

    /**
     * the name of this plugin provider
     * @return
     */
    String getVendor();

    /**
     * the version of this plugin provider
     * @return
     */
    String getVersion();

    /**
     * the name of this plugin name
     * @return
     */
    String getName();

    String getType();

    String getPath();

}
