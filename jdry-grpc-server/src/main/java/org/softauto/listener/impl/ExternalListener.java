package org.softauto.listener.impl;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.fasterxml.jackson.databind.node.ArrayNode;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.*;
import org.aspectj.lang.reflect.MethodSignature;
import org.softauto.annotations.ListenerType;
import org.softauto.core.Configuration;
import org.softauto.core.TestContext;
import org.softauto.espl.Espl;
import org.softauto.system.ScenarioContext;
import org.softauto.system.Scenarios;


//import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.Method;
import java.util.ArrayList;

@Aspect
public abstract class ExternalListener {

    static org.apache.logging.log4j.Logger logger = org.apache.logging.log4j.LogManager.getLogger(ExternalListener.class);

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
            e.printStackTrace();
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

    @Before(value = "externalPointcut()")
    public synchronized void captureScenarioId(JoinPoint thisJoinPoint)throws Throwable {
        if(thisJoinPoint.getArgs() != null && thisJoinPoint.getArgs().length > 0){
           String scenarioId = Espl.getInstance().addProperty("args",thisJoinPoint.getArgs()).evaluate(Configuration.get("capture_scenario_id").asString()).toString();
           Threadlocal.getInstance().add("scenarioId", scenarioId);
        }
    }

    @Around(value = "listenerBeforePointcut()")
    public synchronized Object captureBeforeListener(ProceedingJoinPoint joinPoint)throws Throwable {
        Object o = null;
        //AtomicReference<String> fqmn = new AtomicReference();
        try {
            if(serviceImpl != null ){
                MethodSignature sig = (MethodSignature) joinPoint.getSignature();
                Method method = serviceImpl.getClass().getDeclaredMethod("executeBefore", new Class[]{String.class, Object[].class, Class[].class});
                String fqmn = sig.getDeclaringType().getName().replace(".","_")+"_"+sig.getName();
                //ScenarioContext scenarioContext = Scenarios.getScenario(getScenarioId());
                //if(scenarioContext.getConfiguration().containsKey("listener")){
                    //ArrayNode listeners = (ArrayNode) scenarioContext.getConfiguration().get("listener");
                    //for(JsonNode node : listeners){
                       // if(node.get("listener").asText().equals(fqmn) && node.get("type").asText().equals(ListenerType.BEFORE.name())){
                            try {

                                logger.debug("invoke listener on " + serviceImpl + " fqmn: " + fqmn + " args:" + joinPoint.getArgs().toString() + " types:" + sig.getMethod().getParameterTypes());
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
                                logger.error("send message " + fqmn + " fail  ", e);
                            }
                }else {
                  o = joinPoint.proceed();
                }
        } catch (Throwable e) {
            //logger.error("capture message "+fqmn.get()+" fail  ",e );
        }
        return o;
    }

    //@AfterReturning(pointcut = "execution(* *(..))  && @annotation(org.softauto.annotations.ListenerForTesting)  ", returning = "result")
    //@AfterReturning(value = ("@annotation(org.softauto.annotations.ListenerForTesting)"), returning="result")
    @AfterReturning(value = "listenerAfterPointcut()", returning="result")
    public synchronized   void captureAfterListener(JoinPoint joinPoint,Object result) {
        try {
            if(serviceImpl != null) {

                Method method = serviceImpl.getClass().getDeclaredMethod("executeAfter", new Class[]{String.class, Object[].class, Class[].class});
                MethodSignature sig = (MethodSignature) joinPoint.getSignature();
                //Thread currentThread = Thread.currentThread();
                String fqmn = Utils.buildMethodFQMN(sig.getName(), sig.getDeclaringType().getName());
                //if(sig.getMethod().getAnnotation(org.softauto.annotations.ListenerForTesting.class) != null ) {
                 //   org.softauto.annotations.ListenerForTesting annotation = sig.getMethod().getAnnotation(org.softauto.annotations.ListenerForTesting.class);
                   // if (annotation.type().toString().equals(ListenerType.AFTER.name())) {

                        if (!sig.getMethod().getReturnType().getName().equals("void")) {
                            try {

                                logger.debug("invoke returning listener on " + serviceImpl + " fqmn:" + fqmn + " args:" + joinPoint.getArgs().toString() + " types:" + sig.getMethod().getParameterTypes());
                                method.setAccessible(true);
                                method.invoke(serviceImpl, new Object[]{fqmn, new Object[]{result}, new Class[]{sig.getMethod().getReturnType()}});

                            } catch (Exception e) {
                                logger.error("sendResult returning fail for " + fqmn, e);
                            }

                        } else {
                            try {

                                logger.debug("invoke returning listener on " + serviceImpl + " fqmn:" + fqmn + " args:" + org.softauto.core.Utils.result2String(joinPoint.getArgs()) + " types:" + org.softauto.core.Utils.result2String(sig.getMethod().getParameterTypes()));
                                method.setAccessible(true);
                                method.invoke(serviceImpl, new Object[]{fqmn, Utils.getArgs(joinPoint.getArgs()), Utils.getTypes(sig.getMethod().getParameterTypes())});
                            } catch (Exception e) {
                                logger.error("sendResult returning fail for " + fqmn, e);
                            }
                        }
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }
}
