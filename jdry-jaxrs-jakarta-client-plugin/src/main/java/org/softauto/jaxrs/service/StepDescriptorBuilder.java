package org.softauto.jaxrs.service;




import java.util.HashMap;

public class StepDescriptorBuilder {

    public static Builder newBuilder (){
        return new Builder();
    }

    IStepDescriptor stepDescriptor;

    public StepDescriptorBuilder(IStepDescriptor stepDescriptor){
        this.stepDescriptor = stepDescriptor;
    }

    public IStepDescriptor getStepDescriptor() {
        return stepDescriptor;
    }

    public static class Builder{

        HashMap<String,Object> callOptions;

        HashMap<String,Object> configuration;

        String name;

        Object[] args;

        Class[] types;

        Object returnType;

        String scenarioId;

        IStepDescriptor stepDescriptor;



        public Builder setScenarioId(String scenarioId) {
            this.scenarioId = scenarioId;
            return this;
        }

        public Builder setTypes(Class[] types) {
            this.types = types;
            return this;
        }

        public Builder setReturnType(Object returnType) {
            this.returnType = returnType;
            return this;
        }

        public Builder setName(String name) {
            this.name = name;
            return this;
        }

        public Builder setCallOptions(HashMap<String, Object> callOptions) {
            this.callOptions = callOptions;
            return this;
        }

        public Builder setConfiguration(HashMap<String, Object> configuration) {
            this.configuration = configuration;
            return this;
        }



        public Builder setArgs(Object[] args) {
            this.args = args;
            return this;
        }



        public Builder setStepDescriptor(IStepDescriptor stepDescriptor) {
            this.stepDescriptor = stepDescriptor;
            return this;
        }


        public StepDescriptorBuilder build(){
            stepDescriptor.setCallOptions(callOptions);
            stepDescriptor.setArgs(args);
            stepDescriptor.setFullMethodName(name);
            stepDescriptor.setTypes(types);
            stepDescriptor.setScenarioId(scenarioId);
            return new StepDescriptorBuilder(stepDescriptor.build());
        }
    }
}
