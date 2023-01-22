package org.softauto.tester;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.softauto.core.Multimap;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Suite {

    Multimap publish = new Multimap();
    //HashMap<String,Object> publishDataForTesting = new HashMap<>();


    public Suite addPublish(String id, Object data){
        //if(dataType.equals("DEFAULT_DATA")) {
            publish.put(id,data);
       //}
       // if(dataType.equals("DATA_FOR_TESTING")) {
          //  publishDataForTesting.put(id,data);
       // }
        return this;
    }

    public Suite addPublish(String id, String key, Object value){
       // if(dataType.equals("DEFAULT_DATA")) {
            HashMap<String ,Object> data = new HashMap<>();
            data.put(key,value);
            publish.put(id,data);
       // }
        //if(dataType.equals("DATA_FOR_TESTING")) {
        //    HashMap<String ,Object> data = new HashMap<>();
        //    data.put(key,value);
        //    publishDataForTesting.put(id,data);
       // }
        return this;
    }

    public HashMap<String, Object> getPublish(String id){
        try {
            Object obj = publish.getMap().get(id);
            if(obj instanceof ArrayList){
                HashMap<String, Object> r = new HashMap<>();
                List<HashMap<String, Object>> list = (List<HashMap<String, Object>>) obj;
                for(HashMap<String, Object> hm : list){
                    for(Map.Entry entry : hm.entrySet()){
                        r.put(entry.getKey().toString(),entry.getValue());
                    }
                }
                return r;
            }else {
                return ((HashMap<String,Object>)publish.getMap().get(id));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }



    /*
    public HashMap<String, Object> getPublishDataForTesting(String id){
        try {
            return ((HashMap<String,Object>)publishDataForTesting.get(id));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public HashMap<String,Object> getPublishDataForTesting(){
        try {
            return publishDataForTesting;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }



     */


}
