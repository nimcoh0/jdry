package org.softauto.core;


public interface CallerHandler {
    <T> T startCall(IStepDescriptor stepDescriptor, Object[] args);
}
