package org.softauto.tester;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.HashMap;

public class Suite {

    HashMap<String,Object> publish = new HashMap<>();



    public void addPublish(String key,Object value){
        publish.put(key,value);
    }

    public <T> T getPublish(String key){
        try {
          return (T)publish.get(key);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }


}
