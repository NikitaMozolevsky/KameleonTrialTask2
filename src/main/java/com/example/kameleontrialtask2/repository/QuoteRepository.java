package com.example.kameleontrialtask2.repository;

import com.example.kameleontrialtask2.entity.Person;
import com.example.kameleontrialtask2.entity.Quote;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface QuoteRepository extends JpaRepository<Quote, Integer> {

    List<Quote> findAllByPerson(Person person);
    @Query("""
            SELECT element
            FROM Quote element
            WHERE element.creationDate = (
            SELECT MAX(e.creationDate)
            FROM Quote e
            )
            """)
    Quote findLastCreatedQuote();
}
