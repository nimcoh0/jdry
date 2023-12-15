package org.softauto.listener.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.logging.log4j.Marker;
import org.apache.logging.log4j.MarkerManager;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.*;
import org.aspectj.lang.reflect.MethodSignature;
import org.softauto.annotations.ListenerType;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicReference;

@Aspect
public class Listener {

    static org.apache.logging.log4j.Logger logger = org.apache.logging.log4j.LogManager.getLogger(Listener.class);

    private static final Marker JDRY = MarkerManager.getMarker("JDRY");

    static org.softauto.listener.manager.ListenerServiceImpl serviceImpl = new org.softauto.listener.manager.ListenerServiceImpl();

    /** marker for trace logs */
    private static final Marker TRACER = MarkerManager.getMarker("TRACER");
    //static Object serviceImpl;
    boolean trace = false;



    public void setTrace(boolean trace) {
        this.trace = trace;
    }

    private  static ExecutorService executor = Executors.newFixedThreadPool(100);



    private Object[] castToOrgType(Object[] args, Class[] types){
        try {
            for(int i=0;i<args.length;i++){
                String json = new ObjectMapper().writeValueAsString(args[i]);
                args[i] = new ObjectMapper().readValue(json,types[i]);
            }
        } catch (Exception e) {
            logger.error(JDRY,"fail cast to org type",e);
        }
        return args;
    }

    private Object[] getArgs(Object[] args){
        if(args != null && args.length > 0) {
            if (args instanceof Object[]) {
                if (args[0] instanceof ArrayList) {
                    return ((ArrayList) args[0]).toArray();
                }
                return (Object[]) args[0];
            }
        }
      return args;
    }



    @Around(value = "@annotation(org.softauto.annotations.ListenerForTesting)")
    public synchronized Object captureAll(ProceedingJoinPoint joinPoint){
        Object o = null;
        AtomicReference<String> fqmn = new AtomicReference();
        try {
            if(serviceImpl != null && joinPoint.getKind().equals(JoinPoint.METHOD_CALL)) { //&& joinPoint.getKind().equals(JoinPoint.METHOD_CALL)
                Method method = serviceImpl.getClass().getDeclaredMethod("executeBefore", new Class[]{String.class, Object[].class, Class[].class});
                MethodSignature sig = (MethodSignature) joinPoint.getSignature();
                fqmn.set(Utils.buildMethodFQMN(sig.getName(), sig.getDeclaringType().getName()));
                AtomicReference<Object[]> ref = new AtomicReference(null);
                if(sig.getMethod().getAnnotation(org.softauto.annotations.ListenerForTesting.class) != null ) {
                    org.softauto.annotations.ListenerForTesting annotation = sig.getMethod().getAnnotation(org.softauto.annotations.ListenerForTesting.class);
                    if(annotation.type().toString().equals(ListenerType.BEFORE.name())) {
                        try {

                            logger.debug(JDRY,"invoke listener on " + serviceImpl + " fqmn: " + fqmn.get() + " args:" + joinPoint.getArgs().toString() + " types:" + sig.getMethod().getParameterTypes());
                            method.setAccessible(true);
                            Object[] args = null;
                            synchronized (this) {
                                while (args == null)
                                    args = (Object[]) method.invoke(serviceImpl, new Object[]{fqmn.get(), Utils.getArgs(joinPoint.getArgs()), Utils.getTypes(sig.getMethod().getParameterTypes())});

                            }
                            if(args != null && args.length > 0) {
                                //args = getArgs(args);
                                args = castToOrgType(args, sig.getMethod().getParameterTypes());
                                o = joinPoint.proceed(args);
                            }else {
                                o = joinPoint.proceed();
                            }
                        } catch (Exception e) {
                            logger.error(JDRY,"send message " + fqmn.get() + " fail  ", e);
                            o = joinPoint.proceed();
                        }
                    }else {
                        o = joinPoint.proceed();
                    }
                }else {
                    o = joinPoint.proceed();
                }
            }else {
                o = joinPoint.proceed();
            }
        } catch (Throwable e) {
            logger.error(JDRY,"capture message "+fqmn.get()+" fail  ",e );
        }
        return o;

    }






