package org.softauto.jaxrs.analyzer;


import com.fasterxml.jackson.databind.JsonNode;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
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



}
