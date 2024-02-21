package org.softauto.state;

import org.softauto.analyzer.model.genericItem.External;
import soot.*;
import soot.jimple.internal.JAssignStmt;
import soot.jimple.internal.JInvokeStmt;

import java.util.*;

public class ExternalFactory {

    private String method;

    private String clazz;

    private boolean api = false;

    private  String apiClass ;

    private  String apiMethod ;

    private Unit unit;

    private Body body;

    LinkedList<External> externals = new LinkedList<>();

    private static Map locals = new HashMap();

    private Set<String> relevantMethods = new HashSet<>();

    private Set<String> relevantClasses = new HashSet<>();

    private static int id;

    LinkedList<External> allExternals = new LinkedList<>();

    private SootMethod sootMethod;

    private SootClass sootClass;

    public static Map getLocals() {
        return locals;
    }

    public static void setLocals(Map locals) {
        ExternalFactory.locals = locals;
    }

    public ExternalFactory setSootMethod(SootMethod sootMethod) {
        this.sootMethod = sootMethod;
        return this;
    }

    public ExternalFactory setSootClass(SootClass sootClass) {
        this.sootClass = sootClass;
        return this;
    }

    public ExternalFactory setAllExternals(LinkedList<External> allExternals) {
        this.allExternals = allExternals;
        return this;
    }

    public ExternalFactory setUnit(Unit unit) {
        this.unit = unit;
        return this;
    }

    private LinkedList<String> calling = new LinkedList<>();

    public LinkedList<String> getCalling() {
        return calling;
    }

    public ExternalFactory setCalling(LinkedList<String> calling) {
        this.calling = calling;
        return this;
    }

    public ExternalFactory addCalling(String calling) {
        this.calling.add(calling);
        return this;
    }

    public LinkedList<External> getExternals() {
        return externals;
    }

    public ExternalFactory setMethod(String method) {
        this.method = method;
        return this;
    }

    public ExternalFactory setClazz(String clazz) {
        this.clazz = clazz;
        return this;
    }

    public ExternalFactory setApi(boolean api) {
        this.api = api;
        return this;
    }

    public ExternalFactory setApiClass(String apiClass) {
        this.apiClass = apiClass;
        return this;
    }

    public ExternalFactory setApiMethod(String apiMethod) {
        this.apiMethod = apiMethod;
        return this;
    }

    public ExternalFactory setBody(Body body) {
        this.body = body;
        return this;
    }

    public static void setId(int id) {
        ExternalFactory.id = id;
    }

    public ExternalFactory setId1(int id) {
        ExternalFactory.id = id;
        return this;
    }

    public ExternalFactory build(){
        try {
            ExternalVisitor externalVisitor = new ExternalVisitor().setId(id).setApi(api).setApiClass(apiClass).setApiMethod(apiMethod).setClazz(clazz).setMethod(method);
                if(unit instanceof JAssignStmt) {
                    externalVisitor.visit((JAssignStmt)unit);
                }
                if(unit instanceof JInvokeStmt) {
                    externalVisitor.visit((JInvokeStmt)unit);
                }

            externals = externalVisitor.getExternals();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return this;
    }


}
