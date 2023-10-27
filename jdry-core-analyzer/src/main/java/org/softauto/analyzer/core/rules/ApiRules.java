package org.softauto.analyzer.core.rules;

import org.apache.commons.lang3.SerializationUtils;
import org.softauto.analyzer.model.genericItem.GenericItem;
import org.softauto.analyzer.core.utils.ApplyRule;
import org.softauto.analyzer.core.utils.Utils;

public class ApiRules {

    public static String buildFullName(GenericItem genericItem){
        return ApplyRule.setRule("api_build_full_name").setRootObject(genericItem).addContext("Utils",Utils.class).addContext("method", SerializationUtils.clone(genericItem)).apply().getResult();
        /*
        String fullName = genericItem.getNamespce() + "." + genericItem.getName();
        if(genericItem.getName().equals("<init>")){
            fullName = genericItem.getNamespce();
        }
        return fullName.replace(".","_");

         */
    }

    public static String buildName(GenericItem genericItem){
        return ApplyRule.setRule("api_build_name").setRootObject(genericItem).addContext("method", SerializationUtils.clone(genericItem)).apply().getResult();
        /*
        String name = genericItem.getName();

        try {
            if(genericItem.getName().equals("<init>")){
                name =  Utils.unCapitalizeFirstLetter(Utils.getShortName(genericItem.getNamespce()));
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return name;

         */
    }
}