    @AfterReturning(pointcut = "execution(* *(..))  && @annotation(org.softauto.annotations.ListenerForTesting)  ", returning = "result")
    public synchronized   void returning(JoinPoint joinPoint,Object result) {
        try {
            if(serviceImpl != null) {

                Method method = serviceImpl.getClass().getDeclaredMethod("executeAfter", new Class[]{String.class, Object[].class, Class[].class});
                MethodSignature sig = (MethodSignature) joinPoint.getSignature();
                Thread currentThread = Thread.currentThread();
                String fqmn = Utils.buildMethodFQMN(sig.getName(), sig.getDeclaringType().getName());
                if(sig.getMethod().getAnnotation(org.softauto.annotations.ListenerForTesting.class) != null ) {
                    org.softauto.annotations.ListenerForTesting annotation = sig.getMethod().getAnnotation(org.softauto.annotations.ListenerForTesting.class);
                    if (annotation.type().toString().equals(ListenerType.AFTER.name())) {

                        if (!sig.getMethod().getReturnType().getName().equals("void")) {
                            try {

                                logger.debug(JDRY,"invoke returning listener on " + serviceImpl + " fqmn:" + fqmn + " args:" + joinPoint.getArgs().toString() + " types:" + sig.getMethod().getParameterTypes());
                                method.setAccessible(true);
                                method.invoke(serviceImpl, new Object[]{fqmn, new Object[]{result}, new Class[]{sig.getMethod().getReturnType()}});

                            } catch (Exception e) {
                                logger.error(JDRY,"sendResult returning fail for " + fqmn, e);
                            }

                        } else {
                            try {

                                logger.debug(JDRY,"invoke returning listener on " + serviceImpl + " fqmn:" + fqmn + " args:" + org.softauto.core.Utils.result2String(joinPoint.getArgs()) + " types:" + org.softauto.core.Utils.result2String(sig.getMethod().getParameterTypes()));
                                method.setAccessible(true);
                                method.invoke(serviceImpl, new Object[]{fqmn, Utils.getArgs(joinPoint.getArgs()), Utils.getTypes(sig.getMethod().getParameterTypes())});
                            } catch (Exception e) {
                                logger.error(JDRY,"sendResult returning fail for " + fqmn, e);
                            }
                        }
                    }
                }
             }
        }catch (Exception e){
            logger.error(JDRY,"capture returning  fail  ",e );
        }
    }


    @AfterThrowing(value = "execution(* *(..)) && !within(org.softauto..*)", throwing = "ee")
    public synchronized   void captureException(JoinPoint joinPoint,Throwable ee) {
        try {
            if (serviceImpl != null) {
                Method method = serviceImpl.getClass().getDeclaredMethod("executeException", new Class[]{String.class, Object[].class, Class[].class});
                MethodSignature sig = (MethodSignature) joinPoint.getSignature();
                String fqmn = Utils.buildMethodFQMN(sig.getName(), sig.getDeclaringType().getName());
                try {
                    logger.debug(JDRY,"invoke Exception on " + serviceImpl + " fqmn:" + fqmn + " args:" + joinPoint.getArgs().toString() + " types:" + sig.getMethod().getParameterTypes());
                    method.setAccessible(true);
                    method.invoke(serviceImpl, new Object[]{fqmn, new Object[]{ee}, new Class[]{Throwable.class}});
                } catch (Exception e) {
                    logger.error(JDRY,"send Exception  for " + fqmn, e);
                }
            }
        } catch (NoSuchMethodException ex) {
            logger.error(JDRY,"capture Exception  fail  ",ex );
        }
    }

}
