package org.softauto.tester;

import org.apache.avro.ipc.CallFuture;
import org.softauto.core.Configuration;
import org.softauto.core.Context;

import org.softauto.plugin.ProviderManager;
import org.softauto.plugin.api.Provider;
import org.softauto.serializer.Serializer;
import org.softauto.serializer.service.Message;

import java.util.Arrays;
import java.util.HashMap;

public class InvocationHandler {

    private  org.apache.logging.log4j.Logger logger = org.apache.logging.log4j.LogManager.getLogger(InvocationHandler.class);

    public  void invoke(String methodName, Object[] args, Class[] types, CallFuture callback, String transceiver)  {
        try {
            Provider provider = ProviderManager.provider(transceiver).create();
            logger.debug("invoke method " + methodName+ " using protocol "+ transceiver);
            provider.exec( methodName, callback,null,args,types,null);
            logger.debug("callback value  get error "+callback.getError());
        } catch (Throwable e) {
            logger.error("fail invoke method "+ methodName+ " with args "+ Arrays.toString(args),e);
        }

    }



    public  void invoke(String methodName, Object[] args, Class[] types, org.apache.avro.ipc.Callback callback, String transceiver, HashMap<String,Object> callOptions)  {
        try {
            Provider provider = ProviderManager.provider(transceiver).create();
            logger.debug("invoke method " + methodName+ " using protocol "+ transceiver+ " call options "+ Arrays.toString(callOptions.entrySet().toArray()));
            provider.exec( methodName, callback,null,args,types,callOptions);
            //logger.debug("callback value  get error "+callback.getError());
        } catch (Throwable e) {
            logger.error("fail invoke method "+ methodName+ " with args "+ Arrays.toString(args),e);
        }

    }

    public  void invoke(String methodName, Object[] args, Class[] types,  String transceiver, HashMap<String,Object> callOptions)  {
        try {
            Provider provider = ProviderManager.provider(transceiver).create();
            logger.debug("invoke method " + methodName+ " using protocol "+ transceiver+ " call options "+ Arrays.toString(callOptions.entrySet().toArray()));
            provider.exec( methodName, null,null,args,types,callOptions);
            //logger.debug("callback value  get error "+callback.getError());
        } catch (Throwable e) {
            logger.error("fail invoke method "+ methodName+ " with args "+ Arrays.toString(args),e);
        }

    }

    public <T> T invoke(String methodName, Object[] args, Class[] types)  throws Exception{
        CallFuture<T> future = new CallFuture<>();
        //T t = null;
        try {
            String host = Configuration.get(Context.SERIALIZER_HOST).asString();
            int port = Configuration.get(Context.SERIALIZER_PORT).asInteger();
            Serializer serializer = new Serializer().setHost(host).setPort(port).build();
            Message message = Message.newBuilder().setDescriptor(methodName).setArgs(args).setTypes(types).build();
            return (T) serializer.write(message);

        } catch (Throwable e) {
            logger.error("fail invoke method "+ methodName+ " with args "+ Arrays.toString(args),e);
        }
        return null;
    }




}
