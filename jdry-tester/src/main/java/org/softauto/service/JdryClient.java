package org.softauto.service;


import org.apache.avro.ipc.Callback;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.Marker;
import org.apache.logging.log4j.MarkerManager;
import org.softauto.analyzer.model.genericItem.GenericItem;
import org.softauto.core.*;
import org.softauto.tester.Listener;
import org.softauto.tester.Step;
import java.lang.reflect.*;
import java.lang.reflect.InvocationHandler;
import java.util.Arrays;
import java.util.HashMap;
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

        private static final Marker TESTER = MarkerManager.getMarker("TESTER");

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
                if(tree.getType().equals("method")){
                   return invokeStep(tree,method,args);
                }
                if(tree.getType().equals("listener")){
                    return invokeListener(tree,method,args);
                }
            } catch (Exception e) {
                logger.error("fail invoke method " + method.getName() + " with args " + Arrays.toString(args), e);
                throw new Exception(e);
            }
            return null;
        }

        public Object invokeListener(GenericItem tree, Method method, Object[] args) throws Exception {
            Listener listener = new Listener();
            listener.setFqmn(method.getName());
            listener.setTypes(method.getParameterTypes());
            TestContext.getScenario().addListeners(method.getName());
            return listener;
        }

        public Object invokeStep(GenericItem tree, Method method, Object[] args) throws Exception {
            try {
                //GenericItem tree = JdryUtils.getStep(method,analyze);
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
                            if (TestContext.getStepState().equals(StepLifeCycle.START)) {
                                return invokeUnaryMethod(method, args);
                            } else {
                                TestContext.setStepState(StepLifeCycle.SKIP);
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
                Level level = Level.DEBUG;
                //logger.debug("invoke async request :" + method.getName());
                logger.log(level,TESTER,"invoke sync request :" + method.getName() +" transceiver: "+ transceiver+ " callOptions: "+callOptions);
                Object[] finalArgs = Arrays.copyOf(args, args.length - 1);
                Callback<Object> callback = (Callback<Object>) args[args.length - 1];
                Class[] classList  = Arrays.stream(finalTypes).map(t -> (Class)t).collect(Collectors.toList()).toArray(new Class[1]);
                //provider.exec(testDescriptor.setProvider(provider).getStep(method.getName(),finalArgs,classList,callOptions,TestContext.getScenario().getId(),Configuration.get(transceiver).asMap().get("auth").toString()),callback);
                //return  new Step(method.getName(), finalArgs, classList, transceiver, callOptions,callback);
                //IStepDescriptor stepDescriptor = testDescriptor.getStep(method.getName(),finalArgs,classList,callOptions,TestContext.getScenario().getId(),Configuration.get(transceiver).asMap().get("auth").toString());
                return  new Step(method.getName(), finalArgs, classList, transceiver, callOptions,callback);
            } else {
                Level level = Level.DEBUG;
                logger.log(level,TESTER,"invoke sync request :" + method.getName() +" transceiver: "+ transceiver+ " callOptions: "+callOptions);
                Class[] classList  = Arrays.stream(parameterTypes).map(t -> (Class)t).collect(Collectors.toList()).toArray(new Class[1]);
                //return provider.exec(testDescriptor.setProvider(provider).getStep(method.getName(),args,classList,callOptions,TestContext.getScenario().getId(),Configuration.get(transceiver).asMap().get("auth").toString()));
                //return testDescriptor.getStep(method.getName(),args,classList,callOptions,TestContext.getScenario().getId(),Configuration.get(transceiver).asMap().get("auth").toString());
                return new Step(method.getName(), args, method.getParameterTypes(), transceiver, callOptions);
            }
        }
    }
}
