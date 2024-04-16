package org.softauto.model.item;


import java.util.*;

public class Item {

    protected String name;

    protected String fullname;

    protected String namespce;

    private HashMap<String, Object> annotations ;

    protected LinkedList<String> parametersTypes = new LinkedList<>();

    protected List<String> argumentsNames = new ArrayList<>();

    protected HashMap<Integer,Boolean> argsType = new HashMap<>();

    protected boolean returnTypeGeneric ;

    protected int id;

    private String unboxReturnTypeTargetObject;


    protected String resultParameterizedType;

    protected String type ;

    private String returnType;

    private String unboxReturnType;

    private String returnName;

    private int modifier;

    private List<String> responseChain = new ArrayList<>();

    private String subsignature;

    private HashMap<Integer,String> parametersParameterizedType = new HashMap<>();

    public String getFullname() {
        return fullname;
    }

    public void setFullname(String fullname) {
        this.fullname = fullname;
    }

    public HashMap<Integer, String> getParametersParameterizedType() {
        return parametersParameterizedType;
    }

    public String getUnboxReturnTypeTargetObject() {
        return unboxReturnTypeTargetObject;
    }

    public void setUnboxReturnTypeTargetObject(String unboxReturnTypeTargetObject) {
        this.unboxReturnTypeTargetObject = unboxReturnTypeTargetObject;
    }

    public void setParametersParameterizedType(HashMap<Integer, String> parametersParameterizedType) {
        this.parametersParameterizedType = parametersParameterizedType;
    }

    public String getResultParameterizedType() {
        return resultParameterizedType;
    }

    public void setResultParameterizedType(String resultParameterizedType) {
        this.resultParameterizedType = resultParameterizedType;
    }

    public String getSubsignature() {
        return subsignature;
    }

    public void setSubsignature(String subsignature) {
        this.subsignature = subsignature;
    }


    public List<String> getResponseChain() {
        return responseChain;
    }

    public void setResponseChain(List<String> responseChain) {
        this.responseChain = responseChain;
    }

    public String getUnboxReturnType() {
        return unboxReturnType;
    }

    public void setUnboxReturnType(String unboxReturnType) {
        this.unboxReturnType = unboxReturnType;
    }

    public int getModifier() {
        return modifier;
    }

    public void setModifier(int modifier) {
        this.modifier = modifier;
    }

    public List<String> getArgumentsNames() {
        return argumentsNames;
    }

    public void setArgumentsNames(List<String> argumentsNames) {
        this.argumentsNames = argumentsNames;
    }

    public String getReturnName() {
        return returnName;
    }

    public void setReturnName(String returnName) {
        this.returnName = returnName;
    }

    List<Item> childes = new ArrayList<>();



    public List<Item> getChildes() {
        return childes;
    }

    public void setChildes(List<Item> childes) {
        this.childes = childes;
    }

    public void addChilde(Item child) {
        this.childes.add(child);
    }

    public String getReturnType() {
        return returnType;
    }

    public void setReturnType(String returnType) {
        this.returnType = returnType;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getNamespce() {
        return namespce;
    }

    public void setNamespce(String namespce) {
        this.namespce = namespce;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public HashMap<String, Object> getAnnotations() {
        return annotations;
    }

    public void setAnnotations(HashMap<String, Object> annotations) {
        this.annotations = annotations;
    }

    public LinkedList<String> getParametersTypes() {
        return parametersTypes;
    }

    public void setParametersTypes(LinkedList<String> parametersTypes) {
        this.parametersTypes = parametersTypes;
    }

    public HashMap<Integer, Boolean> getArgsType() {
        return argsType;
    }

    public void setArgsType(HashMap<Integer, Boolean> argsType) {
        this.argsType = argsType;
    }

    public boolean isReturnTypeGeneric() {
        return returnTypeGeneric;
    }

    public void setReturnTypeGeneric(boolean returnTypeGeneric) {
        this.returnTypeGeneric = returnTypeGeneric;
    }

}
