package org.softauto.plugin;


import org.apache.logging.log4j.Marker;
import org.apache.logging.log4j.MarkerManager;
import org.softauto.plugin.api.Provider;
import org.softauto.plugin.spi.PluginProvider;
import java.util.*;

/**
 * manage plugins
 */
public class ProviderManager {

    private static org.apache.logging.log4j.Logger logger = org.apache.logging.log4j.LogManager.getLogger(ProviderManager.class);

    private static final Marker JDRY = MarkerManager.getMarker("JDRY");


    /**
     * get all PluginProvider's
     * @return List<PluginProvider>
     */
    public static List<org.softauto.plugin.spi.PluginProvider> providers() {
        List<PluginProvider> services = new ArrayList<>();
        try {
            ServiceLoader<PluginProvider> loader = ServiceLoader.load(PluginProvider.class, ProviderManager.class.getClassLoader());
            loader.forEach(provider -> {
                    services.add(provider);
                    logger.debug(JDRY,"found plugin " + provider.getName());
            });
            logger.debug(JDRY,"found " + services.size() + " plugins");
        }catch (Exception e){
            logger.error(JDRY,"fail get providers ",e);
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
                if (providerName.equals(provider.getName())) {
                    logger.debug(JDRY,"found plugin " + provider.getName());
                    return provider;
                }
            }
            logger.warn(JDRY,"provider " + providerName + " not found");
        }catch (Exception e){
            logger.error(JDRY,"provider " + providerName + " not found",e);
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
                    providers.add(p.create());
            }
            logger.debug(JDRY,"found " + providers.size() + " providers " + Arrays.toString(providers.toArray()));
        }catch (Exception e){
            logger.error(JDRY,"fail getting provider list ",e);
        }
        return providers;
    }





}
