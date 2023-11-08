package org.softauto.listener.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.module.SimpleModule;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.softauto.core.Configuration;
import org.softauto.core.TestContext;
import org.softauto.espl.Espl;


import javax.servlet.http.HttpServletRequest;

@Aspect
public abstract class ExternalListener {

    @Pointcut
    public void externalPointcut(){

    };

    @Before(value = "externalPointcut()")
    public synchronized void captureScenarioId(JoinPoint thisJoinPoint)throws Throwable {
        if(thisJoinPoint.getArgs() != null && thisJoinPoint.getArgs().length > 0){
           String scenarioId = Espl.getInstance().addProperty("args",thisJoinPoint.getArgs()).evaluate(Configuration.get("capture_scenario_id").asString()).toString();
           Threadlocal.getInstance().add("scenarioId", scenarioId);
        }
    }
}
