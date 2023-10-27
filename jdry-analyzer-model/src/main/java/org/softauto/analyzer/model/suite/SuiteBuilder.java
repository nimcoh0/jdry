package org.softauto.analyzer.model.suite;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.softauto.analyzer.directivs.field.Field;
import org.softauto.analyzer.model.listener.Listener;
import org.softauto.analyzer.model.scenario.Scenario;
import org.softauto.analyzer.model.test.Test;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

public class SuiteBuilder {

    private static Logger logger = LogManager.getLogger(SuiteBuilder.class);

    public static Builder newBuilder() { return new Builder();}

    private Suite suite;

    public SuiteBuilder(Suite suite){
        this.suite = suite;
    }

    public Suite getSuite() {
        return suite;
    }

    public static class Builder {

        protected String name;

        protected String namespace;

        protected LinkedList<Test> tests = new LinkedList<>();



        protected List<Field> fields = new ArrayList<>();

        protected LinkedList<Scenario> scenarios = new LinkedList<>();

        protected List<Listener> listeners = new ArrayList<>();



        protected HashMap<String, Object> ids = new HashMap<>();

        private Test loginTest;

        public Builder setLoginTestName(Test loginTest) {
            this.loginTest = loginTest;
            return this;
        }

        public Builder addIds(String key, String value){
            this.ids.put(key,value);
            return this;
        }

        public Builder setFields(List<Field> fields) {
            this.fields = fields;
            return this;
        }

        public Builder addField(Field field) {
            this.fields.add(field);
            return this;
        }





        public List<Listener> getListeners() {
            return listeners;
        }

        public LinkedList<Scenario> getScenarios() {
            return scenarios;
        }

        public Builder setListeners(List<Listener> listeners) {
            this.listeners = listeners;
            return this;
        }

        public Builder addListener(Listener listener) {
            this.listeners.add(listener) ;
            return this;
        }



        public List<Test> getTests() {
            return tests;
        }

        public Builder setScenarios(LinkedList<Scenario> scenarios) {
            this.scenarios = scenarios;
            return this;
        }

        public Builder addScenario(Scenario scenario) {
            this.scenarios.add(scenario);
            return this;
        }

        public Builder setName(String name) {
            this.name = name;
            return this;
        }

        public Builder setNamespace(String namespace) {
            this.namespace = namespace;
            return this;
        }

        public Builder setTests(LinkedList<Test> tests) {
            this.tests = tests;
            return this;
        }

        public Builder addTest(Test test) {
            test.setOrder(tests.size() );
            this.tests.add(test);
            return this;
        }

        public SuiteBuilder build(){
            Suite suite = new Suite();
            try {
                suite.setName(name);
                suite.setNamespace(namespace);
                suite.setTests(tests);

                suite.setScenarios(scenarios);
                suite.setListeners(listeners);

                suite.setFields(fields);
                suite.setLoginTest(loginTest);
               // logger.debug("successfully build suite ");
            } catch (Exception e) {
                logger.error("fail build suite ",e);
            }
            return new SuiteBuilder(suite);
        }
    }
}
