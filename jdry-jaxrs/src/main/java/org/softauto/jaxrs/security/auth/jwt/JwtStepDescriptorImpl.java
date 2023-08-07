package org.softauto.jaxrs.security.auth.jwt;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import org.glassfish.jersey.media.multipart.FormDataMultiPart;
import org.glassfish.jersey.media.multipart.MultiPart;
import org.glassfish.jersey.media.multipart.MultiPartMediaTypes;
import org.glassfish.jersey.server.ContainerRequest;
import org.softauto.core.Configuration;
import org.softauto.core.Utils;
import org.softauto.jaxrs.security.auth.jwt.model.UserCredentials;
import org.softauto.jaxrs.service.IStepDescriptor;
import org.softauto.core.TestContext;
import org.softauto.jaxrs.service.*;
import org.softauto.jaxrs.util.ChannelBuilder;
import org.softauto.jaxrs.util.EntityBuilder;
import org.springframework.http.HttpEntity;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.core.*;
import java.util.*;

public class JwtStepDescriptorImpl implements IStepDescriptor {

    private static final String JWT_TOKEN_HEADER_PARAM = "X-Authorization";

    //protected URI uri;

    //protected Client client;

    UserCredentials credentials;

    HashMap<String,Object> callOptions;

    Object[] args;

    Class[] types;

    MediaType produce;

    MediaType consume;

    String fullMethodName;

    String httpMethod;

    private String token;

    private String refreshToken;

    String authorizationHeader;


    public UserCredentials buildCredentials(String username, String password){
        UserCredentials credentials = new UserCredentials();
        credentials.setUsername(username);
        credentials.setPassword(password);
        return this.credentials = credentials;
    }



