package org.softauto.listener;


import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.Marker;
import org.apache.logging.log4j.MarkerManager;
import org.softauto.core.*;
import org.softauto.serializer.service.Message;
import org.softauto.serializer.service.SerializerService;
import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;

public class ListenerServiceImpl implements SerializerService{

    private static final org.apache.logging.log4j.Logger logger = org.apache.logging.log4j.LogManager.getLogger(ListenerServiceImpl.class);

    @Override
    public synchronized Object execute(ByteBuffer mes) throws Exception {
        Object methodResponse = null;
        String newContent = new String(mes.array(), StandardCharsets.UTF_8);
        Message message = new ObjectMapper().readValue(newContent,Message.class);

        try {
            Object[] args = message.getArgs();
            Class[] types = message.getTypes();
            for(int i=0;i<args.length;i++){
                String json = new ObjectMapper().writeValueAsString(args[i]);
                args[i] = new ObjectMapper().readValue(json,types[i]);
            }
            if (message.getDescriptor().equals("log") || message.getDescriptor().equals("logError")) {
                printLog(message);
                return null;
            }
            if (Context.getTestState().equals(TestLifeCycle.START)) {
                logger.debug("execute message " + message.toJson());
                Object o = ListenerObserver.getInstance().getLastChannel(message.getDescriptor());
                if (o != null) {
                    methodResponse = message.getArgs();
                    if (o instanceof FunctionBefore) {
                        if (message.getState().equals(ListenerType.BEFORE.name())) {
                            logger.debug("got message Before " + message.toJson());
                            methodResponse = ((FunctionBefore) o).apply(message.getArgs());
                            logger.debug("result of function Before " + methodResponse);
                        }
                    }
                    if (o instanceof FunctionAfter) {
                        if (message.getState().equals(ListenerType.AFTER.name())) {
                            logger.debug("got message After " + message.toJson());
                            if (message.getArgs().length == 1) {
                                methodResponse = ((FunctionAfter) o).apply(message.getArgs()[0]);
                            } else {
                                methodResponse = ((FunctionAfter) o).apply(message.getArgs());
                            }
                            logger.debug("result of function After " + methodResponse);
                        }
                    }


                }else {
                    byte[] m = new ObjectMapper().writeValueAsBytes(message.getArgs());
                    ByteBuffer byteBuffer = ByteBuffer.wrap(m);
                    return byteBuffer;

                }
            }else {
                byte[] m = new ObjectMapper().writeValueAsBytes(message.getArgs());
                ByteBuffer byteBuffer = ByteBuffer.wrap(m);
                return byteBuffer;

            }
        } catch(Exception e){
            logger.error("fail invoke method " + message.getDescriptor(), e);
        }
        byte[] m = new ObjectMapper().writeValueAsBytes(methodResponse);
        ByteBuffer byteBuffer = ByteBuffer.wrap(m);
        return byteBuffer;
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
