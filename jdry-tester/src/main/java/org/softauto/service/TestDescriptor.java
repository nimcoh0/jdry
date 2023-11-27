package org.softauto.service;



import org.apache.avro.ipc.Callback;
import org.softauto.core.IStepDescriptor;
import org.softauto.plugin.ProviderManager;
import org.softauto.plugin.api.Provider;
import org.softauto.plugin.spi.PluginProvider;
import java.util.HashMap;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

public class TestDescriptor {

    private static final org.apache.logging.log4j.Logger logger = org.apache.logging.log4j.LogManager.getLogger(TestDescriptor.class);
    // cache for service descriptors.
    private static ConcurrentMap<String, TestDescriptor> SERVICE_DESCRIPTORS = new ConcurrentHashMap<>();
    private String serviceName;
    Class iface;
    //Provider provider;

    // cache for method descriptors.
    private final ConcurrentMap<String, IStepDescriptor> steps = new ConcurrentHashMap<>();

    public ConcurrentMap<String, IStepDescriptor> getSteps() {
        return steps;
    }


    private TestDescriptor(Class iface,String serviceName) {
        this.serviceName = serviceName;
        this.iface = iface;
        //this.test = test;

    }



    public static TestDescriptor create(Class iface) {
        String serviceName = JdryUtils.getServiceName(iface);
        return SERVICE_DESCRIPTORS.computeIfAbsent(serviceName, key -> new TestDescriptor(iface, serviceName));
    }

    /*
    public IStepDescriptor getStep(String stepName, Object[] args, Class[] types, HashMap<String, Object> callOptions, String scenarioId, String auth) {
         return steps.computeIfAbsent(stepName.replace(".", "_"),
              key -> provider.buildStepDescriptor(stepName, args, types, callOptions, scenarioId,  auth,null));
    }

    public IStepDescriptor getStep(String stepName, Object[] args, Class[] types, HashMap<String, Object> callOptions, String scenarioId, String auth, Callback<Object> callback) {
        return steps.computeIfAbsent(stepName.replace(".", "_"),
                key -> provider.buildStepDescriptor(stepName, args, types, callOptions, scenarioId,  auth,callback));
    }

     */
}
