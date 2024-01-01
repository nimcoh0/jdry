package org.softauto.discovery;


import org.softauto.discovery.plugin.api.Provider;
import org.softauto.discovery.plugin.spi.PluginProvider;

public class PluginWebSpring implements PluginProvider {



    @Override
    public Provider create(Object...objs) {
        return new PluginWebSpringImpl();
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
        return "WEB-SPRING";
    }

    @Override
    public String getType() {
        return "discovery";
    }


}
