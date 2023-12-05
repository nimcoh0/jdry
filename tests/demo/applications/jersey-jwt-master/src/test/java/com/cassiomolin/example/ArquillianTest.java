package com.cassiomolin.example;

import com.cassiomolin.example.security.jwt.model.UserCredentials;
import com.cassiomolin.example.security.jwt.model.AuthenticationToken;
import org.jboss.shrinkwrap.api.Archive;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.junit.Before;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.core.MediaType;
import java.net.URI;
import java.net.URISyntaxException;

import static io.undertow.servlet.Servlets.listener;

/**
 * Base Arquillian test class.
 *
 * @author cassiomolin
 */
public abstract class ArquillianTest {

    //@ArquillianResource
    protected URI uri  ;

    protected Client client;

    //@Deployment(testable = false)
    public static Archive<WebArchive> createDeployment() {

/*
        return ShrinkWrap.create(UndertowWebArchive.class).from(
                deployment()
                        .setClassLoader(Application.class.getClassLoader())
                        .setContextPath("/")
                        .addListeners(listener(Listener.class))
                        .addServlets(
                                Servlets.servlet("jerseyServlet", ServletContainer.class)
                                        .setLoadOnStartup(1)
                                        .addInitParam("javax.ws.rs.Application", JerseyConfig.class.getName())
                                        .addMapping("/api/*"))
                        .setDeploymentName("application.war"));



 */


        return null;
    }

    @Before
    public void beforeTest() throws Exception {
        this.client = ClientBuilder.newClient();
    }

    protected String getTokenForAdmin() {
        try {
            URI uri  = new URI("http://localhost:8080/");
            UserCredentials credentials = new UserCredentials();
            credentials.setUsername("admin");
            credentials.setPassword("password");

            AuthenticationToken authenticationToken = client.target(uri).path("api").path("auth").request()
                    .post(Entity.entity(credentials, MediaType.APPLICATION_JSON), AuthenticationToken.class);
            return authenticationToken.getToken();
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }
        return null;
    }

    protected String getTokenForUser() {

        UserCredentials credentials = new UserCredentials();
        credentials.setUsername("user");
        credentials.setPassword("password");

        AuthenticationToken authenticationToken = client.target(uri).path("api").path("auth").request()
                .post(Entity.entity(credentials, MediaType.APPLICATION_JSON), AuthenticationToken.class);
        return authenticationToken.getToken();
    }

    protected String composeAuthorizationHeader(String authenticationToken) {
        return "Bearer" + " " + authenticationToken;
    }
}
