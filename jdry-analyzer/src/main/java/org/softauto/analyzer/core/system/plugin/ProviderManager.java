package org.softauto.analyzer.core.system.plugin;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.Marker;
import org.apache.logging.log4j.MarkerManager;
import org.softauto.analyzer.core.system.plugin.api.Provider;
import org.softauto.analyzer.core.system.plugin.spi.PluginProvider;

import java.util.*;

/**
 * @// TODO: 14/11/2022 change plugin impl to observer 
 */
public class ProviderManager {

    private static Logger logger = LogManager.getLogger(ProviderManager.class);

    private static final Marker JDRY = MarkerManager.getMarker("JDRY");


    List<Provider> plugins = new ArrayList<>();

    public void register(Provider plugin){
        plugins.add(plugin);
    }



    public static List<PluginProvider> providers() {
       return providers(ProviderManager.class.getClassLoader());
    }

    
    public static List<PluginProvider> providers(ClassLoader classLoader) {
        List<PluginProvider> services = new ArrayList<>();
        try {
            ServiceLoader<PluginProvider> loader = ServiceLoader.load(PluginProvider.class, classLoader);
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
                if(provider.getName().equals(providerName)) {
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
