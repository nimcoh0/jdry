package org.softauto.analyzer.core.rules;

import org.apache.commons.lang3.SerializationUtils;
import org.softauto.analyzer.directivs.result.Result;
import org.softauto.analyzer.core.system.espl.Espl;
import org.softauto.analyzer.model.genericItem.GenericItem;
import org.softauto.analyzer.core.system.plugin.ProviderManager;
import org.softauto.analyzer.core.system.plugin.spi.PluginProvider;
import org.softauto.analyzer.core.rules.protocol.rpc.Rpc;
import org.softauto.analyzer.core.system.scanner.AbstractAnnotationScanner;
import org.softauto.analyzer.core.utils.ApplyRule;

import java.util.Map;

public class ResultRules {


    public static String getDefaultResultType(GenericItem genericItem){
        return genericItem.getReturnType();
    }

    public static String getResultType(GenericItem genericItem,String protocol){
        String  schema = null;
        if(protocol.equals("RPC")){
            schema = new Rpc().getResultType(genericItem);
        }else for (PluginProvider plugin : ProviderManager.providers(ClassLoader.getSystemClassLoader())) {
            if(plugin.getProtocol() != null && plugin.getType().equals("protocol") && plugin.getProtocol().equals(protocol)){
                schema = plugin.create(genericItem).getResultTypeAnalyzer().buildResult(genericItem);

            }
        }
        String result =  Espl.getInstance().evaluate(schema).toString();
        if(result != null ){
            return result;
        }
        return getDefaultResultType(genericItem);
    }

    public static Result getResult(GenericItem genericItem,String protocol){
        String type = getResultType(genericItem,protocol);
        String name = getResultName(genericItem);
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

    public static String getResultName(GenericItem genericItem) {
        return ApplyRule.setRule("test_result_name").addContext("method", SerializationUtils.clone(genericItem)).apply().getResult();
    }


    public static Result updateResult(Result result, AbstractAnnotationScanner scanner){
        Map<?,?> map = null;
        if(scanner.has("after")){
            map = scanner.get("after").asMap();
        }
        if(map != null){
            if(map.containsKey("name")){
                result.setName(map.get("name").toString());
            }
            if(map.containsKey("type")){
                result.setType(map.get("type").toString());
            }
            if(map.containsKey("value")){
                result.addValue(map.get("value"));
            }
        }
        return result;
    }
/*
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

 */



    /*
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

    public static String buildResultType(String protocol, GenericItem genericItem){
        String schema= null;
        String result;
        if(protocol.equals("RPC")){
            schema = Configuration.has("default_api_return_type") ? Configuration.get("default_api_return_type").asString() : genericItem.getReturnType();
        }else {
            for (PluginProvider plugin : ProviderManager.providers(ClassLoader.getSystemClassLoader())) {
                if(plugin.getProtocol().equals(protocol)){
                    schema = plugin.create(genericItem).getResultTypeAnalyzer().buildResult(genericItem.getReturnType());
                }
            }
        }
        result = Espl.getInstance().evaluate(schema).toString();
        if(result != null ){
            return result;
        }
        return genericItem.getReturnType();
    }


     */
}
