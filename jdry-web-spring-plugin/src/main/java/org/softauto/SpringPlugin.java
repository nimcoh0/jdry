package org.softauto;


import org.softauto.plugin.api.Provider;
import org.softauto.plugin.spi.PluginProvider;

public class SpringPlugin implements PluginProvider {


    @Override
    public Provider create() {
       return new SpringPluginImpl();
    }


    @Override
    public String getVendor() {
        return null;
    }

    @Override
    public String getName() {
        return "SPRING-WEB";
    }


    @Override
    public String getVersion() {
        return "1.0";
    }


}
