package com.example.kameleontrialtask2.services;

import com.example.kameleontrialtask2.entity.Person;
import com.example.kameleontrialtask2.repository.PersonRepository;
import com.example.kameleontrialtask2.security.PersonDetails;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service //дает понять Spring что мы загружаем пользователя по его имени
public class PersonDetailsService implements UserDetailsService {
    private final PersonRepository personRepository;

    @Autowired
    public PersonDetailsService(PersonRepository personRepository) {
        this.personRepository = personRepository;
    }

    @Override //возвращает все что реализует UserDetails
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<Person> optionalPerson = personRepository.findByUsername(username);

        if (optionalPerson.isEmpty()) {
            throw new UsernameNotFoundException("User not found!");
        }

        return new PersonDetails(optionalPerson.get());
    }
}
