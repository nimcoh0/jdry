package org.softauto.analyzer.model.suite;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import org.softauto.analyzer.directivs.field.Field;
import org.softauto.analyzer.model.listener.Listener;
import org.softauto.analyzer.model.scenario.Scenario;
import org.softauto.analyzer.model.test.Test;

import java.io.File;
import java.io.IOException;
import java.io.Serializable;
import java.util.*;

public class Suite implements Cloneable, Serializable {

    private String name;

    private String namespace;

    private LinkedList<Test> tests = new LinkedList<>();


    private LinkedList<Scenario> scenarios = new LinkedList<>();

    private List<Listener> listeners = new ArrayList<>();



    private static List<Field> fields = new ArrayList<>();

    private static  HashMap<String,String> entities = new HashMap<>();

    private static Test loginTest;

    private static HashMap<String,List<String>> classListOfMethodsAnnotationSummery = new HashMap<>();

    public static HashMap<String, List<String>> getClassListOfMethodsAnnotationSummery() {
        return classListOfMethodsAnnotationSummery;
    }

    public static void setClassListOfMethodsAnnotationSummery(HashMap<String, List<String>> classListOfMethodsAnnotationSummery) {
        Suite.classListOfMethodsAnnotationSummery = classListOfMethodsAnnotationSummery;
    }

    public static void addClassListOfMethodsAnnotationSummery(String key, List<String> value) {
        if(Suite.classListOfMethodsAnnotationSummery.containsKey(key)){
           List<String> m = Suite.classListOfMethodsAnnotationSummery.get(key);
           m.addAll(value);
           Suite.classListOfMethodsAnnotationSummery.put(key,m) ;
        }else {
           Suite.classListOfMethodsAnnotationSummery.put(key, value);
        }
    }

    public  static HashMap<String,String> getEntities() {
        return entities;
    }

    public static String getEntityClass(String name){
        if(name.endsWith("s")){
            name = name.substring(0,name.length()-1);
        }
        for(Map.Entry entity : entities.entrySet()){
            if(entity.getKey().equals(name.toLowerCase())){
                return entity.getValue().toString();
            }
        }
        return null;
    }

    public static boolean hasEntity(String name){
        if(name.endsWith("s")){
            name = name.substring(0,name.length()-1);
        }
        for(Map.Entry entity : entities.entrySet()){
            if(entity.getKey().equals(name.toLowerCase())){
                return true;
            }
        }
        return false;
    }

    public static void setEntities( HashMap<String,String> entities) {
        Suite.entities = entities;
    }

    public static void addEntity(String key, String value) {
        entities.put(key,value);
    }

    public static Test getLoginTest() {
        return loginTest;
    }

    public static void setLoginTest(Test _loginTest) {
        loginTest = _loginTest;
    }

    public String getName() {
        return name;
    }

    public Suite setName(String name) {
        this.name = name;
        return this;
    }

    public String getNamespace() {
        return namespace;
    }

    public Suite setNamespace(String namespace) {
        this.namespace = namespace;
        return this;
    }

    public LinkedList<Test> getTests() {
        return tests;
    }

    public Suite setTests(LinkedList<Test> tests) {
        this.tests = tests;
        return this;
    }


    public Suite addTest(Test test) {
        this.tests.add(test);
        return this;
    }



    public LinkedList<Scenario> getScenarios() {
        return scenarios;
    }

    public Suite setScenarios(LinkedList<Scenario> scenarios) {
        this.scenarios = scenarios;
        return this;
    }

    public Suite addScenario(Scenario scenario) {
        this.scenarios.add(scenario);
        return this;
    }

    public Listener getListener(String name) {
        for(Listener listener : listeners){
            if(listener.getFullName().equals(name.replace(".","_"))){
                return listener;
            }
        }
        return null;
    }


    public List<Listener> getListeners() {
        return listeners;
    }

    public  void setListeners(List<Listener> listeners) {
        this.listeners = listeners;
    }

    public void addListener(Listener listener) {
        this.listeners.add(listener);
    }



    public static List<Field> getFields() {
        return fields;
    }

    public static void setFields(List<Field> fields) {
        Suite.fields = fields;
    }

    static final JsonFactory FACTORY = new JsonFactory();

    static final ObjectMapper MAPPER = new ObjectMapper(FACTORY);

    public String toJson(){
        try {
            ObjectMapper mapper =  new ObjectMapper();
            mapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
            JsonNode node = mapper.valueToTree(this);
            return node.toString();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    private static Suite parse(JsonParser parser) {
        try {
            Suite suite = new Suite();
            suite.parse((JsonNode) Suite.MAPPER.readTree(parser));
            return suite;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    private void parse(JsonNode readTree) {
        try {
            name = readTree.get("name").asText();
            namespace = readTree.get("namespace").asText();
            parseTests((ArrayNode)readTree.get("tests"));

            parseListeners((ArrayNode)readTree.get("listeners"));
            parseScenarios((ArrayNode)readTree.get("scenarios"));
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    private void parseScenarios(ArrayNode arrayNode){
        for(JsonNode node : arrayNode) {
            Scenario scenario = new ObjectMapper().convertValue(node,Scenario.class);
            scenarios.add(scenario);
        }
    }

    private void parseTests(ArrayNode arrayNode){
        for(JsonNode node : arrayNode) {
            parseTest(node);
        }
    }



    private void parseListeners(ArrayNode arrayNode){
        for(JsonNode node : arrayNode) {
            parseListeners(node);
        }
    }


    private void parseTest(JsonNode node){
        Test test = new ObjectMapper().convertValue(node,Test.class);
        tests.add(test);
    }



    private void parseListeners(JsonNode node){
        Listener listener = new ObjectMapper().convertValue(node,Listener.class);
        listeners.add(listener);
    }

    @JsonIgnore
    public String getPublishId(String dependencieName){
        for(Test test : tests){
            if(test.getFullName().equals(dependencieName)){
                return test.getTestId();
            }
        }
        return null;
    }

    public static Suite parse(File file) throws IOException {
        return parse(Suite.FACTORY.createParser(file));
    }

    public boolean isScenarioExist(String scenarioId){
        for(Scenario scenario : scenarios){
            if(scenario.getId().equals(scenarioId)){
                return true;
            }
        }
        return false;
    }

    public Scenario getScenario(String scenarioId){
        for(Scenario scenario : scenarios){
            if(scenario.getId().equals(scenarioId)){
                return scenario;
            }
        }
        return null;
    }



}
