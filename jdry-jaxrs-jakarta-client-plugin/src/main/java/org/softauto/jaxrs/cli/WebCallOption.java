package org.softauto.jaxrs.cli;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.ws.rs.core.MediaType;
import org.softauto.core.AbstractCallOption;

import java.util.HashMap;

public class WebCallOption extends AbstractCallOption {

    private Class[] types;

    private MediaType produce;

    private MediaType consume;

    private String fullMethodName;

    private String method;

    private String scenarioId;

    private String path;

    private String response;

    private String[] argumentsNames;

    public String getPath() {
        return path;
    }

    public WebCallOption setPath(String path) {
        this.path = path;
        return this;
    }

    public String getResponse() {
        return response;
    }

    public WebCallOption setResponse(String response) {
        this.response = response;
        return this;
    }

    public String[] getArgumentsNames() {
        return argumentsNames;
    }

    public WebCallOption setArgumentsNames(String[] argumentsNames) {
        this.argumentsNames = argumentsNames;
        return this;
    }



    public Class[] getTypes() {
        return types;
    }

    public WebCallOption setTypes(Class[] types) {
        this.types = types;
        return this;
    }

    public MediaType getProduce() {
        return produce;
    }

    public WebCallOption setProduce(MediaType produce) {
        this.produce = produce;
        return this;
    }

    public MediaType getConsume() {
        return consume;
    }

    public WebCallOption setConsume(MediaType consume) {
        this.consume = consume;
        return this;
    }

    public String getFullMethodName() {
        return fullMethodName;
    }

    public WebCallOption setFullMethodName(String fullMethodName) {
        this.fullMethodName = fullMethodName;
        return this;
    }

    public String getMethod() {
        return method;
    }

    public WebCallOption setMethod(String Method) {
        this.method = Method;
        return this;
    }

    public String getScenarioId() {
        return scenarioId;
    }

    public WebCallOption setScenarioId(String scenarioId) {
        this.scenarioId = scenarioId;
        return this;
    }

    @Override
    public HashMap<String,Object> toMap() {
        try {
            String json =  new ObjectMapper().writeValueAsString(this);
            return new ObjectMapper().readValue(json,HashMap.class);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        return null;
    }
}
