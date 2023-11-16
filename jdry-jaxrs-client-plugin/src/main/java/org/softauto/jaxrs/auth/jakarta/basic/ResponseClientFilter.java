package org.softauto.jaxrs.auth.jakarta.basic;

import jakarta.ws.rs.client.ClientRequestContext;
import jakarta.ws.rs.client.ClientResponseContext;
import jakarta.ws.rs.client.ClientResponseFilter;
import org.softauto.core.TestContext;

import java.io.IOException;

public class ResponseClientFilter implements ClientResponseFilter {

    @Override
    public void filter(ClientRequestContext requestContext, ClientResponseContext responseContext) throws IOException {
        if(responseContext.getCookies().get("JSESSIONID") != null){
          TestContext.getScenario().addProperty("JSESSIONID",responseContext.getCookies().get("JSESSIONID"));
        }
    }
}
