package com.cassiomolin.example.security.service;


import com.cassiomolin.example.security.exception.AuthenticationException;
import com.cassiomolin.example.user.domain.Person;
import com.cassiomolin.example.user.service.PersonService;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

/**
 * Component for validating user credentials.
 *
 * @author cassiomolin
 */
@ApplicationScoped
public class UsernamePasswordValidator {

    @Inject
    private PersonService personService;

    @Inject
    private PasswordEncoder passwordEncoder;

    /**
     * Validate username and password.
     *
     * @param username
     * @param password
     * @return
     */
    public Person validateCredentials(String username, String password) {

        Person person = personService.findByUsernameOrEmail(username);

        if (person == null) {
            // User cannot be found with the given username/email
            throw new AuthenticationException("Bad credentials.");
        }

        if (!person.isActive()) {
            // User is not active
            throw new AuthenticationException("The user is inactive.");
        }

        if (!passwordEncoder.checkPassword(password, person.getPassword())) {
            // Invalid password
            throw new AuthenticationException("Bad credentials.");
        }

        return person;
    }
}