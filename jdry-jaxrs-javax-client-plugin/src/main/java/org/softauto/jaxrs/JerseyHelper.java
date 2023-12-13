package org.softauto.jaxrs;


import org.apache.logging.log4j.Marker;
import org.apache.logging.log4j.MarkerManager;
import org.softauto.jaxrs.util.Threadlocal;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.Cookie;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.core.Response;

import java.util.HashMap;
import java.util.Map;


/**
 * Jersey helper
 */
public class JerseyHelper {

    protected Client client = null;

    private static final org.apache.logging.log4j.Logger logger = org.apache.logging.log4j.LogManager.getLogger(JerseyHelper.class);

    private static final Marker JDRY = MarkerManager.getMarker("JDRY");

    public JerseyHelper setClient(Client client) {
        this.client = client;
        return this;
    }

    public <T> Response get(String url, String mediaType, MultivaluedMap<String, Object> headers, Class<?> response, Cookie cookie,String scenarioId)throws Exception{
        Response res = null;
        try{
            Threadlocal.getInstance().add("scenarioId",scenarioId);
            WebTarget webTarget = this.client.target(url);
            res = webTarget.request(mediaType).headers(headers).cookie(cookie).get();
        }catch(Exception e){
           logger.error(JDRY,"Get request fail for url "+ url + " status "+ res.getStatusInfo(),e);
           throw new Exception(res.getStatusInfo().toString()) ;
        }
        return res;
    }

    public <T> Response put(String url, String mediaType,   MultivaluedMap<String, Object> headers,Class<T> response,Entity<?> entity,Cookie cookie,String scenarioId)throws Exception{
        Response res = null;
        try{
            Threadlocal.getInstance().add("scenarioId",scenarioId);
            WebTarget webTarget = client.target(url);
            res = webTarget.request(mediaType).headers(headers).cookie(cookie).put(entity);

        }catch(Exception e){
            logger.error(JDRY,"put request fail for url "+ url + " status "+ res.getStatusInfo(),e);
            throw new Exception(res.getStatusInfo().toString()) ;
        }
        return res;
    }


    public <T> Response post(String url, String mediaType, MultivaluedMap<String, Object> headers, Class<?> response, Entity<?> entity,Cookie cookie,String scenarioId)throws Exception{
        Response res = null;
        try{
            Threadlocal.getInstance().add("scenarioId",scenarioId);
            WebTarget webTarget = client.target(url);

            res = webTarget.request(mediaType).headers(headers).cookie(cookie).post(entity);

        }catch(Exception e){
            logger.error(JDRY,"post request fail for url "+ url + " status "+ res.getStatusInfo(),e);
            throw new Exception(res.getStatusInfo().toString()) ;
        }
        return res;
    }

    public <T> Response delete(String url, String mediaType,   MultivaluedMap<String, Object> headers,Class<T> response,Cookie cookie,String scenarioId)throws Exception{
        Response res = null;
        try{
            Threadlocal.getInstance().add("scenarioId",scenarioId);
            WebTarget webTarget = client.target(url);
            res = webTarget.request(mediaType).headers(headers).cookie(cookie).delete();

        }catch(Exception e){
            logger.error(JDRY,"delete request fail for url "+ url + " status "+ res.getStatusInfo(),e);
            throw new Exception(res.getStatusInfo().toString()) ;
        }
        return res;
    }
}
