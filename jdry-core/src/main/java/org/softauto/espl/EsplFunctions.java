package org.softauto.espl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.base.Splitter;
import org.apache.commons.lang3.StringUtils;
import org.softauto.core.Multimap;
import org.softauto.core.Utils;
import org.softauto.podam.ExtendPodamFactoryImpl;
import org.springframework.expression.spel.standard.SpelExpressionParser;
import uk.co.jemos.podam.api.PodamFactory;

import java.net.URL;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;
import java.util.stream.Collectors;


public class EsplFunctions {

    static Multimap _publish;

    public static void setPublish(Multimap publish) {
        _publish = publish;

    }

    public static Object getPublish(String expression){
        try {
            String root = new ObjectMapper().writeValueAsString((HashMap<String, Object>)_publish.getMap());
            JsonNode rootNode = new ObjectMapper().readTree(root);
            return rootNode.at(expression);

        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static class Map{

        public static String map(String expression){
            return getPublish(expression).toString();

        }
    }

    public static class Strategy{

        static PodamFactory factory;



        static Class c;

        static HashMap<String,Object> parameters = new HashMap<>();




        public static Class setParameters(HashMap<String,Object> p){
            parameters = p;
            return Strategy.class;
        }

        public static Class addParameters(String key,Object value){
            //HashMap<String,Object> hm = new HashMap<>();
            //hm.put(key,value);
            parameters.put(key,value);
            return Strategy.class;
        }

        public static Object strategy(String type){
            try {
                Class c = Class.forName(type,false,ClassLoader.getSystemClassLoader());
                return c.getConstructors()[0].newInstance();
            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }

        public static void exclude(String property){
            //Object field = Espl.getInstance().getProperty("field");
            List<String> excludedFields = (List<String>) Espl.getInstance().getProperty("excludedFields");
            excludedFields.add(property);
        }

        public static String random(String type){
            try {
                c = Class.forName(type,false,ClassLoader.getSystemClassLoader());
                factory = new ExtendPodamFactoryImpl();
                Object pojo = factory.manufacturePojo(c);
                return new ObjectMapper().writeValueAsString(pojo);
            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }

        /*
        public static String randomk(String type,String...attributes){
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
                c = Class.forName(type,false,ClassLoader.getSystemClassLoader());
                factory = new ExtendPodamFactoryImpl().setAttributeValueMap(attributeMap);
                Object pojo = factory.manufacturePojo(c);
                return new ObjectMapper().writeValueAsString(pojo);
            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }

         */

        public static String randomk(String type,String...attributes){
            String attributeString = "";
            if(attributes != null && attributes.length > 0) {
                for (String s : attributes) {
                    if(attributeString.isEmpty()) {
                        attributeString ="\"" + s + "\"";
                    }else {
                        attributeString = attributeString + ",\"" + s + "\"";
                    }
                }
            }
            //String joinedString = StringUtils..prependIfMissing("\"")..join(attributes,",");
            return Utils.toObject(type,"randomk(\""+type+"\","+attributeString+")");
        }

        public static String randomf(String type){
            return Utils.toObject(type,"random(\""+type+"\")");
        }


        private Class<?> replaceDataStrategyWithPodamStrategy(Class c){
            try {
                String str = new ObjectMapper().writeValueAsString(c);

            } catch (JsonProcessingException e) {
                e.printStackTrace();
            }
            return null;
        }

    }


    }


