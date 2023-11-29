package org.softauto.jaxrs.auth.jwt;

import com.fasterxml.jackson.databind.MapperFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.Entity;
import javax.ws.rs.core.Cookie;
import javax.ws.rs.core.MultivaluedHashMap;
import javax.ws.rs.core.MultivaluedMap;
import org.glassfish.jersey.client.ClientConfig;
import org.glassfish.jersey.client.JerseyClientBuilder;
import org.glassfish.jersey.jackson.JacksonFeature;
import org.glassfish.jersey.jackson.internal.jackson.jaxrs.json.JacksonJaxbJsonProvider;
import org.glassfish.jersey.media.multipart.MultiPartFeature;
import org.softauto.core.Configuration;
import org.softauto.core.TestContext;
import org.softauto.jaxrs.AbstractStepDescriptorImpl;
import org.softauto.jaxrs.auth.EntityBuilder;
import org.softauto.jaxrs.providers.ObjectMapperProvider;
import org.softauto.jaxrs.service.ChannelDescriptor;
import org.softauto.jaxrs.util.ChannelBuilder;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class JwtStepDescriptorImpl extends AbstractStepDescriptorImpl {

    @Override
    public Entity<?> getEntity() {
        List<String> argumentsNames = (ArrayList)callOptions.get("argumentsNames");
        return (Entity<?>) EntityBuilder.newBuilder().setCallOptions(callOptions).setProduce(consume).setArgs(args).setArgsNames(argumentsNames).build().getEntity();
    }



    @Override
    public Cookie getCookie() {
        return null;
    }

    @Override
    public Client getClient() {
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.disable(MapperFeature.USE_ANNOTATIONS);
        //JacksonJaxbJsonProvider provider = new JacksonJaxbJsonProvider();
        //provider.setMapper(objectMapper);

        Client client = null;
        ClientConfig clientConfig = new ClientConfig();
        clientConfig.register(JacksonFeature.class);
        clientConfig.register(new ObjectMapperProvider());
        clientConfig.register(ClientHttpRequestInterceptor.class);
        clientConfig.register(RequestServerReaderInterceptor.class);
        clientConfig.register(MultiPartFeature.class);
        //clientConfig.register(provider);
        client = JerseyClientBuilder.createClient(clientConfig);
        return client;
    }

    @Override
    public ChannelDescriptor getChannel() {
        String host = (Configuration.get("jaxrs").asMap().get("host").toString());
        String port = (Configuration.get("jaxrs").asMap().get("port").toString());
        String protocol = (Configuration.get("jaxrs").asMap().get("protocol").toString());
        String baseUrl = Configuration.get("jaxrs").asMap().containsKey("base_url") ? (Configuration.get("jaxrs").asMap().get("base_url").toString()):"";
        List<String> argumentsNames = (ArrayList)callOptions.get("argumentsNames");
        Map<String,Object> parameters = new HashMap();
        List<Object> newArgs = new ArrayList<>();
        String newPath = callOptions.get("path").toString();
        if(!newPath.contains(baseUrl)){
            if(newPath.startsWith("/")){
                newPath = baseUrl+newPath;
            }else {
                newPath = baseUrl+"/"+newPath;
            }
        }

        boolean first = true;
        List<Integer> argumentsRequestTypeArray = new ArrayList<>();
        if (this.callOptions.containsKey("argumentsRequestType")) {
            if(callOptions.get("argumentsRequestType") instanceof HashMap) {
                HashMap<String, Object> argumentsRequestTypes = (HashMap<String, Object>) callOptions.get("argumentsRequestType");
                for (Map.Entry entry : argumentsRequestTypes.entrySet()) {
                    if (entry.getKey().equals("QueryParam") || entry.getKey().equals("RequestParam")) {
                        if (entry.getValue() instanceof ArrayList<?>) {
                            for (Object o : (ArrayList) entry.getValue()) {
                                argumentsRequestTypeArray.add(Integer.valueOf(((HashMap<String, Object>) o).get("index").toString()));
                            }
                        } else {
                            argumentsRequestTypeArray.add(Integer.valueOf(((HashMap<String, Object>) entry.getValue()).get("index").toString()));
                        }
                    }
                }
            }
        }


        for(int i=0;i<args.length;i++) {
            if(argumentsRequestTypeArray.contains(i)){
                if (first) {
                    newPath = newPath + "?" + argumentsNames.get(i) + "=" + args[i];
                    first = false;
                } else {
                    newPath = newPath + "&" + argumentsNames.get(i) + "=" + args[i];
                }
            }
        }



        return ChannelBuilder.newBuilder().setHost(host)
                .setProtocol(protocol)
                .setArgs(newArgs.toArray())
                .setPath(newPath)
                .setPort(port)
                //.setBaseUrl(base_url)
                .build()
                .getChannelDescriptor();
    }


    @Override
    public MultivaluedMap<String, Object> getHeaders() {
        MultivaluedMap<String, Object> mm = new MultivaluedHashMap<>();
        if(TestContext.getScenario() != null ) {
            if (TestContext.getScenario().getProperty("token") != null) {
                Object token = TestContext.getScenario().getProperty("token");
                mm.add("Authorization", "Bearer " + token.toString());
            }
            //String scenarioId = Threadlocal.getInstance().get("scenarioId").toString();
            mm.add("scenarioId", scenarioId);
        }

        if(callOptions.get("headers") != null){
            mm.putAll((MultivaluedMap<String, Object>)callOptions.get("headers"));
        }

        return mm;
    }
}
