package org.softauto.listener.impl;

import org.apache.logging.log4j.Marker;
import org.apache.logging.log4j.MarkerManager;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.*;
import org.aspectj.lang.reflect.MethodSignature;
import org.softauto.annotations.Mock;
import org.softauto.annotations.UpdateForTesting;
import org.softauto.core.Iprovider;
import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.HashMap;
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


    @Pointcut("@annotation(updateForTesting)")
    public void callAt(UpdateForTesting updateForTesting) {
    }

    //@annotation(UpdateForTesting) && execution(* *(..))  && !within(org.softauto..*) ")
    //@Around("@annotation(UpdateForTesting) && execution(* *(..))  && !within(org.softauto..*) ")
    @Around("callAt(updateForTesting)")
    public synchronized Object captureUpdateProvider(org.aspectj.lang.ProceedingJoinPoint joinPoint,UpdateForTesting updateForTesting){
        Object o = null;
        try {
            Class c = Class.forName(updateForTesting.provider());
            Constructor<?> ctor = c.getConstructor();
            Iprovider p = (Iprovider)ctor.newInstance();
            o = joinPoint.proceed();
            o = p.apply(o);
        }catch (Throwable e){
            e.printStackTrace();
        }
       return o;
    }



    @Pointcut("@annotation(mock)")
    public void callMock(Mock mock) {
    }

    /**
     * @// TODO: 14/11/2022 impl convert mock annotations to Object[]  
     * @param joinPoint
     * @param mock
     * @return
     */
    @Around("callMock(mock)")
    public  synchronized   Object captureMock(org.aspectj.lang.ProceedingJoinPoint joinPoint,Mock mock){
        String fqmn = null;
        try {
            if(serviceImpl != null) {
                MethodSignature sig = (MethodSignature) joinPoint.getSignature();
                fqmn = Utils.buildMethodFQMN(sig.getName(), sig.getDeclaringType().getName());
                Object[] parameters = sig.getMethod().getAnnotation(org.softauto.annotations.Mock.class).parameter();
                //return joinPoint.proceed(parameters);
            }
            return joinPoint.proceed();
        } catch (Throwable e) {
            logger.error("capture message "+fqmn+" fail  ",e );
        }
        return null;
    }




    @Before(value = ("@annotation(org.softauto.annotations.ListenerForTesting) || @annotation(org.softauto.annotations.VerifyForTesting)"))
    //@Before("execution(* *(..)) && !within(org.softauto..*)")
    public synchronized void captureAll(JoinPoint joinPoint){
        AtomicReference<String> fqmn = new AtomicReference();
        try {
            if(serviceImpl != null) {
                Method method = serviceImpl.getClass().getDeclaredMethod("executeBefore", new Class[]{String.class, Object[].class, Class[].class});
                MethodSignature sig = (MethodSignature) joinPoint.getSignature();
                fqmn.set(Utils.buildMethodFQMN(sig.getName(), sig.getDeclaringType().getName()));
                //logger.trace(TRACER, "IN " + fqmn.get() + "( " + Arrays.toString(sig.getMethod().getParameterTypes()) + ")[" + Arrays.toString(joinPoint.getArgs()) + "]");
                AtomicReference<Object[]> ref = new AtomicReference();

                //if (Listeners.isExist(sig)) {
                    //result = joinPoint.proceed();
                    //executor.submit(() -> {
                    try {

                        logger.debug("invoke listener on " + serviceImpl + " fqmn: " + fqmn.get() + " args:" + joinPoint.getArgs().toString() + " types:" + sig.getMethod().getParameterTypes());
                        method.setAccessible(true);
                        ref.set((Object[]) method.invoke(serviceImpl, new Object[]{fqmn.get(), Utils.getArgs(joinPoint.getArgs()), Utils.getTypes(sig.getMethod().getParameterTypes())}));

                    } catch (Exception e) {
                        logger.error("send message " + fqmn.get() + " fail  ", e);
                    }


                   // logger.trace(TRACER, "OUT " + fqmn.get() + " (" + sig.getReturnType() + ") " );
                //}
            }
        } catch (Throwable e) {
            logger.error("capture message "+fqmn.get()+" fail  ",e );
        }
        //returning(joinPoint,result);

    }







    @AfterReturning(value = ("@annotation(org.softauto.annotations.ListenerForTesting) || @annotation(org.softauto.annotations.VerifyForTesting)"), returning="result")
    //@AfterReturning(pointcut="execution(* *(..)) && !within(org.softauto..*)", returning="result")
    public synchronized   void returning(JoinPoint joinPoint,Object result) {
        try {
            if(serviceImpl != null) {

                Method method = serviceImpl.getClass().getDeclaredMethod("executeAfter", new Class[]{String.class, Object[].class, Class[].class});
                MethodSignature sig = (MethodSignature) joinPoint.getSignature();
                //if(Listeners.isExist(sig)) {
                    Thread currentThread = Thread.currentThread();
                    String fqmn = Utils.buildMethodFQMN(sig.getName(), sig.getDeclaringType().getName());
                    if (!sig.getMethod().getReturnType().getName().equals("void")) {

                     //executor.submit(() -> {
                                try {

                                    logger.debug("invoke returning listener on "+serviceImpl+ " fqmn:" + fqmn + " args:" + joinPoint.getArgs().toString() + " types:" + sig.getMethod().getParameterTypes());
                                    method.setAccessible(true);
                                    method.invoke(serviceImpl, new Object[]{fqmn, new Object[]{result}, new Class[]{sig.getMethod().getReturnType()}});
                                   // currentThread.interrupt();
                                } catch (Exception e) {
                                    logger.error("sendResult returning fail for " + fqmn , e);
                                }
                           // });
                    } else {
                   // executor.submit(() -> {
                            try {

                                logger.debug("invoke returning listener on "+serviceImpl+ " fqmn:" + fqmn + " args:" + org.softauto.core.Utils.result2String(joinPoint.getArgs()) + " types:" + org.softauto.core.Utils.result2String(sig.getMethod().getParameterTypes()));
                                method.setAccessible(true);
                                method.invoke(serviceImpl, new Object[]{fqmn , Utils.getArgs(joinPoint.getArgs()), Utils.getTypes(sig.getMethod().getParameterTypes())});
                               // currentThread.interrupt();
                             } catch (Exception e) {
                                logger.error("sendResult returning fail for " + fqmn , e);
                            }
                      //  });
                    }
                    //synchronized(currentThread){
                       //Thread.currentThread().wait(1000L);
                   // }
                   // if(!executor.isTerminated()) {

                   // }
               // }
             }
        }catch (Exception e){
            e.printStackTrace();
        }
    }










/*
    static public void addListener(String methodName, Object[] types){
        Listeners.addListener(methodName,types);
    }

    static public void addSchema(HashMap<String,Object> hm){
        Listeners.addSchema(hm);

    }

    static public void addSchema(Class iface){
        Listeners.addSchema(iface);

    }

    static public void addModule(AbstractModule module){
        Listeners.addModule(module);
      }


 */

}
