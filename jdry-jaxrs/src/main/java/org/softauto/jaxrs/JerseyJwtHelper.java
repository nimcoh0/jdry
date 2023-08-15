package org.softauto.jaxrs;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.softauto.core.TestContext;
import org.softauto.jaxrs.util.Threadlocal;

import jakarta.ws.rs.client.Client;
import jakarta.ws.rs.client.Entity;
import jakarta.ws.rs.client.WebTarget;
import jakarta.ws.rs.core.Cookie;
import jakarta.ws.rs.core.MultivaluedMap;
import jakarta.ws.rs.core.Response;


/**
 * Jersey helper
 */
public class JerseyJwtHelper  {

    protected Client client = null;

    private static final org.apache.logging.log4j.Logger logger = org.apache.logging.log4j.LogManager.getLogger(JerseyJwtHelper.class);
    private String token;
    private static String refreshToken;
    private static final String JWT_TOKEN_HEADER_PARAM_X = "X-Authorization";
    private static final String JWT_TOKEN_HEADER_PARAM = "Authorization";






    public JerseyJwtHelper setClient(Client client) {
        this.client = client;
        return this;
    }

    public <T> T get(String url, String mediaType, MultivaluedMap<String, Object> headers, Class<T> response, Cookie cookie)throws Exception{
        T t = null;
        Response res = null;
        try{
            res = client.target(url).request(mediaType).headers(headers).cookie(cookie).get();
            if (Response.Status.fromStatusCode(res.getStatus()).getFamily() == Response.Status.Family.SUCCESSFUL) {
                logger.debug("get request successfully for url "+ url + " status "+ res.getStatusInfo());
                TestContext.put("sessionId", res.getCookies().get("JSESSIONID"));
                if(res.hasEntity()) {
                    if(response.getTypeName().equals(Response.class.getTypeName()) ){
                        t = (T) res;
                    }else {
                        t = (T) res.readEntity(response);
                    }
                }else {
                    t = (T) res;
                }
            }else {
                t = (T) res;
            }
        }catch(Exception e){
           logger.error("Get request fail for url "+ url + " status "+ res.getStatusInfo(),e);
           throw new Exception(res.getStatusInfo().toString()) ;
        }
        return t;
    }

    public <T> T put(String url, String mediaType,   MultivaluedMap<String, Object> headers,Class<T> response,Entity<?> entity,Cookie cookie)throws Exception{
        T t = null;
        Response res = null;
        try{
            WebTarget webTarget = client.target(url);
            res = webTarget.request(mediaType).headers(headers).cookie(cookie).put(entity);
            if (Response.Status.fromStatusCode(res.getStatus()).getFamily() == Response.Status.Family.SUCCESSFUL) {
                logger.debug("put request successfully for url "+ url + " status "+ res.getStatusInfo());
                TestContext.put("sessionId", res.getCookies().get("JSESSIONID"));
                if(res.hasEntity()) {
                    if(response.getTypeName().equals(Response.class.getTypeName()) ){
                        t = (T) res;
                    }else {
                        t = (T) res.readEntity(response);
                    }
                }else {
                    t = (T) res;
                }
            }else {
                t = (T) res;
            }
        }catch(Exception e){
            logger.error("put request fail for url "+ url + " status "+ res.getStatusInfo(),e);
            throw new Exception(res.getStatusInfo().toString()) ;
        }
        return t;
    }

