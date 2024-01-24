package org.softauto.handlers;

import org.softauto.config.Configuration;
import org.softauto.config.Context;
import org.softauto.utils.ApplyRule;
import org.softauto.utils.Entity;
import soot.SootClass;
import soot.SootField;

public class HandleEntity {

    SootClass sc;

    boolean entity ;

    public boolean isEntity() {
        return entity;
    }

    public HandleEntity setEntity(boolean entity) {
        this.entity = entity;
        return this;
    }

    public SootClass getClazz() {
        return sc;
    }

    public HandleEntity setClazz(SootClass sc) {
        this.sc = sc;
        return this;
    }

    private boolean isFieldExist(SootClass sc){
        boolean exist = false ;
        if(sc.getPackageName().contains(Configuration.get(Context.DOMAIN).asString()) ) {
            for (SootField f : sc.getFields()) {
                if (f.getName().equals("id")) {
                    exist = true;
                }
            }
        }
        if(!exist && sc.getPackageName().contains(Configuration.get(Context.DOMAIN).asString()) && sc.getSuperclass() != null){
            exist =  isFieldExist(sc.getSuperclass());
        }
        return exist ;
    }

    public HandleEntity build(){
        try {
            Object result = ApplyRule.setRule("entity_identify").addContext("class", sc).apply().getResult();
            if(result != null){
                setEntity((Boolean) result);
            }
            if(isFieldExist(sc)){
                setEntity(true);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        //if(sc.getPackageName().contains(Configuration.get(Context.DOMAIN).asString()) && sc.getFields().contains("id")){
         //   setEntity(true);
        //}
        return this;
    }
}
