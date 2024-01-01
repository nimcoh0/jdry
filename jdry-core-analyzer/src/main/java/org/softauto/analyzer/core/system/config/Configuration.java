package org.softauto.analyzer.core.system.config;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.Marker;
import org.apache.logging.log4j.MarkerManager;

import java.util.*;

/**
 * generic class that hold the configuration
 *
 */
public class Configuration {

    private static Logger logger = LogManager.getLogger(Configuration.class);

    private static final Marker JDRY = MarkerManager.getMarker("JDRY");
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
            logger.error(JDRY,"fail update configuration ", e);
        }
    }

    public static HashMap<String,Object> getConfiguration() {
        return configuration;
    }

    public String asString(){
        if(result !=null && result instanceof String){
            return result.toString();
        }
        return null;
    }

    public <T>  List<T> asList(){
        if(result !=null) {
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
        if(result !=null && result instanceof Map){
            return (HashMap<String,Object>)result;
        }
        return null;
    }


    public Object asObject(){
        return result;
    }

    public Integer asInteger(){
        if(result !=null) {
            if (result instanceof String) {
                return Integer.valueOf(result.toString());
            }
            if (result instanceof Integer) {
                return (Integer) result;
            }
        }
        return null;
    }

    public  static Configuration get(String key){
        try {
            if (configuration.containsKey(key)) {
                return  new Configuration(configuration.get(key));
            }
        }catch (Exception e){
            logger.error(JDRY,"",e);
        }
        return  new Configuration(null);
    }

    public  static boolean has(String key){
        try {
            if (configuration.containsKey(key)) {
                return  true;
            }
        }catch (Exception e){
            logger.error(JDRY,"",e);
        }
        return  false;
    }



    public static void put(String key,Object value){
        configuration.put(key,value);
    }

    public static void add(String key,Object value){
        if(value != null ) {
            Object o = configuration.get(key);
            if (o == null) {
                List<Object> list = new ArrayList<>();
                list.add(value);
                configuration.put(key, list);
                return;
            }
            if (o instanceof ArrayList<?>) {
                ((List<Object>) o).addAll((Collection<?>) value);
            } else {
                logger.error("can't add object to non list element");
            }
        }
    }

    public static <T> void add(String key,List<T> value){
        if(value != null && value.size() > 0) {
            List<T> o = (List<T>) configuration.get(key);
            if (o == null) {
                configuration.put(key, (List<T>) value);
                return;
            }
            if (o instanceof ArrayList<?>) {
                ((List<Object>) o).addAll(value);
            } else {
                logger.error("can't add object to non list element");
            }
        }
    }

    public static void putAll(HashMap<String,Object> value){
        configuration.putAll(value);
    }

    public  static boolean contains(String key){
        try {
            for(Map.Entry entry : configuration.entrySet()){
                if(entry.getKey().toString().contains(key)){
                    return true;
                }
            }
        }catch (Exception e){
            logger.error(JDRY,"",e);
        }
        return  false;
    }

    public  static Object getKeyValue(String key){
        try {
            for(Map.Entry entry : configuration.entrySet()){
                if(entry.getKey().toString().contains(key)){
                    return entry.getValue();
                }
            }

        }catch (Exception e){
            logger.error(JDRY,"",e);
        }
        return null;
    }

    public  static Object getKey(String key){
        try {
            for(Map.Entry entry : configuration.entrySet()){
                if(entry.getKey().toString().contains(key)){
                    return entry.getKey();
                }
            }

        }catch (Exception e){
            logger.error(JDRY,"",e);
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
            logger.error(JDRY,"",e);
        }
        return o;
    }

}