    @Override
    public ChannelDescriptor getChannel() {
        String host = (Configuration.get("jaxrs").asMap().get("host").toString());
        String port = (Configuration.get("jaxrs").asMap().get("port").toString());
        String protocol = (Configuration.get("jaxrs").asMap().get("protocol").toString());
        //String base_url = ( Configuration.get("jaxrs").asMap().get("base_url").toString());

        List<String> argumentsNames = (ArrayList)callOptions.get("argumentsNames");
        Map<String,Object> parameters = new HashMap();
        List<Object> newArgs = new ArrayList<>();
        String newPath = callOptions.get("path").toString();
        boolean first = true;
        List<Integer> argumentsRequestTypeArray = new ArrayList<>();
        if (this.callOptions.containsKey("argumentsRequestType")) {
            HashMap<String, Object> argumentsRequestTypes = (HashMap<String, Object>) callOptions.get("argumentsRequestType");
            for(Map.Entry entry : argumentsRequestTypes.entrySet()){
                if(entry.getKey().equals("RequestParam")){
                    if(entry.getValue() instanceof ArrayList<?>) {
                        for(Object o : (ArrayList)entry.getValue()){
                            argumentsRequestTypeArray.add(Integer.valueOf(((HashMap<String,Object>)o).get("index").toString()));
                        }
                    }else {
                        argumentsRequestTypeArray.add(Integer.valueOf(((HashMap<String,Object>)entry.getValue()).get("index").toString()));
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
            /*
                if (o.getKey().equals("PathVariable")) {
                    newArgs.add(args[i]);
                    newPath = newPath+"/{"+argumentsNames.get(i)+"}";
                    //newArgs.add(((Map)((ArrayList)o.getValue()).get(0)).get("index"));

                }


             */






        return ChannelBuilder.newBuilder().setHost(host)
                .setProtocol(protocol)
                .setArgs(newArgs.toArray())
                .setPath(newPath)
                .setPort(port)
                //.setBaseUrl(base_url)
                .build()
                .getChannelDescriptor();
    }

    @Override
    public Client getClient() {
        //HttpAuthenticationFeature feature = HttpAuthenticationFeature.basic("sysadmin@thingsboard.org", "sysadmin");
        //return javax.ws.rs.client.ClientBuilder.newBuilder().register(feature).build();
        return ClientBuilder.newClient();

    }

    @Override
    public MultivaluedMap<String, Object> getHeaders() {
        String authorizationHeader = composeAuthorizationHeader();
        MultivaluedMap<String, Object> map = new MultivaluedHashMap<>();
        if(callOptions.get("headers") != null){
          map =   (MultivaluedMap<String, Object>) callOptions.get("headers");
        }
        if(authorizationHeader != null ){
            map.add(JWT_TOKEN_HEADER_PARAM, authorizationHeader);
        }
        return map;
        //return callOptions.get("headers") != null ? (MultivaluedMap<String, Object>) callOptions.get("headers") : new MultivaluedHashMap<>();
    }

    public Entity getEntity(){
        try {
            if (this.callOptions.get("role") != null && this.callOptions.get("role").toString().equals("AUTH")) {
                UserCredentials credentials = buildCredentials(args[0].toString(), args[1].toString());
                return Entity.entity(credentials, MediaType.APPLICATION_JSON);
            }
            int index = 0;
            if(args.length > 1){
              Map<?,?> m = (Map<?, ?>) callOptions.get("argumentRoles");
              for(Map.Entry entry : m.entrySet() ){
                  if(entry.getKey().equals("role") && entry.getValue().equals("ENTITY")){
                      index = Integer.valueOf(m.get("index").toString());
                  }
              }
            }
            return Entity.entity(args[index], callOptions.get("produces").toString());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public Object getEntity1(){
        try {
            if (this.callOptions.get("role") != null && this.callOptions.get("role").toString().equals("AUTH")) {
              return args[0];
            }
            for(int i=0;i<args.length;i++) {
                if (this.callOptions.containsKey("argumentsRequestType")) {
                    HashMap<String, Object> argumentsRequestTypes = (HashMap<String, Object>) callOptions.get("argumentsRequestType");
                    Map.Entry o = (Map.Entry) argumentsRequestTypes.entrySet().toArray()[i];
                    if (o.getKey().equals("RequestBody")) {

                        org.springframework.http.HttpHeaders headers = new org.springframework.http.HttpHeaders();
                        headers.setContentType(org.springframework.http.MediaType.APPLICATION_JSON);
                        HttpEntity<MultiValueMap<String, Object>> requestEntity = new HttpEntity(args[i], headers);
                        return requestEntity;
                        //return args[i];
                    }
                 }
            }


/*
                List<String> argumentsNames = (ArrayList)callOptions.get("argumentsNames");
                Map<String,Object> parameters = new HashMap();
                for(int i=0;i<args.length;i++){
                    Object param = null;
                    if(Utils.isPrimitive(args[i].getClass().getTypeName())){
                        param = args[i];
                    }else {
                        param = new ObjectMapper().writeValueAsString(args[i]);
                    }
                    parameters.put(argumentsNames.get(i),param);
                }
                //MultiValueMap<String, Object> headers =  new LinkedMultiValueMap<>();
                //headers.add().setContentType(MediaType.APPLICATION_JSON);
                //String json = new ObjectMapper().writeValueAsString(parameters);
                //HttpEntity<String> entity = new HttpEntity(json, headers);
                return parameters;


 */


                /*
                List<String> argumentsNames = (ArrayList)callOptions.get("argumentsNames");

                MultiValueMap<String, Object> body  = new LinkedMultiValueMap<>();
                for(int i=0;i<args.length;i++) {
                    body.add(argumentsNames.get(i), args[i]);
                }
                MultiValueMap<String, Object> headers =  new LinkedMultiValueMap<>();
                HttpEntity<MultiValueMap<String, Object>> requestEntity = new HttpEntity(body, headers);
                return requestEntity;


                 */





/*
                Entity<?>  entity = null;
                List<String> argumentsNames = (ArrayList)callOptions.get("argumentsNames");
                if (produce != null &&  produce.toString().equals(MediaType.APPLICATION_JSON)) {
                    if(args.length == 1){
                       entity = Entity.entity(args[0],MediaType.APPLICATION_JSON);
                    }else if(args.length > 1){
                        FormDataMultiPart multipart = new FormDataMultiPart();
                        for(int i=0;i<args.length;i++){
                            multipart.field(argumentsNames.get(i),args[i],MediaType.APPLICATION_JSON_TYPE);
                        }
                        entity = Entity.entity(multipart,MediaType.APPLICATION_JSON_TYPE);
                    }
                return entity;
                }


 */

           // }


            //return args[index];


        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
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
        return produce = MediaType.APPLICATION_JSON_TYPE;
    }




    @Override
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

    protected String composeAuthorizationHeader() {
        if(TestContext.get("token") != null)
            return "Bearer" + " " + TestContext.get("token").toString();
        return null;
    }

    public void saveAuth(javax.ws.rs.core.Response res){
        try {
            String entity = res.readEntity(String.class);
            JsonNode node = new ObjectMapper().readTree(entity);
            token = node.get("token").asText();
            refreshToken = node.get("refreshToken").asText();
            TestContext.put("token", token);
            TestContext.put("refreshToken", refreshToken);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
    }

    @Override
    public Class getReturnType() {
        try {
           // if(callOptions.get("role") != null && callOptions.get("role").toString().equals("AUTH")){
            //    return String.class;
           // }

            String s = callOptions.get("response").toString();
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

    @Override
    public HashMap<String, Object> getCallOptions() {
        return callOptions;
    }

    @Override
    public void setArgs(Object[] args) {
        this.args = args;
    }

    @Override
    public void setTypes(Class[] types) {
        this.types = types;
    }



    @Override
    public Cookie getCookie() {
        return (Cookie) TestContext.get("sessionId");
    }

    @Override
    public void setCallOptions(HashMap<String, Object> callOptions) {
        this.callOptions = callOptions;
    }

    public void getMethod(){
        httpMethod = callOptions.get("method").toString();
    }

    @Override
    public ServiceCaller.UnaryClass getMethodImpl(){
        ServiceCaller.UnaryClass method = null ;
        getMethod();
        if(httpMethod.equals("POST")){
            method = new SpringRestService.POSTMethodHandler();
        }
        if(httpMethod.equals("GET")){
            method = new SpringRestService.GETMethodHandler();
        }
        if(httpMethod.equals("PUT")){
            method = new SpringRestService.PUTMethodHandler();
        }
        if(httpMethod.equals("DELETE")){
            method = new SpringRestService.DELETEMethodHandler();
        }

        return method;
    }
}
