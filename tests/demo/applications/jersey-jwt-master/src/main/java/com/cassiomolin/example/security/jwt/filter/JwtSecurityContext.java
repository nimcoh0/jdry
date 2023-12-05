package com.cassiomolin.example.security.jwt.filter;

import javax.ws.rs.core.SecurityContext;
import java.security.Principal;

public class JwtSecurityContext implements  javax.ws.rs.core.SecurityContext {

    private final String username;

    public JwtSecurityContext(String username) {
        this.username = username;
    }

    @Override
    public Principal getUserPrincipal() {
        return () -> username;
    }

    @Override
    public boolean isUserInRole(String role) {
        // Implement role-based access control logic if needed
        return false;
    }

    @Override
    public boolean isSecure() {
        // Implement your logic to determine if the connection is secure (e.g., over HTTPS)
        return false;
    }

    @Override
    public String getAuthenticationScheme() {
        // Implement your authentication scheme, if needed
        return null;
    }
}
