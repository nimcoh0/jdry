package org.softauto.listener.impl;

import org.apache.logging.log4j.Marker;
import org.apache.logging.log4j.MarkerManager;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.*;
import org.aspectj.lang.reflect.MethodSignature;
import org.softauto.annotations.UpdateForTesting;
import org.softauto.core.Iprovider;
import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicReference;

@Aspect
public class Listener {

    static org.apache.logging.log4j.Logger logger = org.apache.logging.log4j.LogManager.getLogger(Listener.class);
    /** marker for trace logs */
    private static final Marker TRACER = MarkerManager.getMarker("TRACER");
    static Object serviceImpl;
    boolean trace = false;


    public void setTrace(boolean trace) {
        this.trace = trace;
    }

    private  static ExecutorService executor = Executors.newFixedThreadPool(100);


    public static void init(Object serviceImpl) {
        init(serviceImpl, true);
    }

    public static void init(Object serviceImpl, boolean loadWeaver) {
        try {
            Listener.serviceImpl = serviceImpl;
            logger.info("Weaver not attache by configuration .  make sure you load it before the app start ");

        } catch (Exception var3) {
            logger.error("ServiceImpl not found ", var3);
        }

    }




    @Before(value = ("@annotation(org.softauto.annotations.ListenerForTesting) "))
    public synchronized void captureAll(JoinPoint joinPoint){
        AtomicReference<String> fqmn = new AtomicReference();
        try {
            if(serviceImpl != null) {
                Method method = serviceImpl.getClass().getDeclaredMethod("executeBefore", new Class[]{String.class, Object[].class, Class[].class});
                MethodSignature sig = (MethodSignature) joinPoint.getSignature();
                fqmn.set(Utils.buildMethodFQMN(sig.getName(), sig.getDeclaringType().getName()));
                AtomicReference<Object[]> ref = new AtomicReference();

                    try {

                        logger.debug("invoke listener on " + serviceImpl + " fqmn: " + fqmn.get() + " args:" + joinPoint.getArgs().toString() + " types:" + sig.getMethod().getParameterTypes());
                        method.setAccessible(true);
                        ref.set((Object[]) method.invoke(serviceImpl, new Object[]{fqmn.get(), Utils.getArgs(joinPoint.getArgs()), Utils.getTypes(sig.getMethod().getParameterTypes())}));

                    } catch (Exception e) {
                        logger.error("send message " + fqmn.get() + " fail  ", e);
                    }


            }
        } catch (Throwable e) {
            logger.error("capture message "+fqmn.get()+" fail  ",e );
        }


    }







    @AfterReturning(value = ("@annotation(org.softauto.annotations.VerifyForTesting)"), returning="result")
    public synchronized   void returning(JoinPoint joinPoint,Object result) {
        try {
            if(serviceImpl != null) {

                Method method = serviceImpl.getClass().getDeclaredMethod("executeAfter", new Class[]{String.class, Object[].class, Class[].class});
                MethodSignature sig = (MethodSignature) joinPoint.getSignature();
                Thread currentThread = Thread.currentThread();
                String fqmn = Utils.buildMethodFQMN(sig.getName(), sig.getDeclaringType().getName());
               if (!sig.getMethod().getReturnType().getName().equals("void")) {
                                try {

                                    logger.debug("invoke returning listener on "+serviceImpl+ " fqmn:" + fqmn + " args:" + joinPoint.getArgs().toString() + " types:" + sig.getMethod().getParameterTypes());
                                    method.setAccessible(true);
                                    method.invoke(serviceImpl, new Object[]{fqmn, new Object[]{result}, new Class[]{sig.getMethod().getReturnType()}});

                                } catch (Exception e) {
                                    logger.error("sendResult returning fail for " + fqmn , e);
                                }

                    } else {
                            try {

                                logger.debug("invoke returning listener on "+serviceImpl+ " fqmn:" + fqmn + " args:" + org.softauto.core.Utils.result2String(joinPoint.getArgs()) + " types:" + org.softauto.core.Utils.result2String(sig.getMethod().getParameterTypes()));
                                method.setAccessible(true);
                                method.invoke(serviceImpl, new Object[]{fqmn , Utils.getArgs(joinPoint.getArgs()), Utils.getTypes(sig.getMethod().getParameterTypes())});
                            } catch (Exception e) {
                                logger.error("sendResult returning fail for " + fqmn , e);
                            }
                    }
             }
        }catch (Exception e){
            e.printStackTrace();
        }
    }


}
