package org.softauto.espl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.softauto.core.Multimap;
import org.softauto.podam.ExtendPodamFactoryImpl;
import uk.co.jemos.podam.api.PodamFactory;

import java.net.URL;
import java.util.HashMap;
import java.util.List;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class EsplFunctionsCompileTime extends AbstractEsplFunctions{





    public static class Strategy{

        static PodamFactory factory;

        static Class c;


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
    }

    public static class HandleUri{

        public static String getUri(String expression){
            try {
                URL url = new URL(expression);
                return url.getPath().toString();
            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }

        public static String buildUri(String expression,String regex,String...params){
            try {
                URL url = new URL(expression);
                String path = url.getPath().toString();
                String pattern = regex; // *? = "reluctant": minimum string from last "/" to end
                Pattern r = Pattern.compile(regex);
                String key = r
                        .matcher(path.toString())
                        .results()
                        .collect(Collectors.toList())
                        .get(0)
                        .group(0);
                if(params.length > 0) {
                    if(key.endsWith("/")){
                        key = key.substring(0,key.length()-1);
                    }
                    for (String s : params) {
                        key = key + "/" + s;
                    }
                }
                return key;
            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }

        public static String editUriAdvance(String expression,String regex,int group){
            try {
                URL url = new URL(expression);
                String path = url.getPath().toString();
                String pattern = regex; // *? = "reluctant": minimum string from last "/" to end
                Pattern r = Pattern.compile(regex);
                String key = r
                        .matcher(path.toString())
                        .results()
                        .collect(Collectors.toList())
                        .get(0)
                        .group(group);
                return key;
            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }



    }

}
