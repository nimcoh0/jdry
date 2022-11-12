package org.softauto.injector.core;

import java.util.HashMap;

public class DefaultConfiguration {

    static HashMap<String,Object> configuration = new HashMap<>();

    static{
        configuration.put(Context.ENABLE_SESSION,"true");

    }



}
