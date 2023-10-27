package org.softauto.analyzer.core.rules;

import org.softauto.analyzer.core.utils.ApplyRule;

public class DaoApiRules {

    public static String overrideType(String str) {
        return ApplyRule.setRule("override_types").addContext("type", str).apply().getResult();
        /*
        if(Configuration.has("override_types")) {
            List<HashMap<String, String>> overrideList = Configuration.get("override_types").asList();
            for (HashMap<String, String> hm : overrideList) {
                if (hm.get("old_type").equals(str)) {
                    return hm.get("new_type");
                }
            }
        }
        return str;

         */
    }
}
