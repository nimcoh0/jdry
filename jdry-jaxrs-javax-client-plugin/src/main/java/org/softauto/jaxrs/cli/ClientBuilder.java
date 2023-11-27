package org.softauto.jaxrs.cli;

import org.glassfish.jersey.client.ClientConfig;
import org.softauto.jaxrs.filter.RequestClientFilter;

import javax.ws.rs.core.Configuration;
import javax.ws.rs.client.Client;



public abstract class ClientBuilder extends javax.ws.rs.client.ClientBuilder  {

    public static Client newClient(final Configuration configuration) {
        ((ClientConfig)configuration).register(RequestClientFilter.class);
        return javax.ws.rs.client.ClientBuilder.newClient(configuration);
    }

    public static Client newClient() {
        ClientConfig clientConfig = new ClientConfig();
        clientConfig.register(RequestClientFilter.class);
        Client client =  javax.ws.rs.client.ClientBuilder.newClient((Configuration) clientConfig);
        return client;
    }

    public static javax.ws.rs.client.ClientBuilder newBuilder() {
        ClientConfig config = new ClientConfig();
        config.register(RequestClientFilter.class);
        return javax.ws.rs.client.ClientBuilder.newBuilder().withConfig((Configuration) config);
    }
}
