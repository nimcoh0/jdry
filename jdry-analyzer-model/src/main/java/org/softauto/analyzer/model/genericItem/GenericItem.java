package org.softauto.analyzer.model.genericItem;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.softauto.analyzer.directivs.request.Request;
import org.softauto.analyzer.directivs.result.Result;

import java.io.Serializable;
import java.util.*;

public class GenericItem implements   Cloneable, Serializable {

    protected String name;

    protected String type;

    protected String namespce;

    boolean namespaceChange = false;

    protected LinkedHashMap<String,Object> annotations = new LinkedHashMap();

    protected List<String> parametersTypes = new ArrayList<>();

    protected int id;

    protected String returnType;

    protected LinkedList<GenericItem> childes = new LinkedList<>();

    protected HashMap<String,Boolean> classInfo = new HashMap<>();

    protected LinkedList<String> argumentsNames = new LinkedList<>();

    private int modifier;


    public int getModifier() {
        return modifier;
    }

    public void setModifier(int modifier) {
        this.modifier = modifier;
    }

    public boolean isNamespaceChange() {
        return namespaceChange;
    }

    public void setNamespaceChange(boolean namespaceChange) {
        namespaceChange = namespaceChange;
    }

    @JsonIgnore
    LinkedHashMap<String,Object> classList = new LinkedHashMap<>();

    LinkedList<String> annotationsHelper = new LinkedList<>();

    @JsonIgnore
    protected Result response;

    private List<HashMap<String,String>> crudToSubject = new ArrayList<>();

    public List<HashMap<String, String>> getCrudToSubject() {
        return crudToSubject;
    }

    public void setCrudToSubject(List<HashMap<String, String>> crudToSubject) {
        this.crudToSubject = crudToSubject;
    }

    public void addCrudToSubject(String crud,String entity) {
        HashMap<String, String> hm = new HashMap<>();
        hm.put(crud,entity);
        this.crudToSubject.add(hm);
    }

    public LinkedList<String> getAnnotationsHelper() {
        return annotationsHelper;
    }

    public void setAnnotationsHelper(LinkedList<String> annotationsHelper) {
        this.annotationsHelper = annotationsHelper;
    }

    public void addAnnotationsHelper(String annotationsHelper) {
        this.annotationsHelper.add(annotationsHelper);
    }

    @JsonIgnore
    public LinkedHashMap<String, Object> getClassList() {
        return classList;
    }

    @JsonIgnore
    public void setClassList(LinkedHashMap<String, Object> classList) {
        this.classList = classList;
    }

    @JsonIgnore
    public void addClassList(LinkedHashMap<String, Object> classList) {
        this.classList.putAll(classList);
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

    @JsonIgnore
    public Result getResponse() {
        return response;
    }

    @JsonIgnore
    public void setResponse(Result response) {
        this.response = response;
    }

    @JsonIgnore
    protected Request request = new Request();

    @JsonIgnore
    public Request getRequest() {
        return request;
    }

    @JsonIgnore
    public void setRequest(Request request) {
        this.request = request;
    }

    public LinkedList<String> getArgumentsNames() {
        return argumentsNames;
    }

    public void setArgumentsNames(LinkedList<String> argumentsNames) {
        this.argumentsNames = argumentsNames;
    }

    public HashMap<String, Boolean> getClassInfo() {
        return classInfo;
    }

    public void setClassInfo(HashMap<String, Boolean> classInfo) {
        this.classInfo = classInfo;
    }

    public LinkedList<GenericItem> getChildes() {
        return childes;
    }

    public void setChildes(LinkedList<GenericItem> childes) {
        this.childes = childes;
    }

    public void addChilde(GenericItem child) {
        this.childes.add(child);
    }

    public String getReturnType() {
        return returnType;
    }

    public void setReturnType(String returnType) {
        this.returnType = returnType;
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

    public LinkedHashMap<String, Object> getAnnotations() {
        return annotations;
    }

    public void setAnnotations(LinkedHashMap<String, Object> annotations) {
        this.annotations = annotations;
    }

    public List<String> getParametersTypes() {
        return parametersTypes;
    }

    public void setParametersTypes(List<String> parametersTypes) {
        this.parametersTypes = parametersTypes;
    }




    @JsonIgnore
    public boolean isConstructor(){
        String clazz = namespce.substring(namespce.lastIndexOf(".")+1);
        if(name.equals(clazz)){
            return true;
        }
        return false;
    }
}
