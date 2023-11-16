package org.softauto.jaxrs.service;


public interface CallerHandler {
    <T> T startCall(IStepDescriptor stepDescriptor, Object[] args);
}
