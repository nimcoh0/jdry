package org.softauto.jaxrs.security.auth.jwt;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import org.glassfish.jersey.media.multipart.FormDataMultiPart;
import org.glassfish.jersey.media.multipart.MultiPart;
import org.glassfish.jersey.media.multipart.MultiPartMediaTypes;
import org.softauto.core.Configuration;
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
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
        return ChannelBuilder.newBuilder().setHost(host)
                .setProtocol(protocol)
                .setArgs(args)
                .setPath(callOptions.get("path").toString())
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
                //UserCredentials credentials = buildCredentials(args[0].toString(), args[1].toString());
                //return Entity.entity(args[0], MediaType.APPLICATION_JSON);
                return args[0];
            }

            int index = 0;
            if(args.length > 1){
                List<String> argumentsNames = (ArrayList)callOptions.get("argumentsNames");

                MultiValueMap<String, Object> body  = new LinkedMultiValueMap<>();
                for(int i=0;i<args.length;i++) {
                    body.add(argumentsNames.get(i), args[i]);
                }
                MultiValueMap<String, String> headers =  new LinkedMultiValueMap<>();
                HttpEntity<MultiValueMap<String, String>> requestEntity = new HttpEntity(body, headers);
                return requestEntity;

                /*
                FormDataMultiPart multipart = new FormDataMultiPart();
                for(int i=0;i<args.length;i++){
                   multipart.bodyPart(args[i],MediaType.TEXT_PLAIN_TYPE);
                }
                return Entity.entity(multipart,multipart.getMediaType());

                 */
                //return EntityBuilder.newBuilder().setProduce(consume).setArgs(args).setArgsNames(argumentsNames).build().getEntity();
                /*
                Map<?,?> m = (Map<?, ?>) callOptions.get("argumentRoles");
                for(Map.Entry entry : m.entrySet() ){
                    if(entry.getKey().equals("role") && entry.getValue().equals("ENTITY")){
                        index = Integer.valueOf(m.get("index").toString());
                    }
                }

                 */
            }
            return args[index];


        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public MediaType getProduce() {
        return produce = MediaType.valueOf(callOptions.get("produces").toString());
    }

    @Override
    public MediaType getConsume() {
        return consume = MediaType.valueOf(callOptions.get("consumes").toString());
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
