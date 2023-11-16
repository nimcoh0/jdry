package org.softauto.system;

import org.softauto.core.ScenarioState;
import org.softauto.core.TestLifeCycle;
import java.util.HashMap;

public class ScenarioContext {

    private TestLifeCycle testState;

    private ScenarioState scenarioState;

    private String testName;

    private String scenarioName;

    //private List<Listener> listeners = new ArrayList<>();


    private HashMap<String,Object> configuration = new HashMap<>();

    private String id;

    public TestLifeCycle getTestState() {
        return testState;
    }

    public ScenarioContext setTestState(TestLifeCycle testState) {
        this.testState = testState;
        return this;
    }



    public ScenarioState getScenarioState() {
        return scenarioState;
    }

    public ScenarioContext setScenarioState(ScenarioState scenarioState) {
        this.scenarioState = scenarioState;
        return this;
    }

    public String getTestName() {
        return testName;
    }

    public ScenarioContext setTestName(String testName) {
        this.testName = testName;
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
}
