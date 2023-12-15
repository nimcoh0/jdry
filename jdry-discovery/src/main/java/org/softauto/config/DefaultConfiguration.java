package org.softauto.config;


import java.util.HashMap;

public class DefaultConfiguration {

    static HashMap<String,Object> configuration = new HashMap<>();

    static{
        configuration.put(Context.OUTPUT_FILE_PATH,"target/generated-sources/");
        configuration.put(Context.FILE_NAME,"demo");
        configuration.put(Context.FILE_NAMESPACE,"tests.infrastructure");
        configuration.put(Context.CLASS_DIR, "");
        configuration.put(Context.MAIN_CLASS,"");
        configuration.put(Context.DOMAIN,"");
        configuration.put(Context.DISCOVER_BY_ANNOTATION,new Object[]{"ApiForTesting", "ListenerForTesting"});
        configuration.put(Context.UNBOX_EXCLUDE_RETURN_TYPE,"java.lang.Exception");
        configuration.put(Context.UNBOX_RETURN_TYPE,"${#method.getReturnType().toString()}");
    }

    public static HashMap<String,Object> getConfiguration() {
        return configuration;
    }
}
