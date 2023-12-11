package org.softauto.jaxrs;

import org.softauto.core.Configuration;
import org.softauto.core.TestContext;
import org.softauto.core.Utils;
import org.softauto.jaxrs.auth.EntityBuilder;
import org.softauto.jaxrs.service.ChannelDescriptor;
import org.softauto.jaxrs.service.IStepDescriptor;
import org.softauto.jaxrs.service.RestService;
import org.softauto.jaxrs.service.ServiceCaller;
import org.softauto.jaxrs.util.ChannelBuilder;

import javax.ws.rs.client.Entity;
import javax.ws.rs.core.MediaType;
import java.util.*;

public abstract class AbstractStepDescriptorImpl implements IStepDescriptor {

    private static final org.apache.logging.log4j.Logger logger = org.apache.logging.log4j.LogManager.getLogger(AbstractStepDescriptorImpl.class);

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
        if(callOptions.containsKey("scenarioId") && callOptions.get("scenarioId") != null ) {
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

    @Override
    public ChannelDescriptor getChannel() {
        String host = (Configuration.get("jaxrs").asMap().get("host").toString());
        String port = (Configuration.get("jaxrs").asMap().get("port").toString());
        String protocol = (Configuration.get("jaxrs").asMap().get("protocol").toString());
        String baseUrl = Configuration.get("jaxrs").asMap().containsKey("base_url") ? (Configuration.get("jaxrs").asMap().get("base_url").toString()):"";
        List<String> argumentsNames = (ArrayList)callOptions.get("argumentsNames");
        Map<String,Object> parameters = new HashMap();
        List<Object> newArgs = new ArrayList<>();
        String newPath = callOptions.get("path").toString();
        if(!newPath.contains(baseUrl)){
            if(newPath.startsWith("/")){
                newPath = baseUrl+newPath;
            }else {
                newPath = baseUrl+"/"+newPath;
            }
        }

        boolean first = true;
        List<Integer> argumentsRequestTypeArray = new ArrayList<>();
        if (this.callOptions.containsKey("argumentsRequestType")) {
            if(callOptions.get("argumentsRequestType") instanceof HashMap) {
                HashMap<String, Object> argumentsRequestTypes = (HashMap<String, Object>) callOptions.get("argumentsRequestType");
                for (Map.Entry entry : argumentsRequestTypes.entrySet()) {
                    if (entry.getKey().equals("QueryParam") || entry.getKey().equals("RequestParam") ) {
                        if (entry.getValue() instanceof ArrayList<?>) {
                            for (Object o : (ArrayList) entry.getValue()) {
                                argumentsRequestTypeArray.add(Integer.valueOf(((HashMap<String, Object>) o).get("index").toString()));
                            }
                        } else {
                            argumentsRequestTypeArray.add(Integer.valueOf(((HashMap<String, Object>) entry.getValue()).get("index").toString()));
                        }
                    }else if(entry.getKey().equals("PathParam")){
                        newArgs = Arrays.asList(args);
                    }
                }
            }
        }


        for(int i=0;i<args.length;i++) {
            if(argumentsRequestTypeArray.contains(i)){
                if (first) {
                    newPath = newPath + "?" + argumentsNames.get(i) + "=" + args[i];
                    first = false;
                } else {
                    newPath = newPath + "&" + argumentsNames.get(i) + "=" + args[i];
                }
            }
        }



        return ChannelBuilder.newBuilder().setHost(host)
                .setProtocol(protocol)
                .setArgs(newArgs.toArray())
                .setPath(newPath)
                .setPort(port)
                //.setBaseUrl(base_url)
                .build()
                .getChannelDescriptor();
    }

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

    @Override
    public Entity<?> getEntity() {
        List<String> argumentsNames = (ArrayList)callOptions.get("argumentsNames");
        return (Entity<?>) EntityBuilder.newBuilder().setCallOptions(callOptions).setProduce(consume).setArgs(args).setArgsNames(argumentsNames).build().getEntity();
    }

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
                logger.error("no method found ");
            }
        } catch (Exception e) {
            logger.error("fail getting method ",e);
        }
        return method;
    }

}
