package org.softauto.model.item;

import org.softauto.clazz.ClassInfo;
import org.softauto.analyzer.model.genericItem.External;

import java.util.*;

public class Item {

    protected String name;

    protected String namespce;

    private HashMap<String, Object> annotations ;

    protected List<String> parametersTypes = new ArrayList<>();

    protected List<String> argumentsNames = new ArrayList<>();

    protected HashMap<Integer,Boolean> argsType = new HashMap<>();

    protected boolean returnTypeGeneric ;

    protected int id;

    protected String type ;

    private String returnType;

    private String unboxReturnType;

    private String returnTypeName;

    private ClassInfo classInfo;

    private int modifier;

    private List<String> responseChain = new ArrayList<>();

    private List<External> externals = new ArrayList<>();

    public List<External> getExternals() {
        return externals;
    }

    public void setExternals(List<External> externals) {
        this.externals = externals;
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

    public String getReturnTypeName() {
        return returnTypeName;
    }

    public void setReturnTypeName(String returnTypeName) {
        this.returnTypeName = returnTypeName;
    }

    List<Item> childes = new ArrayList<>();

    public ClassInfo getClassInfo() {
        return classInfo;
    }

    public void setClassInfo(ClassInfo classInfo) {
        this.classInfo = classInfo;
    }

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

    public List<String> getParametersTypes() {
        return parametersTypes;
    }

    public void setParametersTypes(List<String> parametersTypes) {
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
