package org.softauto.utils;

import soot.SootClass;

import java.util.LinkedList;

public class Entity {

    SootClass entity;

    public SootClass getEntity() {
        return entity;
    }

    public Entity setEntity(SootClass entity) {
        this.entity = entity;
        return this;
    }
}
