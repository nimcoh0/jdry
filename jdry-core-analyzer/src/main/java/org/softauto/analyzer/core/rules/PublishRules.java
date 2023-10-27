package org.softauto.analyzer.core.rules;

import org.softauto.analyzer.model.test.Test;
import org.softauto.analyzer.core.utils.ApplyRule;

public class PublishRules {

    public static String buildTestPublishResultName(Test test){
        String p = getPublishTestResult(test);
        p =  NameRules.removeEntityPostfix(p);
        p = NameRules.removeEntityPrefix(p);
        p = NameRules.splitByCharacterTypeCamel(p);
        return p;
    }

    public static String buildTestPublishResultName(String str){
        str =  NameRules.removeEntityPostfix(str);
        str = NameRules.removeEntityPrefix(str);
        str = NameRules.splitByCharacterTypeCamel(str);
        return str;
    }

    public static String getPublishTestResult(Test test){
        return  ApplyRule.setRule("publish_test_result_name").addContext("test",test).apply().getResult();
    }
}
