package org.softauto.analyzer.model.scenario;

import org.softauto.analyzer.model.test.Test;

import java.io.Serializable;
import java.util.LinkedList;

public class Scenario implements Cloneable, Serializable {

    LinkedList<String> tests = new LinkedList<>();

    Integer order;

    String id;

    String namespace;

    String suiteName;

    public LinkedList<String> getTests() {
        return tests;
    }

    public Scenario setTests(LinkedList<String> tests) {
        this.tests = tests;
        return this;
    }

    public Integer getOrder() {
        return order;
    }

    public Scenario setOrder(Integer order) {
        this.order = order;
        return this;
    }

    public String getId() {
        return id;
    }

    public Scenario setId(String id) {
        this.id = id;
        return this;
    }

    public String getNamespace() {
        return namespace;
    }

    public Scenario setNamespace(String namespace) {
        this.namespace = namespace;
        return this;
    }

    public String getSuiteName() {
        return suiteName;
    }

    public Scenario setSuiteName(String suiteName) {
        this.suiteName = suiteName;
        return this;
    }

    public Scenario addTest(Test test) {
        this.tests.add(test.getFullName());
        return this;
    }

    public Scenario addTest(String test) {
        this.tests.add(test);
        return this;
    }


    public boolean isExist(String name){
        for(String s : tests){
            if(s.equals(name)){
                return true;
            }
        }
        return false;
    }
}
