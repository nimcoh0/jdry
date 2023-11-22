package org.softauto.jaxrs.service;

public class StepDefinition {

    private IStepDescriptor step;
    private CallerHandler handler;


    private StepDefinition(IStepDescriptor step, CallerHandler callHandler) {
        this.step = step;
        handler = callHandler;

    }

    public static StepDefinition create(IStepDescriptor step, CallerHandler handler) {
        return new StepDefinition(step, handler);
    }

    public IStepDescriptor getStepDescriptor() {
        return this.step;
    }

    public CallerHandler getCallerHandler() {
        return handler;
    }



}
