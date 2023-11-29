package com.cassiomolin.example.security.api.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.softauto.annotations.*;

/**
 * API model for the authentication token.
 *
 * @author cassiomolin
 */
@InitializeForTesting(value = ClassType.INITIALIZE_EVERY_TIME,parameters = @Parameter(type = "String",value = "helo"))
public class AuthenticationToken {

    private String token;

    public AuthenticationToken() {

    }

    public AuthenticationToken(String s) {
        System.out.println(s);
    }

    public String getToken() {
        return token;
    }

    @ListenerForTesting(type = ListenerType.BEFORE)
    public void setToken(String token) {
        this.token = token;
    }
}