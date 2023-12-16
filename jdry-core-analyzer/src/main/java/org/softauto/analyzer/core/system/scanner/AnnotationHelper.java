package org.softauto.analyzer.core.system.scanner;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.Marker;
import org.apache.logging.log4j.MarkerManager;

import java.util.*;

public  class AnnotationHelper {

    private static Logger logger = LogManager.getLogger(AnnotationHelper.class);

    private static final Marker JDRY = MarkerManager.getMarker("JDRY");

    LinkedList<LinkedHashMap<String, Object>> mapList = new LinkedList();


    protected AnnotationHelper(){

    }


    protected AnnotationHelper(LinkedList<LinkedHashMap<String, Object>> mapList){
        this.mapList = mapList;
    }

    /**
     * check if annotation exist
     * @param annotations
     * @param path
     * @return
     */
    protected static boolean has(HashMap<String,Object> annotations, String path ){
        if(get(annotations,path).isNull()){
            return false;
        }
        return true;
    }

    /**
     * check if annotation property exist
     * @param annotations
     * @param path
     * @param element
     * @return
     */
    protected static boolean has(HashMap<String,Object> annotations, String path ,String element){
        if(get(annotations,path,element).isNull()){
            return false;
        }
        return true;
    }

    /**
     * check if result is null
     * @return
     */
    protected boolean isNull(){
        if(mapList != null && mapList.size() > 0){
            return false;
        }
        return true;
    }



    /**
     * return result as list
     * @return
     */
    protected  LinkedList<LinkedHashMap<String, Object>> getMapList(){
        return mapList;
    }



    /**
     * get property value as string
     * @return
     */
    protected  String getValueAsString(int index){
        if(mapList!= null && mapList.size()> 0) {
            Map<String, Object> map = mapList.get(index);
            return map.toString();
        }
        return null;
    }



    /**
     * get annotation property
     * @param annotations
     * @param path
     * @param element
     * @return
     */
    protected static  AnnotationHelper get(HashMap<String,Object> annotations, String path,String element){
        LinkedList<LinkedHashMap<String, Object>> mapList = new LinkedList();
       try {
            Object result = annotations.get(path);
            if(result != null) {
                if (result instanceof ArrayList) {
                    mapList = (LinkedList<LinkedHashMap<String, Object>>) result;
                }
                if (result instanceof Map) {
                    Object elem = ((HashMap<String, Object>) result).get(element);
                    if(elem != null) {
                        if (elem instanceof Map) {
                            mapList.add((LinkedHashMap<String, Object>) elem);
                        } else {
                            //HashMap<String, Object> hm = new HashMap<>();
                            //hm.put(element, elem);
                            mapList.addAll((Collection<? extends LinkedHashMap<String, Object>>) elem);
                        }
                    }else {
                        new AnnotationHelper(null);
                    }
                }
            }else {
                new AnnotationHelper(null);
            }
        } catch (Exception e) {
           logger.error(JDRY,"fail get property "+path+"/"+ element);
        }
        return new AnnotationHelper(mapList);
    }

    /**
     * get annotation
     * @param annotations
     * @param path
     * @return
     */
    protected static AnnotationHelper get(HashMap<String,Object> annotations, String path){
        LinkedList<LinkedHashMap<String, Object>> mapList = new LinkedList();
        try {
           Object result = annotations.get(path);
           if(result != null) {
               if (result instanceof ArrayList) {
                   mapList = new ObjectMapper().convertValue(result, new TypeReference<LinkedList<LinkedHashMap<String, Object>>>() {
                   });
               } else {
                   mapList.add((LinkedHashMap<String, Object>)result);
               }
           }else {
               return new AnnotationHelper(null);
           }
        } catch (Exception e) {
           logger.error(JDRY,"fail get annotation for "+ path);
        }
        return new AnnotationHelper(mapList);
    }

    public static boolean isContains(Set<String> pathList,LinkedHashMap<String, Object> annotations){
        for(String path : pathList) {
            boolean result =  isContains(path, annotations);
            if(result){
                return true;
            }
        }
        return false;
    }

    public static boolean isContains(String path,LinkedHashMap<String, Object> annotations){
        for(Map.Entry entry : annotations.entrySet()){
            if(entry.getKey().toString().contains(path)){
                return true;
            }
        }
        return false;
    }

    public static boolean isExist(String path,LinkedHashMap<String, Object> annotations){
       return isExist(path,annotations,null);
    }

    public static boolean isExist(LinkedList<String> pathList,LinkedHashMap<String, Object> annotations){
        for(String path : pathList) {
            boolean result =  isExist(path, annotations, null);
            if(result){
                return true;
            }
        }
        return false;
    }

    public static boolean isExist(String path,LinkedHashMap<String, Object> annotations,String parameter){
        //path = "L"+path.replace(".","/")+";";
        if(AnnotationHelper.has(annotations,"constructor")){
            if(AnnotationHelper.has(annotations,"constructor",path)){
                if(parameter != null){
                    String str = AnnotationHelper.get(annotations,"constructor",path).getValueAsString(0);
                    if(str.contains(parameter)){
                        return true;
                    }
                    return false;
                }
                return true;
            }
        }
        if(AnnotationHelper.has(annotations,"class")){
            if(AnnotationHelper.has(annotations,"class",path)){
                if(parameter != null){
                    String str = AnnotationHelper.get(annotations,"class",path).getValueAsString(0);
                    if(str.contains(parameter)){
                        return true;
                    }
                    return false;
                }
                return true;
            }
        }
        if(AnnotationHelper.has(annotations,path)){
            if(parameter != null){
                String str = AnnotationHelper.get(annotations,path).getValueAsString(0);
                if(str.contains(parameter)){
                    return true;
                }
                return false;
            }
            return true;
        }
        return false;
    }

    public static boolean isTextExist(LinkedHashMap<String, Object> annotations,String text){
        try {
            String str = new ObjectMapper().writeValueAsString(annotations);
            if(str.contains(text)){
                return true;
            }
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        return false;
    }

}