                         //String url, String mediaType, MultivaluedMap<String, Object> headers, Class<T> response, Entity<?> entity,Cookie cookie
    public <T> T post(String url, String mediaType, MultivaluedMap<String, Object> headers, Class<T> response, Entity<?> entity,Cookie cookie)throws Exception{
        T t = null;
        Response res = null;
        try{
            Thread.currentThread().getId();
            //WebTarget webTarget = client.target(url);

            if(Threadlocal.getInstance().get("token") == null) {
              //  ClientConfig clientConfig = new ClientConfig();
               // clientConfig.register(ClientHttpRequestInterceptor.class);
               // client = javax.ws.rs.client.ClientBuilder.newBuilder().withConfig(clientConfig).build();

                //Threadlocal.getInstance().add("client",client);
                WebTarget webTarget = client.target(url);
                res = webTarget.request(mediaType).headers(headers).cookie(cookie).post(entity);
                    if (Response.Status.fromStatusCode(res.getStatus()).getFamily() == Response.Status.Family.SUCCESSFUL) {
                        logger.debug("post request successfully for url " + url + " status " + res.getStatusInfo());


                        if (res.hasEntity()) {
                                ///Object tokenInfo =  null;
                               // if(Configuration.has("jaxrs") ) {
                                   // HashMap<String,Object> conf = Configuration.get("jaxrs").asMap();
                                   //if (conf.containsKey("unbox_response") && conf.containsKey("response_type")) {
                                    //    expression = conf.get("unbox_response").toString();
                                    //    String type = conf.get("response_type").toString();
                                     //   clazz = Utils.typeToClass(type);
                                     //   tokenInfo = Espl.getInstance().addProperty("response", res.readEntity(Object.class)).evaluate(expression, clazz);
                                    //}
                                //}
                                setTokenInfo(res.readEntity(Object.class));
                        }
                    }
                }else {
                //Client client = (Client) Threadlocal.getInstance().get("client");
                String token = (String) Threadlocal.getInstance().get("token");
                WebTarget webTarget = client.target(url);
                res = webTarget.request(mediaType).headers(headers).post(entity);
                if (Response.Status.fromStatusCode(res.getStatus()).getFamily() == Response.Status.Family.SUCCESSFUL) {
                    logger.debug("post request successfully for url " + url + " status " + res.getStatusInfo());
                    if (res.hasEntity()) {
                        //t = (T) Espl.getInstance().addProperty("response", res.readEntity(Object.class)).evaluate(expression, clazz);
                        t = (T) res.readEntity(response);
                    }
                } else {
                    t = (T) res;
                }
            }

        }catch(Exception e){
            logger.error("post request fail for url "+ url + " status "+ res.getStatusInfo(),e);
            throw new Exception(res.getStatusInfo().toString()) ;
        }
        return t;
    }

    public <T> T delete(String url, String mediaType,   MultivaluedMap<String, Object> headers,Class<T> response,Cookie cookie)throws Exception{
        T t = null;
        Response res = null;
        try{
            WebTarget webTarget = client.target(url);
            res = webTarget.request(mediaType).headers(headers).cookie(cookie).delete();
            if (Response.Status.fromStatusCode(res.getStatus()).getFamily() == Response.Status.Family.SUCCESSFUL) {
                logger.debug("delete request successfully for url "+ url + " status "+ res.getStatusInfo());
                TestContext.put("sessionId", res.getCookies().get("JSESSIONID"));
                if(res.hasEntity()) {
                    if(response.getTypeName().equals(Response.class.getTypeName()) ){
                        t = (T) res;
                    }else {
                        t = (T) res.readEntity(response);
                    }
                }else {
                    t = (T) Integer.valueOf(res.getStatus());
                }
            }else {
                t = (T) Integer.valueOf(res.getStatus());
            }
        }catch(Exception e){
            logger.error("delete request fail for url "+ url + " status "+ res.getStatusInfo(),e);
            throw new Exception(res.getStatusInfo().toString()) ;
        }
        return t;
    }


    public void setTokenInfo(Object tokenInfo) {
        try {
            String str = new ObjectMapper().writeValueAsString(tokenInfo);
            JsonNode node =   new ObjectMapper().readTree(str);
            if(str.contains("token")){
                this.token = node.findValue("token").asText();
                Threadlocal.getInstance().add("token",token);
            }
            if(str.contains("refreshToken")){
                this.refreshToken = node.findValue("refreshToken").asText();
            }

        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
    }


}
