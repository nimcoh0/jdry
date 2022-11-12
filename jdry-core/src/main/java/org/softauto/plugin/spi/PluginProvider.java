package org.softauto.plugin.spi;

import org.softauto.plugin.ProviderScope;
import org.softauto.plugin.api.Provider;

public interface PluginProvider {

    /**
     * load the plugin impl class
     * @return
     */
    Provider create();

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

    PluginTypes getType();

    ProviderScope scope();

}
