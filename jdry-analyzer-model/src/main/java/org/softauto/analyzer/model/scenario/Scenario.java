package org.softauto.analyzer.model.scenario;

import org.softauto.analyzer.model.test.Test;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

public class Scenario implements Cloneable, Serializable {

    LinkedList<String> tests = new LinkedList<>();

    Integer order;

    String id;

    String namespace;

    String suiteName;

    String state;

    List<Object> error = new ArrayList<>();

    HashMap<String,Object> configuration = new HashMap<>();

    HashMap<String,Object> properties = new HashMap<>();

    public HashMap<String, Object> getProperties() {
        return properties;
    }


    public List<Object> getError() {
        return error;
    }

    public void setError(List<Object> error) {
        this.error = error;
    }

    public void addError(Object error) {
        this.error.add(error);
    }

    public Object getProperty(String key) {
        if(properties.containsKey(key)){
           return properties.get(key);
        }
        return null;
    }


    public HashMap<String, Object> getConfiguration() {
        return configuration;
    }

    public void setConfiguration(HashMap<String, Object> configuration) {
        this.configuration = configuration;
    }

    public void setProperties(HashMap<String, Object> properties) {
        this.properties = properties;
    }

    public void addProperty(String key,Object value) {
        this.properties.put(key,value);
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

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
