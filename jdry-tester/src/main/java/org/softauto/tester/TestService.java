package org.softauto.tester;


import io.grpc.ClientCall;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import io.grpc.MethodDescriptor;
import io.grpc.stub.ClientCalls;
import io.grpc.stub.StreamObserver;
import jakarta.ws.rs.core.Response;
import org.apache.avro.AvroRemoteException;
import org.apache.avro.AvroRuntimeException;
import org.apache.avro.ipc.CallFuture;
import org.apache.avro.ipc.Callback;
import org.apache.commons.lang3.ClassUtils;
import org.softauto.core.CallbackToResponseStreamObserverAdpater;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.lang.reflect.Type;
import java.util.Arrays;
import java.util.HashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.stream.Collectors;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * generic client that get the request and execute it using the correct protocol provider
 * using the data from the protocol schema, can execute async & sync request.
 * this client is base on AVRO grpc client impl. for more detail on avro client see avro doc for grpc .
 * for the differents between this impl and avro see wiki .
 */
public class TestService {

    private static final org.apache.logging.log4j.Logger logger = org.apache.logging.log4j.LogManager.getLogger(TestService.class);

    public Object tests;

    public static ServiceInvocationHandler serviceInvocationHandler;



    private static Object result;

    public Object getTests() {
        return tests;
    }

    public static Object getResult() {
        return TestService.result;
    }

    public static void setResult(Object result) {
        TestService.result = result;
    }

    public ServiceInvocationHandler getServiceInvocationHandler() {
        return serviceInvocationHandler;
    }


    public TestService create(Class<?> iface) {
        return new TestService(iface);
    }

    public TestService() {

    }

    public TestService(Class<?> iface) {
        serviceInvocationHandler = new ServiceInvocationHandler(iface);
        tests =  Proxy.newProxyInstance(iface.getClassLoader(), new Class[]{iface}, serviceInvocationHandler);
        logger.debug("tester proxy instance create successfully ");

    }

    public   static class ServiceInvocationHandler implements InvocationHandler {
        Class<?> iface;
        HashMap<String,Object> callOptions;
        String transceiver;
        private Object result;
        public ExecutorService executor = Executors.newSingleThreadExecutor();

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
            return new Step().setResult(result);
        }




        private Object invokeUnaryMethod(Method method, Object[] args) throws Exception {
            Type[] parameterTypes = method.getParameterTypes();
            if ((parameterTypes.length > 0) && (parameterTypes[parameterTypes.length - 1] instanceof Class)
                    && org.apache.avro.ipc.CallFuture.class.isAssignableFrom(((Class<?>) parameterTypes[parameterTypes.length - 1]))) {
                Type[] finalTypes = Arrays.copyOf(parameterTypes, parameterTypes.length - 1);
                // get the callback argument from the end
                logger.debug("invoke async request :" + method.getName());
                Object[] finalArgs = Arrays.copyOf(args, args.length - 1);
                Callback<?> callback = (Callback<?>) args[args.length - 1];
                Class[] classList  = Arrays.stream(finalTypes).map(t -> (Class)t).collect(Collectors.toList()).toArray(new Class[1]);
                //return new Step(method.getName(),finalArgs,classList,transceiver,callOptions,callback);
                unaryRequest(method.getName(), finalArgs, callback,classList);
                return null;
            } else {
                logger.debug("invoke sync request :" + method.getName());
                Class[] classList  = Arrays.stream(parameterTypes).map(t -> (Class)t).collect(Collectors.toList()).toArray(new Class[1]);
               //return new Step(method.getName(),args,classList,transceiver,callOptions);
                return unaryRequest(method.getName(), args,classList);
             }
        }

        private Object unaryRequest(String methodName, Object[] args,Class[] classList) throws Exception {
            CallFuture<Object> callFuture = new CallFuture<>();
            unaryRequest(methodName, args, callFuture,classList);
            try {
                return callFuture.get();
            } catch (Exception e) {
                if (e.getCause() instanceof Exception) {
                    throw (Exception) e.getCause();
                }
                throw new AvroRemoteException(e.getCause());
            }
        }

        private <RespT> void unaryRequest(String methodName, Object[] args, org.apache.avro.ipc.Callback<RespT> callback,Class[] classList) throws Exception {
            new Step(methodName, args, classList, transceiver, callOptions, callback);
        }


/*
        private <RespT> void unaryRequest(String methodName, Object[] args, org.apache.avro.ipc.Callback<RespT> callback) throws Exception {
            ManagedChannel channel = ManagedChannelBuilder.forAddress("localhost", 8080).usePlaintext()
                    .build();
            StreamObserver<Object> observerAdpater = new CallbackToResponseStreamObserverAdpater(callback);
            ClientCalls.asyncUnaryCall(
                    channel.newCall(serviceDescriptor.getMethod(methodName, MethodDescriptor.MethodType.UNARY), callOptions),
                    args, observerAdpater);
        }

        public static <ReqT, RespT> void asyncUnaryCall(
                ClientCall<ReqT, RespT> call, ReqT req, StreamObserver<RespT> responseObserver) {
            checkNotNull(responseObserver, "responseObserver");
            asyncUnaryRequestCall(call, req, responseObserver, false);
        }


        private static <ReqT, RespT> void asyncUnaryRequestCall(
                ClientCall<ReqT, RespT> call, ReqT req, StreamObserver<RespT> responseObserver,
                boolean streamingResponse) {
            asyncUnaryRequestCall(
                    call,
                    req,
                    new ClientCalls.StreamObserverToCallListenerAdapter<>(
                            responseObserver,
                            new ClientCalls.CallToStreamObserverAdapter<>(call, streamingResponse)));
        }



        private static class CallbackToResponseStreamObserverAdpater<T> implements StreamObserver<Object> {
            private final Callback<T> callback;

            CallbackToResponseStreamObserverAdpater(Callback<T> callback) {
                this.callback = callback;
            }

            @Override
            public void onNext(Object value) {
                if (value instanceof Throwable) {
                    callback.handleError((Throwable) value);
                } else {
                    callback.handleResult((T) value);
                }
            }

            @Override
            public void onError(Throwable t) {
                callback.handleError(new AvroRuntimeException(t));
            }

            @Override
            public void onCompleted() {
                // do nothing as there is no equivalent in Callback.
            }
        }


 */
    }
}
