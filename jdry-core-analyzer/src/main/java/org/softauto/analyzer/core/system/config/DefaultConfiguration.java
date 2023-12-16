package org.softauto.analyzer.core.system.config;

import java.util.HashMap;

public class DefaultConfiguration {

    static HashMap<String,Object> configuration = new HashMap<>();

    static{
        configuration.put(Context.FILE_OUTPUT,"jdry-analyzer/target/generated-sources/");
        configuration.put(Context.FILE_NAMESPACE,"tests.infrastructure");
        configuration.put(Context.DEFAULT_PROTOCOL,"RPC");
    }


    public static HashMap<String,Object> getConfiguration() {
        return configuration;
    }
}
