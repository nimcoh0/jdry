package org.softauto.jaxrs.auth.basic;

import javax.ws.rs.WebApplicationException;
import javax.ws.rs.ext.WriterInterceptor;
import javax.ws.rs.ext.WriterInterceptorContext;
import org.softauto.core.TestContext;

import java.io.IOException;
import java.util.Base64;

public class ClientHttpRequestInterceptor implements WriterInterceptor {

    @Override
    public void aroundWriteTo(WriterInterceptorContext context) throws IOException, WebApplicationException {
        Object jsessioId = TestContext.getScenario().getProperty("JSESSIONID");
        if(jsessioId == null){
            Object[] args =  (Object[])TestContext.getScenario().getProperty("args");
            if(args != null && args.length ==2) {
                String valueToEncode = args[0] + ":" + args[1];
                String pass = "Basic " + Base64.getEncoder().encodeToString(valueToEncode.getBytes());
                context.getHeaders().add("Authorization", pass);
            }
        }
        context.proceed();
    }
}
