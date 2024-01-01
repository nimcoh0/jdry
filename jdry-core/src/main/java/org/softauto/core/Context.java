package org.softauto.core;

import org.apache.logging.log4j.Marker;
import org.apache.logging.log4j.MarkerManager;

public class Context {


    private static final org.apache.logging.log4j.Logger logger = org.apache.logging.log4j.LogManager.getLogger(Context.class);

    private static final Marker JDRY = MarkerManager.getMarker("JDRY");

    public final static String SEND_LOG_TO_TESTER = "send_log_to_tester";
    public final static String SEND_JDRY_LOG_TO_TESTER = "send_jdry_log_to_tester";
    public final static String LOAD_WEAVER = "load_weaver";
    public final static String TEST_MACHINE = "test_machine";
    public final static String LISTENER_PORT = "listener_port";
    public final static String TEST_MACHINE_NAME = "test_machine_name";
    public final static String TEST_INFRASTRUCTURE_PATH = "tests_infrastructure_path";
    public final static String SUITE_CLASS_NAME = "suite_class_name";
    public final static String STEP_SERVICE_NAME =  "step_service_name";
    public final static String STEP_SERVICE =  "StepService";
    public final static String STEP_SERVICE_INTERFACE =  "StepServiceInterface";
    public final static String STEP_SERVICE_IMPL =  "Step";
    public final static String STEP_SERVICE_IMPL_NAME =  "step_service_impl";
    public final static String STEP_SERVICE_IMPL_FOR_PROXY =  "step_service_impl_for_proxy";
    public final static String LISTENER_SERVICE_NAME = "listener_service_name";
    public final static String LISTENER_SERVICE = "ListenerService";
    public final static String LISTENER_SERVICE_INTERFACE = "ListenerServiceInterface";
    public final static String LISTENERS = "Listeners";
    public final static String LISTENER_SERVICE_IMPL = "Listener";
    public final static String LISTENER_SERVICE_IMPL_NAME = "listener_service_impl";
    public final static String LISTENER_SERVICE_IMPL_FOR_PROXY = "listener_service_impl_for_proxy";
    public final static String SERIALIZER_PORT = "serializer_port";
    public final static String SERIALIZER_HOST = "serializer_host";
    public final static String USERNAME_FIELD = "username_field";
    public final static String PASSWORD_FIELD = "password_field";
    public final static String EMAIL_FIELD = "email_field";
    public final static String ASPECT_WEAVER = "aspectj_weaver";
    public static final String DEFUALT_DATETIME_FORMAT = "default_datatime_format";
    public static final String SERVICE_MODE = "service_mode";
    public static final String SUITE_SRC_FILE = "suite_src_file";
    public final static String SERIALIZERS = "serializers";
    public final static String SERIALIZER = "serializer";
    public final static String SERIALIZER_CLASS = "class";
    public final static String SERIALIZER_TYPE = "type";
    public final static String SERIALIZATION_FEATURES = "serializationFeatures";
    public final static String SERIALIZATION_FEATURE = "serializationFeature";
    public final static String SERIALIZER_NAME = "name";
    public final static String SERIALIZER_SET = "set";
    public final static String PLUGINS = "plugins";
    public final static String PLUGIN = "plugin";
    public final static String TEST_LIB = "test_lib";
    public final static String CACHE_IMPL = "cache_impl";
    public final static String ENTITY_NAME_POSTFIX = "entity_name_postfix";
    public final static String ENTITY_NAME_PREFIX = "entity_name_prefix";
    public final static String TESTER_MODE = "tester_mode";

}
