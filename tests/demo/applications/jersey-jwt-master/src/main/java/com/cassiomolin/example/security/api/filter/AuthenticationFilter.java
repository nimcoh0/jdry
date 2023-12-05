package com.cassiomolin.example.security.api.filter;

import com.cassiomolin.example.security.jwt.AuthenticatedUserDetails;
import com.cassiomolin.example.security.jwt.AuthenticationTokenDetails;
import com.cassiomolin.example.security.jwt.TokenBasedSecurityContext;
import com.cassiomolin.example.security.service.AuthenticationTokenService;
import com.cassiomolin.example.user.domain.Person;
import com.cassiomolin.example.user.service.PersonService;

import javax.inject.Inject;
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerRequestFilter;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.SecurityContext;
import java.io.IOException;

/**
 * JWT authentication filter.
 *
 * @author cassiomolin
 */
//@Provider
//@Dependent
//@Priority(Priorities.AUTHENTICATION)
public class AuthenticationFilter implements ContainerRequestFilter {

    @Inject
    private PersonService personService;

    @Inject
    private AuthenticationTokenService authenticationTokenService;

    @Override
    public void filter(ContainerRequestContext requestContext) throws IOException {

            String authorizationHeader = requestContext.getHeaderString(HttpHeaders.AUTHORIZATION);
            if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
                String authenticationToken = authorizationHeader.substring(7);
                handleTokenBasedAuthentication(authenticationToken, requestContext);
                return;
            }

        // Other authentication schemes (such as Basic) could be supported
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