package org.softauto.jaxrs;

import org.softauto.core.TestContext;
import org.softauto.core.Utils;
import org.softauto.jaxrs.service.ChannelDescriptor;
import org.softauto.jaxrs.service.IStepDescriptor;
import org.softauto.jaxrs.service.RestService;
import org.softauto.jaxrs.service.ServiceCaller;
import jakarta.ws.rs.client.Client;
import jakarta.ws.rs.client.Entity;
import jakarta.ws.rs.core.Cookie;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.MultivaluedHashMap;
import jakarta.ws.rs.core.MultivaluedMap;
import java.util.HashMap;
import java.util.Map;

public abstract class AbstractStepDescriptorImpl implements IStepDescriptor {

    protected HashMap<String,Object> callOptions;

    protected Object[] args;

    protected Class[] types;

    protected MediaType produce;

    protected MediaType consume;

    protected String fullMethodName;

    protected String httpMethod;

    protected String scenarioId;

    public String getScenarioId() {
        if(callOptions.containsKey("scenarioId")) {
            return scenarioId = callOptions.get("scenarioId").toString();
        }
       return TestContext.getScenarioKey();
    }

    public void setCallOptions(HashMap<String,Object> callOptions) {
        this.callOptions = callOptions;
    }

    public HashMap<String, Object> getCallOptions() {
        return callOptions;
    }

    public abstract Cookie getCookie();

    public void setArgs(Object[] args) {
        this.args = args;

        TestContext.getScenario(callOptions.containsKey("scenarioId") ? callOptions.get("scenarioId").toString() : null).addProperty("args", args);
        //Threadlocal.getInstance().add("args", args);
    }

    public void setTypes(Class[] types) {
        this.types = types;
    }

    public void saveAuth(jakarta.ws.rs.core.Response res){
        TestContext.put("sessionId", res.readEntity(String.class));
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

    public abstract Client getClient();



    /*
    public ChannelDescriptor getChannel() {
        String host = (Configuration.get("jaxrs").asMap().get("host").toString());
        String port = (Configuration.get("jaxrs").asMap().get("port").toString());
        String protocol = (Configuration.get("jaxrs").asMap().get("protocol").toString());
        //String base_url = ( Configuration.get("jaxrs").asMap().get("base_url").toString());
        return ChannelBuilder.newBuilder().setHost(host)
                .setProtocol(protocol)
                .setArgs(args)
                .setPath(callOptions.get("path").toString())
                .setPort(port)
                //.setBaseUrl(base_url)
                .build()
                .getChannelDescriptor();
    }

     */

    public abstract ChannelDescriptor getChannel();

    public abstract MultivaluedMap<String, Object> getHeaders();



    public Class getReturnType(){
        try {
            String s = callOptions.get("response").toString();
            if(Utils.isPrimitive(s)){
                return Utils.getPrimitiveClass(s);
            }
            Class c = Class.forName(s);
            return c;

        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public IStepDescriptor build() {
        return this;
    }

    public abstract Entity<?> getEntity();

    /*
    public Entity<?> getEntity() {
        List<String> argumentsNames = (ArrayList)callOptions.get("argumentsNames");
        //Threadlocal.getInstance().add("argumentsNames", argumentsNames);
        return EntityBuilder.newBuilder().setCallOptions(callOptions).setProduce(consume).setArgs(args).setArgsNames(argumentsNames).build().getEntity();
    }

     */


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
        getMethod();
        if(httpMethod.equals("POST")){
            method = new RestService.POSTMethodHandler();
        }
        if(httpMethod.equals("GET")){
            method = new RestService.GETMethodHandler();
        }
        if(httpMethod.equals("PUT")){
            method = new RestService.PUTMethodHandler();
        }
        if(httpMethod.equals("DELETE")){
            method = new RestService.DELETEMethodHandler();
        }

        return method;
    }
}
