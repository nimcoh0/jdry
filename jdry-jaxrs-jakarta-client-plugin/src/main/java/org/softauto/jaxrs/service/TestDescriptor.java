package org.softauto.jaxrs.service;


import org.apache.logging.log4j.Marker;
import org.apache.logging.log4j.MarkerManager;
import org.softauto.core.Configuration;
import org.softauto.jaxrs.auth.AuthFactory;

import java.util.HashMap;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

public class TestDescriptor {

    private static final org.apache.logging.log4j.Logger logger = org.apache.logging.log4j.LogManager.getLogger(TestDescriptor.class);

    private static final Marker JDRY = MarkerManager.getMarker("JDRY");
    // cache for service descriptors.
    private  static ConcurrentMap<String, TestDescriptor> SERVICE_DESCRIPTORS = new ConcurrentHashMap<>();
    private  String serviceName;




    // cache for method descriptors.
    private final ConcurrentMap<String, IStepDescriptor> steps = new ConcurrentHashMap<>();

    public ConcurrentMap<String, IStepDescriptor> getSteps() {
        return steps;
    }



    private TestDescriptor(String serviceName) {
        this.serviceName = serviceName;
    }


    public static TestDescriptor create(String stepName) {
        SERVICE_DESCRIPTORS = new ConcurrentHashMap<>();
        String serviceName = stepName;
        return SERVICE_DESCRIPTORS.computeIfAbsent(serviceName, key -> new TestDescriptor(serviceName));
    }

    public IStepDescriptor getStep(String stepName, Object[] args,Class[] types, HashMap<String,Object> callOptions,String scenarioId) {
        String auto = Configuration.get("jaxrs").asMap().get("auth").toString();
        IStepDescriptor stepDescriptor = AuthFactory.getStepDescriptor(auto);

        return steps.computeIfAbsent(stepName.replace(".","_"),
                key -> StepDescriptorBuilder.newBuilder().setConfiguration(Configuration.getConfiguration())
                        .setArgs(args)
                        .setName(stepName)
                        .setTypes(types)
                        .setCallOptions(callOptions)
                        .setScenarioId(scenarioId)
                        .setStepDescriptor(stepDescriptor)
                        .build()
                        .getStepDescriptor());


    }

}
