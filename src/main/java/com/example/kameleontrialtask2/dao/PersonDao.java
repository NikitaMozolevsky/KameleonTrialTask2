package com.example.kameleontrialtask2.dao;

import com.example.kameleontrialtask2.entity.Person;
import com.example.kameleontrialtask2.repository.PersonRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public record PersonDao(PersonRepository personRepository) {

    Optional<Person> findByUsername(String username) {
        return personRepository.findByUsername(username);
    }
}
