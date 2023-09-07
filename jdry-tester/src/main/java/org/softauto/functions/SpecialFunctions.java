package org.softauto.functions;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.softauto.core.*;
import org.softauto.espl.Espl;
import org.softauto.podam.ExtendPodamFactoryImpl;
import org.softauto.tester.Comparator;
import uk.co.jemos.podam.api.PodamFactory;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;


public class SpecialFunctions {

    private static Logger logger = LogManager.getLogger(SpecialFunctions.class);

    public AbstractSuite suite ;

    public void setSuite(Suite suite) {
        this.suite = suite;
    }

    public SpecialFunctions(){
        //suite = (AbstractSuite)SuiteFactory.getSuite(Configuration.get(Context.CACHE_IMPL).asString());
    }


    public boolean compare(Object expected, Object actual){
        return new Comparator().setExpected(expected).setActual(actual).compare();
    }

    public long timestamp(){
        return new Date().getTime();
    }


    public Object retrieve(String name,String type){
        try {
            Object r = suite.getPublishParameter(name,type);
            if(r != null && !r.toString().isEmpty()){
                logger.debug(name + " found in cache ");
                if(Utils.isPrimitive(type)){
                    return r;
                }else {
                    String s = new ObjectMapper().writeValueAsString(r);
                    Class<?> c = Class.forName(type);
                    return new ObjectMapper().readValue(s, c);
                }
            }else {
                logger.debug(name + " not found in cache ! creating random value");
                return random(type);
            }
        } catch (Exception e) {
            logger.error("fail retrieve " + name,e);
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
                logger.debug("successfully create random value for " + type);
                return pojo;
            }
        } catch (Exception e) {
            logger.error("fail create random value for  " + type,e);
        }
        return null;
    }

    public static Object random(String type,String[] excludes,String[] consumes,HashMap<String,Object> data){
        try {
            HashMap<String, Object> attributeMap = new HashMap<>();
            if(type != null && !type.isEmpty()) {
                Class c = Class.forName(type, false, ClassLoader.getSystemClassLoader());
                PodamFactory factory = new ExtendPodamFactoryImpl().setAttributeValueMap(data).setExcludedFields(Arrays.asList(excludes),c).setConsumeList(Arrays.asList(consumes));
                Object pojo = factory.manufacturePojo(c);
                logger.debug("successfully create random value for " + type );
                return pojo;
            }
        } catch (Exception e) {
            logger.error("fail create random value for  " + type ,e);
        }
        return null;
    }



/*
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
                logger.debug("successfully create random value for " + type + " with attributes "+ Arrays.toString(keys));
                return pojo;
            }
        } catch (Exception e) {
            logger.error("fail create random value for  " + type + " with attributes "+ Arrays.toString(keys),e);
        }
        return null;
    }


 */

    public <T> T consume(String expression,Class type){
        try {
            if(expression != null && !expression.isEmpty()) {
                if (expression.contains("/")) {
                    return (T) suite.getPublishParameter(expression,type.getTypeName());
                } else {
                    return (T)suite.findKey(expression);
                }
            }
        } catch (Exception e) {
            logger.error("fail consume " + expression,e);
        }
        return null;
    }

    public <T> T consume(String expression,String type){
        try {
            if(expression != null && !expression.isEmpty()) {
                if (expression.contains("/")) {
                    return (T) suite.getPublishParameter(expression,type);
                } else {
                    return (T)suite.findKey(expression);
                }
            }
        } catch (Exception e) {
            logger.error("fail consume " + expression,e);
        }
        return null;
    }

    public <T> T consume(String expression){
        try {
            if(expression != null && !expression.isEmpty()) {
                if (expression.contains("/")) {
                    return (T) suite.getPublishParameter(expression);
                } else {
                    return (T)suite.findKey(expression);
                }
            }
        } catch (Exception e) {
            logger.error("fail consume " + expression,e);
        }
        return null;
    }


    public <T> T exec(String expression){
        try {
            if(expression != null && !expression.isEmpty()) {
                return (T)Espl.getInstance().evaluate(expression);
            }
        } catch (Exception e) {
            logger.error("fail exec " + expression,e);
        }
        return null;
    }
}
