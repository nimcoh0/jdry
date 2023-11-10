package org.softauto.analyzer.core.dao.api;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.commons.lang3.SerializationUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.softauto.analyzer.core.rules.DaoApiRules;
import org.softauto.analyzer.model.genericItem.GenericItem;
import org.softauto.analyzer.model.suite.Suite;
import org.softauto.analyzer.core.utils.Utils;

import java.io.IOException;
import java.nio.file.Paths;
import java.util.*;

public class Parser {

    private static Logger logger = LogManager.getLogger(Parser.class);

    JsonNode json;

    ObjectMapper objectMapper;

    List<GenericItem> genericItems = new ArrayList<>();

    HashMap<String,String> entities = new HashMap<>();

    public List<GenericItem> getGenericItems() {
        return genericItems;
    }

    public HashMap<String, String> getEntities() {
        return entities;
    }

    public Parser(String path){
        try {
            objectMapper = new ObjectMapper();
            if(Paths.get(path).toFile().exists()) {
                json = objectMapper.readValue(Paths.get(path).toFile(), JsonNode.class);
            }else {
                logger.error("file not exist "+ path);
            }
            logger.debug("successfully parser "+path);
        } catch (IOException e) {
            logger.error("parser fail on "+path,e);
        }
    }


    private List<LinkedHashMap<String,Object>> buildClazzList(JsonNode classes){
        List<LinkedHashMap<String,Object>> list = new ArrayList<>();
        if(!classes.isNull() ){
            for(JsonNode node : classes){
                if(node.isContainerNode() && node.get("type").asText().equals("clazz")) {
                    LinkedHashMap<String, Object> result = new ObjectMapper().convertValue(node.get("annotations"), new TypeReference<LinkedHashMap<String, Object>>(){});
                    list.add(result);
                }
            }
        }
        logger.debug(list.size() > 0 ? "successfully build class list " :"class list is empty" );
        return list;
    }


    private LinkedList<String> buildAnnotationsHelper(GenericItem genericItem){
        LinkedList<String> helperList = new LinkedList<>();
        for(Map.Entry annotation : genericItem.getAnnotations().entrySet()){
            if(!annotation.getKey().equals("class")) {
                String key = annotation.getKey().toString();
                key = key.substring(1, key.length() - 1).replace("/", ".");
                if (annotation.getValue() instanceof ArrayList<?>) {
                    for (Object o : (ArrayList) annotation.getValue()) {
                        //for(Map.Entry map : ((HashMap<String,Object>)o).entrySet()){
                        String s = key + "_" + ((HashMap<?, ?>) o).get("value");
                        helperList.add(s);
                        //}
                    }
                } else {
                    String s = key + "_" + ((Map) annotation.getValue()).get("value");
                    helperList.add(s);
                }
            }
        }
        return helperList;
    }

    private List<LinkedHashMap<String,Object>> findClazzList(GenericItem genericItem,List<LinkedHashMap<String,Object>> clazzList){
        List<LinkedHashMap<String,Object>> list = new ArrayList<>();
        for(LinkedHashMap<String,Object> hm : clazzList){
            for(Map.Entry entry : hm.entrySet()){
               if(entry.getKey().toString().contains(genericItem.getNamespce())){
                    list.add(hm);
                    //genericItem.setAnnotationsHelper(buildAnnotationsHelper(genericItem));
                    break;
                }
            }
        }
        if(list.size() == 0) {
            logger.warn("no Clazz List found for " + genericItem.getNamespce());
        }
        return list;
    }


    public Parser parseProcess(){
        try {
            if(!json.isNull() ) {
                List<LinkedHashMap<String, Object>> clazzList = buildClazzList(json.get("classes"));
                for (JsonNode node : json.get("methods")) {
                    //if (!node.get("type").asText().equals("clazz") && !node.get("type").asText().equals("entity")) {
                        String jsonString = objectMapper.writeValueAsString(node);
                        GenericItem genericItem = objectMapper.readValue(jsonString, GenericItem.class);
                        List<LinkedHashMap<String, Object>> currentClazzList = findClazzList(genericItem, clazzList);
                        genericItem.setAnnotationsHelper(buildAnnotationsHelper(genericItem));
                        if (genericItem.getCrudToSubject().size() > 0) {
                            for (HashMap<String, String> map : genericItem.getCrudToSubject()) {
                                for (Map.Entry entry : map.entrySet()) {
                                    if (entry.getValue() != null) {
                                        //String entityShortName = entry.getValue().toString();
                                        //entityShortName = entityShortName.substring(entityShortName.lastIndexOf(".")).replace("Dto","").toLowerCase();
                                        entities.put(Utils.getShortName(entry.getValue().toString().replace("Dto", "")).toLowerCase(), entry.getValue().toString());
                                    }
                                }
                            }
                        }
                        Suite.addClassListOfMethodsAnnotationSummery(genericItem.getNamespce(), genericItem.getAnnotationsHelper());
                        if (currentClazzList.size() > 0) {
                            for (LinkedHashMap<String, Object> list : currentClazzList) {
                                GenericItem newGenericItem = SerializationUtils.clone(genericItem);
                                newGenericItem.setClassList(list);
                                String newType = DaoApiRules.overrideType(newGenericItem.getReturnType());
                                newGenericItem.setReturnType(newType);
                                genericItems.add(newGenericItem);
                            }
                        } else {
                            GenericItem newGenericItem = SerializationUtils.clone(genericItem);
                            String newType = DaoApiRules.overrideType(newGenericItem.getReturnType());
                            newGenericItem.setReturnType(newType);
                            //newGenericItem.setClassList(list);
                            genericItems.add(newGenericItem);
                        }
                        //genericItem.setClassList(currentClazzList);
                        //genericItems.add(genericItem);
                        logger.debug("successfully create genericItem for " + genericItem.getName());
                   //}
                }
                /*
                for (JsonNode node : json.get("classes")) {
                    if (node.get("type").asText().equals("clazz")) {
                        String jsonString = objectMapper.writeValueAsString(node);
                        GenericItem genericItem = objectMapper.readValue(jsonString, GenericItem.class);
                        for (Map.Entry entry : genericItem.getAnnotations().entrySet()) {
                            if (entry.getValue() instanceof Map) {
                                for (Map.Entry s : ((Map<?, ?>) entry.getValue()).entrySet()) {
                                    if (s.getKey().equals("Lorg/softauto/annotations/EntityForTesting;"))
                                        entities.put(Utils.getShortName(entry.getKey().toString().replace("Dto", "")).toLowerCase(), entry.getKey().toString());
                                }

                            }
                        }
                    }
                }

                 */
            }
        } catch (JsonProcessingException e) {
            logger.error("fail create  genericItem ",e);
        }
        return this;
    }


}
