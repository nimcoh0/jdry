package org.softauto.jaxrs.security.auth.basic;

import org.glassfish.jersey.client.ClientConfig;
import org.glassfish.jersey.jackson.JacksonFeature;
import org.glassfish.jersey.media.multipart.MultiPartFeature;
import org.softauto.core.Configuration;
import org.softauto.core.TestContext;
import org.softauto.jaxrs.security.auth.AbstractStepDescriptorImpl;
import org.softauto.jaxrs.service.ChannelDescriptor;
import org.softauto.jaxrs.util.ChannelBuilder;
import org.softauto.jaxrs.util.Threadlocal;

import jakarta.ws.rs.client.Client;
import jakarta.ws.rs.core.Cookie;

public class BasicStepDescriptorImpl extends AbstractStepDescriptorImpl  {


    @Override
    public Cookie getCookie(){
        if(TestContext.getScenario(callOptions.get("scenarioId").toString()).getProperty("JSESSIONID") != null){
        //if(Threadlocal.getInstance().get("JSESSIONID") != null){
           return (Cookie) TestContext.getScenario(callOptions.get("scenarioId").toString()).getProperty("JSESSIONID");
        }
        return null;
    }

    @Override
    public Client getClient() {
        Client client = null;
        ClientConfig clientConfig = new ClientConfig();

        clientConfig.register(JacksonFeature.class);
        //clientConfig.register(new ObjectMapperProvider());
        clientConfig.register(ResponseClientFilter.class);
        clientConfig.register(ClientHttpRequestInterceptor.class);
        clientConfig.register(MultiPartFeature.class);
        //HttpAuthenticationFeature feature = HttpAuthenticationFeature.basic("admin", "admin");
        //clientConfig.register(feature);
        client = jakarta.ws.rs.client.ClientBuilder.newBuilder().withConfig(clientConfig).build();
        /*
        String auth = Configuration.get("jaxrs").asMap().get(org.softauto.jaxrs.configuration.Context.AUTH).toString();
        if(auth != null && auth.equals(AuthenticationType.BASIC.getValue()) && TestContext.get("sessionId") == null && callOptions.get("role").toString().equals("AUTH") ) {
            client =  ClientBuilder.newBuilder().setPassword(args[1].toString())
                    .setUsername(args[0].toString())
                    .build()
                    .getClient();
        }else {
            client = ClientBuilder.newBuilder().build().getClient();
        }
        Map<String, Object> map = getProperties();
        if(map != null && map.size() > 0) {
            for (Map.Entry entry : map.entrySet()) {
                client.property(entry.getKey().toString(), entry.getValue());
            }
        }

         */
        return client;
    }

    public ChannelDescriptor getChannel() {
        String host = (Configuration.get("jaxrs").asMap().get("host").toString());
        String port = (Configuration.get("jaxrs").asMap().get("port").toString());
        String protocol = (Configuration.get("jaxrs").asMap().get("protocol").toString());
        //String base_url = ( Configuration.get("jaxrs").asMap().get("base_url").toString());
        return ChannelBuilder.newBuilder().setHost(host)
                .setProtocol(protocol)
                .setArgs(args)
                .setPath(callOptions.get("path").toString())
                .setPort(port)
                //.setBaseUrl(base_url)
                .build()
                .getChannelDescriptor();
    }
}
