package org.softauto.analyzer.model.scenario;


import com.fasterxml.jackson.annotation.JsonIgnore;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

public class Scenario implements Cloneable, Serializable {

    /**
     * list of tests names that include in this scenario
     */
    private LinkedList<String> tests = new LinkedList<>();

    private LinkedList<String> listeners = new LinkedList<>();

    private Integer order;

    /**
     * scenario UUID
     */
    private String id ;

    private String namespace;

    /**
     * scenario name
     */
    String suiteName;

    /**
     * scenario state (can be run,pass,fail,skip)
     */
    private String state;

    /**
     * list of errors
     */
    private List<Object> error = new ArrayList<>();

    /**
     * scenario configuration
     */
    private HashMap<String,Object> configuration = new HashMap<>();

    private HashMap<String,Object> properties = new HashMap<>();

    private boolean negative = false;

    public boolean isNegative() {
        return negative;
    }

    @JsonIgnore
    public void setNegative(boolean negative) {
        this.negative = negative;
    }

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
        this.tests.addAll(tests) ;
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

    public Scenario addTest(String fullName) {
        this.tests.add(fullName);
        return this;
    }

    public LinkedList<String> getListeners() {
        return listeners;
    }

    public void setListeners(LinkedList<String> listeners) {
        this.listeners = listeners;
    }

    public void addListeners(String listener) {
        this.listeners.add(listener);
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
