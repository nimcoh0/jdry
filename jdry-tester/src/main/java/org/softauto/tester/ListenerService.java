package org.softauto.tester;

import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

public class ListenerService {

    private static final org.apache.logging.log4j.Logger logger = org.apache.logging.log4j.LogManager.getLogger(ListenerService.class);

    public Object listeners;

    public List<String> listenerNames = new ArrayList<>();

    public static ListenerService.ServiceInvocationHandler serviceInvocationHandler;

    public Object getListeners() {
        return listeners;
    }

    public List<String> getListenerNames() {
        return listenerNames;
    }

    public ListenerService.ServiceInvocationHandler getServiceInvocationHandler() {
        return serviceInvocationHandler;
    }

    public ListenerService create(Class<?> iface) {
        return new ListenerService(iface);
    }

    public ListenerService()  {
        listenerNames.add("aa");
        listenerNames.add("bb");
    }

    public ListenerService(Class<?> iface) {
        serviceInvocationHandler = new ListenerService.ServiceInvocationHandler(iface);
        listeners =  Proxy.newProxyInstance(iface.getClassLoader(), new Class[]{iface}, serviceInvocationHandler);
        logger.debug("tester proxy instance create successfully ");

    }



    public   static class ServiceInvocationHandler implements java.lang.reflect.InvocationHandler {
        Class<?> iface;


        public ServiceInvocationHandler(Class<?> iface){
            this.iface = iface;
        }

        public Listener register(Function function, String fqmn, Class... types) throws Exception {
            return new Test().addListener(function,fqmn, types);
        }

        ServiceInvocationHandler(Class<?> iface, String transceiver, HashMap<String,Object> callOptions ) {
            this.iface = iface;
            //this.callOptions = callOptions;
            //this.transceiver = transceiver;
        }

        //@Override
        public Object invoke(Object proxy, Method method, Object[] args) {
            try {
                if (args == null) {
                    args = new Object[]{};
                }
                logger.debug("invoke method " + method.getName() + " with args " + Arrays.toString(args));
                return invokeUnaryMethod(method, args);
            } catch (Exception e) {
                logger.error("fail invoke method " + method.getName() + " with args " + Arrays.toString(args), e);
            }
            return null;
        }


        private Object invokeUnaryMethod(Method method, Object[] args) throws Exception {
            Type[] parameterTypes = method.getParameterTypes();
            if ((parameterTypes.length > 0) && (parameterTypes[parameterTypes.length - 1] instanceof Class)
                    && org.softauto.core.CallFuture.class.isAssignableFrom(((Class<?>) parameterTypes[parameterTypes.length - 1]))) {
                Type[] finalTypes = Arrays.copyOf(parameterTypes, parameterTypes.length - 1);
                // get the callback argument from the end
                logger.debug("invoke async request :" + method.getName());
                Object[] finalArgs = Arrays.copyOf(args, args.length - 1);
                org.apache.avro.ipc.CallFuture<?> callback = (org.apache.avro.ipc.CallFuture<?>) args[args.length - 1];
                unaryRequest(method.getName(), callback, finalArgs, finalTypes);
                return null;
            } else {
                logger.debug("invoke sync request :" + method.getName());
                return unaryRequest(method.getName(), args, parameterTypes);

            }
        }

        private Object unaryRequest(String methodName, Object[]  args,Type[] parameterTypes) throws Exception {
            org.apache.avro.ipc.CallFuture<?> callFuture = new org.apache.avro.ipc.CallFuture<>();
            unaryRequest(methodName, callFuture, args,parameterTypes);
            return callFuture.get();
        }


        private <RespT> void unaryRequest(String methodName, org.apache.avro.ipc.CallFuture<RespT> callback, Object[]  args, Type[] types) throws Exception {
            //Provider provider = ProviderManager.provider(transceiver).create();
            Class[] classList  = Arrays.stream(types).map(t -> (Class)t).collect(Collectors.toList()).toArray(new Class[1]);
           // new org.softauto.tester.InvocationHandler().invoke(methodName,args,classList,callback,transceiver,callOptions);
        }

    }
}
