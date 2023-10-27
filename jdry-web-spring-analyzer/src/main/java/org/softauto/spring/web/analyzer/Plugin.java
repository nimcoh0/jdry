package org.softauto.spring.web.analyzer;

import org.softauto.analyzer.core.system.plugin.api.Provider;
import org.softauto.analyzer.core.system.plugin.spi.PluginProvider;

public class Plugin implements PluginProvider {



    @Override
    public Provider create(Object...objs) {
        return new PluginFactory(objs);
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
        return "protocol";
    }

    @Override
    public String getPath() {
        return "org.softauto.jaxrs.annotations";
    }

    @Override
    public String getProtocol(){
        return "JAXRS";
    }
}
