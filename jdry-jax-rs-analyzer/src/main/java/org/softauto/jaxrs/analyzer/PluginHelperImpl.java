package org.softauto.jaxrs.analyzer;


import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.softauto.analyzer.model.data.Data;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class PluginHelperImpl {


    private static Logger logger = LogManager.getLogger(PluginHelperImpl.class);




    public static LinkedHashMap<String, Object> getCallOption(List<LinkedHashMap<String, Object>> mapList) {
        LinkedHashMap<String,Object> callOption = new LinkedHashMap() ;
        try {
            if(mapList != null && mapList.size()>0) {
                for (HashMap<String, Object> map : mapList) {
                    if (map != null && map.size() > 0) {
                        for (Map.Entry entry : map.entrySet()) {
                            callOption.put(entry.getKey().toString(), entry.getValue().toString());
                        }
                    }
                }
            }
        } catch (Exception e) {
            logger.error("fail create callOptions  ",e);
        }

        return callOption;
    }

    public static HashMap<String, Object> getCallOption(Data data) {
        HashMap<String,Object> callOption = new HashMap<>() ;
        callOption.put("response",data.getResponse().getType());
        return callOption;
    }

    public static String getPath(JsonNode request){
        try {
            if (request != null && request.has("AbsolutePath")) {
                String absolutePath  = request.get("AbsolutePath").asText();
                String s = absolutePath.substring(absolutePath.lastIndexOf(":")+5,absolutePath.length()).trim();
                if(!s.endsWith("/")){
                    s = s+"/";
                }
                return s;
            }
        } catch (Exception e) {
            logger.error("fail get Path ",e);
        }
        return null;
    }


    public static String getMethod(JsonNode request){
        try {
            if (request != null && request.has("Method")) {
                return request.get("Method").asText();
            }
        } catch (Exception e) {
            logger.error("fail get Method ",e);
        }
        return null;
    }

    public static String getMediaType(JsonNode request){
        try {
            if (request != null && request.has("MediaType")) {
                String type = request.get("MediaType").get("type").asText();
                String subType = request.get("MediaType").get("subtype").asText();
                return type + "/" + subType;
            }
        } catch (Exception e) {
            logger.error("fail get MediaType ",e);
        }
        return null;
    }

    public static JsonNode getRequest(Data data){
        JsonNode request = null;
        try {
            if(data.getThreadLocal().get("args") != null ) {
                JsonNode node = new ObjectMapper().readTree(data.getThreadLocal().get("args").toString());
                if(node.isArray()) {
                    ArrayNode nodes = (ArrayNode) new ObjectMapper().readTree(data.getThreadLocal().get("args").toString());
                    if (nodes.get(0).has("request")) {
                        request = nodes.get(0).get("request");
                    }
                }else {
                    request = node.get("request");
                }
            }
        } catch (Exception e) {
            logger.error("fail get Request ",e);
        }
        return request;
    }

}
