package org.softauto.analyzer.core.utils;

import org.softauto.analyzer.core.system.config.Configuration;
import org.softauto.analyzer.core.system.config.Context;
import org.softauto.analyzer.model.test.Test;

import java.util.HashMap;

public class ProtocolUtils {

    public static String getProtocol(){
       return Configuration.get(Context.DEFAULT_PROTOCOL).asString();
    }

    public static String getProtocol(Test test){
        if(test != null && test.getData().getPluginData().containsKey("protocol")){
            return getProtocol(test.getData().getPluginData());
        }
        return Configuration.get(Context.DEFAULT_PROTOCOL).asString();
    }

    public static String getProtocol(HashMap<String,Object> callOption){
        if(callOption.containsKey("protocol")){
            return callOption.get("protocol").toString();
        }
        return Configuration.get(Context.DEFAULT_PROTOCOL).asString();
    }

    /*
    public static String getProtocol(AbstractAnnotationScanner scanner, GenericItem genericItem){
        try {
            if(scanner != null && scanner.has("protocol")){
                return scanner.get("protocol").asString();
            }

            List<HashMap<String,Object>> protocolIdentifierList = Configuration.get("protocol_identifier").asList();
            for(HashMap<String,Object> hm : protocolIdentifierList) {
                for (Map.Entry entry : hm.entrySet()) {
                    for(Map.Entry s : genericItem.getAnnotations().entrySet()){
                        if(s.getKey().toString().contains(((Map)entry.getValue()).get("identifier").toString().replace(".","/"))){
                            return ((Map)entry.getValue()).get("name").toString();
                        }
                    }
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return Configuration.get(Context.DEFAULT_PROTOCOL).asString();
    }

     */
}
