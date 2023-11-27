package org.softauto.analyzer.model.genericItem;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import org.softauto.analyzer.model.request.Request;
import org.softauto.analyzer.model.result.Result;

import java.io.Serializable;
import java.util.*;

/**
 * object represent discovery data for one method
 */
public class GenericItem implements   Cloneable, Serializable {

    /**
     * method name
     */
    protected String name;

    /**
     * method type can be "method" or "class"
     */
    protected String type;

    /**
     * method class full name
     */
    protected String namespce;

    boolean namespaceChange = false;

    String fullname;

    /**
     * method annotations
     */
    protected LinkedHashMap<String,Object> annotations = new LinkedHashMap();

    /**
     * method parameters Types
      */
    protected List<String> parametersTypes = new ArrayList<>();

    /**
     * method id by soot
     */
    protected int id;

    /**
     * method return type
     */
    protected String returnType;

    /**
     * call methods with interest
     */
    protected LinkedList<GenericItem> childes = new LinkedList<>();

    protected HashMap<String,Boolean> classInfo = new HashMap<>();

    /**
     * method arguments name
     */
    protected LinkedList<String> argumentsNames = new LinkedList<>();

    /**
     * method modifier
     */
    private int modifier;




    HashMap<String,Object>  properties = new HashMap<>();

    public String getFullname() {
        return fullname;
    }

    public void setFullname(String fullname) {
        this.fullname = fullname;
    }


    public HashMap<String, Object> getProperties() {
        return properties;
    }


    public void setProperties(HashMap<String, Object> properties) {
        this.properties = properties;
    }

    @JsonIgnore
    public void addProperty(String key,Object value) {
        this.properties.put(key,value) ;
    }

    public int getModifier() {
        return modifier;
    }

    public void setModifier(int modifier) {
        this.modifier = modifier;
    }

    @JsonIgnore
    LinkedHashMap<String,Object> classList = new LinkedHashMap<>();

    LinkedList<String> annotationsHelper = new LinkedList<>();

    @JsonIgnore
    protected Result response;



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

    public String toJson(){
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            objectMapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS,false);
            objectMapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
            JsonNode node =objectMapper.valueToTree(this);
            String schema = objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(node);
            return schema;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
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
