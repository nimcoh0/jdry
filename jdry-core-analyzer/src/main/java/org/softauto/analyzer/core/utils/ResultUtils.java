package org.softauto.analyzer.core.utils;

import org.softauto.analyzer.core.system.config.Configuration;
import org.softauto.analyzer.core.system.plugin.api.Provider;
import org.softauto.analyzer.directivs.result.Result;
import org.softauto.analyzer.core.system.espl.Espl;
import org.softauto.analyzer.model.genericItem.GenericItem;
import org.softauto.analyzer.core.system.plugin.ProviderManager;
import org.softauto.analyzer.core.system.plugin.spi.PluginProvider;

import java.util.Map;

public class ResultUtils {



    public static String getRpcConfiguration(String protocol,GenericItem genericItem){
        String schema= null;
        if(protocol.equals("RPC")){
            schema = Configuration.has("default_api_return_type") ? Configuration.get("default_api_return_type").asString() : genericItem.getReturnType();
        }
        return Espl.getInstance().evaluate(schema).toString();
    }

    public static String getPluginConfiguration(String protocol,GenericItem genericItem){
        String schema= null;
        if(Utils.isPrimitive(genericItem.getReturnType()) && Configuration.has("plugin_"+protocol.toLowerCase()+"_default_api_primitive_return_type")){
            schema =   Configuration.get("plugin_"+protocol.toLowerCase()+"_default_api_primitive_return_type").asString();
        }else {
            schema = Configuration.has("plugin_" + protocol.toLowerCase() + "_default_api_return_type") ? Configuration.get("plugin_" + protocol.toLowerCase() + "_default_api_return_type").asString() : genericItem.getReturnType();
        }
        return Espl.getInstance().evaluate(schema).toString();
    }

    public static String buildResultType(String protocol, GenericItem genericItem, Provider provider){
        String schema= null;
        String result;
        if(protocol.equals("RPC")){
            schema = Configuration.has("default_api_return_type") ? Configuration.get("default_api_return_type").asString() : genericItem.getReturnType();
        }else {
            schema = provider.getResultTypeAnalyzer().buildResult(genericItem);
        }
        result = Espl.getInstance().evaluate(schema).toString();
        if(result != null ){
            return result;
        }
        return genericItem.getReturnType();
    }


    public static String buildResultType(String protocol, GenericItem genericItem){
        String schema= null;
        String result;
        if(protocol.equals("RPC")){
            schema = Configuration.has("default_api_return_type") ? Configuration.get("default_api_return_type").asString() : genericItem.getReturnType();
        }else {
            for (PluginProvider plugin : ProviderManager.providers(ClassLoader.getSystemClassLoader())) {
                if(plugin.getType().equals("protocol") && plugin.getProtocol().equals(protocol)){
                    schema = plugin.create(genericItem).getResultTypeAnalyzer().buildResult(genericItem);
                }
            }
        }
        result = Espl.getInstance().evaluate(schema).toString();
        if(result != null ){
            return result;
        }
        return genericItem.getReturnType();
    }

    /*
    private Result buildResult(String protocol){
        String schema= null;
        if(protocol.equals("RPC")){
            schema = Configuration.has("default_api_return_type") ? Configuration.get("default_api_return_type").asString() : genericItem.getReturnType();
        }else {
            if(Utils.isPrimitive(genericItem.getReturnType()) && Configuration.has("plugin_"+protocol.toLowerCase()+"_default_api_primitive_return_type")){
                schema =   Configuration.get("plugin_"+protocol.toLowerCase()+"_default_api_primitive_return_type").asString();
            }else {
                schema = Configuration.has("plugin_" + protocol.toLowerCase() + "_default_api_return_type") ? Configuration.get("plugin_" + protocol.toLowerCase() + "_default_api_return_type").asString() : genericItem.getReturnType();
            }
        }
        String s = Espl.getInstance().evaluate(schema).toString();
        return buildResult(genericItem,s);

    }

     */

    public static Result buildResult(GenericItem genericItem, String type, String name){
        Result result = new Result();
        try {
            if(type != null){
                result.setType(type);
            }else {
                result.setType(genericItem.getReturnType());
            }
            result.setName(name);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    public static Result buildResult(GenericItem genericItem,String type){
        return buildResult(genericItem,type,buildResultName(genericItem));
    }

    public static String buildResultName(GenericItem genericItem,Map map){
        if(map != null && map.containsKey("callback") && map.get("callback") != null){
            return map.get("callback").toString();
        }
        return buildResultName(genericItem);
    }

    public static String buildResultName(GenericItem genericItem){
        String result = null;
        try {
            if(genericItem.getName().equals("<init>")){
                result = "result_" + (genericItem.getNamespce()).replace(".", "_");
            }else {
                if (genericItem.getReturnType() != null && !genericItem.getReturnType().equals("Void") && !genericItem.getReturnType().equals("void")) {
                    result = "result_" + (genericItem.getNamespce() + "." + genericItem.getName()).replace(".", "_");
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return result;
    }


}
