package org.softauto.analyzer.core.system.config;

import java.util.HashMap;

public class DefaultConfiguration {

    static HashMap<String,Object> configuration = new HashMap<>();

    static{
        configuration.put(Context.FILE_INPUT_PATH,"jdry-analyzer/src/main/resources");
        configuration.put(Context.FILE_INPUT_NAME, "Demo.json");
        configuration.put(Context.FILE_DATA_PATH,"jdry-analyzer/src/main/resources");
        configuration.put(Context.FILE_DATA_NAME, "Demo.data");
        configuration.put(Context.FILE_OUTPUT_PATH,"jdry-analyzer/target/generated-sources");
        configuration.put(Context.FILE_OUTPUT_NAME, "");
        configuration.put(Context.FILE_NAMESPACE,"tests.infrastructure");
        configuration.put(Context.USE_DATA_GENERATOR,"true");
        configuration.put(Context.SCHEMA_NAME,"");
        configuration.put(Context.DEFAULT_PROTOCOL,"RPC");
        //configuration.put(Context.ASSERT_TYPE_DEFAULT_VALUE, AssertType.AssertEquals.getValue());
        configuration.put(Context.DEFAULT_ASSERT, "org.junit.Assert.assertEquals(${#result},${#expected})");
        configuration.put("publish_test_result_name", new String[]{
                "#type.contains(#domain) ? T(org.softauto.analyzer.utils.Utils).getShortName(#test.getApi().getResponse().getType())  : 'result'"
        });


    }


    public static HashMap<String,Object> getConfiguration() {
        return configuration;
    }
}
