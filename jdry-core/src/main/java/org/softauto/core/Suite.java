package org.softauto.core;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.MapperFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.MissingNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.fasterxml.jackson.databind.node.TextNode;
import org.softauto.espl.Espl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Suite {

    Multimap publish = new Multimap();

    ObjectNode data = new ObjectMapper().createObjectNode();

    Espl espl = Espl.reset().setPublish(publish);


    private static Suite suite = null;

    public static Suite getInstance(){
        if(suite == null){
            suite = new Suite();
        }
        return suite;
    }

    private Suite(){}


    public Suite addPublish(ObjectNode data){
       this.data.setAll(data);
       return this;
    }

    public Suite addPublish(String name,Object data){
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            objectMapper.disable(MapperFeature.USE_ANNOTATIONS);
            String str = objectMapper.writeValueAsString(data);
            Object o = objectMapper.readTree(str);
            if(o instanceof JsonNode){
                this.data.set(name, (JsonNode) o);
            }else {
                JsonNode node = (ObjectNode) new ObjectMapper().readTree(str);
                this.data.set(name,node);
            }

        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        return this;
    }


    public Suite addPublish(Object data){
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            objectMapper.disable(MapperFeature.USE_ANNOTATIONS);
            String str = objectMapper.writeValueAsString(data);
            ObjectNode node = (ObjectNode) objectMapper.readTree(str);
            this.data.setAll(node);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        return this;
    }

    public Suite addPublish(Multimap data){
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            objectMapper.disable(MapperFeature.USE_ANNOTATIONS);
            String str = objectMapper.writeValueAsString(data.getMap());
            ObjectNode node = (ObjectNode) objectMapper.readTree(str);
            this.data.setAll(node);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        return this;
    }

    public Suite addPublish(Map<?,?> data){
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            objectMapper.disable(MapperFeature.USE_ANNOTATIONS);
            String str = objectMapper.writeValueAsString(data);
            ObjectNode node = (ObjectNode) objectMapper.readTree(str);
            this.data.setAll(node);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        return this;
    }

    public Suite addPublish(List<?> data){
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            objectMapper.disable(MapperFeature.USE_ANNOTATIONS);
            String str = objectMapper.writeValueAsString(data);
            ObjectNode node = (ObjectNode) objectMapper.readTree(str);
            this.data.setAll(node);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        return this;
    }



    public Suite addPublish(String id, String key, Object value){
            HashMap<String ,Object> data = new HashMap<>();
            data.put(key,value);
            publish.put(id,data);
            espl.addProperty(key,value);
      return this;
    }


    public <T> T findKey(String key){
        try {
            JsonNode r = data.findValue(key);
            Object o = Utils.jsonNodeToJavaType(r);
            if(o != null){
                return (T)o;
            }
            return (T)r;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public <T> T findKey(String key,Class type){
        try {
            JsonNode r = data.findValue(key);
            Object o = Utils.jsonNodeToJavaType(r,type.getTypeName().toLowerCase());
            if(o != null){
                return (T)o;
            }
            return (T)r;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public Object getPath(String path){
        try {
           return data.at(path);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public <T> T getPublish(String expression){
        JsonNode r = data.at(expression);
        Object o = Utils.jsonNodeToJavaType(r);
        if(o != null && !(o instanceof MissingNode)){
            return (T)o;
        }
        return null;
    }

    public <T> T getPublish(String expression,String type){
        JsonNode r = data.at(expression);
        Object o = Utils.jsonNodeToJavaType(r,type.toLowerCase());
        if(o != null && !(o instanceof MissingNode)){
            return (T)o;
        }
        return null;
    }


    public Object getPublish(String id,String expression,String type){
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            objectMapper.disable(MapperFeature.USE_ANNOTATIONS);
            String root = objectMapper.writeValueAsString((HashMap<String, Object>)publish.getMap());
            JsonNode rootNode = objectMapper.readTree(root);
            HashMap<String,Object> childNode = new HashMap<>();
            if(rootNode.get(id).isArray()) {
                for (JsonNode node : (ArrayNode) rootNode.get(id)) {
                    Map<String, Object> map = objectMapper.convertValue(node, new TypeReference<Map<String, Object>>() {});
                    childNode.putAll(map);
                }
            }

            String str = objectMapper.writeValueAsString(childNode);
            JsonNode node = objectMapper.readTree(str);
            JsonNode r = node.at(expression);
            if(r instanceof TextNode){
                return node.at(expression).asText();
            }
            return r;
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        return null;
    }

    private Object resolve(Object o){
        if(o instanceof ArrayList){
            for(int i=0;i<((ArrayList)o).size();i++){
                Object _o = resolve(((ArrayList)o).get(i));
                ((ArrayList)o).set(i,_o);
            }
        }else if(o instanceof Map){
            for(Map.Entry entry : ((Map<String,String>)o).entrySet()){
                Object _o = resolve(entry.getValue());
                ((Map<String,String>)o).put(entry.getKey().toString(),_o.toString());
            }
        }else {
            o =  espl.evaluate(espl.evaluate(o.toString()).toString());

        }
        return o;
    }









}
