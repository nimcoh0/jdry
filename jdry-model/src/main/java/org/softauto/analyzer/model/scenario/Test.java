package org.softauto.analyzer.model.scenario;

import java.io.Serializable;
import java.util.LinkedList;

public class Test implements Cloneable, Serializable {

    private LinkedList<String> steps = new LinkedList<>();

    String name;

    String clazz;

    public LinkedList<String> getSteps() {
        return steps;
    }

    public Test setSteps(LinkedList<String> steps) {
        this.steps = steps;
        return this;
    }

    public Test addStep(String step) {
        this.steps.add(step);
        return this;
    }

    public String getName() {
        return name;
    }

    public Test setName(String name) {
        this.name = name;
        return this;
    }

    public String getClazz() {
        return clazz;
    }

    public Test setClazz(String clazz) {
        this.clazz = clazz;
        return this;
    }

    public boolean isExist(String name){
        for(String s : steps){
            if(s.equals(name)){
                return true;
            }
        }
        return false;
    }
}
