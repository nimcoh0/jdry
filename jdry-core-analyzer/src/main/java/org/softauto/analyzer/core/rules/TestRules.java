package org.softauto.analyzer.core.rules;

import org.apache.commons.lang3.SerializationUtils;
import org.softauto.analyzer.model.genericItem.GenericItem;
import org.softauto.analyzer.model.test.Test;
import org.softauto.analyzer.core.utils.ApplyRule;
import org.softauto.analyzer.core.utils.TestNameGenerator;

public class TestRules {

    public static String createTestId(GenericItem genericItem){
        //return String.valueOf(genericItem.getId());
        return ApplyRule.setRule("test_id").addContext("method",SerializationUtils.clone(genericItem)).apply().getResult();
    }

    public static String createTestFullName(GenericItem genericItem){
        return TestNameGenerator.getInstance().generateNameIfExist(buildTestFullName(genericItem));
    }

    public static String createTestName(GenericItem genericItem){
        //String name = genericItem.getName();
       // genericItem =  genericItem.getName().equals("<init>") ? genericItem : genericItem;
        return ApplyRule.setRule("test_name").setRootObject(genericItem).addContext("method",SerializationUtils.clone(genericItem)).apply().getResult();
    }

    /*
    public static String createTestName(GenericItem genericItem){
        String name = genericItem.getName();
        if(genericItem.getName().equals("<init>")){
                name =  Utils.unCapitalizeFirstLetter(Utils.getShortName(genericItem.getNamespce()));
            }
        if(Configuration.has("test_name")){
            String schema =  Configuration.get("test_name").asString();
            name = Espl.getInstance().addProperty("method",genericItem).addProperty("result",name).evaluate(schema).toString();
        }
        return name;
    }

     */

    public static String buildTestFullName(GenericItem genericItem){
        return ApplyRule.setRule("test_full_name").addContext("method", SerializationUtils.clone(genericItem)).apply().getResult();
        /*
        String fullName = genericItem.getNamespce() + "." + createTestName(genericItem);
        if(genericItem.getName().equals("<init>")){
            fullName = genericItem.getNamespce();
        }
        return fullName.replace(".","_");

         */
    }

    public static String buildFullName(GenericItem genericItem){
        String fullName = genericItem.getNamespce() + "." + genericItem.getName();
        if(genericItem.getName().equals("<init>")){
            fullName = genericItem.getNamespce();
        }
        return fullName.replace(".","_");
    }

    public static String createTestREsultPublishName(String type){
        return ApplyRule.setRule("publish_test_result_name").addContext("type",type).apply().getResult();
    }

    public static String getTestContext(Test test){
        return ApplyRule.setRule("test_context").addContext("test",test).apply().getResult();
    }

    public static String getTestSubject(Test test){
        return ApplyRule.setRule("test_subject").addContext("test",test).apply().getResult();
    }


}
