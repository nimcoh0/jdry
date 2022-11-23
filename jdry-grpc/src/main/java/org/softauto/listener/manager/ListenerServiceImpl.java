package org.softauto.listener.manager;

import org.softauto.core.Configuration;
import org.softauto.core.Context;
import org.softauto.core.ListenerType;
import org.softauto.core.TestLifeCycle;
import org.softauto.listener.ListenerService;
import org.softauto.serializer.Serializer;
import org.softauto.serializer.service.Message;


public class ListenerServiceImpl implements ListenerService {


    private  final org.apache.logging.log4j.Logger logger = org.apache.logging.log4j.LogManager.getLogger(ListenerServiceImpl.class);

    @Override
    public Object[] executeBefore(String methodName, Object[] args, Class[] types) throws Exception {
        Object result = null;
        try {
            if(Context.getTestState().equals(TestLifeCycle.START)) {
                Serializer serializer = new Serializer().setHost(Configuration.get(Context.TEST_MACHINE)).setPort(Integer.valueOf(Configuration.get(Context.LISTENER_PORT))).build();
                Message message = Message.newBuilder().setState(ListenerType.BEFORE.name()).setDescriptor(methodName).setArgs(args).setTypes(types).build();
                result = serializer.write(message);
                logger.debug("send message successfully " + methodName);
            }

        } catch (Exception e) {
            if (e.getCause().toString().contains("UNAVAILABLE")) {
                logger.debug("fail on UNAVAILABLE ", e);
                return (new Object[]{});
            }
            logger.debug("send message "+methodName+" fail  ",e );
        }
        if(result == null){
            logger.debug("returning from rexecuteBefore" );
            return (new Object[]{});
        }
        if(result instanceof Object[]){
            logger.debug("returning from rexecuteBefore" );
            return (Object[])result;
        }
        logger.debug("returning from rexecuteBefore" );
        return new Object[]{result};
    }

    @Override
    public  void executeAfter(String methodName, Object[] args, Class[] types) throws Exception {
        //Object result = null;
        try {
            if(Context.getTestState().equals(TestLifeCycle.START)) {
                Serializer serializer = new Serializer().setHost(Configuration.get(Context.TEST_MACHINE)).setPort(Integer.valueOf(Configuration.get(Context.LISTENER_PORT))).build();
                Message message = Message.newBuilder().setState(ListenerType.AFTER.name()).setDescriptor(methodName).setArgs(args).setTypes(types).build();
                serializer.write(message);
                logger.debug("send message successfully " + methodName);
            }
        } catch (Exception e) {
            if (e.getCause().toString().contains("UNAVAILABLE")) {
                logger.debug("fail on UNAVAILABLE ", e);

            }
            logger.debug("send message "+methodName+" fail  ",e );
        }
        logger.debug("returning from executeAfter" );
        //return result;
    }



}
