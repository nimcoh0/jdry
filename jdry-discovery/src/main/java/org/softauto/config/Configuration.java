package org.softauto.config;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.softauto.Main;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * generic class that hold the configuration
 *
 */
public class Configuration {

    private static Logger logger = LogManager.getLogger(Main.class);
    static HashMap<String,Object> configuration = new HashMap<>();
    Object result;

    public Configuration(Object result){
        this.result = result;
    }

    public Object getResult() {
        return result;
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
        if(result != null && result instanceof String){
            return result.toString();
        }
        return null;
    }

    public <T>  List<T> asList(){
        if(result != null) {
            List<Object> l = new ArrayList<>();
            if (result instanceof ArrayList) {
                return (List<T>) result;
            }
            l.add(result);
            return (List<T>) l;
        }
        return null;
    }

    public <T>  List<T> asList(Class<T> t){
        if(result != null) {
            List<Object> l = new ArrayList<>();
            if (result instanceof ArrayList) {
                return (List<T>) result;
            }
            l.add(result);
            return (List<T>) l;
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
                return  new Configuration(configuration.get(key));
            }
        }catch (Exception e){
            logger.error("fail get configuration "+ key,e);
        }
        return new Configuration(null);
    }

    public  static Boolean has(String key){
        try {
            if (configuration.containsKey(key)) {
                return  true;
            }
        }catch (Exception e){
            logger.error("fail has configuration "+key,e);
        }
        return false;
    }


    public static void put(String key,Object value){
        configuration.put(key,value);
    }

    public  static boolean contains(String key){
        try {
            for(Map.Entry entry : configuration.entrySet()){
                if(entry.getKey().toString().contains(key)){
                    return true;
                }
            }
        }catch (Exception e){
            logger.error("",e);
        }
        return  false;
    }

    public  static Object getKey(String key){
        try {
            for(Map.Entry entry : configuration.entrySet()){
                if(entry.getKey().toString().contains(key)){
                    return entry.getValue();
                }
            }

        }catch (Exception e){
            logger.error("",e);
        }
        return null;
    }

    public  static List<Object> getKeyList(String key){
        List<Object> o = new ArrayList<>();
        try {
            for(Map.Entry entry : configuration.entrySet()){
                if(entry.getKey().toString().contains(key)){
                    o.add(entry.getValue());
                }
            }

        }catch (Exception e){
            logger.error("",e);
        }
        return o;
    }

}
