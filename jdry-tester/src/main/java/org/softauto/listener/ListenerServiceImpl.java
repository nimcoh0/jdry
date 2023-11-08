package org.softauto.listener;


import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.Marker;
import org.apache.logging.log4j.MarkerManager;
import org.softauto.annotations.ListenerType;
import org.softauto.core.*;
import org.softauto.serializer.service.Message;
import org.softauto.serializer.service.SerializerService;
import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;

public class ListenerServiceImpl implements SerializerService {

    private static final org.apache.logging.log4j.Logger logger = org.apache.logging.log4j.LogManager.getLogger(ListenerServiceImpl.class);

    ObjectMapper objectMapper;

    public ListenerServiceImpl(){
        objectMapper =  new ObjectMapper();
        objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
    }

    //@Override
    public synchronized Object execute(ByteBuffer mes) throws Exception {
        Object methodResponse = null;
        String newContent = new String(mes.array(), StandardCharsets.UTF_8);
        Message message = objectMapper.readValue(newContent,Message.class);

        try {
            Object[] args = message.getArgs();
            Class[] types = message.getTypes();
            for(int i=0;i<args.length;i++){
                String json = objectMapper.writeValueAsString(args[i]);
                args[i] = objectMapper.readValue(json,types[i]);
            }
            if (message.getDescriptor().equals("log") || message.getDescriptor().equals("logError")) {
                printLog(message);
                return null;
            }
            if (TestContext.getTestState().equals(TestLifeCycle.START)) {
                logger.debug("execute message " + message.toJson());
                Object o = ListenerObserver.getInstance().getLastChannel(message.getDescriptor());
                if (o != null) {
                    methodResponse = message.getArgs();
                    //if (o instanceof Exec) {
                        if (message.getState().equals(ListenerType.BEFORE.name())) {
                            logger.debug("got message Before " + message.toJson());
                            methodResponse = ((org.softauto.listener.Function) o).apply(message.getArgs());
                            logger.debug("result of function Before " + methodResponse);
                            //byte[] m = objectMapper.writeValueAsBytes(methodResponse);
                            //ByteBuffer byteBuffer = ByteBuffer.wrap(m);
                            //return byteBuffer;
                        }
                    //}
                    //if (o instanceof Exec) {
                        if (message.getState().equals(ListenerType.AFTER.name())) {
                            logger.debug("got message After " + message.toJson());
                            if (message.getArgs().length == 1) {
                                methodResponse = ((org.softauto.listener.Function) o).apply(message.getArgs()[0]);
                            } else {
                                methodResponse = ((org.softauto.listener.Function) o).apply(message.getArgs());
                            }
                            logger.debug("result of function After " + methodResponse);
                        }


                    //}


                }else {
                    if (message.getState().equals("Exception")) {
                        logger.debug("got message Exception " + message.toJson());
                        if (message.getArgs().length == 1) {
                            TestContext.getScenario().setState(ScenarioState.FAIL.name());
                            TestContext.getScenario().addProperty("method",message.getDescriptor());
                            TestContext.getScenario().addError(message.getArgs());
                            logger.error("scenario fail and throw exception",(Throwable) args[0]);
                            methodResponse = "";
                            //throw new Exception((Throwable)args[0]);
                        }
                        logger.debug("result of function After " + methodResponse);
                    }

                }
            }else {
               // byte[] m = objectMapper.writeValueAsBytes(message.getArgs());
               // ByteBuffer byteBuffer = ByteBuffer.wrap(m);
              //  return byteBuffer;

            }
        } catch(Exception e){
            logger.error("fail invoke method " + message.getDescriptor(), e);
           // throw (e);
        }
        byte[] m = objectMapper.writeValueAsBytes(methodResponse);
        ByteBuffer byteBuffer = ByteBuffer.wrap(m);
        return byteBuffer;
        //return "ok";
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
