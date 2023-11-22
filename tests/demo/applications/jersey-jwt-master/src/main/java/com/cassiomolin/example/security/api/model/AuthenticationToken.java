package com.cassiomolin.example.security.api.model;

import org.softauto.annotations.ListenerForTesting;
import org.softauto.annotations.ListenerType;

/**
 * API model for the authentication token.
 *
 * @author cassiomolin
 */
public class AuthenticationToken {

    private String token;

    public AuthenticationToken() {

    }

    public String getToken() {
        return token;
    }

    @ListenerForTesting(type = ListenerType.BEFORE)
    public void setToken(String token) {
        this.token = token;
    }
}