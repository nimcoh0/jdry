package org.softauto.handlers;

import org.apache.commons.lang3.ObjectUtils;
import org.softauto.analyzer.model.genericItem.External;
import org.softauto.config.Configuration;
import org.softauto.config.Context;
import org.softauto.utils.Util;
import soot.*;
import soot.jimple.internal.*;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class HandleExternal {


    private Body body;

    private String method;

    private String clazz;

    public HandleExternal setClazz(String clazz) {
        this.clazz = clazz;
        return this;
    }

    List<External> externals = new ArrayList<>();

    public HandleExternal setMethod(String method) {
        this.method = method;
        return this;
    }

    public List<External> getExternals() {
        return externals;
    }

    private HandleExternal setExternals(List<External> externals) {
        this.externals = externals;
        return this;
    }

    private HandleExternal addExternals(External external) {
        this.externals.add(external) ;
        return this;
    }





    public HandleExternal setBody(Body body) {
        this.body = body;
        return this;
    }



    public void parse() {
        for(Unit unit : body.getUnits()){
            unit.getTags();
            for(ValueBox valueBox : unit.getUseAndDefBoxes()) {
                if(valueBox.getValue() instanceof JInstanceFieldRef){
                    //if(((JInstanceFieldRef)valueBox.getValue()).getFieldRef().declaringClass().getName().contains(Configuration.get(Context.DOMAIN).asString())) {
                        External external = new External();
                        external.setName(((JInstanceFieldRef) valueBox.getValue()).getFieldRef().name());
                        //external.setTarget(((JInstanceFieldRef)valueBox.getValue()).getFieldRef().type().toString());
                        //external.setSootType("JVirtualInvokeExpr");
                        external.setClazz(((JInstanceFieldRef) valueBox.getValue()).getFieldRef().declaringClass().getName());
                        external.setMethod(method);
                        //external.setResult(((AbstractInvokeExpr) valueBox.getValue()).getMethodRef().getReturnType().toString());
                        external.setType(((JInstanceFieldRef) valueBox.getValue()).getFieldRef().type().toString());
                        //external.setArgs(setArgs(((AbstractInvokeExpr) valueBox.getValue()).getArgs()));
                        external.setStatic(((JInstanceFieldRef) valueBox.getValue()).getFieldRef().isStatic());
                        external.setMode("field");
                        if (!Util.isExist(externals, external)) {
                            externals.add(external);
                        }
                  // }
                }
                if(valueBox instanceof JAssignStmt){

                }
                if (valueBox.getValue() instanceof AbstractInvokeExpr) {
                    valueBox.getTags();
                    String  refClassName = ((AbstractInvokeExpr) valueBox.getValue()).getMethodRef().getDeclaringClass().getName();
                    if(refClassName.contains(Configuration.get(Context.DOMAIN).asString()) &&  valueBox.getValue() instanceof JVirtualInvokeExpr ) {
                        External external = new External();
                        external.setTarget(refClassName);
                        //external.setSootType("JVirtualInvokeExpr");
                        external.setClazz(clazz);
                        external.setMethod(((AbstractInvokeExpr) valueBox.getValue()).getMethodRef().getName());
                        external.setResult(((AbstractInvokeExpr) valueBox.getValue()).getMethodRef().getReturnType().toString());
                        external.setTypes(setTypes(((AbstractInvokeExpr) valueBox.getValue()).getMethodRef().getParameterTypes()));
                        external.setArgs(setArgs(((AbstractInvokeExpr) valueBox.getValue()).getArgs()));
                        external.setMode("method");
                        if (!Util.isExist(externals, external)) {
                            externals.add(external);
                        }
                    }
                }
/*
                    if(valueBox.getValue() instanceof JInterfaceInvokeExpr ) {
                        valueBox.getTags();
                        External external = new External();
                        //external.setTarget(refClassName);
                        //external.setSootType("JInterfaceInvokeExpr");
                        external.setClazz(clazz);
                        external.setMethod(((AbstractInvokeExpr) valueBox.getValue()).getMethodRef().getName());
                        external.setResult(((AbstractInvokeExpr) valueBox.getValue()).getMethodRef().getReturnType().toString());
                        external.setTypes(setTypes(((AbstractInvokeExpr) valueBox.getValue()).getMethodRef().getParameterTypes()));
                        external.setArgs(setArgs(((AbstractInvokeExpr) valueBox.getValue()).getArgs()));
                        external.setMode("interface");
                        if(!Util.isExist(externals,external)) {
                            externals.add(external);
                        }

                    }


 */


                   }
                }
            }

//&& refClassName.contains(Configuration.get(Context.DOMAIN).asString())

    private LinkedList<String> setArgs(List<Value> args) {
        LinkedList<String> list = new LinkedList<>();
        for(Value value : args){
           list.add(value.toString());
        }
        return list;
    }

    private LinkedList<String> setTypes(List<Type> types) {
        LinkedList<String> list = new LinkedList<>();
        for(Type type : types){
            list.add(type.toString());
        }
        return list;
    }
}
