package org.softauto.jaxrs.util;

import org.glassfish.jersey.client.authentication.HttpAuthenticationFeature;
import org.softauto.core.TestContext;

import jakarta.ws.rs.client.Client;
import java.util.HashMap;

public class ClientBuilder {

    public static Builder newBuilder (){
        return new Builder();
    }

    Client client;

    public ClientBuilder(Client client){
        this.client = client;
        TestContext.put("client",client);
    }

    public Client getClient() {
        return client;
    }

    public static class Builder{

        Client client ;

        String schema;

        String username;

        String password;

        HashMap<String,Object> properties;

        public Builder setProperties(HashMap<String, Object> properties) {
            this.properties = properties;
            return this;
        }

        public Builder setSchema(String schema) {
            this.schema = schema;
            return this;
        }

        public Builder setUsername(String username) {
            this.username = username;
            return this;
        }

        public Builder setPassword(String password) {
            this.password = password;
            return this;
        }




        public ClientBuilder build(){
            //ClientConfig clientConfig = new ClientConfig();
            //clientConfig.register(JacksonFeature.class);  // usually auto-discovered
           // clientConfig.register(new ObjectMapperProvider());
           // clientConfig.register(ClientHttpRequestInterceptor.class);
            if(username != null) {
                HttpAuthenticationFeature feature = HttpAuthenticationFeature.basic(username, password);
                client = jakarta.ws.rs.client.ClientBuilder.newBuilder().register(feature).build();
            }else {
                client = jakarta.ws.rs.client.ClientBuilder.newBuilder().build();
            }
                //}
                //if(schema.equals("WWW-Authenticate")) {
                //    HttpAuthenticationFeature feature = HttpAuthenticationFeature.basic(username, password);
                 //   client = javax.ws.rs.client.ClientBuilder.newBuilder().register(feature).build();
                //}
           // }else {
              //  client = javax.ws.rs.client.ClientBuilder.newBuilder().build();
           // }
            if(properties != null){
                properties.forEach((k,v)->{
                    client.property(k,v);
                });
            }



            return new ClientBuilder(client);
        }

    }
}
