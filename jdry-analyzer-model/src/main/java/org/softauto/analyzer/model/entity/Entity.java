package org.softauto.analyzer.model.entity;

import org.softauto.analyzer.model.test.Test;

import java.util.ArrayList;
import java.util.List;

public class Entity {

    String name;

    String clazz;

    List<Test> creator = new ArrayList<>();




    public String getName() {
        return name;
    }

    public Entity setName(String name) {
        this.name = name;
        return this;
    }

    public String getClazz() {
        return clazz;
    }

    public Entity setClazz(String clazz) {
        this.clazz = clazz;
        return this;
    }

    public List<Test> getCreator() {
        return creator;
    }

    public void setCreator(List<Test> creator) {
        this.creator = creator;
    }

    public void addCreator(Test creator) {
        this.creator.add(creator);
    }
}
