package com.cassiomolin.example.greeting.service;


import org.softauto.annotations.ApiForTesting;
import org.softauto.annotations.ListenerForTesting;
import org.softauto.annotations.ListenerType;

import javax.enterprise.context.ApplicationScoped;



/**
 * Service class that provides operations for greetings.
 *
 * @author cassiomolin
 */
@ApplicationScoped
public class GreetingService {

    /**
     * Get a public greeting.
     *
     * @return
     */
    @ApiForTesting(protocol = "RPC")
    public String getPublicGreeting() {
        return "Hello from the other side!";
    }

    /**
     * Get a greeting for a user.
     *
     * @return
     */
    @ListenerForTesting(type = ListenerType.BEFORE)
    public String getGreetingForUser(String username) {
        return String.format("Hello %s!", username);
    }

    @ListenerForTesting(type = ListenerType.AFTER)
    public String getGreetingForUser1(String username) {
        return String.format("Hello %s!", username);
    }
}