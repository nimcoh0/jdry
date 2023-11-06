package org.softauto.auth.basic;

import jakarta.ws.rs.WebApplicationException;
import jakarta.ws.rs.ext.WriterInterceptor;
import jakarta.ws.rs.ext.WriterInterceptorContext;
import org.softauto.core.TestContext;
import org.softauto.jaxrs.util.Threadlocal;

import java.io.IOException;
import java.util.Base64;

public class ClientHttpRequestInterceptor implements WriterInterceptor {





    @Override
    public void aroundWriteTo(WriterInterceptorContext context) throws IOException, WebApplicationException {
        String scenarioId = Threadlocal.getInstance().get("scenarioId").toString();
        Object jsessioId = TestContext.getScenario().getProperty("JSESSIONID");
        if(jsessioId == null){
            Object[] args =  (Object[])TestContext.getScenario().getProperty("args");
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
