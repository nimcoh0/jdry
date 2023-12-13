package org.softauto.service;

import org.apache.logging.log4j.Marker;
import org.apache.logging.log4j.MarkerManager;
import org.softauto.core.IStepDescriptor;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

public class TestDescriptor {

    private static final org.apache.logging.log4j.Logger logger = org.apache.logging.log4j.LogManager.getLogger(TestDescriptor.class);

    private static final Marker JDRY = MarkerManager.getMarker("JDRY");

    // cache for service descriptors.
    private static ConcurrentMap<String, TestDescriptor> SERVICE_DESCRIPTORS = new ConcurrentHashMap<>();
    private String serviceName;
    Class iface;

    // cache for method descriptors.
    private final ConcurrentMap<String, IStepDescriptor> steps = new ConcurrentHashMap<>();

    public ConcurrentMap<String, IStepDescriptor> getSteps() {
        return steps;
    }

    private TestDescriptor(Class iface,String serviceName) {
        this.serviceName = serviceName;
        this.iface = iface;
    }

    public static TestDescriptor create(Class iface) {
        String serviceName = JdryUtils.getServiceName(iface);
        return SERVICE_DESCRIPTORS.computeIfAbsent(serviceName, key -> new TestDescriptor(iface, serviceName));
    }
}
