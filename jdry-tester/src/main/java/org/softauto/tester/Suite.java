package org.softauto.tester;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.fasterxml.jackson.databind.node.TextNode;
import org.softauto.core.Multimap;
import org.softauto.espl.Espl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class Suite {

    Multimap publish = new Multimap();

    ObjectNode data = new ObjectMapper().createObjectNode();

    Espl espl = Espl.reset().setPublish(publish);


    public Suite addPublish(ObjectNode data){
       this.data.setAll(data);
       return this;
    }

    public Suite addPublish(String name,Object data){
        try {
            String str = new ObjectMapper().writeValueAsString(data);
            Object o = new ObjectMapper().readTree(str);
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
            String str = new ObjectMapper().writeValueAsString(data);
            ObjectNode node = (ObjectNode) new ObjectMapper().readTree(str);
            this.data.setAll(node);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        return this;
    }

    public Suite addPublish(Multimap data){
        try {
            String str = new ObjectMapper().writeValueAsString(data.getMap());
            ObjectNode node = (ObjectNode) new ObjectMapper().readTree(str);
            this.data.setAll(node);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        return this;
    }

    public Suite addPublish(Map<?,?> data){
        try {
            String str = new ObjectMapper().writeValueAsString(data);
            ObjectNode node = (ObjectNode) new ObjectMapper().readTree(str);
            this.data.setAll(node);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        return this;
    }

    public Suite addPublish(List<?> data){
        try {
            String str = new ObjectMapper().writeValueAsString(data);
            ObjectNode node = (ObjectNode) new ObjectMapper().readTree(str);
            this.data.setAll(node);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        return this;
    }

    /*
    public Suite addPublish(String id, Object data){
            publish.put(id,data);
            espl.addProperty(id,data);
        return this;
    }

     */

    public Suite addPublish(String id, String key, Object value){
            HashMap<String ,Object> data = new HashMap<>();
            data.put(key,value);
            publish.put(id,data);
            espl.addProperty(key,value);
      return this;
    }


    public Object findKey(String key){
        try {
            //String root = new ObjectMapper().writeValueAsString((HashMap<String, Object>)publish.getMap());
            //JsonNode rootNode = new ObjectMapper().readTree(root);
            return data.findValue(key);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public Object getPath(String path){
        try {
            //String root = new ObjectMapper().writeValueAsString((HashMap<String, Object>)publish.getMap());
            //JsonNode rootNode = new ObjectMapper().readTree(root);
            return data.at(path);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public Object getPublish(String expression){
        JsonNode r = data.at(expression);
        if(r instanceof TextNode){
            return r.asText();
        }
        return r;
    }


    public Object getPublish(String id,String expression){
        try {
            String root = new ObjectMapper().writeValueAsString((HashMap<String, Object>)publish.getMap());
            JsonNode rootNode = new ObjectMapper().readTree(root);
            HashMap<String,Object> childNode = new HashMap<>();
            if(rootNode.get(id).isArray()) {
                for (JsonNode node : (ArrayNode) rootNode.get(id)) {
                    Map<String, Object> map = new ObjectMapper().convertValue(node, new TypeReference<Map<String, Object>>() {});
                    childNode.putAll(map);
                }
            }

            String str = new ObjectMapper().writeValueAsString(childNode);
            JsonNode node = new ObjectMapper().readTree(str);
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
