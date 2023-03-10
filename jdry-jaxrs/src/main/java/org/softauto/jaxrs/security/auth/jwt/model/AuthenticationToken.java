package org.softauto.jaxrs.security.auth.jwt.model;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import javax.ws.rs.core.Response;

public class AuthenticationToken {

    private String token;

    private String refreshToken;

    String authorizationHeader;

    public AuthenticationToken(Response response) {
        try {
            String entity = response.readEntity(String.class);
            JsonNode node = new ObjectMapper().readTree(entity);
            token = node.get("token").asText();
            refreshToken = node.get("refreshToken").asText();
            authorizationHeader = composeAuthorizationHeader(token);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
    }

    public String getRefreshToken() {
        return refreshToken;
    }

    public String getAuthorizationHeader() {
        return authorizationHeader;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    protected String composeAuthorizationHeader(String authenticationToken) {
        return "Bearer" + " " + authenticationToken;
    }
}