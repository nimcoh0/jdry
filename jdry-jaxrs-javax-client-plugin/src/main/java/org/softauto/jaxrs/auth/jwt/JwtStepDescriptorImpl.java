package org.softauto.jaxrs.auth.jwt;

import com.fasterxml.jackson.databind.MapperFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import javax.ws.rs.client.Client;
import javax.ws.rs.core.Cookie;
import javax.ws.rs.core.MultivaluedHashMap;
import javax.ws.rs.core.MultivaluedMap;
import org.glassfish.jersey.client.ClientConfig;
import org.glassfish.jersey.client.JerseyClientBuilder;
import org.glassfish.jersey.jackson.JacksonFeature;
import org.glassfish.jersey.media.multipart.MultiPartFeature;
import org.softauto.core.TestContext;
import org.softauto.jaxrs.AbstractStepDescriptorImpl;
import org.softauto.jaxrs.providers.ObjectMapperProvider;

public class JwtStepDescriptorImpl extends AbstractStepDescriptorImpl {

    @Override
    public Cookie getCookie() {
        return null;
    }

    @Override
    public Client getClient() {
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.disable(MapperFeature.USE_ANNOTATIONS);
        Client client = null;
        ClientConfig clientConfig = new ClientConfig();
        clientConfig.register(JacksonFeature.class);
        clientConfig.register(new ObjectMapperProvider());
        clientConfig.register(ClientHttpRequestInterceptor.class);
        clientConfig.register(RequestServerReaderInterceptor.class);
        clientConfig.register(MultiPartFeature.class);
        client = JerseyClientBuilder.createClient(clientConfig);
        return client;
    }

    @Override
    public MultivaluedMap<String, Object> getHeaders() {
        MultivaluedMap<String, Object> mm = new MultivaluedHashMap<>();
        if(TestContext.getScenario() != null ) {
            if (TestContext.getScenario().getProperty("token") != null) {
                Object token = TestContext.getScenario().getProperty("token");
                mm.add("Authorization", "Bearer " + token.toString());
            }
            mm.add("scenarioId", scenarioId);
        }

        if(callOptions != null && callOptions.containsKey("headers") && callOptions.get("headers") != null){
            mm.putAll((MultivaluedMap<String, Object>)callOptions.get("headers"));
        }

        return mm;
    }
}
