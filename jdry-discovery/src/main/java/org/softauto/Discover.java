package org.softauto;

import com.fasterxml.jackson.databind.JsonNode;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.Marker;
import org.apache.logging.log4j.MarkerManager;
import org.softauto.config.Configuration;
import org.softauto.config.Context;
import org.softauto.discovery.Discovery;
import org.softauto.discovery.plugin.ProviderManager;
import org.softauto.discovery.plugin.api.Provider;
import org.softauto.discovery.plugin.spi.PluginProvider;
import org.softauto.utils.Util;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;


public class Discover {

    private static Logger logger = LogManager.getLogger(Discover.class);

    private static final Marker JDRY = MarkerManager.getMarker("JDRY");

    private List<String> discoveryAnnotations = new ArrayList<>();

    public Discover(String...args){
        initialize(args);
        JsonNode discovery = new Discovery().discover().getDiscovery();
        if(discovery != null ){
            if(args.length == 2) {
                Util.save(discovery,args[1]);
            }else {
                Util.save(discovery);
            }
            logger.debug(JDRY,"discovery finish successfully");
        }else {
            logger.error(JDRY,"discovery fail no items found ");
        }
    }

    public static void main(String[] args){
        new Discover(args);
    }

    public static void initialize(String[] args) {
        try {
            Util.loadDefaultConfiguration();
            if(args.length >0){
                Util.loadConfiguration(args[0]);
            }else {
                Util.loadConfiguration(null);
            }
            initializeArgs(args);
            initializePlugins();
        }catch (Exception e){
           logger.error(JDRY,"initialize discovery fail ",e.getMessage());
        }
    }

    private static void initializeArgs(String[] args){
        if(args.length == 2){
           Configuration.put(Context.OUTPUT_FILE_PATH, args[1].trim());
        }
    }


    public static void initializePlugins(){
        for(PluginProvider plugin : ProviderManager.providers(ClassLoader.getSystemClassLoader())){
            if (plugin.getType() != null && plugin.getType().equals("discovery")) {
                Provider provider = plugin.create(new Object[]{});
                //List<String> apiAnnotations = Configuration.get("api_annotations").asList();
                //apiAnnotations.addAll(provider.getApiAnnotations());
                Configuration.add("discover_by_annotation", provider.getDiscoverByAnnotation());
                Configuration.add("unbox_return_type", provider.getUnboxReturnType());
                Configuration.add("unbox_exclude_return_type", provider.getUnboxEexcludeReturnType());
            }
        }
    }





}
