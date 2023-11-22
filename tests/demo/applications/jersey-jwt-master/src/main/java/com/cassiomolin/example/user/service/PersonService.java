package com.cassiomolin.example.user.service;

import com.cassiomolin.example.user.domain.Person;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import org.softauto.annotations.ApiForTesting;
//import jakarta.persistence.EntityManager;
import javax.persistence.EntityManager;
import java.util.List;
import java.util.Optional;

/**
 * Service that provides operations for {@link Person}s.
 *
 * @author cassiomolin
 */
@ApplicationScoped
public class PersonService {

    @Inject
    private EntityManager em;

    /**
     * Find a user by username or email.
     *
     * @param identifier
     * @return
     */
    @ApiForTesting
    public Person findByUsernameOrEmail(String identifier) {
        List<Person> people = em.createQuery("SELECT u FROM Person u WHERE u.username = :identifier OR u.email = :identifier", Person.class)
                .setParameter("identifier", identifier)
                .setMaxResults(1)
                .getResultList();
        if (people.isEmpty()) {
            return null;
        }
        return people.get(0);
    }

    /**
     * Find all users.
     *
     * @return
     */
    @ApiForTesting
    public List<Person> findAll() {
        return em.createQuery("SELECT u FROM Person u", Person.class).getResultList();
    }

    /**
     * Find a user by id.
     *
     * @param userId
     * @return
     */
    @ApiForTesting
    public Optional<Person> findById(Long userId) {
        return Optional.ofNullable(em.find(Person.class, userId));
    }
}
