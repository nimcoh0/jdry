package org.softauto.analyzer.core.rules;

import org.apache.commons.lang3.SerializationUtils;
import org.softauto.analyzer.model.genericItem.GenericItem;
import org.softauto.analyzer.model.test.Test;
import org.softauto.analyzer.core.utils.ApplyRule;

public class AfterRules {

    public static String getType(GenericItem genericItem){
        return ApplyRule.setRule("override_after_return_type").apply().getResult();
    }

    public static String getName(Test test){
        return ApplyRule.setRule("after_name").addContext("test", SerializationUtils.clone(test)).apply().getResult();
        //return "after_" + test.getResult();
    }

    public static String buildExpression(Test test){
        return ApplyRule.setRule("default_api_after_expression").addContext("test", SerializationUtils.clone(test)).apply().getResult();

        /*
        String expression = null;
        try {
            if(test.getApi().getProtocol() != "RPC"){
                if(Configuration.has("plugin_"+test.getApi().getProtocol().toLowerCase()+"_default_api_after_expression")){
                    String s = Configuration.get("plugin_"+test.getApi().getProtocol().toLowerCase()+"_default_api_after_expression").asString();
                    expression =  Espl.getInstance().evaluate(s).toString();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return expression;

         */
    }
}
