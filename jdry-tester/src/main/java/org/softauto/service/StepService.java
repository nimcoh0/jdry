package org.softauto.service;

import org.apache.avro.AvroRemoteException;
import org.apache.avro.ipc.CallFuture;
import org.apache.avro.ipc.Callback;
import org.softauto.tester.InvocationHandler;
import org.softauto.tester.Step;

import java.lang.reflect.Method;
import java.lang.reflect.Type;
import java.util.Arrays;
import java.util.HashMap;
import java.util.stream.Collectors;

public class StepService {
    private static final org.apache.logging.log4j.Logger logger = org.apache.logging.log4j.LogManager.getLogger(Client.class);


        Class<?> iface;

        HashMap<String,Object> callOptions;

        String transceiver;

        private Object result;



        public Object getResult() {
            return result;
        }


    public StepService setCallOptions(HashMap<String, Object> callOptions) {
        this.callOptions = callOptions;
        return this;
    }

    public StepService setTransceiver(String transceiver) {
        this.transceiver = transceiver;
        return this;
    }


/*
        public Object invoke(Object proxy, Method method, Object[] args) {
            try {
                if (args == null) {
                    args = new Object[]{};
                }
                logger.debug("invoke method " + method.getName() + " with args " + Arrays.toString(args));

                result =  invokeUnaryMethod(method, args);
            } catch (Exception e) {
                logger.error("fail invoke method " + method.getName() + " with args " + Arrays.toString(args), e);
            }
            //return null;
            return new Step().setResult(result);
        }


 */

    public Object invokeUnaryMethod(String methodName ,Type[] parameterTypes , Object[] args) throws Exception {
        //Type[] parameterTypes = method.getParameterTypes();
        if ((parameterTypes.length > 0) && (parameterTypes[parameterTypes.length - 1] instanceof Class)
                && org.apache.avro.ipc.CallFuture.class.isAssignableFrom(((Class<?>) parameterTypes[parameterTypes.length - 1]))) {
            Type[] finalTypes = Arrays.copyOf(parameterTypes, parameterTypes.length - 1);
            // get the callback argument from the end
            logger.debug("invoke async request :" + methodName);
            Object[] finalArgs = Arrays.copyOf(args, args.length - 1);
            Callback<?> callback = (Callback<?>) args[args.length - 1];
            Class[] classList  = Arrays.stream(finalTypes).map(t -> (Class)t).collect(Collectors.toList()).toArray(new Class[1]);
            unaryRequest(methodName, finalArgs, callback,classList);
            return null;
        } else {
            logger.debug("invoke sync request :" + methodName);
            Class[] classList  = Arrays.stream(parameterTypes).map(t -> (Class)t).collect(Collectors.toList()).toArray(new Class[1]);
            return unaryRequest(methodName, args,classList);
        }
    }

/*
        public Object invokeUnaryMethod(Method method, Object[] args) throws Exception {
            Type[] parameterTypes = method.getParameterTypes();
            if ((parameterTypes.length > 0) && (parameterTypes[parameterTypes.length - 1] instanceof Class)
                    && org.apache.avro.ipc.CallFuture.class.isAssignableFrom(((Class<?>) parameterTypes[parameterTypes.length - 1]))) {
                Type[] finalTypes = Arrays.copyOf(parameterTypes, parameterTypes.length - 1);
                // get the callback argument from the end
                logger.debug("invoke async request :" + method.getName());
                Object[] finalArgs = Arrays.copyOf(args, args.length - 1);
                Callback<?> callback = (Callback<?>) args[args.length - 1];
                Class[] classList  = Arrays.stream(finalTypes).map(t -> (Class)t).collect(Collectors.toList()).toArray(new Class[1]);
                unaryRequest(method.getName(), finalArgs, callback,classList);
                return null;
            } else {
                logger.debug("invoke sync request :" + method.getName());
                Class[] classList  = Arrays.stream(parameterTypes).map(t -> (Class)t).collect(Collectors.toList()).toArray(new Class[1]);
                return unaryRequest(method.getName(), args,classList);
            }
        }


 */
        private Object unaryRequest(String methodName, Object[] args,Class[] classList) throws Exception {
            CallFuture<Object> callFuture = new CallFuture<>();
            unaryRequest(methodName, args, callFuture,classList);
            try {
                //return step;
                return callFuture.get();
            } catch (Exception e) {
                if (e.getCause() instanceof Exception) {
                    throw (Exception) e.getCause();
                }
                throw new AvroRemoteException(e.getCause());
            }

        }

        private <RespT> void unaryRequest(String methodName, Object[] args, org.apache.avro.ipc.Callback<RespT> callback,Class[] classList) throws Exception {
            new InvocationHandler().invoke(methodName, args, classList, callback, transceiver, callOptions);
            //return new Step(methodName, args, classList, transceiver, callOptions, callback,"");
        }

}
