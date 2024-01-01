package org.softauto.discovery;


import org.softauto.discovery.plugin.api.Provider;
import org.softauto.discovery.plugin.spi.PluginProvider;

public class PluginRpc implements PluginProvider {



    @Override
    public Provider create(Object...objs) {
        return new PluginRpcImpl();
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
        return "discovery";
    }


}
