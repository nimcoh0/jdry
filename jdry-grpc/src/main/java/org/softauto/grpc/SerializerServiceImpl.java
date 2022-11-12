package org.softauto.grpc;



import org.apache.avro.ipc.CallFuture;
import org.softauto.core.ClassType;
import org.softauto.core.Utils;
import org.softauto.injector.Injector;
import org.softauto.serializer.service.Message;
import org.softauto.serializer.service.MessageType;
import org.softauto.serializer.service.SerializerService;
import org.softauto.system.SystemServiceImpl;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.Arrays;


public class SerializerServiceImpl implements SerializerService,SerializerService.Callback{

    private static final org.apache.logging.log4j.Logger logger = org.apache.logging.log4j.LogManager.getLogger(SerializerServiceImpl.class);
    Injector injector = null;


    public SerializerServiceImpl(){
        injector = new Injector();
    }


    @Override
    public void execute(Message message, CallFuture<Object> callback) throws Exception {
        Object methodResponse = null;
        try {
            String fullClassName = Utils.getFullClassName(message.getDescriptor());
            String methodName = Utils.getMethodName(message.getDescriptor());
            Object serviceImpl;
            String classType = ClassType.INITIALIZE_NO_PARAM.name();
            if(message.getData().get("classType") != null) {
                classType = message.getData().get("classType").toString();
            }
            if(injector != null) {
                if(classType.equals(ClassType.INITIALIZE) && !Utils.getClassName(fullClassName).equals(methodName)){
                    logger.debug("got request to method "+methodName+" in class type mark as INITIALIZE . trying to load class");
                    Class[] types = Utils.extractConstructorDefaultArgsTypes(fullClassName);
                    Object[] args = Utils.getConstructorDefaultValues(fullClassName);
                    serviceImpl = injector.inject(fullClassName,args,types, ClassType.fromString(classType));
                    if(serviceImpl == null){
                        logger.error("fail to initialize class " +fullClassName+ "with types "+ Arrays.toString(types) + " and args "+ Arrays.toString( args));
                    }else {
                        logger.debug("successfully initialize class " +fullClassName+ "with types "+ Arrays.toString(types) + " and args "+ Arrays.toString( args));
                    }
                }
                serviceImpl = injector.inject(fullClassName,message.getArgs(),message.getTypes(), ClassType.fromString(classType));
            }else {
                serviceImpl = SystemServiceImpl.getInstance();
            }
            if(message.getMessageType().equals(MessageType.VARIABLE)){
                methodResponse = injector.inject(fullClassName,methodName,message.getArgs()[0]);
            }else {
                Method m = Utils.getMethod(serviceImpl, methodName, message.getTypes());
                logger.debug("invoking " + message.getDescriptor());
                m.setAccessible(true);
                if (Modifier.isStatic(m.getModifiers())) {
                    methodResponse = m.invoke(null, message.getArgs());
                } else {
                    methodResponse = m.invoke(serviceImpl, message.getArgs());
                }
            }
            logger.debug("successfully invoke "+message.getDescriptor()+ " with args "+ Utils.result2String(message.getArgs())+ " on " +serviceImpl.getClass().getName());
        } catch (
            InvocationTargetException e) {
                logger.error("fail invoke method "+ message.getDescriptor(),e );
                callback.handleError(e);
            } catch (Exception e) {
                logger.error("fail invoke method "+ message.getDescriptor(),e );
                callback.handleError(e);
              }
            logger.debug("return in callback "+methodResponse);
            callback.handleResult(methodResponse);
    }

    @Override
    public Object execute(Message message) throws Exception {
        Object methodResponse = null;
        try {
            String fullClassName = Utils.getFullClassName(message.getDescriptor());
            Object serviceImpl;
            //AbstractInjector injector = (AbstractInjector)ServiceLocator.getInstance().getService("INJECTOR");
            String classType = ClassType.INITIALIZE_NO_PARAM.name();
            if(message.getData().get("classType") != null) {
               classType = message.getData().get("classType").toString();
            }
            String methodName = Utils.getMethodName(message.getDescriptor());
            if(injector != null && !fullClassName.equals("org.softauto.system.SystemServiceImpl")) {
                if(Utils.getClassName(message.getDescriptor()).equals(Utils.getMethodName(message.getDescriptor())) && message.getArgs().length > 0) {
                    serviceImpl = injector.inject(fullClassName,message.getArgs(),message.getTypes(),ClassType.fromString(classType))[0];
                    return serviceImpl;
                }else {
                    if(classType.equals(ClassType.INITIALIZE) && !Utils.getClassName(fullClassName).equals(methodName)){
                        logger.debug("got request to method "+methodName+" in class type mark as INITIALIZE . trying to load class");
                        Class[] types = Utils.extractConstructorDefaultArgsTypes(fullClassName);
                        Object[] args = Utils.getConstructorDefaultValues(fullClassName);
                        serviceImpl = injector.inject(fullClassName,args,types, ClassType.fromString(classType));
                        if(serviceImpl == null){
                            logger.error("fail to initialize class " +fullClassName+ "with types "+ Arrays.toString(types) + " and args "+ Arrays.toString( args));
                        }else {
                            logger.debug("successfully initialize class " +fullClassName+ "with types "+ Arrays.toString(types) + " and args "+ Arrays.toString( args));
                        }
                    }
                    serviceImpl = injector.inject(fullClassName,message.getArgs(),message.getTypes(),ClassType.fromString(classType))[0];
                }
            }else {
                serviceImpl = SystemServiceImpl.getInstance();
            }
            if(message.getMessageType().equals(MessageType.VARIABLE)){
                methodResponse = injector.inject(fullClassName,methodName,message.getArgs()[0]);
            }else {
                Method m = Utils.getMethod(serviceImpl, methodName, message.getTypes());
                logger.debug("invoking " + message.getDescriptor());
                m.setAccessible(true);
                if (Modifier.isStatic(m.getModifiers())) {
                    methodResponse = m.invoke(null, message.getArgs());
                } else {
                    methodResponse = m.invoke(serviceImpl, message.getArgs());
                }
            }
        logger.debug("successfully invoke "+message.getDescriptor()+ " with args "+ Utils.result2String(message.getArgs())+ " on " +serviceImpl.getClass().getName());
        } catch (
                InvocationTargetException e) {
            logger.error("fail invoke method "+ message.getDescriptor(),e );
            methodResponse = e.getTargetException();

        } catch (Exception e) {
            logger.error("fail invoke method "+ message.getDescriptor(),e );
            methodResponse = e;
        }
        logger.debug("return "+methodResponse);
        return methodResponse;
    }
}
