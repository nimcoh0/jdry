package org.softauto.auth.basic;

import jakarta.ws.rs.client.ClientRequestContext;
import jakarta.ws.rs.client.ClientResponseContext;
import jakarta.ws.rs.client.ClientResponseFilter;
import org.softauto.core.TestContext;
import org.softauto.jaxrs.util.Threadlocal;

import java.io.IOException;

public class ResponseClientFilter implements ClientResponseFilter {

    @Override
    public void filter(ClientRequestContext requestContext, ClientResponseContext responseContext) throws IOException {
        if(responseContext.getCookies().get("JSESSIONID") != null){
            String scenarioId = Threadlocal.getInstance().get("scenarioId").toString();
            TestContext.getScenario(scenarioId).addProperty("JSESSIONID",responseContext.getCookies().get("JSESSIONID"));
            //Threadlocal.getInstance().add("JSESSIONID",responseContext.getCookies().get("JSESSIONID"));
        }
    }
}
