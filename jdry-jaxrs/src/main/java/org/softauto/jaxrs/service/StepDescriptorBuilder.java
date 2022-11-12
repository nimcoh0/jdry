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



        Object[] args;



        IStepDescriptor stepDescriptor;

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
            stepDescriptor.setArgs(args);
            stepDescriptor.setConfiguration(configuration);
            //stepDescriptor.setItem(item);
            //stepDescriptor.setTest(test);
            stepDescriptor.setCallOptions(callOptions);
            return new StepDescriptorBuilder(stepDescriptor.build());
        }
    }
}
