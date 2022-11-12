package org.softauto.jaxrs.service;



import org.softauto.core.Configuration;
import org.softauto.core.TestContext;
import org.softauto.core.Utils;

import java.util.HashMap;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

public class TestDescriptor {

    private static final org.apache.logging.log4j.Logger logger = org.apache.logging.log4j.LogManager.getLogger(TestDescriptor.class);
    // cache for service descriptors.
    private static final ConcurrentMap<String, TestDescriptor> SERVICE_DESCRIPTORS = new ConcurrentHashMap<>();
    private  String serviceName;




    // cache for method descriptors.
    private final ConcurrentMap<String, IStepDescriptor> steps = new ConcurrentHashMap<>();

    public ConcurrentMap<String, IStepDescriptor> getSteps() {
        return steps;
    }



    private TestDescriptor(String serviceName) {
        this.serviceName = serviceName;
        //this.test = test;

    }

/*
    public static TestDescriptor create(Test test) {
        String serviceName = Utils.getServiceName(test);
        return SERVICE_DESCRIPTORS.computeIfAbsent(serviceName, key -> new TestDescriptor(test, serviceName));
    }


 */
    public static TestDescriptor create(String stepName) {
        String serviceName = stepName;
        return SERVICE_DESCRIPTORS.computeIfAbsent(serviceName, key -> new TestDescriptor(serviceName));
    }

    /*
    public StepDescriptor getStep(Item item) {
        return steps.computeIfAbsent(item.getName().replace(".","_"),
                key -> StepDescriptor.<Object[], Object>newBuilder()
                        .setItem(item)
                        .build());

    }


     */
    public IStepDescriptor getStep(String stepName, Object[] args, HashMap<String,Object> callOptions) {
        return steps.computeIfAbsent(stepName.replace(".","_"),
                key -> StepDescriptorBuilder.newBuilder().setConfiguration(Configuration.getConfiguration())
                        .setArgs(args)
                        //.setItem(item)
                        .setCallOptions(callOptions)
                        //.setTest(test)
                        .setStepDescriptor((IStepDescriptor) TestContext.get("stepDescriptor"))
                        .build()
                        .getStepDescriptor());


    }

}
