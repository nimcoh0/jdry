package org.softauto.jaxrs.service;

public class ServiceCaller {

    public static CallerHandler call() {
        return new Call();
    }

    public interface requestMethod {
        <T> T invoke(IStepDescriptor stepDescriptor, Object[] args);
    }

    public interface UnaryClass extends requestMethod {
    }

    public static  class Call implements CallerHandler {
        //private final requestMethod method;


        //Call(requestMethod method) {
         //   this.method = method;

        //}
        @Override
        public <T> T startCall(IStepDescriptor stepDescriptor, Object[] args) {
            try {
                return (T) stepDescriptor.getMethodImpl().invoke(stepDescriptor,  args);
            }catch (Exception e){
                e.printStackTrace();
            }
            return null;
        }


    }



}
