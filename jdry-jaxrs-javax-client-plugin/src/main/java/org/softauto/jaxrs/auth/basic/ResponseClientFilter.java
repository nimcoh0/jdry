package org.softauto.jaxrs.auth.basic;

import javax.ws.rs.client.ClientRequestContext;
import javax.ws.rs.client.ClientResponseContext;
import javax.ws.rs.client.ClientResponseFilter;
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
