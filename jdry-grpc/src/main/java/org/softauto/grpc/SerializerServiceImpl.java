package org.softauto.grpc;



import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.avro.ipc.CallFuture;
import org.apache.avro.ipc.Callback;
import org.softauto.core.ClassType;
import org.softauto.core.Utils;
import org.softauto.injector.Injector;
import org.softauto.serializer.service.Message;
import org.softauto.serializer.service.SerializerService;
import org.softauto.system.SystemServiceImpl;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.HashMap;


public class SerializerServiceImpl implements SerializerService{

    private static final org.apache.logging.log4j.Logger logger = org.apache.logging.log4j.LogManager.getLogger(SerializerServiceImpl.class);
    Injector injector = null;


    public SerializerServiceImpl(){
        injector = new Injector();
    }


/*
    @Override
    public <RespT> void execute(ByteBuffer mess, org.apache.avro.ipc.Callback<RespT> callback) throws Exception {
        Object methodResponse = null;
        try{

            String newContent = new String(mess.array(), StandardCharsets.UTF_8);
            Message message = new ObjectMapper().readValue(newContent,Message.class);
            String fullClassName = Utils.getFullClassName(message.getDescriptor());
            String methodName = Utils.getMethodName(message.getDescriptor());
            HashMap<String, Object> callOption = null;
            if(message.getData().containsKey("callOption")) {
                callOption = (HashMap<String, Object>) message.getData().get("callOption");
            }
            Object serviceImpl;
            String classType = ClassType.INITIALIZE_IF_NOT_EXIST.name();
            if(callOption != null && callOption.get("classType") != null) {
                classType = callOption.get("classType").toString();
            }
            if(injector != null && !fullClassName.equals("org.softauto.system.SystemServiceImpl") && callOption != null) {
                //if(classType.equals(ClassType.INITIALIZE) && !Utils.getClassName(fullClassName).equals(methodName)){
                logger.debug("got request to method "+methodName+"  . trying to load class");
                Class[] types = Utils.extractConstructorArgsTypes(fullClassName);
                Object[] args = Utils.getConstructorArgsValues(callOption,types);
                serviceImpl = injector.inject(fullClassName,args,types, ClassType.fromString(classType));
                if(serviceImpl == null){
                    logger.error("fail to initialize class " +fullClassName+ "with types "+ Arrays.toString(types) + " and args "+ Arrays.toString( args));
                }else {
                    logger.debug("successfully initialize class " +fullClassName+ "with types "+ Arrays.toString(types) + " and args "+ Arrays.toString( args));
                }

            }else {
                serviceImpl = SystemServiceImpl.getInstance();
            }
            if(serviceImpl instanceof Object[]){
                serviceImpl = ((Object[])serviceImpl)[0];
            }
            Method m = Utils.getMethod(serviceImpl, methodName, message.getTypes());
            logger.debug("invoking " + message.getDescriptor());
            m.setAccessible(true);
            Object[] args = buildArgs(message);
            if (Modifier.isStatic(m.getModifiers())) {
                methodResponse = m.invoke(null, args);
            } else {
                methodResponse = m.invoke(serviceImpl, args);
            }

            logger.debug("successfully invoke "+message.getDescriptor()+ " with args "+ Utils.result2String(message.getArgs())+ " on " +serviceImpl.getClass().getName());

        }catch (Exception e){
            methodResponse = e;
            e.printStackTrace();
        }

        byte[] m = new ObjectMapper().writeValueAsBytes(methodResponse);
        ByteBuffer byteBuffer = ByteBuffer.wrap(m);
        callback.handleResult((RespT) byteBuffer);
    }


 */


    /*
    @Override
    public void execute(ByteBuffer mes, CallFuture<Object> callback) throws Exception {
        ByteBuffer methodResponse = (ByteBuffer) doExecute(mes);
        callback.handleResult(methodResponse);
    }

     */

