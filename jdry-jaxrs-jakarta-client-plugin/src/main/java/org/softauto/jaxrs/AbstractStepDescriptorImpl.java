package org.softauto.jaxrs;

import jakarta.ws.rs.core.MediaType;
import org.apache.logging.log4j.Marker;
import org.apache.logging.log4j.MarkerManager;
import org.softauto.core.TestContext;
import org.softauto.core.Utils;
import org.softauto.jaxrs.service.ChannelDescriptor;
import org.softauto.jaxrs.service.IStepDescriptor;
import org.softauto.jaxrs.service.RestService;
import org.softauto.jaxrs.service.ServiceCaller;

import java.util.HashMap;
import java.util.Map;

public abstract class AbstractStepDescriptorImpl implements IStepDescriptor {

    private static final org.apache.logging.log4j.Logger logger = org.apache.logging.log4j.LogManager.getLogger(AbstractStepDescriptorImpl.class);

    private static final Marker JDRY = MarkerManager.getMarker("JDRY");

    /**
     * additional data for call operation
     */
    protected HashMap<String,Object> callOptions;

    /**
     * call arguments value
     */
    protected Object[] args;

    /**
     * call arguments type
     */
    protected Class[] types;

    /**
     * media produce
     */
    protected MediaType produce;

    /**
     * media consume
     */
    protected MediaType consume;

    /**
     * full method name include package and class with "_" insted of ".'
     */
    protected String fullMethodName;

    /**
     * call method ..post/get ....
     */
    protected String httpMethod;

    /**
     * scenario id
     */
    protected String scenarioId;


    public String getScenarioId() {
        if(callOptions.containsKey("scenarioId")) {
            return scenarioId = callOptions.get("scenarioId").toString();
        }
       return TestContext.getScenario().getId();
    }

    public void setScenarioId(String scenarioId) {
        this.scenarioId = scenarioId;
    }

    public void setCallOptions(HashMap<String,Object> callOptions) {
        this.callOptions = callOptions;
    }

    public HashMap<String, Object> getCallOptions() {
        return callOptions;
    }

    public abstract <T> T getCookie();

    public void setArgs(Object[] args) {
        this.args = args;
    }

    public void setTypes(Class[] types) {
        this.types = types;
    }

    public Map<String, Object> getProperties() {
        return callOptions.get("props") != null ? (Map<String, Object>) callOptions.get("props") : new HashMap<>();
    }

    @Override
    public void setFullMethodName(String fullMethodName) {
        this.fullMethodName = fullMethodName;
    }

    @Override
    public String getFullMethodName() {
        return fullMethodName;
    }

    public abstract <T> T getClient();

    public abstract ChannelDescriptor getChannel();

    public abstract <T> T getHeaders();

    public Class getReturnType(){
        try {
            String s = callOptions.get("response").toString();
            if(Utils.isPrimitive(s)){
                return Utils.getPrimitiveClass(s);
            }
            Class c = Class.forName(s);
            return c;

        } catch (ClassNotFoundException e) {
            logger.error("fail get Return Type",e);
        }
        return null;
    }

    @Override
    public IStepDescriptor build() {
        return this;
    }

    public abstract <T> T getEntity();

    public MediaType getProduce() {
        if(callOptions.containsKey("produces") && callOptions.get("produces") != null && !callOptions.get("produces").toString().isEmpty()) {
            return produce = MediaType.valueOf(callOptions.get("produces").toString());
        }
        return produce = MediaType.APPLICATION_JSON_TYPE;
    }

    public MediaType getConsume() {
        if(callOptions.containsKey("consumes") && callOptions.get("consumes") != null && !callOptions.get("consumes").toString().isEmpty()) {
            return consume = MediaType.valueOf(callOptions.get("consumes").toString());
        }
        return consume = MediaType.APPLICATION_JSON_TYPE;
    }

    public void getMethod(){
        httpMethod = callOptions.get("method").toString();
    }

    public ServiceCaller.UnaryClass getMethodImpl(){
        ServiceCaller.UnaryClass method = null ;
        try {
            getMethod();
            if(httpMethod.equals("POST")){
                method = new RestService.POSTMethodHandler();
            }else
            if(httpMethod.equals("GET")){
                method = new RestService.GETMethodHandler();
            }else
            if(httpMethod.equals("PUT")){
                method = new RestService.PUTMethodHandler();
            }else
            if(httpMethod.equals("DELETE")){
                method = new RestService.DELETEMethodHandler();
            }else
            {
                logger.error(JDRY,"no method found ");
            }
        } catch (Exception e) {
            logger.error(JDRY,"fail getting method ",e);
        }
        return method;
    }

}
