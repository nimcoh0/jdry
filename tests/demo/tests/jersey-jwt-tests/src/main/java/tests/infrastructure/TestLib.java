package tests.infrastructure;

import com.cassiomolin.example.security.api.model.AuthenticationToken;
import com.cassiomolin.example.security.api.model.UserCredentials;
import javax.ws.rs.client.Entity;
import javax.ws.rs.core.MediaType;

import java.net.URI;
import java.net.URISyntaxException;

public class TestLib {

    protected String composeAuthorizationHeader(String authenticationToken) {
        return "Bearer" + " " + authenticationToken;
    }

    protected String getTokenForAdmin() {

        AuthenticationToken authenticationToken = null;
        try {
            UserCredentials credentials = new UserCredentials();
            credentials.setUsername("sysadmin@thingsboard.org");
            credentials.setPassword("sysadmin");
            javax.ws.rs.client.Client client = javax.ws.rs.client.ClientBuilder.newBuilder().build();
            authenticationToken = client.target(new URI("http://localhost:8080")).path("/api/auth/login").request()
                    .post(Entity.entity(credentials, MediaType.APPLICATION_JSON), AuthenticationToken.class);
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }
        return authenticationToken.getToken();
    }

}
