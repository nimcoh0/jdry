package org.softauto.service;



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

    private static final Marker JDRY = MarkerManager.getMarker("JDRY");

    private Class<?> iface;

    public JdryClient(Class<?> iface) {
        this.iface = iface;
    }

    public static <T> T create(Class<T> iface) {
        Analyze analyze = JdryUtils.getAnalyze(iface);
        TestDescriptor testDescriptor = TestDescriptor.create(iface);
        ServiceInvocationHandler proxyHandler = new ServiceInvocationHandler(analyze,testDescriptor);
        return (T) Proxy.newProxyInstance(iface.getClassLoader(), new Class[] { iface }, proxyHandler);
    }

    public    static class ServiceInvocationHandler implements InvocationHandler {

        HashMap<String,Object> callOptions;

        String transceiver;

        TestDescriptor testDescriptor;

        Analyze analyze;

        public ServiceInvocationHandler(Analyze analyze,TestDescriptor testDescriptor){
           this.testDescriptor = testDescriptor;
           this.analyze = analyze;
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

                GenericItem tree = JdryUtils.getStep(method,analyze);
                if(tree.getType().equals("method")){
                   TestContext.put("step_name",method.getName());
                   TestContext.setStepState(StepLifeCycle.START);
                   Object r =  invokeStep(tree,method,args);
                   return r;
                }
                if(tree.getType().equals("listener")){
                    TestContext.put("listener_name",method.getName());
                    return invokeListener(tree,method,args);
                }
            } catch (Exception e) {
                logger.error(JDRY,"fail invoke method " + method.getName() + " with args " + Arrays.toString(args), e);
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
                transceiver = JdryUtils.getTransceiver(tree);
                callOptions = JdryUtils.getCallOption(tree);
                           if (args == null) {
                                args = new Object[]{};
                            }
                            logger.debug(JDRY,"invoke method " + method.getName() + " with args " + Arrays.toString(args));
                            if (TestContext.getScenario().getState().equals(ScenarioLifeCycle.START.name())) {
                                return  invokeUnaryMethod(method, args);
                            } else {
                                TestContext.setStepState(StepLifeCycle.SKIP);
                                logger.debug(JDRY,"step skip due test status  " + TestContext.getScenario().getState() + " on " + TestContext.getScenario().getProperty("method"));
                                throw new Exception("step skip due test status  " + TestContext.getScenario().getState() + " on " + TestContext.getScenario().getProperty("method"));
                            }
            } catch (Exception e) {
                logger.error(JDRY,"fail invoke method " + method.getName() + " with args " + Arrays.toString(args), e);
                throw new Exception(e);
            }
        }

        private Object invokeUnaryMethod(Method method, Object[] args) throws Exception {
            Type[] parameterTypes = method.getParameterTypes();
            if ((parameterTypes.length > 0) && (parameterTypes[parameterTypes.length - 1] instanceof Class)
                    && org.softauto.core.CallFuture.class.isAssignableFrom(((Class<?>) parameterTypes[parameterTypes.length - 1]))) {
                Type[] finalTypes = Arrays.copyOf(parameterTypes, parameterTypes.length - 1);
                // get the callback argument from the end
                Level level = Level.DEBUG;
                logger.log(level,JDRY,"invoke sync request :" + method.getName() +" transceiver: "+ transceiver+ " callOptions: "+callOptions);
                Object[] finalArgs = Arrays.copyOf(args, args.length - 1);
                org.softauto.core.Callback<Object> callback = (org.softauto.core.Callback<Object>) args[args.length - 1];
                Class[] classList  = Arrays.stream(finalTypes).map(t -> (Class)t).collect(Collectors.toList()).toArray(new Class[1]);
                return  new Step(method.getName(), finalArgs, classList, transceiver, callOptions,callback);
            } else {
                Level level = Level.DEBUG;
                logger.log(level,JDRY,"invoke sync request :" + method.getName() +" transceiver: "+ transceiver+ " callOptions: "+callOptions);
                return new Step(method.getName(), args, method.getParameterTypes(), transceiver, callOptions);
            }
        }


    }
}
