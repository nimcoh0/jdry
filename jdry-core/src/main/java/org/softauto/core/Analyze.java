package org.softauto.core;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.Marker;
import org.apache.logging.log4j.MarkerManager;
import org.softauto.analyzer.model.genericItem.GenericItem;

import java.util.ArrayList;
import java.util.List;

public class Analyze {


    private static final Logger logger = LogManager.getLogger(Analyze.class);

    private static final Marker JDRY = MarkerManager.getMarker("JDRY");

    String name;

    String namespace;

    String version;

    String doc;


    List<GenericItem> steps = new ArrayList<>();

    List<GenericItem> listeners = new ArrayList<>();



    public String getName() {
        return name;
    }

    public Analyze setName(String name) {
        this.name = name;
        return this;
    }

    public String getNamespace() {
        return namespace;
    }

    public Analyze setNamespace(String namespace) {
        this.namespace = namespace;
        return this;
    }

    public String getVersion() {
        return version;
    }

    public Analyze setVersion(String version) {
        this.version = version;
        return this;
    }

    public String getDoc() {
        return doc;
    }

    public Analyze setDoc(String doc) {
        this.doc = doc;
        return this;
    }

    public List<GenericItem> getSteps() {
        return steps;
    }

    public Analyze setSteps(List<GenericItem> steps) {
        this.steps = steps;
        return this;
    }

    public Analyze addStep(GenericItem step) {
        this.steps.add(step);
        return this;
    }

    public List<GenericItem> getListeners() {
        return listeners;
    }

    public Analyze setListeners(List<GenericItem> listeners) {
        this.listeners = listeners;
        return this;
    }

    public Analyze addListener(GenericItem listener) {
        this.listeners.add(listener);
        return this;
    }

    public String toJson(){
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            objectMapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS,false);
            objectMapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
            JsonNode node =objectMapper.valueToTree(this);
            String schema = objectMapper.writeValueAsString(node);
            return schema;
        } catch (Exception e) {
            logger.error(JDRY,"fail toJson",e);
        }
        return null;
    }

    public static Analyze parse(String json){
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            objectMapper.enable(DeserializationFeature.ACCEPT_SINGLE_VALUE_AS_ARRAY);
            return objectMapper.readValue(json,Analyze.class);
        } catch (JsonProcessingException e) {
            logger.error(JDRY,"fail parse",e);
        }
        return null;
    }

}
