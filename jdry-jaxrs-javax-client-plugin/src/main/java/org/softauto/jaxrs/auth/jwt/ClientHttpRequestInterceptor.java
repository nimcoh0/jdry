package org.softauto.jaxrs.auth.jwt;

import javax.ws.rs.WebApplicationException;
import javax.ws.rs.ext.WriterInterceptor;
import javax.ws.rs.ext.WriterInterceptorContext;
import org.softauto.core.TestContext;

import java.io.IOException;

public class ClientHttpRequestInterceptor implements WriterInterceptor  {

    @Override
    public void aroundWriteTo(WriterInterceptorContext context) throws IOException, WebApplicationException {
        Object token = TestContext.getScenario().getProperty("token");
        if(token != null){
            context.getHeaders().add("Authorization", "Bearer " + token.toString());
        }
        context.proceed();
    }
}
