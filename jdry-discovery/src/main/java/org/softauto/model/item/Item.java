package org.softauto.model.item;

import org.softauto.clazz.ClassInfo;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class Item {

    protected String name;

    protected String namespce;

    private HashMap<String, Object> annotations ;

    protected List<String> parametersTypes = new ArrayList<>();

    protected List<String> argumentsNames = new ArrayList<>();

    protected int id;

    protected String type ;

    private String returnType;

    private String returnTypeName;

    private ClassInfo classInfo;

    private int modifier;

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
}
