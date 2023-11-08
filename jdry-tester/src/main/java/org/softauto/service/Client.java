package org.softauto.service;


import org.apache.avro.AvroRemoteException;
import org.apache.avro.ipc.CallFuture;
import org.apache.avro.ipc.Callback;
import org.softauto.core.ScenarioState;
import org.softauto.core.TestContext;
import org.softauto.core.TestLifeCycle;
import org.softauto.tester.Step;
import org.testng.ITestResult;
import org.testng.Reporter;

import java.lang.reflect.*;
import java.util.Arrays;
import java.util.HashMap;
import java.util.stream.Collectors;

public class Client {

    private static final org.apache.logging.log4j.Logger logger = org.apache.logging.log4j.LogManager.getLogger(Client.class);

    private Object tests;

    private Class<?> iface;



    private HashMap<String,Object> callOptions = new HashMap<>();

    private String transceiver;

    public Object getTests() {
        return tests;
    }

    public Client setCallOptions(HashMap<String, Object> callOptions) {
        this.callOptions = callOptions;
        return this;
    }

    public Client setTransceiver(String transceiver) {
        this.transceiver = transceiver;
        return this;
    }

    public static Client create(Class<?> iface) {
       return new Client(iface);
    }

    public Client(Class<?> iface) {
        this.iface = iface;
    }



    public Client build(){
        ServiceInvocationHandler serviceInvocationHandler = new ServiceInvocationHandler(iface).setCallOptions(callOptions).setTransceiver(transceiver);
        tests =  Proxy.newProxyInstance(iface.getClassLoader(), new Class[]{iface}, serviceInvocationHandler);
        logger.debug("tester proxy instance create successfully ");
        return this;
    }

    public    static class ServiceInvocationHandler implements InvocationHandler {

        Class<?> iface;

        HashMap<String,Object> callOptions;

        String transceiver;

        private Object result;

        public ServiceInvocationHandler(Class<?> iface){
            this.iface = iface;
        }

        public Object getResult() {
            return result;
        }

        public ServiceInvocationHandler setCallOptions(HashMap<String, Object> callOptions) {
            this.callOptions = callOptions;
            return this;
        }

        public ServiceInvocationHandler setTransceiver(String transceiver) {
            this.transceiver = transceiver;
            return this;
        }



        @Override
        public Object invoke(Object proxy, Method method, Object[] args) throws Exception {
            try {
                if (args == null) {
                    args = new Object[]{};
                }
                logger.debug("invoke method " + method.getName() + " with args " + Arrays.toString(args));
                //return new Step(method.getName(), args, method.getParameterTypes(), transceiver, callOptions);
                if(TestContext.getScenario().getState().equals(ScenarioState.RUN.name())) {
                    return invokeUnaryMethod(method, args);
                }else {
                    //Client.testResult.setStatus(ITestResult.SKIP);
                   // Reporter.getCurrentTestResult().setStatus(ITestResult.SKIP);
                    TestContext.setTestState(TestLifeCycle.SKIP);
                    logger.debug("step skip due test status  " + TestContext.getScenario().getState() +" on "+TestContext.getScenario().getProperty("method"));
                    throw new Exception("step skip due test status  " + TestContext.getScenario().getState() +" on "+TestContext.getScenario().getProperty("method"));
                }
            } catch (Exception e) {
                logger.error("fail invoke method " + method.getName() + " with args " + Arrays.toString(args), e);
                throw new Exception(e);
            }
            //return null;
            //return new Step().setResult(result);
            //return this;
        }





        private Object invokeUnaryMethod(Method method, Object[] args) throws Exception {
            Type[] parameterTypes = method.getParameterTypes();
            if ((parameterTypes.length > 0) && (parameterTypes[parameterTypes.length - 1] instanceof Class)
                    && org.apache.avro.ipc.CallFuture.class.isAssignableFrom(((Class<?>) parameterTypes[parameterTypes.length - 1]))) {
                Type[] finalTypes = Arrays.copyOf(parameterTypes, parameterTypes.length - 1);
                // get the callback argument from the end
                logger.debug("invoke async request :" + method.getName());
                Object[] finalArgs = Arrays.copyOf(args, args.length - 1);
                Callback<Object> callback = (Callback<Object>) args[args.length - 1];
                Class[] classList  = Arrays.stream(finalTypes).map(t -> (Class)t).collect(Collectors.toList()).toArray(new Class[1]);
                //unaryRequest(method.getName(), finalArgs, callback,classList);
                //Class[] classList  = Arrays.stream(finalTypes).map(t -> (Class)t).collect(Collectors.toList()).toArray(new Class[1]);
                return  new Step(method.getName(), finalArgs, classList, transceiver, callOptions,callback);

            } else {
                logger.debug("invoke sync request :" + method.getName());
                Class[] classList  = Arrays.stream(parameterTypes).map(t -> (Class)t).collect(Collectors.toList()).toArray(new Class[1]);
                return new Step(method.getName(), args, method.getParameterTypes(), transceiver, callOptions);
                //return unaryRequest(method.getName(), args,classList);
             }
        }

        private Object unaryRequest(String methodName, Object[] args,Class[] classList) throws Exception {
            CallFuture<Object> callFuture = new CallFuture<>();
            unaryRequest(methodName, args, callFuture,classList);
            try {
                //return step;
                return callFuture.getResult();
            } catch (Exception e) {
                if (e.getCause() instanceof Exception) {
                    throw (Exception) e.getCause();
                }
                throw new AvroRemoteException(e.getCause());
            }

        }

        private <RespT> Step unaryRequest(String methodName, Object[] args, org.apache.avro.ipc.Callback<Object> callback,Class[] classList) throws Exception {
           return new Step(methodName, args, classList, transceiver, callOptions, callback);
        }


    }
}
