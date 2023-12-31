package org.softauto.tester;

import org.softauto.core.CallFuture;
import org.apache.logging.log4j.Marker;
import org.apache.logging.log4j.MarkerManager;
import org.softauto.core.Configuration;
import org.softauto.core.Context;
import org.softauto.plugin.ProviderManager;
import org.softauto.plugin.api.Provider;
import org.softauto.serializer.Serializer;
import org.softauto.serializer.service.Message;
import org.softauto.serializer.service.MessageBuilder;
import org.softauto.system.SystemState;
import java.util.Arrays;
import java.util.HashMap;

public class InvocationHandler {

    private  org.apache.logging.log4j.Logger logger = org.apache.logging.log4j.LogManager.getLogger(InvocationHandler.class);

    private static final Marker JDRY = MarkerManager.getMarker("JDRY");

    public <T> void invoke(String methodName, Object[] args, Class[] types, org.softauto.core.Callback<T> callback, String transceiver)  {
        try {
            Provider provider = ProviderManager.provider(transceiver).create();
            logger.debug(JDRY,"invoke method " + methodName+ " using protocol "+ transceiver);
            provider.exec( methodName, callback,null,args,types,null,SystemState.getInstance().getScenarioId());
            //logger.debug(JDRY,"callback value  get error "+callback.getError());
        } catch (Throwable e) {
            logger.error(JDRY,"fail invoke method "+ methodName+ " with args "+ Arrays.toString(args),e);
        }

    }



    public <T> void invoke(String methodName, Object[] args, Class[] types, org.softauto.core.Callback<T> callback, String transceiver, HashMap<String,Object> callOptions)  {
        try {
            Provider provider = ProviderManager.provider(transceiver).create();
            logger.debug(JDRY,"invoke method " + methodName+ " using protocol "+ transceiver);
            provider.exec( methodName, callback,null,args,types,callOptions,SystemState.getInstance().getScenarioId());
        } catch (Throwable e) {
            logger.error(JDRY,"fail invoke method "+ methodName+ " with args "+ Arrays.toString(args),e);
        }

    }

    public  Object invoke(String methodName, Object[] args, Class[] types,  String transceiver, HashMap<String,Object> callOptions)  {
        try {
            Provider provider = ProviderManager.provider(transceiver).create();
            logger.debug(JDRY,"invoke method " + methodName+ " using protocol "+ transceiver);
            return provider.exec( methodName, null,args,types,callOptions, SystemState.getInstance().getScenarioId());
        } catch (Throwable e) {
            logger.error("fail invoke method "+ methodName+ " with args "+ Arrays.toString(args),e);
        }
        return null;
    }

    public <T> T invoke(String methodName, Object[] args, Class[] types)  throws Exception{
        try {
            String host = Configuration.get(Context.SERIALIZER_HOST).asString();
            int port = Configuration.get(Context.SERIALIZER_PORT).asInteger();
            Serializer serializer = new Serializer().setHost(host).setPort(port).build();
            Message message = MessageBuilder.newBuilder().setScenarioId(SystemState.getInstance().getScenarioId()).setDescriptor(methodName).setArgs(args).setTypes(types).build().getMessage();
            return (T) serializer.write(message);

        } catch (Throwable e) {
            logger.error(JDRY,"fail invoke method "+ methodName+ " with args "+ Arrays.toString(args),e);
        }
        return null;
    }




}
