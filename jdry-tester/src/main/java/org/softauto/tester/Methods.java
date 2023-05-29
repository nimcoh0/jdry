package org.softauto.tester;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.base.Splitter;
import org.softauto.core.Utils;
import org.softauto.podam.ExtendPodamFactoryImpl;
import uk.co.jemos.podam.api.PodamFactory;

import java.util.Date;
import java.util.HashMap;

public class Methods {

    public Suite suite = new Suite();

    public boolean compare(Object expected,Object actual){
        return new Comparator().setExpected(expected).setActual(actual).compare();
    }

    public Object map(String reference){
        return suite.getPath(reference);
    }

    public long timestamp(){
        return new Date().getTime();
    }

    public static Object randomk(String type,String...attributes){
        try {
            HashMap<String,String> attributeMap = new HashMap<>();
            if(attributes != null && attributes.length >0){
                for(String attribute : attributes) {
                    java.util.Map<String, String> map = (java.util.Map<String, String>) Splitter.on(",").withKeyValueSeparator("=").split(attribute);
                    String key = map.keySet().toArray()[0].toString().trim();
                    String value = map.values().toArray()[0].toString().trim();
                    attributeMap.put(type+"."+key,value);
                }
            }
            Class c = Class.forName(type,false,ClassLoader.getSystemClassLoader());
            PodamFactory factory = new ExtendPodamFactoryImpl().setAttributeValueMap(attributeMap);
            Object pojo = factory.manufacturePojo(c);
            return pojo;
            //return new ObjectMapper().writeValueAsString(pojo);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }


    public  Object random(String type){
        try {
            Class c = Class.forName(type,false,ClassLoader.getSystemClassLoader());
            PodamFactory factory = new ExtendPodamFactoryImpl();
            Object pojo = factory.manufacturePojo(c);
            String str = new ObjectMapper().writeValueAsString(pojo);
            return new ObjectMapper().readValue(str,c);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public Object consume(String expression){
        if(expression.contains("/")) {
            return suite.getPublish(expression);
        }else {
            return suite.findKey(expression);
        }
        //return Utils.toObject(type,str);
    }

    public Object consume(String id,String expression,String type){
        return suite.getPublish(id , expression ).toString();
        //return Utils.toObject(type,str);
    }

    public Object consume(String expression,String type){
        return suite.getPath(expression ).toString();
        //return Utils.toObject(type,str);
    }
}
