package org.softauto.core;

import java.util.HashMap;

public class DefaultConfiguration {

    static HashMap<String,Object> configuration = new HashMap<>();

    static{
        configuration.put(Context.LOAD_WEAVER,"true");
        configuration.put(Context.SEND_LOG_TO_TESTER,"false");
        configuration.put(Context.SEND_JDRY_LOG_TO_TESTER,"false");
        configuration.put(Context.TEST_INFRASTRUCTURE_PATH,System.getProperty("user.dir") + "/target/test-classes/tests/infrastructure");
        configuration.put(Context.SUITE_CLASS_NAME,"demo.class");
        configuration.put(Context.LISTENER_PORT,"9091");
        configuration.put(Context.SERIALIZER_PORT,"8085");
        configuration.put(Context.SERIALIZER_HOST,"localhost");
        configuration.put(Context.ENABLE_SESSION,"true");
        configuration.put(Context.STEP_SERVICE_NAME,"StepService");
        configuration.put(Context.LISTENER_SERVICE_NAME,"ListenerService");
        configuration.put(Context.LISTENER_SERVICE_IMPL,"Listener");
        configuration.put(Context.LISTENER_SERVICE_IMPL_FOR_PROXY,"tests.infrastructure.Listener");
        configuration.put(Context.STEP_SERVICE_IMPL,"Step");
        configuration.put(Context.STEP_SERVICE_IMPL_FOR_PROXY,"tests.infrastructure.Step");
        configuration.put(Context.ASPECT_WEAVER,"aspectjweaver-1.9.6.jar");
        configuration.put(Context.DEFUALT_DATETIME_FORMAT,"yyyy-MM-dd HH:mm:ss");
        configuration.put(Context.TEST_MACHINE,Utils.getMachineIp());
        configuration.put(Context.TEST_MACHINE_NAME,Utils.getMachineName());
        configuration.put(Context.SERVICE_MODE,true);
        configuration.put(Context.USERNAME_FIELD,"username");
        configuration.put(Context.PASSWORD_FIELD,"password");
        configuration.put(Context.EMAIL_FIELD,"email");
    }

    public static HashMap<String,Object> getConfiguration() {
        return configuration;
    }
}
