package com.cassiomolin.example.security.api.resource;

import com.cassiomolin.example.ArquillianTest;
import com.cassiomolin.example.security.jwt.model.AuthenticationToken;
import com.cassiomolin.example.security.jwt.model.UserCredentials;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.glassfish.jersey.client.authentication.HttpAuthenticationFeature;
import org.junit.Test;

import javax.ws.rs.client.Entity;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import java.net.URI;
import java.net.URISyntaxException;

import static io.undertow.servlet.Servlets.listener;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

/**
 * Tests for the authentication resource class.
 *
 * @author cassiomolin
 */
//@RunWith(Arquillian.class)
public class AuthenticationResourceTest extends ArquillianTest {

    private static final String JWT_TOKEN_HEADER_PARAM = "X-Authorization";

    @Test
    public void authenticateWithValidCredentials() {

        try {
            UserCredentials credentials = new UserCredentials();
            credentials.setUsername("admin");
            credentials.setPassword("password");
            uri = new URI("http://localhost:8080/");
            Response response = client.target(uri).path("api").path("auth").request()
                    .post(Entity.entity(credentials, MediaType.APPLICATION_JSON));
            assertEquals(Response.Status.OK.getStatusCode(), response.getStatus());

            AuthenticationToken authenticationToken = response.readEntity(AuthenticationToken.class);
            assertNotNull(authenticationToken);
            assertNotNull(authenticationToken.getToken());
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void authenticateWithValidCredentialsThingsboard() {

        try {
            UserCredentials credentials = new UserCredentials();
            credentials.setUsername("sysadmin@thingsboard.org");
            credentials.setPassword("sysadmin");
            HttpAuthenticationFeature feature = HttpAuthenticationFeature.basic("sysadmin@thingsboard.org", "sysadmin");
            client = javax.ws.rs.client.ClientBuilder.newBuilder().register(feature).build();
            Response response = client.target(new URI("http://localhost:8080")).path("/api/auth/login").request()
                    .post(Entity.entity(credentials, MediaType.APPLICATION_JSON));
            assertEquals(Response.Status.OK.getStatusCode(), response.getStatus());

            String authenticationToken = response.readEntity(String.class);
            JsonNode node = new ObjectMapper().readTree(authenticationToken);

            String authorizationHeader = composeAuthorizationHeader(node.get("token").asText());

            Response response1 = client.target("http://localhost:8080").path("/api/tenant/1").request()
                    .header(JWT_TOKEN_HEADER_PARAM, authorizationHeader).get();

            assertNotNull(authenticationToken);
            //assertNotNull(authenticationToken.getToken());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    protected String getTokenForAdmin() {

        AuthenticationToken authenticationToken = null;
        try {
            UserCredentials credentials = new UserCredentials();
            credentials.setUsername("sysadmin@thingsboard.org");
            credentials.setPassword("sysadmin");
            authenticationToken = client.target(new URI("http://localhost:8080")).path("/api/auth/login").request()
                    .post(Entity.entity(credentials, MediaType.APPLICATION_JSON), AuthenticationToken.class);
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }
        return authenticationToken.getToken();
    }

    @Test
    public void getProtectedGreetingAsAdmin() {
        try {

            String authorizationHeader = composeAuthorizationHeader(getTokenForAdmin());

            Response response = client.target("http://localhost:8080").path("/api/auth/login").request()
                    .header(HttpHeaders.AUTHORIZATION, authorizationHeader).get();
            assertEquals(Response.Status.OK.getStatusCode(), response.getStatus());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    @Test
    public void authenticateWithInvalidCredentials() {

        UserCredentials credentials = new UserCredentials();
        credentials.setUsername("invalid-user");
        credentials.setPassword("wrong-password");

        Response response = client.target(uri).path("api").path("auth").request()
                .post(Entity.entity(credentials, MediaType.APPLICATION_JSON));
        assertEquals(Response.Status.FORBIDDEN.getStatusCode(), response.getStatus());
    }
}