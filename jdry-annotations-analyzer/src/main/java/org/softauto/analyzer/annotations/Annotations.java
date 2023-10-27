package org.softauto.analyzer.annotations;

import org.softauto.analyzer.core.system.plugin.api.Provider;
import org.softauto.analyzer.core.system.plugin.spi.PluginProvider;

public class Annotations implements PluginProvider {
    @Override
    public Provider create(Object... obj) {
        return new AnnotationsImpl();
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
        return "Annotations";
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
