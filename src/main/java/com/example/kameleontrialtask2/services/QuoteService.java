package com.example.kameleontrialtask2.services;

import com.example.kameleontrialtask2.entity.Person;
import com.example.kameleontrialtask2.entity.Quote;
import com.example.kameleontrialtask2.repository.QuoteRepository;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class QuoteService {


    private final SessionFactory sessionFactory;
    private final QuoteRepository quoteRepository;
    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public QuoteService(SessionFactory sessionFactory,
                        QuoteRepository quoteRepository, JdbcTemplate jdbcTemplate) {
        this.sessionFactory = sessionFactory;
        this.quoteRepository = quoteRepository;
        this.jdbcTemplate = jdbcTemplate;
    }

    /*public List<Quote> findQuoteByPerson(Person person) {
        Optional<List<Quote>> quoteList = quoteRepository.findAllByPerson(person);
        if (quoteList.isPresent()) {
            return quoteList.get();
        } else {
            return Optional.empty();
        }
    }*/



    public List<Quote> findQuoteByPerson(Person person) {
        return quoteRepository.findAllByPerson(person);
    }

    public List<Quote> findTop10QuotesByRating(Person person) {

        Session session = sessionFactory.getCurrentSession();

        return session.createQuery(
                "select q from Quote q order by q.rating desc", Quote.class)
                .setMaxResults(10)
                .getResultList();
    }

    public List<Quote> findBottom10QuotesByRating(Person person) {

        Session session = sessionFactory.getCurrentSession();

        return session.createQuery(
                "select q from Quote q order by q.rating desc", Quote.class)
                .setMaxResults(10)
                .getResultList();
    }
}
