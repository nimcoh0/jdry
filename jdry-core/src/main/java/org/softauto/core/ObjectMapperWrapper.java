package org.softauto.core;

import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.module.SimpleModule;
import org.softauto.serializer.HttpServletRequestWrapperSerializer;
import org.softauto.serializer.ReaderToStringSerializer;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class ObjectMapperWrapper {

    private static ObjectMapperWrapper INSTANCE;
    List<HashMap<String,Object>> serializers = new ArrayList<>();
    List<HashMap<String,Object>> serializationFeatures = new ArrayList<>();
    ObjectMapper objectMapper;
    SimpleModule userModule;

    public ObjectMapper getObjectMapper() {
        return objectMapper;
    }




    public static ObjectMapperWrapper getInstance() {
        if(INSTANCE == null) {
            INSTANCE = new ObjectMapperWrapper();
        }
        return INSTANCE;
    }

    private ObjectMapperWrapper(){
        HashMap<String,Object> jackson = (HashMap<String, Object>) Configuration.get("recorder").asMap().get("jackson");
        if(jackson != null) {
            serializers = (List<HashMap<String, Object>>) jackson.get(Context.SERIALIZERS);
            //serializers = jackson.get(Context.SERIALIZERS);
            serializationFeatures = (List<HashMap<String, Object>>) jackson.get(Context.SERIALIZATION_FEATURES);
        }
            objectMapper = new ObjectMapper();
            userModule = new SimpleModule();

    }

    public ObjectMapperWrapper setSerializationFeatures(){
        for(HashMap<String,Object> f : serializationFeatures){
            HashMap<String,Object> feature = (HashMap<String, Object>) f.get("feature");
            String name = feature.get("name").toString();
            boolean set = Boolean.parseBoolean(feature.get("set").toString());
            if(set){
                objectMapper.enable(SerializationFeature.valueOf(name));
            }else {
                objectMapper.disable(SerializationFeature.valueOf(name));
            }
        }
        return this;
    }


    public ObjectMapperWrapper addSerializers(){
        try {
            for(HashMap<String,Object> ser : serializers){
                HashMap<String,Object> serializer = (HashMap<String, Object>) ser.get("serializer");
                Class<?> type = Class.forName(serializer.get("type").toString());
                Class<?> clazz = Class.forName(serializer.get("class").toString());
                JsonSerializer instance = (JsonSerializer) clazz.getConstructors()[0].newInstance();
                userModule.addSerializer(type, instance);
            }
            //ReaderToStringSerializer readerToStringSerializer = new ReaderToStringSerializer();
            //userModule.addSerializer(java.io.Reader.class, readerToStringSerializer);
           // HttpServletRequestWrapperSerializer httpServletRequestWrapperSerializer = new HttpServletRequestWrapperSerializer();
           // userModule.addSerializer(javax.servlet.http.HttpServletRequestWrapper.class, httpServletRequestWrapperSerializer);

        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        return this;
    }

    public ObjectMapperWrapper build(){
        objectMapper.registerModule(userModule);
        return this;
    }

}
