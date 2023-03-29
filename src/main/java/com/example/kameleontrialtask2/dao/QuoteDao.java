package com.example.kameleontrialtask2.dao;

import com.example.kameleontrialtask2.entity.Person;
import com.example.kameleontrialtask2.entity.Quote;
import com.example.kameleontrialtask2.repository.QuoteRepository;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public record QuoteDao(QuoteRepository quoteRepository) {

    List<Quote> findAllByPerson(Person person) {
        return quoteRepository.findAllByPerson(person);
    }


}
