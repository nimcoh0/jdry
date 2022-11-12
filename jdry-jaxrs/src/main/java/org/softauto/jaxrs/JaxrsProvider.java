package org.softauto.jaxrs;



import org.softauto.plugin.ProviderScope;
import org.softauto.plugin.api.Provider;
import org.softauto.plugin.spi.PluginProvider;
import org.softauto.plugin.spi.PluginTypes;

public class JaxrsProvider implements PluginProvider {


    @Override
    public Provider create() {
       return JaxrsProviderImpl.getInstance();
    }

    @Override
    public String getVendor() {
        return null;
    }

    @Override
    public String getName() {
        return "JAXRS";
    }

    @Override
    public String getVersion() {
        return "1.0";
    }

    @Override
    public PluginTypes getType() {
        return PluginTypes.regular;
    }

    @Override
    public ProviderScope scope() {
        return ProviderScope.Tester;
    }
}
