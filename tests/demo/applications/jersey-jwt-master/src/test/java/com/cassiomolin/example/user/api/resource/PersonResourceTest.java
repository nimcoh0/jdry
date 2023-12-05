package com.cassiomolin.example.user.api.resource;

import com.cassiomolin.example.ArquillianTest;
import com.cassiomolin.example.security.domain.Authority;
import com.cassiomolin.example.user.api.model.QueryPersonResult;
import org.jboss.arquillian.junit.Arquillian;
import org.junit.Test;
import org.junit.runner.RunWith;

import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.core.GenericType;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.Response;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;

import static io.undertow.servlet.Servlets.listener;
import static org.hamcrest.Matchers.*;
import static org.junit.Assert.*;

/**
 * Tests for the user resource class.
 *
 * @author cassiomolin
 */
@RunWith(Arquillian.class)
public class PersonResourceTest extends ArquillianTest {

    @Test
    public void getUsersAsAnonymous() {

        Response response = client.target(uri).path("api").path("users").request().get();
        assertEquals(Response.Status.FORBIDDEN.getStatusCode(), response.getStatus());
    }

    @Test
    public void getUsersAsAsUser() {

        String authorizationHeader = composeAuthorizationHeader(getTokenForUser());

        Response response = client.target(uri).path("api").path("users").request()
                .header(HttpHeaders.AUTHORIZATION, authorizationHeader).get();
        assertEquals(Response.Status.FORBIDDEN.getStatusCode(), response.getStatus());
    }

    @Test
    public void getUsersAsAdmin() {
        try {
            URI uri  = new URI("http://localhost:8080/");
            String authorizationHeader = composeAuthorizationHeader(getTokenForAdmin());
            client = ClientBuilder.newClient();
            Response response = client.target(uri).path("api").path("users").request()
                    .header(HttpHeaders.AUTHORIZATION, authorizationHeader).get();
            assertEquals(Response.Status.OK.getStatusCode(), response.getStatus());

            List<QueryPersonResult> queryDetailsList = response.readEntity(new GenericType<List<QueryPersonResult>>() {});
            assertNotNull(queryDetailsList);
            assertThat(queryDetailsList, hasSize(3));
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void getUserAsAnonymous() {

        Long userId = 1L;

        Response response = client.target(uri).path("api").path("users").path(userId.toString()).request().get();
        assertEquals(Response.Status.FORBIDDEN.getStatusCode(), response.getStatus());
    }

    @Test
    public void getUserAsUser() {

        Long userId = 1L;

        String authorizationHeader = composeAuthorizationHeader(getTokenForUser());

        Response response = client.target(uri).path("api").path("users").path(userId.toString()).request()
                .header(HttpHeaders.AUTHORIZATION, authorizationHeader).get();
        assertEquals(Response.Status.FORBIDDEN.getStatusCode(), response.getStatus());
    }

    @Test
    public void getUserAsAdmin() {

        Long userId = 1L;

        String authorizationHeader = composeAuthorizationHeader(getTokenForAdmin());

        Response response = client.target(uri).path("api").path("users").path(userId.toString()).request()
                .header(HttpHeaders.AUTHORIZATION, authorizationHeader).get();
        assertEquals(Response.Status.OK.getStatusCode(), response.getStatus());

        QueryPersonResult result = response.readEntity(QueryPersonResult.class);
        assertNotNull(result);
        assertEquals(userId, result.getId());
    }

    @Test
    public void getAuthenticatedUserAsAnonymous() {

        Response response = client.target(uri).path("api").path("users").path("me").request().get();
        assertEquals(Response.Status.OK.getStatusCode(), response.getStatus());

        QueryPersonResult user = response.readEntity(QueryPersonResult.class);
        assertNull(user.getId());
        assertEquals("anonymous", user.getUsername());
        assertThat(user.getAuthorities(), is(empty()));
    }

    @Test
    public void getAuthenticatedUserAsUser() {

        String authorizationHeader = composeAuthorizationHeader(getTokenForUser());

        Response response = client.target(uri).path("api").path("users").path("me").request()
                .header(HttpHeaders.AUTHORIZATION, authorizationHeader).get();
        assertEquals(Response.Status.OK.getStatusCode(), response.getStatus());

        QueryPersonResult user = response.readEntity(QueryPersonResult.class);
        assertNotNull(user.getId());
        assertEquals("user", user.getUsername());
        assertThat(user.getAuthorities(), containsInAnyOrder(Authority.USER));
    }

    @Test
    public void getAuthenticatedUserAsAdmin() {

        String authorizationHeader = composeAuthorizationHeader(getTokenForAdmin());

        Response response = client.target(uri).path("api").path("users").path("me").request()
                .header(HttpHeaders.AUTHORIZATION, authorizationHeader).get();
        assertEquals(Response.Status.OK.getStatusCode(), response.getStatus());

        QueryPersonResult user = response.readEntity(QueryPersonResult.class);
        assertNotNull(user.getId());
        assertEquals("admin", user.getUsername());
        assertThat(user.getAuthorities(), containsInAnyOrder(Authority.USER, Authority.ADMIN));
    }
}