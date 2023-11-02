package org.softauto.spring.web.analyzer.analyzers;

import org.apache.commons.lang3.SerializationUtils;
import org.softauto.analyzer.model.genericItem.GenericItem;
import org.softauto.analyzer.core.utils.ApplyRule;
import org.softauto.analyzer.core.utils.ResultTypeAnalyzer;


public class AnalyzeResult implements ResultTypeAnalyzer {

    GenericItem genericItem;

    public AnalyzeResult(GenericItem genericItem){
        this.genericItem = genericItem;
    }



    @Override
    public String buildResult(GenericItem genericItem) {
        return ApplyRule.setRule("plugin_jaxrs_default_api_return_type").addContext("method", SerializationUtils.clone(genericItem)).apply().getResult().toString();

        /*
        String result = null;
        String schema = null;
        try {
            if(str.contains(",")) {
                schema = "com.fasterxml.jackson.databind.node.ArrayNode";
            }else if(Utils.isPrimitive(genericItem.getReturnType()) && Configuration.has("plugin_jaxrs_default_api_primitive_return_type")){
                schema =   Configuration.get("plugin_jaxrs_default_api_primitive_return_type").asString();
            }else {
                schema = Configuration.has("plugin_jaxrs_default_api_return_type") ? Configuration.get("plugin_jaxrs_default_api_return_type").asString() : genericItem.getReturnType();
            }


            result = Espl.getInstance().evaluate(schema).toString();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;

         */
    }

}