package org.softauto.discovery.handlers.flow;

import soot.SootMethod;
import soot.jimple.toolkits.callgraph.CallGraph;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class FlowObject {

    private String name ;

    private String clazz;

    private CallGraph cg ;

    private List<FlowObject> chileds = new ArrayList<>();

    private SootMethod method ;

    private ClassInfo classInfo;

    private List<String> argsname;

    private boolean isConstructor;

    private boolean isStaticInitializer;

    private boolean  isStatic;

    private boolean entity;

    private String returnType = "void";

    private List<HashMap<String,String>> crudToSubject = new ArrayList<>();

    public List<HashMap<String,String>> getCrudToSubject() {
        return crudToSubject;
    }

    public void setCrudToSubject(List<HashMap<String,String>> crudToSubject) {
        this.crudToSubject = crudToSubject;
    }

    public boolean isEntity() {
        return entity;
    }

    public void setEntity(boolean entity) {
        this.entity = entity;
    }

    public String getReturnType() {
        return returnType;
    }

    public void setReturnType(String returnType) {
        this.returnType = returnType;
    }

    public boolean isStatic() {
        return isStatic;
    }

    public void setStatic(boolean aStatic) {
        isStatic = aStatic;
    }

    public boolean isStaticInitializer() {
        return isStaticInitializer;
    }

    public void setStaticInitializer(boolean staticInitializer) {
        isStaticInitializer = staticInitializer;
    }

    public boolean isConstructor() {
        return isConstructor;
    }

    public void setConstructor(boolean constructor) {
        isConstructor = constructor;
    }

    public List<String> getArgsname() {
        return argsname;
    }

    public void setArgsname(List<String> argsname) {
        this.argsname = argsname;
    }

    public ClassInfo getClassInfo() {
        return classInfo;
    }

    public void setClassInfo(ClassInfo classInfo) {
        this.classInfo = classInfo;
    }

    public List<FlowObject> getChileds() {
        return chileds;
    }

    public void setChileds(List<FlowObject> chileds) {
        this.chileds = chileds;
    }

    public void addChiled(FlowObject chiled) {
        this.chileds.add(chiled);
    }

    public String getClazz() {
        return clazz;
    }

    public void setClazz(String clazz) {
        this.clazz = clazz;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public CallGraph getCg() {
        return cg;
    }

    public void setCg(CallGraph cg) {
        this.cg = cg;
    }

    public SootMethod getMethod() {
        return method;
    }

    public void setMethod(SootMethod method) {
        this.method = method;
    }



}
