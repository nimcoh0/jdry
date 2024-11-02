package org.softauto.analyzer.model.genericItem;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import org.softauto.analyzer.model.request.Request;
import org.softauto.analyzer.model.result.Result;
import org.softauto.annotations.ClassType;

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

    //private String unboxReturnTypeTargetObject;

    /**
     * method type can be "method" or "class"
     */
    protected String type;

    /**
     * method class full name
     */
    protected String namespce;

    //boolean namespaceChange = false;

   // protected String resultParameterizedType;

    String fullname;

    /**
     * method annotations
     */
    protected LinkedHashMap<String,Object> annotations = new LinkedHashMap();

    /**
     * method parameters Types
      */
    protected LinkedList<String> parametersTypes = new LinkedList<>();

    /**
     * method id by soot
     */
    protected int id;

    //protected int typeId;

    /**
     * method return type
     */
    protected String returnType;

    protected boolean returnTypeGeneric = false;

    //protected String unboxReturnType;

    //protected String returnName;

    /**
     * call methods with interest
     */
    protected LinkedList<GenericItem> childes = new LinkedList<>();

    //protected HashMap<String,Boolean> classInfo = new HashMap<>();

    /**
     * method arguments name
     */
    protected LinkedList<String> argumentsNames = new LinkedList<>();

    /**
     * method modifier
     */
    private int modifier;

    protected List<HashMap<String,String>> constructorParameter = new ArrayList<>();

    protected ClassType classType;

    //private Set<GenericItem> linkedMethods = new HashSet<>();

    private String subsignature;

    private HashMap<String,Object>  properties = new HashMap<>();

    private LinkedList<String> responseChain = new LinkedList<>();

    private HashMap<Integer,Boolean> argsType = new HashMap<>();

    //public String getUnboxReturnTypeTargetObject() {
      //  return unboxReturnTypeTargetObject;
   // }

    //public void setUnboxReturnTypeTargetObject(String unboxReturnTypeTargetObject) {
      // this.unboxReturnTypeTargetObject = unboxReturnTypeTargetObject;
   // }

    private HashMap<Integer,String> parametersParameterizedType = new HashMap<>();

    public HashMap<Integer, String> getParametersParameterizedType() {
        return parametersParameterizedType;
    }

    public void setParametersParameterizedType(HashMap<Integer, String> parametersParameterizedType) {
        this.parametersParameterizedType = parametersParameterizedType;
    }

    //public String getResultParameterizedType() {
      //  return resultParameterizedType;
    //}

   // public void setResultParameterizedType(String resultParameterizedType) {
     //   this.resultParameterizedType = resultParameterizedType;
   // }

    public String getSubsignature() {
        return subsignature;
    }

    public void setSubsignature(String subsignature) {
        this.subsignature = subsignature;
    }



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

    public LinkedList<String> getResponseChain() {
        return responseChain;
    }

    public void setResponseChain(LinkedList<String> responseChain) {
        this.responseChain = responseChain;
    }

    public List<HashMap<String, String>> getConstructorParameter() {
        return constructorParameter;
    }

    //public String getReturnName() {
      //  return returnName;
    //}

   // public void setReturnName(String returnName) {
     //   this.returnName = returnName;
   // }

    //public String getUnboxReturnType() {
     //   return unboxReturnType;
   // }

   // public void setUnboxReturnType(String unboxReturnType) {
    //    this.unboxReturnType = unboxReturnType;
   // }

    public void setConstructorParameters(List<HashMap<String, String>> parameters) {
        this.constructorParameter = parameters;
    }

    public void addConstructorParameter(String key,String value) {
        HashMap<String, String> map = new HashMap<>();
        map.put(key,value);
        this.constructorParameter.add(map);
    }



    public ClassType getClassType() {
        return classType;
    }

    public void setClassType(ClassType classType) {
        this.classType = classType;
    }

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

    public LinkedList<String> getParametersTypes() {
        return parametersTypes;
    }

    public void setParametersTypes(LinkedList<String> parametersTypes) {
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
