package org.softauto.jaxrs.security.auth.jwt;

import com.fasterxml.jackson.databind.MapperFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import jakarta.ws.rs.client.Client;
import org.glassfish.jersey.client.ClientConfig;
import org.glassfish.jersey.client.JerseyClientBuilder;
import org.glassfish.jersey.jackson.JacksonFeature;
import org.glassfish.jersey.jackson.internal.jackson.jaxrs.json.JacksonJaxbJsonProvider;
import org.glassfish.jersey.media.multipart.MultiPartFeature;
import org.softauto.core.Configuration;
import org.softauto.jaxrs.providers.ObjectMapperProvider;
import org.softauto.jaxrs.security.auth.AbstractStepDescriptorImpl;
import org.softauto.jaxrs.service.ChannelDescriptor;
import org.softauto.jaxrs.util.ChannelBuilder;

//import javax.ws.rs.client.Client;
import jakarta.ws.rs.core.Cookie;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class JwtStepDescriptorImpl extends AbstractStepDescriptorImpl  {

    @Override
    public Cookie getCookie() {
        return null;
    }

    @Override
    public Client getClient() {
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.disable(MapperFeature.USE_ANNOTATIONS);
        JacksonJaxbJsonProvider provider = new JacksonJaxbJsonProvider();
        provider.setMapper(objectMapper);

        Client client = null;
        ClientConfig clientConfig = new ClientConfig();
        clientConfig.register(JacksonFeature.class);
        clientConfig.register(new ObjectMapperProvider());
        clientConfig.register(ClientHttpRequestInterceptor.class);
        clientConfig.register(RequestServerReaderInterceptor.class);
        clientConfig.register(MultiPartFeature.class);
        clientConfig.register(provider);
        client = JerseyClientBuilder.createClient(clientConfig);
        //client = javax.ws.rs.client.newBuilder().withConfig(clientConfig).build();
        return client;
    }

    @Override
    public ChannelDescriptor getChannel() {
        String host = (Configuration.get("jaxrs").asMap().get("host").toString());
        String port = (Configuration.get("jaxrs").asMap().get("port").toString());
        String protocol = (Configuration.get("jaxrs").asMap().get("protocol").toString());
        //String base_url = ( Configuration.get("jaxrs").asMap().get("base_url").toString());

        List<String> argumentsNames = (ArrayList)callOptions.get("argumentsNames");
        Map<String,Object> parameters = new HashMap();
        List<Object> newArgs = new ArrayList<>();
        String newPath = callOptions.get("path").toString();
        boolean first = true;
        List<Integer> argumentsRequestTypeArray = new ArrayList<>();
        if (this.callOptions.containsKey("argumentsRequestType")) {
            HashMap<String, Object> argumentsRequestTypes = (HashMap<String, Object>) callOptions.get("argumentsRequestType");
            for(Map.Entry entry : argumentsRequestTypes.entrySet()){
                if(entry.getKey().equals("RequestParam")){
                    if(entry.getValue() instanceof ArrayList<?>) {
                        for(Object o : (ArrayList)entry.getValue()){
                            argumentsRequestTypeArray.add(Integer.valueOf(((HashMap<String,Object>)o).get("index").toString()));
                        }
                    }else {
                        argumentsRequestTypeArray.add(Integer.valueOf(((HashMap<String,Object>)entry.getValue()).get("index").toString()));
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
            /*
                if (o.getKey().equals("PathVariable")) {
                    newArgs.add(args[i]);
                    newPath = newPath+"/{"+argumentsNames.get(i)+"}";
                    //newArgs.add(((Map)((ArrayList)o.getValue()).get(0)).get("index"));

                }


             */






        return ChannelBuilder.newBuilder().setHost(host)
                .setProtocol(protocol)
                .setArgs(newArgs.toArray())
                .setPath(newPath)
                .setPort(port)
                //.setBaseUrl(base_url)
                .build()
                .getChannelDescriptor();
    }
}
