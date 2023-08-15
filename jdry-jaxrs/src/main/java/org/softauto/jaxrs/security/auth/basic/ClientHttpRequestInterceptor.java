package org.softauto.jaxrs.security.auth.basic;

import org.softauto.core.TestContext;
import org.softauto.jaxrs.util.Threadlocal;

import jakarta.ws.rs.WebApplicationException;
import jakarta.ws.rs.ext.WriterInterceptor;
import jakarta.ws.rs.ext.WriterInterceptorContext;
import java.io.IOException;
import java.util.Base64;
import java.util.List;

public class ClientHttpRequestInterceptor implements WriterInterceptor {





    @Override
    public void aroundWriteTo(WriterInterceptorContext context) throws IOException, WebApplicationException {
        String scenarioId = Threadlocal.getInstance().get("scenarioId").toString();
        Object jsessioId = TestContext.getScenario(scenarioId).getProperty("JSESSIONID");
        if(jsessioId == null){
            Object[] args =  (Object[])TestContext.getScenario(scenarioId).getProperty("args");
            //Object[] args = (Object[]) Threadlocal.getInstance().get("args");
            if(args != null && args.length ==2) {
                String valueToEncode = args[0] + ":" + args[1];
                String pass = "Basic " + Base64.getEncoder().encodeToString(valueToEncode.getBytes());
                context.getHeaders().add("Authorization", pass);
            }
        }

        Thread.currentThread().getId();
        context.proceed();
    }
}
