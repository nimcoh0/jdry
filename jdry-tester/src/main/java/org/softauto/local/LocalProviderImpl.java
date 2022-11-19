package org.softauto.local;

import io.grpc.ManagedChannel;
import org.apache.avro.ipc.CallFuture;
import org.softauto.core.ServiceLocator;
import org.softauto.core.Utils;
import java.io.IOException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.Arrays;
import java.util.HashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;


public class LocalProviderImpl  {


    private static final org.apache.logging.log4j.Logger logger = org.apache.logging.log4j.LogManager.getLogger(LocalProviderImpl.class);
    private static LocalProviderImpl localProviderImpl = null;
    Class iface;
    String type = "LOCAL";
    private ExecutorService executor = Executors.newSingleThreadExecutor();

    public static LocalProviderImpl getInstance(){
        if(localProviderImpl == null){
            localProviderImpl =  new LocalProviderImpl();
        }
        return localProviderImpl;
    }



    public LocalProviderImpl initialize() throws IOException {
       logger.info("local plugin initialize successfully");
       return this;
    }


    public void register() {
        ServiceLocator.getInstance().register(type,this);
    }






    public LocalProviderImpl iface(Class iface) {
        this.iface = iface;
        return this;
    }





    public <RespT> void exec(String methodName, CallFuture<RespT> callback, ManagedChannel channel, Object[] args, Class[] types, HashMap<String, Object> callOptions) {
            executor.submit(()->{
                try {
                    Object methodResponse = null;
                    String fullClassName = Utils.getFullClassName(methodName);
                    Object o = Utils.findClass(fullClassName).newInstance();
                    Method method = Utils.getMethod(o,methodName, types);
                    method.setAccessible(true);
                    logger.debug("invoking "+ methodName+ " with " + Arrays.toString(args));
                    if (Modifier.isStatic(method.getModifiers())) {
                        methodResponse = method.invoke(null, (Object[])args);
                    } else {
                        methodResponse = method.invoke(o, (Object[])args);
                    }
                    logger.debug("got result "+methodResponse);
                    callback.handleResult((RespT) methodResponse);
                    logger.debug("successfully exec Local call  " + methodName);
                }catch (Exception e){
                    logger.error("fail execute local call "+ methodName,e);
                    callback.handleError(e);
                }
            });

    }



}
