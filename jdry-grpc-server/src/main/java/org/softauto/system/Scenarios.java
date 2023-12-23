package org.softauto.system;



import org.softauto.model.scenario.Scenario;

import java.util.HashMap;

public class Scenarios {

    static HashMap<String, ScenarioContext> scenarios = new HashMap<>();


    public static HashMap<String, ScenarioContext> getScenarios() {
        return scenarios;
    }

    public static ScenarioContext getScenario(String id){
        return scenarios.get(id);
    }

    public static void addScenario(String id, Scenario scenario) {
        scenarios.put(id,convert(scenario));
    }

    private static ScenarioContext convert(Scenario scenario){
        ScenarioContext scenarioContext = new ScenarioContext();
        scenarioContext.setId(scenario.getId());
        scenarioContext.setScenarioName(scenario.getSuiteName());
        scenarioContext.setConfiguration(scenario.getConfiguration());
        return scenarioContext;
    }





}
