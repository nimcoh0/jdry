package org.softauto.jaxrs.filter;

import jakarta.ws.rs.client.Client;
import jakarta.ws.rs.client.ClientRequestContext;
import jakarta.ws.rs.client.ClientRequestFilter;
import jakarta.ws.rs.client.Entity;
import jakarta.ws.rs.core.Cookie;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.ext.Provider;
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
            String mediaType ;
            if(requestContext.getMediaType() != null){
                mediaType = requestContext.getMediaType().toString();
            }else {
                mediaType = MediaType.APPLICATION_JSON;
            }
            Entity<?> entity = Entity.entity(requestContext.getEntity(), mediaType);
            if(requestContext.getMethod().equals("POST")) {
                res = new JerseyHelper().setClient(client).post(requestContext.getUri().toString(), mediaType, requestContext.getHeaders(), Object.class, entity, cookie, scenarioId);
                if(res.hasEntity()){
                    status = res.getStatus();
                    result =    res.readEntity(Object.class);
                }

            }
            if(requestContext.getMethod().equals("GET")) {
                res = new JerseyHelper().setClient(client).get(requestContext.getUri().toString(), mediaType, requestContext.getHeaders(), Object.class, cookie, scenarioId);
                if(res.hasEntity()){
                    status = res.getStatus();
                    result =    res.readEntity(String.class);
                }
            }
            if(requestContext.getMethod().equals("PUT")) {
                res = new JerseyHelper().setClient(client).put(requestContext.getUri().toString(), mediaType, requestContext.getHeaders(), Object.class, entity, cookie, scenarioId);
                if(res.hasEntity()){
                    status = res.getStatus();
                    result =    res.readEntity(Object.class);
                }
            }
            if(requestContext.getMethod().equals("DELETE")) {
                res = new JerseyHelper().setClient(client).delete(requestContext.getUri().toString(), mediaType, requestContext.getHeaders(), Object.class, cookie, scenarioId);
                if(res.hasEntity()){
                    status = res.getStatus();
                    result =    res.readEntity(Object.class);
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
