package org.softauto.flow;

import soot.SootMethod;
import soot.Type;
import soot.jimple.toolkits.callgraph.CallGraph;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

public class FlowObject {

    private String name ;

    private String clazz;

    private CallGraph cg ;

    private List<FlowObject> chileds = new ArrayList<>();

    private SootMethod method ;

    //private String unboxReturnTypeTargetObject;

    private List<String> argsname;

    private HashMap<Integer,Boolean> argsType = new HashMap<>();

    private boolean returnTypeGeneric ;

    private boolean isConstructor;

    private boolean isStaticInitializer;

    private boolean  isStatic;

    private boolean entity;

    private String returnType = "void";

    //private String unboxReturnType;

    //private String returnName ;

    private List<String> responseChain = new ArrayList<>();

    //private List<HashMap<String,String>> crudToSubject = new ArrayList<>();

    private LinkedList<String> parametersType = new LinkedList<>();

    private String subsignature;

   // private String resultParameterizedType;

    private HashMap<Integer,String> parametersParameterizedType = new HashMap<>();

    public HashMap<Integer, String> getParametersParameterizedType() {
        return parametersParameterizedType;
    }

    public LinkedList<String> getParametersType() {
        return parametersType;
    }

    //public String getUnboxReturnTypeTargetObject() {
      //  return unboxReturnTypeTargetObject;
   // }

    //public void setUnboxReturnTypeTargetObject(String unboxReturnTypeTargetObject) {
      //  this.unboxReturnTypeTargetObject = unboxReturnTypeTargetObject;
   // }

    public void setParametersType(List<Type> parametersType) {
        LinkedList<String> _parametersType = new LinkedList<>();
        for(Type t : parametersType){
            _parametersType.add(t.toString());
        }
        this.parametersType = _parametersType;
    }

    public void setParametersParameterizedType(HashMap<Integer, String> parametersParameterizedType) {
        this.parametersParameterizedType = parametersParameterizedType;
    }

    //public String getResultParameterizedType() {
      //  return resultParameterizedType;
    //}

    //public void setResultParameterizedType(String resultParameterizedType) {
        //this.resultParameterizedType = resultParameterizedType;
    //}

    public String getSubsignature() {
        return subsignature;
    }

    public void setSubsignature(String subsignature) {
        this.subsignature = subsignature;
    }



    //public List<HashMap<String,String>> getCrudToSubject() {
      //  return crudToSubject;
    //}

    //public void setCrudToSubject(List<HashMap<String,String>> crudToSubject) {
       // this.crudToSubject = crudToSubject;
   // }

    public boolean isReturnTypeGeneric() {
        return returnTypeGeneric;
    }

    public void setReturnTypeGeneric(boolean returnTypeGeneric) {
        this.returnTypeGeneric = returnTypeGeneric;
    }

    public HashMap<Integer, Boolean> getArgsType() {
        return argsType;
    }

    public void setArgsType(HashMap<Integer, Boolean> argsType) {
        this.argsType = argsType;
    }

    public void addArgsType(Integer index,Boolean generic) {
        this.argsType.put(index,generic);
    }

    public List<String> getResponseChain() {
        return responseChain;
    }

    public void setResponseChain(List<String> responseChain) {
        this.responseChain = responseChain;
    }

   // public String getUnboxReturnType() {
       // return unboxReturnType;
   // }

   // public void setUnboxReturnType(String unboxReturnType) {
     //   this.unboxReturnType = unboxReturnType;
   // }

    public boolean isEntity() {
        return entity;
    }

    public void setEntity(boolean entity) {
        this.entity = entity;
    }

    public String getReturnType() {
        return returnType;
    }

    //public String getReturnName() {
       // return returnName;
   // }

   // public void setReturnName(String returnName) {
     //   this.returnName = returnName;
   // }

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
