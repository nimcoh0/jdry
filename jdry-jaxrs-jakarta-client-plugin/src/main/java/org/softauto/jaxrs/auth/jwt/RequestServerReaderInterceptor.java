package org.softauto.jaxrs.auth.jwt;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.ws.rs.WebApplicationException;
import jakarta.ws.rs.ext.ReaderInterceptor;
import jakarta.ws.rs.ext.ReaderInterceptorContext;
import org.softauto.core.TestContext;
import org.softauto.jaxrs.util.Threadlocal;

import java.io.*;
import java.util.stream.Collectors;

public class RequestServerReaderInterceptor implements ReaderInterceptor {

    @Override
    public Object aroundReadFrom(ReaderInterceptorContext context) throws IOException, WebApplicationException {
        InputStream is = context.getInputStream();
        String body = new BufferedReader(new InputStreamReader(is)).lines()
                .collect(Collectors.joining("\n"));
        if(body.contains("token")){
            JsonNode node =   new ObjectMapper().readTree(body);
            String token = node.findValue("token").asText();
            String scenarioId = Threadlocal.getInstance().get("scenarioId").toString();
            TestContext.getScenario().addProperty("token",token);
        }
        context.setInputStream(new ByteArrayInputStream(body.getBytes()));
        return context.proceed();
    }
}
