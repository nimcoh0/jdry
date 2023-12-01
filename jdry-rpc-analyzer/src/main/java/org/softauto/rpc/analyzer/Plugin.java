package org.softauto.rpc.analyzer;

import org.softauto.analyzer.core.system.plugin.api.Provider;
import org.softauto.analyzer.core.system.plugin.spi.PluginProvider;

public class Plugin implements PluginProvider {


    @Override
    public Provider create(Object...objs) {
        return new PluginImpl();
    }

    @Override
    public String getVendor() {
        return null;
    }

    @Override
    public String getVersion() {
        return null;
    }

    @Override
    public String getName() {
        return "RPC";
    }

    @Override
    public String getType() {
        return "protocol";
    }

    @Override
    public String getPath() {
        return null;
    }

    @Override
    public String getProtocol(){
        return "RPC";
    }
}
