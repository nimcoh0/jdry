package org.softauto.listener.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.logging.log4j.Marker;
import org.apache.logging.log4j.MarkerManager;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.*;
import org.aspectj.lang.reflect.MethodSignature;
import org.softauto.spel.SpEL;
import java.lang.reflect.Method;
import java.util.ArrayList;

@Aspect
public abstract class ExternalListener {

    static org.apache.logging.log4j.Logger logger = org.apache.logging.log4j.LogManager.getLogger(ExternalListener.class);

    private static final Marker JDRY = MarkerManager.getMarker("JDRY");

    static org.softauto.listener.manager.ListenerServiceImpl serviceImpl = new org.softauto.listener.manager.ListenerServiceImpl();

    @Pointcut
    public void externalPointcut(){

    };

    @Pointcut
    public void listenerBeforePointcut(){

    };

    @Pointcut
    public void listenerAfterPointcut(){

    };

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



    private String getScenarioId(){
        if(Threadlocal.getInstance().has("scenarioId")){
            return Threadlocal.getInstance().get("scenarioId");
        }
        return null;
    }

    @Before(value = "externalPointcut() && !within(org.softauto..*)")
    public synchronized void captureScenarioId(JoinPoint thisJoinPoint)throws Throwable {
        try {
            if(thisJoinPoint.getArgs() != null && thisJoinPoint.getArgs().length > 0){
                if(thisJoinPoint.getArgs()[0].getClass().getTypeName().equals("org.glassfish.jersey.server.ContainerRequest")) {
                    String scenarioId = SpEL.getInstance().addProperty("args", thisJoinPoint.getArgs()).evaluate("#args[0].getHeaders().get('scenarioId').get(0)").toString();
                    Threadlocal.getInstance().add("scenarioId", scenarioId);
                }
                if(thisJoinPoint.getArgs()[0].getClass().getTypeName().equals("javax.servlet.http.HttpServletRequest")) {
                    String scenarioId = SpEL.getInstance().addProperty("args",thisJoinPoint.getArgs()).evaluate("#args[0].getHeader('scenarioId')").toString();
                    Threadlocal.getInstance().add("scenarioId", scenarioId);
                }
            }
        } catch (Throwable e) {
            logger.error(JDRY,"fail get scenarioId ",e);
        }
    }

    @Around(value = "listenerBeforePointcut() && !within(org.softauto..*)")
    public synchronized Object captureBeforeListener(ProceedingJoinPoint joinPoint)throws Throwable {
        Object o = null;
        try {
            if(serviceImpl != null ){
                MethodSignature sig = (MethodSignature) joinPoint.getSignature();
                Method method = serviceImpl.getClass().getDeclaredMethod("executeBefore", new Class[]{String.class, Object[].class, Class[].class});
                String fqmn = sig.getDeclaringType().getName().replace(".","_")+"_"+sig.getName();
                            try {

                                logger.debug(JDRY,"invoke listener on " + serviceImpl + " fqmn: " + fqmn + " args:" + joinPoint.getArgs().toString() + " types:" + sig.getMethod().getParameterTypes());
                                method.setAccessible(true);
                                Object[] args = null;
                                synchronized (this) {
                                    while (args == null)
                                        args = (Object[]) method.invoke(serviceImpl, new Object[]{fqmn, Utils.getArgs(joinPoint.getArgs()), Utils.getTypes(sig.getMethod().getParameterTypes())});

                                }

                                args = getArgs(args);
                                args = castToOrgType(args,sig.getMethod().getParameterTypes());
                                o = joinPoint.proceed(args);
                            } catch (Exception e) {
                                logger.error(JDRY,"send message " + fqmn + " fail  ", e);
                            }
                }else {
                  o = joinPoint.proceed();
                }
        } catch (Throwable e) {
            logger.error(JDRY,"capture message  fail  ",e );
        }
        return o;
    }


    @AfterReturning(value = "listenerAfterPointcut() && !within(org.softauto..*)", returning="result")
    public synchronized   void captureAfterListener(JoinPoint joinPoint,Object result) {
        try {
            if(serviceImpl != null) {
                Method method = serviceImpl.getClass().getDeclaredMethod("executeAfter", new Class[]{String.class, Object[].class, Class[].class});
                MethodSignature sig = (MethodSignature) joinPoint.getSignature();
                String fqmn = Utils.buildMethodFQMN(sig.getName(), sig.getDeclaringType().getName());
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
        }catch (Exception e){
            e.printStackTrace();
        }
    }
}
