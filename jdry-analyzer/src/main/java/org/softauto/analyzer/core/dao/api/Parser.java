package org.softauto.analyzer.core.dao.api;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.commons.lang3.SerializationUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.softauto.analyzer.model.genericItem.GenericItem;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.*;

public class Parser {

    private static Logger logger = LogManager.getLogger(Parser.class);

    JsonNode json;

    ObjectMapper objectMapper;

    List<GenericItem> genericItems = new ArrayList<>();

    public JsonNode getJson() {
        return json;
    }

    public List<GenericItem> getGenericItems() {
        return genericItems;
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
                    String jsonString = objectMapper.writeValueAsString(node);
                    GenericItem genericItem = objectMapper.readValue(jsonString, GenericItem.class);
                    List<LinkedHashMap<String, Object>> currentClazzList = findClazzList(genericItem, clazzList);
                    if (currentClazzList.size() > 0) {
                        for (LinkedHashMap<String, Object> list : currentClazzList) {
                            GenericItem newGenericItem = SerializationUtils.clone(genericItem);
                            newGenericItem.setClassList(list);
                            genericItems.add(newGenericItem);
                        }
                    } else {
                        GenericItem newGenericItem = SerializationUtils.clone(genericItem);
                        genericItems.add(newGenericItem);
                        logger.debug("successfully create genericItem for " + genericItem.getName());
                    }
                }
            }
        } catch (JsonProcessingException e) {
            logger.error("fail create  genericItem ",e);
        }

        return this;
    }


}
