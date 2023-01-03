package org.softauto.core;

import java.util.HashMap;
import java.util.Map;

/**
 * generic class that hold the configuration
 *
 */
public class Configuration {

    private static final org.apache.logging.log4j.Logger logger = org.apache.logging.log4j.LogManager.getLogger(Configuration.class);
    static HashMap<String,Object> configuration = new HashMap<>();
    Object result;

    public Configuration(Object result){
        this.result = result;
    }



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

    public String asString(){
        if(result instanceof String){
            return result.toString();
        }
        return null;
    }

    public HashMap<String,Object> asMap(){
        if(result instanceof Map){
            return (HashMap<String,Object>)result;
        }
        return null;
    }


    public Object asObject(){
        return result;
    }

    public Integer asInteger(){
        if(result instanceof String ) {
            return Integer.valueOf(result.toString());
        }
        if(result instanceof Integer){
            return (Integer)result;
        }
        return null;
    }

    public  static Configuration get(String key){
        try {
            if (configuration.containsKey(key)) {
                return new Configuration(configuration.get(key));
            }
        }catch (Exception e){
            logger.error(e);
        }
        return null;
    }

    /*
    public  static <T> T get(String key){
        try {
            if (configuration.containsKey(key)) {
               return  (T)configuration.get(key) ;
            }
        }catch (Exception e){
            logger.error(e);
        }
        return null;
    }

     */


    public static void put(String key,Object value){
        configuration.put(key,value);
    }



}
