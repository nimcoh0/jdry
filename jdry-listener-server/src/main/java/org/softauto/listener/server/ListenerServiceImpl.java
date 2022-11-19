package org.softauto.listener.server;


import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.Marker;
import org.apache.logging.log4j.MarkerManager;
import org.softauto.core.*;
import org.softauto.serializer.service.Message;
import org.softauto.serializer.service.SerializerService;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;

public class ListenerServiceImpl implements SerializerService{

    private static final org.apache.logging.log4j.Logger logger = org.apache.logging.log4j.LogManager.getLogger(ListenerServiceImpl.class);
    private Class listener = null;

    public ListenerServiceImpl(){

        //this.listener = Configuration.getAsClass(Context.LISTENER_SERVICE_IMPL,Class.class);
        this.listener = org.softauto.core.Utils.getClazz(Configuration.get(org.softauto.core.Context.TEST_INFRASTRUCTURE_PATH), org.softauto.core.Context.LISTENER_SERVICE_IMPL);
        if(listener == null) {
               logger.warn("listener file not found , using only function listener ");
        }

    }


    @Override
    public synchronized Object execute(ByteBuffer mes) throws Exception {
        Object methodResponse = null;
        String newContent = new String(mes.array(), StandardCharsets.UTF_8);
        Message message = new ObjectMapper().readValue(newContent,Message.class);

        try {
            if(message.getDescriptor().equals("log") || message.getDescriptor().equals("logError")){
                printLog(message);
                return null;
            }
            if (org.softauto.core.Context.getTestState().equals(TestLifeCycle.START)) {

                logger.debug("execute message " + message.toJson());
                Object o = ListenerObserver.getInstance().getLastChannel(message.getDescriptor());
                if (o == null) {
                    logger.debug(message.getDescriptor() + " not found in Observer . using " + message.getDescriptor());
                    if(listener != null) {
                        Class c = org.softauto.core.Utils.getSubClass(listener.getDeclaredClasses(), "tests.infrastructure.Listener$" + message.getDescriptor());
                        if (c == null) {
                            logger.warn("class not found" + message.getDescriptor());
                            return message.getArgs();
                            //throw new Exception("fail getting class " + message.getDescriptor());
                        } else {
                            o = c.newInstance();
                        }
                    }else {
                        logger.warn("class not found" + message.getDescriptor());
                        return message.getArgs();
                    }
                }
                if (o instanceof Function) {
                    return handleFunction(o,message);
                }else {
                    Method method = org.softauto.core.Utils.getMethod2(o, message.getDescriptor(), message.getTypes());
                    if (method == null) {
                        logger.error("no method found for " + message.getDescriptor() + " types " + Utils.result2String(message.getTypes()) + " on " + o.getClass().getName());
                        throw new Exception("method not found for " + message.getDescriptor());
                    }
                    logger.debug("invoking " + message.getDescriptor() + " args " + Utils.result2String(message.getArgs()));
                    method.setAccessible(true);
                    if (Modifier.isStatic(method.getModifiers())) {
                        methodResponse = method.invoke(null, message.getArgs());
                    } else {
                        methodResponse = method.invoke(o, message.getArgs());
                    }
                }
            }else {
                return new Object[]{message.getArgs()};
            }
            } catch(InvocationTargetException e){
                logger.error("fail invoke method " + message.getDescriptor(), e);

            } catch(Exception e){
                logger.error("fail invoke method " + message.getDescriptor(), e);

            }



        return methodResponse;

    }

    private synchronized Object handleFunction(Object o,Message message){
        Object methodResponse = null;
        try {
                logger.debug("got function of type " + message.getState());
                methodResponse = message.getArgs();
                if (!message.getState().equals(ListenerType.BEFORE.name()) && !message.getState().equals(ListenerType.AFTER.name())) {
                    logger.error("message type not supported " + message.getState());
                    return methodResponse;
                }
            if (o instanceof FunctionBefore) {
                if (message.getState().equals(ListenerType.BEFORE.name())) {
                    logger.debug("got message Before " + message.toJson());
                    methodResponse = ((FunctionBefore) o).apply(message.getArgs());
                    logger.debug("result of function Before " + methodResponse);
                }
            }else if (o instanceof FunctionBeforeWithCondition) {
                    if (message.getState().equals(ListenerType.BEFORE.name())) {
                        logger.debug("got message Before " + message.toJson());
                        methodResponse = ((FunctionBeforeWithCondition) o).apply(message.getArgs());
                        logger.debug("result of function Before " + methodResponse);
                    }
            } else if (o instanceof FunctionAfter) {
                if (message.getState().equals(ListenerType.AFTER.name())) {
                    logger.debug("got message After " + message.toJson());
                    if(message.getArgs().length == 1){
                        methodResponse = ((FunctionAfter) o).apply(message.getArgs()[0]);
                    }else {
                        methodResponse = ((FunctionAfter) o).apply(message.getArgs());
                    }
                    logger.debug("result of function After " + methodResponse);
                }
            }
        }catch (Exception e){
            logger.error("fail handle Function "+ message.toJson(),e);
        }
        return methodResponse;
    }


    private void printLog(Message message){
        Marker marker = MarkerManager.getMarker(message.getData("marker").toString());
        if(marker == null){
            marker = MarkerManager.getMarker("SUT");
        }
        if(message.getData("ex") != null){
            logger.log(Level.getLevel(message.getData("level").toString()), marker, message.getData("log").toString(), new Exception(message.getData("ex").toString()));
        }else {
            org.apache.logging.log4j.Logger logger = org.apache.logging.log4j.LogManager.getLogger(Utils.findClass(message.getData("clazz").toString()));
            logger.debug(marker,message.getData("log").toString(), message.getData("clazz"));
        }
    }


}
