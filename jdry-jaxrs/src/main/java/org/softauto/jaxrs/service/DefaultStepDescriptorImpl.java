package org.softauto.jaxrs.service;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.base.Splitter;
import org.softauto.core.Configuration;
import org.softauto.core.Context;
import org.softauto.core.TestContext;
import org.softauto.core.Utils;
import org.softauto.jaxrs.annotations.AuthenticationType;
import org.softauto.jaxrs.util.ChannelBuilder;
import org.softauto.jaxrs.util.ClientBuilder;
import org.softauto.jaxrs.util.EntityBuilder;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.Entity;
import javax.ws.rs.core.Cookie;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.MultivaluedHashMap;
import javax.ws.rs.core.MultivaluedMap;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.*;


public class DefaultStepDescriptorImpl implements IStepDescriptor{

    HashMap<String,Object> configuration;

    JsonNode request ;

    Object[] args;

    //Item item;

    ServiceCaller.UnaryClass method;

    Map<String, Object> props;

    Map<String, Object> annotations;

    Map<String, Object> classAnnotations;

    Map<String, Object>  additionalInfo;

    String httpMethod;

    MediaType produce;

    //Test test;

    String schema;

    String username;

    String password;

    String email;

    String path;

    String fullMethodName;

    URI uri ;



    HashMap<String,Object> callOptions;

    MultivaluedMap<String, Object> headers = new MultivaluedHashMap<>();

    Client client;

    private String hashedPassword;

    private String salt;

    public Cookie getCookie(){
        return (Cookie) TestContext.get("sessionId");
    }

    public void setFullMethodName(String fullMethodName) {
        this.fullMethodName = fullMethodName;
    }


    @Override
    public void setCallOptions(HashMap<String,Object> callOptions) {
        this.callOptions = (HashMap<String, Object>) callOptions.get("callOption");
    }




    @Override
    public ChannelDescriptor getChannel() {
        String host = (((HashMap<String,Object>) Configuration.get("jaxrs")).get("host").toString());
        String port = (((HashMap<String,Object>) Configuration.get("jaxrs")).get("port").toString());
        String protocol = (((HashMap<String,Object>) Configuration.get("jaxrs")).get("protocol").toString());

        return  ChannelBuilder.newBuilder().setHost(host)
                .setProtocol(protocol)
                .setArgs(args)
                .setPath(path)
                .setPort(port)
                //.setBaseUrl(base_url)
                .build()
                .getChannelDescriptor();
    }

    @Override
    public Client getClient() {
        String auth = Configuration.get(org.softauto.jaxrs.configuration.Context.AUTH);
        if(auth != null && auth.equals(AuthenticationType.BASIC.getValue()) && TestContext.get("sessionId") == null && username != null ) {
            client =  ClientBuilder.newBuilder().setPassword(password)
                    .setUsername(username)
                    .build()
                    .getClient();
        }else {
            client = ClientBuilder.newBuilder().build().getClient();
        }
        Map<String, Object> map = getProperties();
        if(map != null && map.size() > 0) {
            for (Map.Entry entry : map.entrySet()) {
                client.property(entry.getKey().toString(), entry.getValue());
            }
        }
        return client;
    }

    @Override
    public MultivaluedMap<String, Object> getHeaders() {
       return (MultivaluedMap<String, Object>) callOptions.get("headers");
    }

    private List<String> getArgsNames() {
        List<String> argsNames = new ArrayList<>();
        for(int i=0;i<args.length;i++){
            argsNames.add("arg"+i);
        }
        return argsNames;
    }

    @Override
    public Entity<?> getEntity() {
        //if(request.has("entity") && !request.get("entity").asText().isEmpty()){
          //  return Entity.entity(request.get("entity").asText(), request.get("MediaType").get("type").asText() +"/" + request.get("MediaType").get("subtype").asText());

       // }
        return EntityBuilder.newBuilder().setProduce(produce).setArgs(args).setArgsNames(getArgsNames()).build().getEntity();
    }

    @Override
    public MediaType getProduce() {
        return produce = MediaType.valueOf(callOptions.get("mediaType").toString());
    }

    @Override
    public MediaType getConsume() {
        return null;
    }

    @Override
    public Map<String, Object> getProperties() {
        return (Map<String, Object>) callOptions.get("props");
    }

    @Override
    public String getFullMethodName() {
        return fullMethodName;
    }




    @Override
    public boolean isSession() {
        return Boolean.valueOf (((HashMap<String,Object>) Configuration.get("jaxrs")).get("session").toString());
    }



    @Override
    public IStepDescriptor build() {
        //props = (Map<String, Object>)item.getObjectProps();
        //annotations = props.get("annotations") != null ? (Map<String, Object>)props.get("annotations") : null;
        //classAnnotations = props.get("classAnnotations") != null ? (Map<String, Object>)props.get("classAnnotations") : null;
        gethttpMethod();
        parsePath();
        getProduce();
        return this;
    }

    @Override
    public void setConfiguration(HashMap<String, Object> configuration) {
        this.configuration = configuration;
    }

    @Override
    public void setArgs(Object[] args) {
        this.args = args;
    }

    @Override
    public void setMethod(ServiceCaller.UnaryClass method) {
        this.method = method;
    }

    @Override
    public ServiceCaller.UnaryClass getMethod() {
        return buildMethod();
    }




    private void parseEntity(String entity){
        Map<String, String> result = null;
        if(produce.getSubtype().equals("x-www-form-urlencoded")){
            result = Splitter.on('&')
                    .trimResults()
                    .withKeyValueSeparator(
                            Splitter.on('=')
                                    .limit(2)
                                    .trimResults())
                    .split(entity);
        }
        if(produce.getSubtype().equals("json")){
            try {
                JsonNode node = new ObjectMapper().readTree(entity);
                result = new ObjectMapper().convertValue(node, new TypeReference<Map<String, String>>() {
                });
            }catch (Exception e){
                e.printStackTrace();
            }
        }
        if(result != null){
            password = result.get(Configuration.get(Context.PASSWORD_FIELD));
            username = result.get(Configuration.get(Context.USERNAME_FIELD));
            email = result.get(Configuration.get(Context.EMAIL_FIELD));
        }
    }






    private void parsePath(){
        try {
            path = new URL(callOptions.get("requestUri").toString()).toURI().getPath();
        } catch (URISyntaxException e) {
            e.printStackTrace();
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }

    }

    private void gethttpMethod(){
        httpMethod = callOptions.get("method").toString();
    }

    private ServiceCaller.UnaryClass buildMethod(){
        ServiceCaller.UnaryClass method = null ;
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
