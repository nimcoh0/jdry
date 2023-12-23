package org.softauto.core;

import org.softauto.model.scenario.Scenario;


import java.util.HashMap;

public class TestContext {

    static HashMap<String,Object> hm = new HashMap<>();

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

    public static StepLifeCycle stepState = StepLifeCycle.NONE;

    public static StepLifeCycle getStepState() {
        return stepState;
    }



    public static boolean has(String name){
        if(hm.containsKey(name)){
            return true;
        }
        return false;
    }


    public static Scenario getScenario(){
       return scenario;
    }



    public static void setScenario(Scenario scenario){
        TestContext.scenario = scenario;
    }

    public static void restart(){
        hm = new HashMap<>();
        Scenario scenario = null;
        stepState = StepLifeCycle.NONE;
    }

}
