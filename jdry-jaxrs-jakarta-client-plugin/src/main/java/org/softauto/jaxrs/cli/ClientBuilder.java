package org.softauto.jaxrs.cli;

import jakarta.ws.rs.client.Client;
import jakarta.ws.rs.core.Configuration;
import org.glassfish.jersey.client.ClientConfig;
import org.softauto.jaxrs.filter.RequestClientFilter;


public abstract class ClientBuilder extends jakarta.ws.rs.client.ClientBuilder{

    public static Client newClient(final Configuration configuration) {
        ((ClientConfig)configuration).register(RequestClientFilter.class);
        return jakarta.ws.rs.client.ClientBuilder.newClient(configuration);
    }

    public static Client newClient() {
        ClientConfig config = new ClientConfig();
        config.register(RequestClientFilter.class);
        Client client =  jakarta.ws.rs.client.ClientBuilder.newClient(config);
        return client;
    }

    public static jakarta.ws.rs.client.ClientBuilder newBuilder() {
        ClientConfig config = new ClientConfig();
        config.register(RequestClientFilter.class);
        return jakarta.ws.rs.client.ClientBuilder.newBuilder().withConfig(config);
    }
}
