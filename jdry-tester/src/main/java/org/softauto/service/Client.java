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
                if(TestContext.getScenario().getState().equals(ScenarioState.RUN.name())) {
                    return invokeUnaryMethod(method, args);
                }else {
                    TestContext.setTestState(TestLifeCycle.SKIP);
                    logger.debug("step skip due test status  " + TestContext.getScenario().getState() +" on "+TestContext.getScenario().getProperty("method"));
                    throw new Exception("step skip due test status  " + TestContext.getScenario().getState() +" on "+TestContext.getScenario().getProperty("method"));
                }
            } catch (Exception e) {
                logger.error("fail invoke method " + method.getName() + " with args " + Arrays.toString(args), e);
                throw new Exception(e);
            }
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
                return  new Step(method.getName(), finalArgs, classList, transceiver, callOptions,callback);
            } else {
                logger.debug("invoke sync request :" + method.getName());
                Class[] classList  = Arrays.stream(parameterTypes).map(t -> (Class)t).collect(Collectors.toList()).toArray(new Class[1]);
                return new Step(method.getName(), args, method.getParameterTypes(), transceiver, callOptions);
            }
        }
    }
}
