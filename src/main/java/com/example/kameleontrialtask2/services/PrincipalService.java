package com.example.kameleontrialtask2.services;

import com.example.kameleontrialtask2.entity.Person;
import com.example.kameleontrialtask2.security.PersonDetails;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

@Component
public class PrincipalService {

    public Person getPerson() {
        SecurityContext securityContext = SecurityContextHolder.getContext();
        PersonDetails personDetails =
                (PersonDetails) securityContext.getAuthentication().getPrincipal();
        return personDetails.getPerson();
    }

}
