package org.softauto.core;

import org.softauto.analyzer.model.scenario.Scenario;
import org.testng.ITestContext;
import org.testng.ITestListener;
import org.testng.TestRunner;


import java.util.HashMap;

public class TestContext {

    static HashMap<String,Object> hm = new HashMap<>();

    //static HashMap<String, Scenario> scenarios = new HashMap<>();

    public static Object get(String name){
        return hm.get(name);
    }

    public static void put(String key,Object value){
        hm.put(key,value);
    }

    public static void remove(String key){
        hm.remove(key);
    }

    private static Scenario scenario;

    private static IJdryListener jdryStepListener;

    private static ITestContext testContext;

    public static StepLifeCycle stepState = StepLifeCycle.NONE;

    public static StepLifeCycle getStepState() {
        return stepState;
    }

    public static void setStepState(StepLifeCycle stepState) {
        TestContext.stepState = stepState;
        switch (stepState){
            case START : jdryStepListener.onStepStart();
                        break;
            case STOP : jdryStepListener.onStepFinish();
                        break;
            case FAIL : jdryStepListener.onStepFailure();
                        break;
            case PASS : jdryStepListener.onStepSuccess();
                        break;
            case SKIP : jdryStepListener.onStepSkipped();
                        break;
        }
        //logger.debug("test state change to "+ testState);
    }

    public static boolean has(String name){
        if(hm.containsKey(name)){
            return true;
        }
        return false;
    }

    public static void setTestContext(ITestContext testContext) {
        TestContext.testContext = testContext;
        setJdryStepListener(testContext);
    }

    public static void setJdryStepListener(ITestContext context){
        for (ITestListener listener : ((TestRunner) context).getTestListeners()) {
            if(listener instanceof IJdryListener) {
                jdryStepListener = (IJdryListener) listener;
            }
        }
    }

    //public static String getScenarioKey(){
    //       return   scenarios.keySet().toArray(new String[1])[0].toString();
   // }

   // public static Scenario getScenario(String id){
      //  if(id == null){
       //   String key =   scenarios.keySet().toArray(new String[1])[0].toString();
      //    return scenarios.values().toArray(new Scenario[1])[0];
     //   }
      //  return scenarios.get(id);
   // }

    public static Scenario getScenario(){
       return scenario;
    }


    //public static Scenario getScenarioByName(String name){
     //   if(name == null){
         //   for(Map.Entry entry : scenarios.entrySet()){
           //     if(((Scenario)entry.getValue()).getSuiteName().equals(name)){
             //       return (Scenario)entry.getValue();
             //   }
            //}
      //  }
      //  return null;
    //}

    public static void setScenario(Scenario scenario){
        TestContext.scenario = scenario;
    }

    public static void restart(){
        hm = new HashMap<>();
        Scenario scenario = null;
        IJdryListener jdryStepListener = null;
        ITestContext testContext = null;
        stepState = StepLifeCycle.NONE;
    }

}
