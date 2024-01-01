package org.softauto.discovery;


import org.softauto.discovery.plugin.api.Provider;
import org.softauto.discovery.plugin.spi.PluginProvider;

public class PluginJaxrs implements PluginProvider {



    @Override
    public Provider create(Object...objs) {
        return new PluginJaxrsImpl();
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
        return "JAXRS";
    }

    @Override
    public String getType() {
        return "discovery";
    }


}
