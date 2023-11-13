package org.softauto.service;

import org.softauto.listener.Exec;
import org.softauto.listener.ListenerObserver;
import org.softauto.system.SystemState;
import org.softauto.tester.Listener;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

public class ListenerService {

    private static final org.apache.logging.log4j.Logger logger = org.apache.logging.log4j.LogManager.getLogger(ListenerService.class);

    private Object listeners;

    private ServiceInvocationHandler serviceInvocationHandler;

    public Object getListeners() {
        return listeners;
    }

    /*
    public org.softauto.tester.Listener register(Function function) throws Exception {
        return new ListenerService().addListener(serviceInvocationHandler.getMethodName(), serviceInvocationHandler.getTypes(),function);
    }

    public org.softauto.tester.Listener register() throws Exception {
        return new ListenerService().addListener(serviceInvocationHandler.getMethodName(), serviceInvocationHandler.getTypes());
    }

     */

    public Listener resetListeners()throws Exception{
        ListenerObserver.getInstance().reset();
        logger.debug("reset Listeners successfully");
        return  new Listener();
    }

    public  Listener removeListener(String fqmn, Class...types)throws Exception{
        ListenerObserver.getInstance().unRegister(fqmn);
        logger.debug("remove Listener successfully "+ fqmn);
        return  new Listener();
    }

/*
    public  Listener addListener(String fqmn, Class...types)throws Exception{
        logger.debug("add Listener successfully "+ fqmn+ " types "+ Arrays.toString(types));
        return  new Listener().setFqmn(fqmn).setTypes(types);
    }


 */
    public  Listener addListener(String fqmn, Class[] types)throws Exception{
        logger.debug("add Listener successfully "+ fqmn+ " types "+ Arrays.toString(types));
        Exec func = new Exec(fqmn);
        ListenerObserver.getInstance().register(fqmn,func);
        //SystemState.getInstance().update("{\"listener\" : \""+ fqmn +"\",\"type\": \""+ listenerType+"\" }" );
        return  new Listener().setFqmn(fqmn).setTypes(types);
    }

    /*
    public  Listener addListener( String fqmn, Class[] types,Function function)throws Exception{
        logger.debug("add Listener successfully "+ fqmn+ " types "+ Arrays.toString(types));
        Exec func = new Exec(function,fqmn);
        ListenerObserver.getInstance().register(fqmn,func);
        return  new Listener().setFqmn(fqmn).setFunc(func);
    }

     */

    public  Listener addListener( String fqmn, Class[] types,Function function)throws Exception{
        logger.debug("add Listener successfully "+ fqmn+ " types "+ Arrays.toString(types));
        Exec func = new Exec(function,fqmn);
        ListenerObserver.getInstance().register(fqmn,func);
        //SystemState.getInstance().update("{\"listener\" : \""+ fqmn +"\",\"type\": \""+ listenerType+"\" }");
        return  new Listener().setFqmn(fqmn).setFunc(func).setTypes(types);
    }

    public static ListenerService create(Class<?> iface) {
        return new ListenerService(iface);
    }

    public ListenerService() {

    }

    public ListenerService(Class<?> iface) {
        serviceInvocationHandler = new ListenerService.ServiceInvocationHandler();
        listeners =  Proxy.newProxyInstance(iface.getClassLoader(), new Class[]{iface}, serviceInvocationHandler);
        logger.debug("tester proxy instance create successfully ");

    }



    public   static class ServiceInvocationHandler implements InvocationHandler {

        private String methodName;

        private Class[] types;



        public String getMethodName() {
            return methodName;
        }

        public Class[] getTypes() {
            return types;
        }

        @Override
        public Listener invoke(Object proxy, Method method, Object[] args) {
            try {
                if (args == null) {
                    args = new Object[]{};
                }
                logger.debug("invoke method " + method.getName() + " with args " + Arrays.toString(args));
                Listener listener = new Listener();
                listener.setFqmn(method.getName());
                listener.setTypes(method.getParameterTypes());

                return listener;

            } catch (Exception e) {
                logger.error("fail invoke method " + method.getName() + " with args " + Arrays.toString(args), e);
            }
            return null;
        }
    }
}
