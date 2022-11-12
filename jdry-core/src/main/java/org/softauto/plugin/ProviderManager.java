package org.softauto.plugin;


import org.softauto.plugin.api.Provider;
import org.softauto.plugin.spi.PluginProvider;
import org.softauto.plugin.spi.PluginTypes;
import java.util.*;

/**
 * manage plugins
 */
public class ProviderManager {

    private static org.apache.logging.log4j.Logger logger = org.apache.logging.log4j.LogManager.getLogger(ProviderManager.class);


    /**
     * get all PluginProvider's
     * @return List<PluginProvider>
     */
    public static List<org.softauto.plugin.spi.PluginProvider> providers() {
        List<PluginProvider> services = new ArrayList<>();
        try {
            ServiceLoader<PluginProvider> loader = ServiceLoader.load(PluginProvider.class, ProviderManager.class.getClassLoader());
            loader.forEach(provider -> {
                if(provider.getType().equals(PluginTypes.regular)) {
                    services.add(provider);
                    logger.debug("found plugin " + provider.getName());
                }
            });
            logger.debug("found " + services.size() + " plugins");
        }catch (Exception e){
            logger.error("fail get providers ",e);
        }
        return services;
    }

    public static List<PluginProvider> providers(ProviderScope scope) {
        List<PluginProvider> services = new ArrayList<>();
        try {
            ServiceLoader<PluginProvider> loader = ServiceLoader.load(PluginProvider.class, ProviderManager.class.getClassLoader());
            loader.forEach(provider -> {
                if(provider.getType().equals(PluginTypes.regular)) {
                    if(provider.scope().equals(scope)) {
                        services.add(provider);
                        logger.debug("found plugin " + provider.getName());
                    }
                }
            });
            logger.debug("found " + services.size() + " plugins");
        }catch (Exception e){
            logger.error("fail get providers ",e);
        }
        return services;
    }

    /**
     * get  PluginProvider
     * @param providerName
     * @return PluginProvider or null if not found
     * @throws Exception
     */
    public static PluginProvider provider(String providerName)  {
       try {
            ServiceLoader<PluginProvider> loader = ServiceLoader.load(PluginProvider.class);
            Iterator<PluginProvider> it = loader.iterator();
            while (it.hasNext()) {
                PluginProvider provider = it.next();
                if (providerName.equals(provider.create().getType())) {
                    logger.debug("found plugin " + provider.getName());
                    return provider;
                }
            }
            logger.warn("provider " + providerName + " not found");
        }catch (Exception e){
            logger.error("provider " + providerName + " not found",e);
        }
        return null;
    }


    /**
     * get all Providers;
     * @return List<Provider>
     */
    public static List<Provider> getProviders(){
        List<Provider> providers = new ArrayList<>();
        try {
            List<PluginProvider> pluginProviders = providers();
            for (PluginProvider p : pluginProviders) {
                if(p.getType().equals(PluginTypes.regular)) {
                    providers.add(p.create());
                }
            }
            logger.debug("found " + providers.size() + " providers " + Arrays.toString(providers.toArray()));
        }catch (Exception e){
            logger.error("fail getting provider list ",e);
        }
        return providers;
    }

    /**
     * get Provider by type
     * @param type
     * @return provider
     */
    public static Provider getProvider(String type){
        try {
            List<PluginProvider> pluginProviders = providers();
            for (PluginProvider p : pluginProviders) {
                if(p.create().getType().equals(type))
                    logger.debug("found " + p.getName() + " provider " );
                    return p.create();
            }

        }catch (Exception e){
            logger.error("fail getting provider  type "+type,e);
        }
        return null;
    }


    public static List<Provider> getExtendedProviders(){
        List<Provider> providers = new ArrayList<>();
        try {
            List<PluginProvider> pluginProviders = providers();
            for (PluginProvider p : pluginProviders) {
                if(p.getType().equals(PluginTypes.extended)) {
                    providers.add(p.create());
                }
            }
            logger.debug("found " + providers.size() + " extended providers " + Arrays.toString(providers.toArray()));
        }catch (Exception e){
            logger.error("fail getting extended provider list ",e);
        }
        return providers;
    }
}
