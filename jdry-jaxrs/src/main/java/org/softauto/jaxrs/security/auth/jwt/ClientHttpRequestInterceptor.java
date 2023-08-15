package org.softauto.jaxrs.security.auth.jwt;

import org.softauto.core.TestContext;
import org.softauto.jaxrs.util.Threadlocal;

import jakarta.ws.rs.WebApplicationException;
import jakarta.ws.rs.ext.WriterInterceptor;
import jakarta.ws.rs.ext.WriterInterceptorContext;
import java.io.IOException;

public class ClientHttpRequestInterceptor implements WriterInterceptor {





    @Override
    public void aroundWriteTo(WriterInterceptorContext context) throws IOException, WebApplicationException {
        String scenarioId = Threadlocal.getInstance().get("scenarioId").toString();
        Object token = TestContext.getScenario(scenarioId).getProperty("token");
        if(token != null){
            context.getHeaders().add("Authorization", "Bearer " + token.toString());
        }
        context.proceed();
    }
}
