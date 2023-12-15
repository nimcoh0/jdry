package org.softauto.system;

import org.softauto.core.ScenarioLifeCycle;
import org.softauto.core.StepLifeCycle;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class ScenarioContext {

    private StepLifeCycle stepState;

    private ScenarioLifeCycle scenarioState;

    private String stepName;

    private String scenarioName;

    private List<String> listeners = new ArrayList<>();

    private HashMap<String,Object> configuration = new HashMap<>();

    private String id;

    public StepLifeCycle getStepState() {
        return stepState;
    }

    public ScenarioContext setStepState(StepLifeCycle stepState) {
        this.stepState = stepState;
        return this;
    }



    public ScenarioLifeCycle getScenarioState() {
        return scenarioState;
    }

    public ScenarioContext setScenarioState(ScenarioLifeCycle scenarioState) {
        this.scenarioState = scenarioState;
        return this;
    }

    public String getStepName() {
        return stepName;
    }

    public ScenarioContext setStepName(String stepName) {
        this.stepName = stepName;
        return this;
    }

    public String getScenarioName() {
        return scenarioName;
    }

    public ScenarioContext setScenarioName(String scenarioName) {
        this.scenarioName = scenarioName;
        return this;
    }

    public HashMap<String, Object> getConfiguration() {
        return configuration;
    }

    public ScenarioContext setConfiguration(HashMap<String, Object> configuration) {
        this.configuration = configuration;
        return this;
    }

    public String getId() {
        return id;
    }

    public ScenarioContext setId(String id) {
        this.id = id;
        return this;
    }

    public List<String> getListeners() {
        return listeners;
    }

    public void setListeners(List<String> listeners) {
        this.listeners = listeners;
    }

    public boolean isListenerExist(String name){
        return this.listeners.contains(name);
    }
}