    @Override
    public Object execute(ByteBuffer mes) throws Exception {
        Object methodResponse = null;
        try{

            String newContent = new String(mes.array(), StandardCharsets.UTF_8);
            Message message = new ObjectMapper().readValue(newContent,Message.class);
            String fullClassName = Utils.getFullClassName(message.getDescriptor());
            String methodName = Utils.getMethodName(message.getDescriptor());
            HashMap<String, Object> callOption = null;
            if(message.getData().containsKey("callOption")) {
                callOption = (HashMap<String, Object>) message.getData().get("callOption");
            }
            Object serviceImpl;
            String classType = ClassType.INITIALIZE_IF_NOT_EXIST.name();
            if(callOption != null && callOption.get("classType") != null) {
                classType = callOption.get("classType").toString();
            }
            if(injector != null && !fullClassName.equals("org.softauto.system.SystemServiceImpl") && callOption != null) {
                //if(classType.equals(ClassType.INITIALIZE) && !Utils.getClassName(fullClassName).equals(methodName)){
                logger.debug("got request to method "+methodName+"  . trying to load class");
                Class[] types = Utils.extractConstructorArgsTypes(fullClassName);
                Object[] args = Utils.getConstructorArgsValues(callOption,types);
                serviceImpl = injector.inject(fullClassName,args,types, ClassType.fromString(classType));
                if(serviceImpl == null){
                    logger.error("fail to initialize class " +fullClassName+ "with types "+ Arrays.toString(types) + " and args "+ Arrays.toString( args));
                }else {
                    logger.debug("successfully initialize class " +fullClassName+ "with types "+ Arrays.toString(types) + " and args "+ Arrays.toString( args));
                }

            }else {
                serviceImpl = SystemServiceImpl.getInstance();
            }
            if(serviceImpl instanceof Object[]){
                serviceImpl = ((Object[])serviceImpl)[0];
            }
            Method m = Utils.getMethod(serviceImpl, methodName, message.getTypes());
            logger.debug("invoking " + message.getDescriptor());
            m.setAccessible(true);
            Object[] args = buildArgs(message);
            if (Modifier.isStatic(m.getModifiers())) {
                methodResponse = m.invoke(null, args);
            } else {
                methodResponse = m.invoke(serviceImpl, args);
            }

            logger.debug("successfully invoke "+message.getDescriptor()+ " with args "+ Utils.result2String(message.getArgs())+ " on " +serviceImpl.getClass().getName());

        }catch (Exception e){
            methodResponse = e;
            e.printStackTrace();
        }

        byte[] m = new ObjectMapper().writeValueAsBytes(methodResponse);
        ByteBuffer byteBuffer = ByteBuffer.wrap(m);
        return byteBuffer;
    }

