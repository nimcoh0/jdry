package com.cassiomolin.example.user.api.resource;

import com.cassiomolin.example.user.api.model.QueryPersonResult;
import com.cassiomolin.example.user.domain.Person;
import com.cassiomolin.example.user.service.PersonService;

import javax.annotation.security.PermitAll;
import javax.annotation.security.RolesAllowed;
import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.ws.rs.*;
import javax.ws.rs.core.*;
import java.security.Principal;
import java.util.HashSet;
import java.util.List;
import java.util.stream.Collectors;

/**
 * JAX-RS resource class that provides operations for users.
 *
 * @author cassiomolin
 */
@RequestScoped
@Path("users")
public class PersonResource {

    @Context
    private UriInfo uriInfo;

    @Context
    private SecurityContext securityContext;

    @Inject
    private PersonService personService;

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @RolesAllowed({"ADMIN"})
    public Response getUsers() {
        List<QueryPersonResult> queryPersonResults = personService.findAll().stream()
                .map(this::toQueryUserResult)
                .collect(Collectors.toList());

        return Response.ok(queryPersonResults).build();
    }

    @GET
    @Path("{userId}")
    @Produces(MediaType.APPLICATION_JSON)
    @RolesAllowed({"ADMIN"})
    public Response getUser(@PathParam("userId") Long userId) {

        Person person = personService.findById(userId).orElseThrow(NotFoundException::new);
        QueryPersonResult queryPersonResult = toQueryUserResult(person);
        return Response.ok(queryPersonResult).build();
    }

    @GET
    @Path("me")
    @Produces(MediaType.APPLICATION_JSON)
    @PermitAll
    public Response getAuthenticatedUser() {

        Principal principal = securityContext.getUserPrincipal();

        if (principal == null) {
            QueryPersonResult queryPersonResult = new QueryPersonResult();
            queryPersonResult.setUsername("anonymous");
            queryPersonResult.setAuthorities(new HashSet<>());
            return Response.ok(queryPersonResult).build();
        }

        Person person = personService.findByUsernameOrEmail(principal.getName());
        QueryPersonResult queryPersonResult = toQueryUserResult(person);
        return Response.ok(queryPersonResult).build();
    }

    /**
     * Maps a {@link Person} instance to a {@link QueryPersonResult} instance.
     *
     * @param person
     * @return
     */
    private QueryPersonResult toQueryUserResult(Person person) {
        QueryPersonResult queryPersonResult = new QueryPersonResult();
        queryPersonResult.setId(person.getId());
        queryPersonResult.setFirstName(person.getFirstName());
        queryPersonResult.setLastName(person.getLastName());
        queryPersonResult.setEmail(person.getEmail());
        queryPersonResult.setUsername(person.getUsername());
        queryPersonResult.setAuthorities(person.getAuthorities());
        queryPersonResult.setActive(person.isActive());
        return queryPersonResult;
    }
}