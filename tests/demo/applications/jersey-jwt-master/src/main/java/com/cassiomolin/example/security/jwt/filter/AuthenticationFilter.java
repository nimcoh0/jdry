package com.cassiomolin.example.security.jwt.filter;

import com.cassiomolin.example.security.jwt.AuthenticatedUserDetails;
import com.cassiomolin.example.security.jwt.AuthenticationTokenDetails;
import com.cassiomolin.example.security.jwt.TokenBasedSecurityContext;
import com.cassiomolin.example.security.service.AuthenticationTokenService;
import com.cassiomolin.example.user.domain.Person;
import com.cassiomolin.example.user.service.PersonService;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;

import javax.annotation.Priority;
import javax.crypto.SecretKey;
import javax.enterprise.context.Dependent;
import javax.inject.Inject;
import javax.ws.rs.Priorities;
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerRequestFilter;
import javax.ws.rs.container.ContainerResponseContext;
import javax.ws.rs.container.ContainerResponseFilter;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.SecurityContext;
import javax.ws.rs.ext.Provider;
import java.io.IOException;
import java.util.Date;

@Provider
@Dependent
@Priority(Priorities.AUTHENTICATION)
public class AuthenticationFilter implements ContainerRequestFilter {

    private static final SecretKey SECRET_KEY = Keys.secretKeyFor(SignatureAlgorithm.HS256);
    private static final String ISSUER = "your-issuer";
    private static final long EXPIRATION_TIME = 86400000;

    @Inject
    private PersonService personService;

    @Inject
    private AuthenticationTokenService authenticationTokenService;

    @Override
    public void filter(ContainerRequestContext requestContext) throws IOException {
        // Get the JWT token from the request header
        String authorizationHeader = requestContext.getHeaderString(HttpHeaders.AUTHORIZATION);
        if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
            String authenticationToken = authorizationHeader.substring(7);
            handleTokenBasedAuthentication(authenticationToken, requestContext);
            return;
        } else {
            // Token doesn't exist, create and attach a new token to the response
            //String newToken = generateToken("username"); // Replace with your logic for obtaining the username
            //requestContext.setProperty("Token", newToken);
        }
    }

/*
    @Override
    public void filter(ContainerRequestContext requestContext, ContainerResponseContext responseContext)
            throws IOException {
        // Attach the new token to the response header if it was created
        String newToken = (String) requestContext.getProperty("Token");
        if (newToken != null) {
            responseContext.getHeaders().add(HttpHeaders.AUTHORIZATION, "Bearer " + newToken);
        }
    }


 */


    private void validateToken(ContainerRequestContext requestContext, String token) {
        try {
            Claims claims = Jwts.parser()
                    .setSigningKey(SECRET_KEY)
                    .build()
                    .parseClaimsJws(token)
                    .getBody();

            // Extract user information from the token and set up security context
            String username = claims.getSubject();
            // You may perform additional checks or validations here if needed

            // Set up the security context (this is just a simple example)
            requestContext.setSecurityContext(new JwtSecurityContext(username));

        } catch (Exception e) {
            // Token is not valid
            // You may choose to handle this differently based on your requirements
            requestContext.abortWith(Response.status(Response.Status.UNAUTHORIZED).build());
        }
    }

    private String generateToken(String subject) {
        Date now = new Date();
        Date expiration = new Date(now.getTime() + EXPIRATION_TIME);

        return Jwts.builder()
                .setSubject(subject)
                .setIssuer(ISSUER)
                .setIssuedAt(now)
                .setExpiration(expiration)
                .signWith(SECRET_KEY)
                .compact();
    }

    private void handleTokenBasedAuthentication(String authenticationToken, ContainerRequestContext requestContext) {

        AuthenticationTokenDetails authenticationTokenDetails = authenticationTokenService.parseToken(authenticationToken);
        Person person = personService.findByUsernameOrEmail(authenticationTokenDetails.getUsername());
        AuthenticatedUserDetails authenticatedUserDetails = new AuthenticatedUserDetails(person.getUsername(), person.getAuthorities());

        boolean isSecure = requestContext.getSecurityContext().isSecure();
        SecurityContext securityContext = new TokenBasedSecurityContext(authenticatedUserDetails, authenticationTokenDetails, isSecure);
        requestContext.setSecurityContext(securityContext);
    }
}