    private Object[] buildArgs(Message message){
        Object[] args = new Object[message.getArgs().length];
        try {
            if(message.getArgs() != null && message.getArgs().length > 0) {
                for (int i = 0; i < args.length; i++) {
                    String str = new ObjectMapper().writeValueAsString(message.getArgs()[i]);
                    String typename = message.getTypes()[i].getTypeName();
                    Class<?> c = Class.forName(typename);
                    Object o = new ObjectMapper().readValue(str, c);
                    args[i] = o;
                }
            }
        } catch (Throwable e) {
            e.printStackTrace();
        }
        return args;
    }



/*
    private Object doExecute(ByteBuffer mes) throws Exception {
        Object methodResponse = null;
        try{

            String newContent = new String(mes.array(), StandardCharsets.UTF_8);
            Message message = new ObjectMapper().readValue(newContent,Message.class);
            String fullClassName = Utils.getFullClassName(message.getDescriptor());
            String methodName = Utils.getMethodName(message.getDescriptor());
            HashMap<String, Object> callOption = null;
            if(message.getData().containsKey("callOption")) {
                callOption = (HashMap<String, Object>) message.getData().get("callOption");
            }
            Object serviceImpl;
            String classType = ClassType.INITIALIZE_IF_NOT_EXIST.name();
            if(callOption != null && callOption.get("classType") != null) {
                classType = callOption.get("classType").toString();
            }
            if(injector != null && !fullClassName.equals("org.softauto.system.SystemServiceImpl") && callOption != null) {
                //if(classType.equals(ClassType.INITIALIZE) && !Utils.getClassName(fullClassName).equals(methodName)){
                    logger.debug("got request to method "+methodName+"  . trying to load class");
                    Class[] types = Utils.extractConstructorArgsTypes(fullClassName);
                    Object[] args = Utils.getConstructorArgsValues(callOption,types);
                    serviceImpl = injector.inject(fullClassName,args,types, ClassType.fromString(classType));
                    if(serviceImpl == null){
                        logger.error("fail to initialize class " +fullClassName+ "with types "+ Arrays.toString(types) + " and args "+ Arrays.toString( args));
                    }else {
                        logger.debug("successfully initialize class " +fullClassName+ "with types "+ Arrays.toString(types) + " and args "+ Arrays.toString( args));
                    }

            }else {
                serviceImpl = SystemServiceImpl.getInstance();
            }
                if(serviceImpl instanceof Object[]){
                    serviceImpl = ((Object[])serviceImpl)[0];
                }
                Method m = Utils.getMethod(serviceImpl, methodName, message.getTypes());
                logger.debug("invoking " + message.getDescriptor());
                m.setAccessible(true);
                Object[] args = buildArgs(message);
                if (Modifier.isStatic(m.getModifiers())) {
                    methodResponse = m.invoke(null, args);
                } else {
                    methodResponse = m.invoke(serviceImpl, args);
                }

            logger.debug("successfully invoke "+message.getDescriptor()+ " with args "+ Utils.result2String(message.getArgs())+ " on " +serviceImpl.getClass().getName());

        }catch (Exception e){
            e.printStackTrace();
        }

        byte[] m = new ObjectMapper().writeValueAsBytes(methodResponse);
        ByteBuffer byteBuffer = ByteBuffer.wrap(m);
        return byteBuffer;
    }

    private <RespT> Object doExecute(ByteBuffer mes,org.apache.avro.ipc.Callback<RespT> callback) throws Exception {
        Object methodResponse = null;
        try{

            String newContent = new String(mes.array(), StandardCharsets.UTF_8);
            Message message = new ObjectMapper().readValue(newContent,Message.class);
            String fullClassName = Utils.getFullClassName(message.getDescriptor());
            String methodName = Utils.getMethodName(message.getDescriptor());
            HashMap<String, Object> callOption = null;
            if(message.getData().containsKey("callOption")) {
                callOption = (HashMap<String, Object>) message.getData().get("callOption");
            }
            Object serviceImpl;
            String classType = ClassType.INITIALIZE_IF_NOT_EXIST.name();
            if(callOption != null && callOption.get("classType") != null) {
                classType = callOption.get("classType").toString();
            }
            if(injector != null && !fullClassName.equals("org.softauto.system.SystemServiceImpl") && callOption != null) {
                //if(classType.equals(ClassType.INITIALIZE) && !Utils.getClassName(fullClassName).equals(methodName)){
                logger.debug("got request to method "+methodName+"  . trying to load class");
                Class[] types = Utils.extractConstructorArgsTypes(fullClassName);
                Object[] args = Utils.getConstructorArgsValues(callOption,types);
                serviceImpl = injector.inject(fullClassName,args,types, ClassType.fromString(classType));
                if(serviceImpl == null){
                    logger.error("fail to initialize class " +fullClassName+ "with types "+ Arrays.toString(types) + " and args "+ Arrays.toString( args));
                }else {
                    logger.debug("successfully initialize class " +fullClassName+ "with types "+ Arrays.toString(types) + " and args "+ Arrays.toString( args));
                }

            }else {
                serviceImpl = SystemServiceImpl.getInstance();
            }
            if(serviceImpl instanceof Object[]){
                serviceImpl = ((Object[])serviceImpl)[0];
            }
            Method m = Utils.getMethod(serviceImpl, methodName, message.getTypes());
            logger.debug("invoking " + message.getDescriptor());
            m.setAccessible(true);
            Object[] args = buildArgs(message);
            if (Modifier.isStatic(m.getModifiers())) {
                methodResponse = m.invoke(null, args);
            } else {
                methodResponse = m.invoke(serviceImpl, args);
            }

            logger.debug("successfully invoke "+message.getDescriptor()+ " with args "+ Utils.result2String(message.getArgs())+ " on " +serviceImpl.getClass().getName());

        }catch (Exception e){
            e.printStackTrace();
        }

        byte[] m = new ObjectMapper().writeValueAsBytes(methodResponse);
        ByteBuffer byteBuffer = ByteBuffer.wrap(m);
        return byteBuffer;
    }

 */

}
