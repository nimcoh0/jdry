package org.softauto.service;


import com.fasterxml.jackson.databind.JsonNode;
import io.grpc.CallOptions;
import org.apache.avro.ipc.Callback;
import org.softauto.analyzer.model.genericItem.GenericItem;
import org.softauto.core.*;
import org.softauto.plugin.ProviderManager;
import org.softauto.plugin.api.Provider;
import org.softauto.plugin.spi.PluginProvider;
import org.softauto.tester.Step;
import java.lang.reflect.*;
import java.lang.reflect.InvocationHandler;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

public class JdryClient {

    private static final org.apache.logging.log4j.Logger logger = org.apache.logging.log4j.LogManager.getLogger(JdryClient.class);

    //private Object tests;

    private Class<?> iface;



    //private HashMap<String,Object> callOptions = new HashMap<>();

    //private String transceiver;

   // public Object getTests() {
       // return tests;
   // }

    /*
    public JdryClient setCallOptions(HashMap<String, Object> callOptions) {
        this.callOptions = callOptions;
        return this;
    }

    public JdryClient setTransceiver(String transceiver) {
        this.transceiver = transceiver;
        return this;
    }

     */

    //public static Client create(Class<?> iface) {
      // return new Client(iface);
    //}

    public JdryClient(Class<?> iface) {
        this.iface = iface;
    }

    public static <T> T create(Class<T> iface) {
        //T t = null;
        Analyze analyze = JdryUtils.getAnalyze(iface);
        TestDescriptor testDescriptor = TestDescriptor.create(iface);
        //t = (T) Proxy.newProxyInstance(iface.getClassLoader(), new Class[]{iface}, (proxy, method, methodArgs) -> {
           // return (new ServiceInvocationHandler(analyze,testDescriptor)).invoke(proxy, method, methodArgs);
        //});

        //return t;
        ServiceInvocationHandler proxyHandler = new ServiceInvocationHandler(analyze,testDescriptor);
        return (T) Proxy.newProxyInstance(iface.getClassLoader(), new Class[] { iface }, proxyHandler);
       // return (T) Proxy.newProxyInstance(iface.getClassLoader(), new Class[] { iface }, proxyHandler);
    }

   /*
    public JdryClient build(){
        ServiceInvocationHandler serviceInvocationHandler = new ServiceInvocationHandler(iface).setCallOptions(callOptions).setTransceiver(transceiver);
        tests =  Proxy.newProxyInstance(iface.getClassLoader(), new Class[]{iface}, serviceInvocationHandler);
        logger.debug("tester proxy instance create successfully ");
        return this;
    }

    */



    public    static class ServiceInvocationHandler implements InvocationHandler {

       // Class<?> iface;

        HashMap<String,Object> callOptions;

        String transceiver;

        //Provider provider;

        //IChannelDescriptor channelDescriptor;

        TestDescriptor testDescriptor;

        //private Object result;

        Analyze analyze;

       // public ServiceInvocationHandler(Class<?> iface){
          //  this.iface = iface;
       // }

        public ServiceInvocationHandler(Analyze analyze,TestDescriptor testDescriptor){
           this.testDescriptor = testDescriptor;
           this.analyze = analyze;
        }

        //public Object getResult() {
           // return result;
        //}




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
                GenericItem tree = JdryUtils.getStep(method,analyze);
                transceiver = JdryUtils.getTransceiver(tree);
                callOptions = JdryUtils.getCallOption(tree);
               // for (PluginProvider plugin : ProviderManager.providers()) {
                  //  if(plugin.getName().equals(transceiver)) {
                       // provider = plugin.create();
                        //if (provider.getType() != null && provider.getType().equals("protocol") ) {
                            if (args == null) {
                                args = new Object[]{};
                            }
                            logger.debug("invoke method " + method.getName() + " with args " + Arrays.toString(args));
                            if (TestContext.getScenario().getState().equals(ScenarioState.RUN.name())) {
                                return invokeUnaryMethod(method, args);
                            } else {
                                TestContext.setTestState(TestLifeCycle.SKIP);
                                logger.debug("step skip due test status  " + TestContext.getScenario().getState() + " on " + TestContext.getScenario().getProperty("method"));
                                throw new Exception("step skip due test status  " + TestContext.getScenario().getState() + " on " + TestContext.getScenario().getProperty("method"));
                            }
                       // }
                    //}
               // }
            } catch (Exception e) {
                logger.error("fail invoke method " + method.getName() + " with args " + Arrays.toString(args), e);
                throw new Exception(e);
            }
           // return null;
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
                //provider.exec(testDescriptor.setProvider(provider).getStep(method.getName(),finalArgs,classList,callOptions,TestContext.getScenario().getId(),Configuration.get(transceiver).asMap().get("auth").toString()),callback);
                //return  new Step(method.getName(), finalArgs, classList, transceiver, callOptions,callback);
                //IStepDescriptor stepDescriptor = testDescriptor.getStep(method.getName(),finalArgs,classList,callOptions,TestContext.getScenario().getId(),Configuration.get(transceiver).asMap().get("auth").toString());
                return  new Step(method.getName(), finalArgs, classList, transceiver, callOptions,callback);
            } else {
                logger.debug("invoke sync request :" + method.getName());
                Class[] classList  = Arrays.stream(parameterTypes).map(t -> (Class)t).collect(Collectors.toList()).toArray(new Class[1]);
                //return provider.exec(testDescriptor.setProvider(provider).getStep(method.getName(),args,classList,callOptions,TestContext.getScenario().getId(),Configuration.get(transceiver).asMap().get("auth").toString()));
                //return testDescriptor.getStep(method.getName(),args,classList,callOptions,TestContext.getScenario().getId(),Configuration.get(transceiver).asMap().get("auth").toString());
                return new Step(method.getName(), args, method.getParameterTypes(), transceiver, callOptions);
            }
        }
    }
}
