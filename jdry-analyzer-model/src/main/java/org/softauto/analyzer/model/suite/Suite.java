package org.softauto.analyzer.model.suite;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
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

    //private LinkedList<Test> tests = new LinkedList<>();

    private ObjectNode tests = new ObjectMapper().createObjectNode();

    private static List<String> protocols = new ArrayList<>();

    private ObjectNode scenarios = new ObjectMapper().createObjectNode();

    private ObjectNode listeners = new ObjectMapper().createObjectNode();



    private static List<Field> fields = new ArrayList<>();

    private static  HashMap<String,String> entities = new HashMap<>();

    private static Test loginTest;

    private static HashMap<String,List<String>> classListOfMethodsAnnotationSummery = new HashMap<>();

    public List<String> getProtocols() {
        return protocols;
    }

    public static void setProtocols(List<String> protocols) {
        Suite.protocols = protocols;
    }

    public static void addProtocol(String protocol) {
        Suite.protocols.add(protocol);
    }

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

    public List<Test> getTests() {
        List<Test> _tests = new ArrayList<>();
        try {
            for(JsonNode node :this.tests){
                String json = new ObjectMapper().writeValueAsString(node);
                _tests.add(new ObjectMapper().readValue(json,Test.class));
            }
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        return _tests;
    }

    public Suite setTests(ObjectNode tests) {
        this.tests = tests;
        return this;
    }


    public Suite addTest(Test test) {
        try {
            String json = new ObjectMapper().writeValueAsString(test);
            JsonNode node = new ObjectMapper().readTree(json);
            this.tests.set(test.getFullName(),node);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        return this;
    }



    public List<Scenario> getScenarios() {
        List<Scenario> _scenarios = new ArrayList<>();
        try {
            for(JsonNode node : scenarios) {
                String json = new ObjectMapper().writeValueAsString(node);
                _scenarios.add(new ObjectMapper().readValue(json, Scenario.class));
            }
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        return _scenarios;
    }

    public Suite setScenarios(ObjectNode scenarios) {
        this.scenarios = scenarios;
        return this;
    }

    public Suite addScenario(Scenario scenario) {
        try {
            String json = new ObjectMapper().writeValueAsString(scenario);
            JsonNode node = new ObjectMapper().readTree(json);
            this.scenarios.set(scenario.getSuiteName(),node);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        return this;
    }

    public Listener getListener(String name) {
        try {
            for(JsonNode listener : listeners){
                if(listener.get("fullName").equals(name.replace(".","_"))){
                    String json = new ObjectMapper().writeValueAsString(listener);
                    return new ObjectMapper().readValue(json,Listener.class);

                }
            }
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        return null;
    }

    public boolean isListenerExist(Listener listener){
        for(JsonNode listener1 : listeners){
            if(listener1.get("fullName").equals(listener.getFullName())){
                return true;
            }
        }
        return false;
    }

    public List<Listener> getListeners() {
        List<Listener> _listeners = new ArrayList<>();
        try {
            for(JsonNode node : listeners) {
                String json = new ObjectMapper().writeValueAsString(node);
                _listeners.add(new ObjectMapper().readValue(json, Listener.class));
            }
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        return _listeners;
    }

    public  void setListeners(JsonNode listeners) {
        this.listeners = (ObjectNode) listeners;
    }

    public void addListener(Listener listener) {
        try {
            if(!isListenerExist(listener)) {
                String json = new ObjectMapper().writeValueAsString(listener);
                JsonNode node = new ObjectMapper().readTree(json);
                this.listeners.set(listener.getFullName(),node);
            }
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
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
            parseTests(readTree.get("tests"));

            parseListeners(readTree.get("listeners"));
            parseProtocols((ArrayNode)readTree.get("protocols"));
            //parseScenarios(readTree.get("scenarios"));
        } catch (Exception e) {
            e.printStackTrace();
        }

    }




    private static Suite parseTests(JsonParser parser) {
        try {
            Suite suite = new Suite();
            suite.parseTests((JsonNode) Suite.MAPPER.readTree(parser));
            return suite;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    private void parseTests(JsonNode jsonNode){
        try {
            name = jsonNode.get("name").asText();
            namespace = jsonNode.get("namespace").asText();
            for(JsonNode node : jsonNode.get("tests")) {
                parseTest(node);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }




    private static Suite parseListeners(JsonParser parser) {
        try {
            Suite suite = new Suite();
            suite.parseListeners((JsonNode) Suite.MAPPER.readTree(parser));
            return suite;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    private void parseListeners(JsonNode jsonNode){
        name = jsonNode.get("name").asText();
        namespace = jsonNode.get("namespace").asText();
        for(JsonNode node : jsonNode.get("listeners")) {
            Listener listener = new ObjectMapper().convertValue(node,Listener.class);
            listeners.set(node.get("fullName").asText(),node);
        }
    }

    private static Suite parseProtocols(JsonParser parser) {
        try {
            Suite suite = new Suite();
            suite.parseProtocols((JsonNode) Suite.MAPPER.readTree(parser));
            return suite;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    private void parseProtocols(JsonNode readTree) {
        name = readTree.get("name").asText();
        namespace = readTree.get("namespace").asText();
        parseProtocols((ArrayNode)readTree.get("protocols"));
    }


    private void parseProtocols(ArrayNode arrayNode){
       for(JsonNode node : arrayNode){
           protocols.add(node.asText());
       }
    }











    private void parseTest(JsonNode node){
        //Test test = new ObjectMapper().convertValue(node,Test.class);
        tests.set(node.get("fullName").asText(),node);
    }


/*
    private void parseListeners1(JsonNode node){
        Listener listener = new ObjectMapper().convertValue(node,Listener.class);
        listeners.set(node.get("fullName").asText(),node);
    }


 */
    @JsonIgnore
    public String getPublishId(String dependencieName){
        for(JsonNode test : tests){
            if(test.get("fullName").equals(dependencieName)){
                return test.get("testId").asText();
            }
        }
        return null;
    }

    public static Suite parse(File file) throws IOException {
        return parse(Suite.FACTORY.createParser(file));
    }

    public static Suite parseTests(File file) throws IOException {
        return parseTests(Suite.FACTORY.createParser(file));
    }

    public static Suite parseListeners(File file) throws IOException {
        return parseListeners(Suite.FACTORY.createParser(file));
    }

    public static Suite parseProtocols(File file) throws IOException {
        return parseProtocols(Suite.FACTORY.createParser(file));
    }



    public boolean isScenarioExist(String scenarioId){
        for(JsonNode scenario : scenarios){
            if(scenario.get("id").equals(scenarioId)){
                return true;
            }
        }
        return false;
    }

    public Scenario getScenario(String scenarioId){
        try {
            for(JsonNode scenario : scenarios){
                if(scenario.get("id").equals(scenarioId)){
                    String json = new ObjectMapper().writeValueAsString(scenario);
                    return new ObjectMapper().readValue(json, Scenario.class);
                }
            }
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        return null;
    }



}
