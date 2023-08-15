package org.softauto.functions;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.MissingNode;
import org.softauto.core.Utils;
import org.softauto.espl.Espl;
import org.softauto.podam.ExtendPodamFactoryImpl;
import org.softauto.tester.Comparator;
import org.softauto.tester.Suite;
import uk.co.jemos.podam.api.PodamFactory;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

public class SpecialFunctions {

    public Suite suite = new Suite();

    public void setSuite(Suite suite) {
        this.suite = suite;
    }

    public boolean compare(Object expected, Object actual){
        return new Comparator().setExpected(expected).setActual(actual).compare();
    }

    public long timestamp(){
        return new Date().getTime();
    }


    public Object retrieve(String name,String type){
        try {
            Object r = suite.getPublish(name,type);
            if(r != null){
                if(Utils.isPrimitive(type)){
                    return r;
                }else {
                    //if(r instanceof JsonNode && r != MissingNode.getInstance()) {
                    //Object javaType = Utils.jsonNodeToJavaType((JsonNode)r);
                    //if (javaType == null) {
                    String s = new ObjectMapper().writeValueAsString(r);
                    Class<?> c = Class.forName(type);
                    return new ObjectMapper().readValue(s, c);
                    //}else {
                    //   return javaType;
                    //}
                }
            }else {
                return random(type);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }


    public  Object random(String type){
        try {
            if(type != null && !type.isEmpty()) {
                Class c = null;
                if (Utils.isPrimitive(type)) {
                    c = Utils.getPrimitiveClass(type);
                } else {
                    c = Class.forName(type, false, ClassLoader.getSystemClassLoader());
                }
                PodamFactory factory = new ExtendPodamFactoryImpl();
                Object pojo = factory.manufacturePojo(c);
                return pojo;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static Object random(String type,String[] keys,Object[] values){
        try {
            if(type != null && !type.isEmpty()) {
                HashMap<String, Object> attributeMap = new HashMap<>();
                if (keys != null && keys.length > 0 && values != null && values.length >0 && keys.length == values.length) {
                    for(int i=0;i<keys.length;i++){
                        if(values[i] instanceof String) {
                            Object v = Espl.getInstance().evaluate(values[i].toString());
                            attributeMap.put(type+"."+keys[i],v);
                        }else {
                            attributeMap.put(type+"."+keys[i], values[i]);
                        }
                    }
                }

                Class c = Class.forName(type, false, ClassLoader.getSystemClassLoader());
                PodamFactory factory = new ExtendPodamFactoryImpl().setAttributeValueMap(attributeMap);
                Object pojo = factory.manufacturePojo(c);
                return pojo;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }


    /*
    public static Object random(String type,Object...attributes){
        try {
            if(type != null && !type.isEmpty()) {
                HashMap<String, Object> attributeMap = new HashMap<>();
                if (attributes != null && attributes.length > 0) {
                    for (Object attribute : attributes) {
                        if(attribute instanceof String) {
                            Properties p = Utils.stringToProperties(attribute.toString());
                            Map map = Utils.propertiesToMap(p);
                            Map<String, String> newMap = new HashMap<>();
                            map.forEach((k, v) -> {
                                v = Espl.getInstance().evaluate(v.toString());
                                newMap.put((type + "." + k).toString(), v.toString());
                            });

                            attributeMap.putAll(newMap);
                        }else {
                            Map<String, Object> newMap = new HashMap<>();
                            newMap.put(attribute.getClass().getName(), attribute);
                            attributeMap.putAll(newMap);
                        }
                    }
                }

                Class c = Class.forName(type, false, ClassLoader.getSystemClassLoader());
                PodamFactory factory = new ExtendPodamFactoryImpl().setAttributeValueMap(attributeMap);
                Object pojo = factory.manufacturePojo(c);
                return pojo;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }


     */
    public <T> T consume(String expression,String type){
        if(expression != null && !expression.isEmpty()) {
            if (expression.contains("/")) {
                return (T) suite.getPublish(expression,type);
            } else {
                return (T)suite.findKey(expression);
            }
        }
        return null;
    }

    public <T> T consume(String expression){
        if(expression != null && !expression.isEmpty()) {
            if (expression.contains("/")) {
                return (T) suite.getPublish(expression);
            } else {
                return (T)suite.findKey(expression);
            }
        }
        return null;
    }


    public <T> T exec(String expression){
        if(expression != null && !expression.isEmpty()) {
            return (T)Espl.getInstance().evaluate(expression);
        }
        return null;
    }
}
