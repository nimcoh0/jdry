package org.softauto.analyzer.base;

import org.softauto.analyzer.core.system.plugin.api.Provider;
import org.softauto.analyzer.core.system.plugin.spi.PluginProvider;


public class Base implements PluginProvider {
    @Override
    public Provider create(Object... obj) {
        return new BaseImpl();
    }

    @Override
    public String getProtocol() {
        return null;
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
        return "Base";
    }

    @Override
    public String getType() {
        return "analyzer";
    }

    @Override
    public String getPath() {
        return null;
    }
}
