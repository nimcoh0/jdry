package org.softauto.jaxrs.filter;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientRequestContext;
import javax.ws.rs.client.ClientRequestFilter;
import javax.ws.rs.client.Entity;
import javax.ws.rs.core.Cookie;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.Provider;
import org.softauto.core.Configuration;
import org.softauto.core.TestContext;
import org.softauto.jaxrs.JerseyHelper;
import org.softauto.jaxrs.auth.AuthFactory;
import org.softauto.jaxrs.service.IStepDescriptor;

import java.io.IOException;


@Provider
public class RequestClientFilter implements ClientRequestFilter {

    @Override
    public void filter(ClientRequestContext requestContext) throws IOException {
        Response res = null;
        Object result= null;
        int status = -1;
        try {
            Cookie cookie = requestContext.getCookies().values().size() > 0 ? requestContext.getCookies().values().toArray(new Cookie[requestContext.getCookies().values().size()])[0] : null;
            String scenarioId = TestContext.getScenario().getId();
            String auto = Configuration.get("jaxrs").asMap().get("auth").toString();
            IStepDescriptor stepDescriptor = AuthFactory.getStepDescriptor(auto);
            Client client = stepDescriptor.getClient();
            MultivaluedMap<String, Object> headers = stepDescriptor.getHeaders();
            headers.putAll(requestContext.getHeaders());
            headers.add("scenarioId",scenarioId);
            String mediaType ;
            if(requestContext.getMediaType() != null){
                mediaType = requestContext.getMediaType().toString();
            }else {
                mediaType = MediaType.APPLICATION_JSON;
            }
            Entity<?> entity = Entity.entity(requestContext.getEntity(), mediaType);
            if(requestContext.getMethod().equals("POST")) {
                res = new JerseyHelper().setClient(client).post(requestContext.getUri().toString(), mediaType, headers, Object.class, entity, cookie, scenarioId);
                if(res.hasEntity()){
                    status = res.getStatus();
                    result =    res.readEntity(Object.class);
                }

            }
            if(requestContext.getMethod().equals("GET")) {
                res = new JerseyHelper().setClient(client).get(requestContext.getUri().toString(), mediaType, headers, Object.class, cookie, scenarioId);
                if(res.hasEntity()){
                    status = res.getStatus();
                    result =    res.getEntity();
                }
            }
            if(requestContext.getMethod().equals("PUT")) {
                res = new JerseyHelper().setClient(client).put(requestContext.getUri().toString(), mediaType, headers, Object.class, entity, cookie, scenarioId);
                if(res.hasEntity()){
                    status = res.getStatus();
                    result =    res.getEntity();
                }
            }
            if(requestContext.getMethod().equals("DELETE")) {
                res = new JerseyHelper().setClient(client).delete(requestContext.getUri().toString(), mediaType, headers, Object.class, cookie, scenarioId);
                if(res.hasEntity()){
                    status = res.getStatus();
                    result =    res.getEntity();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        //Response r = new Response.ResponseBuilder().header().header("Content-Type","application/json");
        //requestContext.abortWith(Response.ok(Entity.entity(result, MediaType.APPLICATION_JSON),MediaType.APPLICATION_JSON).build());
        requestContext.abortWith(Response.ok(result,MediaType.APPLICATION_JSON).build());
    }
}
