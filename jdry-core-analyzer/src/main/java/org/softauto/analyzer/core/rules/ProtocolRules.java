package org.softauto.analyzer.core.rules;

import org.softauto.analyzer.core.system.config.Configuration;
import org.softauto.analyzer.core.system.config.Context;
import org.softauto.analyzer.core.system.plugin.ProviderManager;
import org.softauto.analyzer.core.system.plugin.api.Provider;
import org.softauto.analyzer.core.system.plugin.spi.PluginProvider;
import org.softauto.analyzer.model.Item;
import org.softauto.analyzer.model.test.Test;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

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

    public List<String> getProtocols(){
        List<String> pList = new ArrayList<>();
        pList.add("RPC");
        List<PluginProvider> pluginProviders = ProviderManager.providers();
        for(PluginProvider pluginProvider : pluginProviders){
            if(pluginProvider.getType().equals("protocol")){
                pList.add(pluginProvider.getName());
            }
        }
      return pList;
    }

}
