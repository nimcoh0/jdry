package org.softauto.analyzer.model.scenario;


import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;


import java.util.LinkedList;

public class ScenarioBuilder {

    private static Logger logger = LogManager.getLogger(ScenarioBuilder.class);

    public static Builder newBuilder() { return new Builder();}

    private Scenario scenario;

    public ScenarioBuilder(Scenario scenario){
        this.scenario = scenario;
    }

    public Scenario getScenario() {
        return scenario;
    }

    public static class Builder {

        private LinkedList<String> tests = new LinkedList<>();

        private Integer order;

        String id ;

        String namespace;

        String suiteName;

       public Builder setSuiteName(String suiteName) {
            this.suiteName = suiteName;
            return this;
        }

        public Builder setNamespace(String namespace) {
            this.namespace = namespace;
            return this;
        }

        public Builder setId(String id) {
            this.id = id;
            return this;
        }

        public Builder setOrder(Integer order) {
            this.order = order;
            return this;
        }

        public Builder setTests(LinkedList<String> tests) {
            this.tests = tests;
            return this;
        }



        public Builder addTest(String name) {
            this.tests.add(name);
            return this;
        }

        public LinkedList<String> getTests() {
            return tests;
        }

        public ScenarioBuilder build(){
            Scenario scenario = new Scenario();
            try {
                scenario.setTests(tests);
                scenario.setOrder(order);
                scenario.setId(id);
                scenario.setSuiteName(suiteName);

            } catch (Exception e) {
                logger.error("fail build Scenario "+ e);
            }
            return new ScenarioBuilder(scenario);
        }

    }
}
