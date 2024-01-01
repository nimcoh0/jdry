package org.softauto.jaxrs.auth.jwt;

import jakarta.ws.rs.WebApplicationException;
import jakarta.ws.rs.ext.WriterInterceptor;
import jakarta.ws.rs.ext.WriterInterceptorContext;
import org.softauto.core.TestContext;

import jakarta.ws.rs.core.MultivaluedMap;
import java.io.IOException;

public class ClientHttpRequestInterceptor implements WriterInterceptor  {

    @Override
    public void aroundWriteTo(WriterInterceptorContext context) throws IOException, WebApplicationException {
        Object token = TestContext.getScenario().getProperty("token");
        boolean exist = false;
        if(token != null){
            MultivaluedMap<String, Object> headers =  context.getHeaders();
            if(headers.containsKey("Authorization") ){
                for(Object header : headers.get("Authorization")){
                    if(header.toString().contains("Bearer ")){
                        exist = true;
                    }
                }
            }
            if(!exist)
                context.getHeaders().add("Authorization", "Bearer " + token.toString());
        }
        context.proceed();
    }
}
