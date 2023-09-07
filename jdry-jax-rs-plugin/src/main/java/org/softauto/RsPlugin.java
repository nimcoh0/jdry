package org.softauto;



import org.softauto.plugin.api.Provider;
import org.softauto.plugin.spi.PluginProvider;


public class RsPlugin implements PluginProvider {


    @Override
    public Provider create() {
       return new RsPluginImpl();
    }

    @Override
    public String getVendor() {
        return null;
    }

    @Override
    public String getName() {
        return "JAX-RS";
    }

    @Override
    public String getVersion() {
        return "1.0";
    }

}
