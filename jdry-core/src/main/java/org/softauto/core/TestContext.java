package org.softauto.core;

import org.hamcrest.Condition;
import org.softauto.analyzer.model.scenario.Scenario;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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

    public static TestLifeCycle TestState = TestLifeCycle.NONE;

    public static TestLifeCycle getTestState() {
        return TestState;
    }

    public static void setTestState(TestLifeCycle testState) {
        TestState = testState;
        //logger.debug("test state change to "+ testState);
    }

    public static boolean has(String name){
        if(hm.containsKey(name)){
            return true;
        }
        return false;
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

}
