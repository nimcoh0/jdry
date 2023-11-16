package org.softauto.grpc.client;



import org.softauto.plugin.api.Provider;
import org.softauto.plugin.spi.PluginProvider;


public class RpcProvider implements PluginProvider {


    @Override
    public Provider create() {
       return RpcProviderImpl.getInstance();
    }

    @Override
    public String getVendor() {
        return "Jdry Software";
    }

    @Override
    public String getName() {
        return "RPC";
    }


    @Override
    public String getVersion() {
        return "beta 1.0";
    }


}
