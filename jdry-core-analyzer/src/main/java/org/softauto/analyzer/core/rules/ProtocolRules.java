package org.softauto.analyzer.core.rules;

import org.softauto.analyzer.core.system.config.Configuration;
import org.softauto.analyzer.core.system.config.Context;
import org.softauto.analyzer.model.test.Test;

import java.util.HashMap;

public class ProtocolRules {

    public static String getDefaultProtocol(){
        if(Configuration.has(Context.DEFAULT_PROTOCOL)){
            return Configuration.get(Context.DEFAULT_PROTOCOL).asString();
        }
        return "RPC";
    }

    public static String getProtocol(HashMap<String, Object> callOption){
        if(callOption != null && callOption.containsKey("protocol")){
            return callOption.get("protocol").toString();
        }
        return getDefaultProtocol();
    }

    public static String getProtocol(Test test){
        if(test != null && test.getData().getPluginData().containsKey("protocol")){
            return test.getData().getPluginData().get("protocol").toString();
        }
        return getDefaultProtocol();
    }

}
