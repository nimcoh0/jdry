package org.softauto.jaxrs;

import org.softauto.core.TestContext;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.Cookie;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.core.Response;


/**
 * Jersey helper
 */
public class JerseyHelper {

    protected Client client = null;

    private static final org.apache.logging.log4j.Logger logger = org.apache.logging.log4j.LogManager.getLogger(JerseyHelper.class);

    public JerseyHelper(Client client){
        this.client = client;

    }



    public <T> T get(String url, String mediaType, MultivaluedMap<String, Object> headers, Class<T> response,Cookie cookie)throws Exception{
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


    public <T> T post(String url, String mediaType, MultivaluedMap<String, Object> headers, Class<T> response, Entity<?> entity,Cookie cookie)throws Exception{
        T t = null;
        Response res = null;
        try{
            WebTarget webTarget = client.target(url);
            res = webTarget.request(mediaType).headers(headers).cookie(cookie).post(entity);
            if (Response.Status.fromStatusCode(res.getStatus()).getFamily() == Response.Status.Family.SUCCESSFUL) {
                logger.debug("post request successfully for url "+ url + " status "+ res.getStatusInfo());
                if(res.getCookies().get("JSESSIONID") != null) {
                    TestContext.put("sessionId", res.getCookies().get("JSESSIONID"));//((Cookie)res.getHeaders().get("Cookie").get(0)).getValue())
                }
                if(res.hasEntity()) {
                    if(response.getTypeName().equals(Response.class.getTypeName()) ){
                        t = (T) res;
                    }else {
                        t = (T) res.readEntity(response);
                    }
                }else {
                    t = (T)res;
                }
            }else {
                t = (T)res;
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
}
