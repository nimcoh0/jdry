package org.softauto.core;

import java.util.HashMap;

/**
 * generic class that hold the configuration
 *
 */
public class Configuration {

    private static final org.apache.logging.log4j.Logger logger = org.apache.logging.log4j.LogManager.getLogger(Configuration.class);
    static HashMap<String,Object> configuration = new HashMap<>();


    public static void setConfiguration(HashMap<String,Object> configuration){
        try {
            Configuration.configuration = configuration;
        }catch (Exception e){
            logger.error("fail update configuration ", e);
        }
    }

    public static HashMap<String,Object> getConfiguration() {
        return configuration;
    }



    public  static <T> T get(String key){
        try {
            if (configuration.containsKey(key)) {
                return (T)configuration.get(key);
             }
        }catch (Exception e){
            logger.error(e);
        }
        return null;
    }

    public static void put(String key,Object value){
        configuration.put(key,value);
    }



}
