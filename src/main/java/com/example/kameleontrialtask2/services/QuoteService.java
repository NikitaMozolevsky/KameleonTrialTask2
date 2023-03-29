package com.example.kameleontrialtask2.services;

import com.example.kameleontrialtask2.exceptions.ServiceException;
import com.example.kameleontrialtask2.entity.Person;
import com.example.kameleontrialtask2.entity.Quote;
import com.example.kameleontrialtask2.repository.PersonRepository;
import com.example.kameleontrialtask2.repository.QuoteRepository;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;
import java.util.Optional;

import static org.hibernate.type.IntegerType.ZERO;

@Service
public class QuoteService {


    private final SessionFactory sessionFactory;
    private final QuoteRepository quoteRepository;
    private final PersonRepository personRepository;
    private final PrincipalService principalService;
    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public QuoteService(SessionFactory sessionFactory,
                        QuoteRepository quoteRepository, PersonRepository personRepository, PrincipalService principalService, JdbcTemplate jdbcTemplate) {
        this.sessionFactory = sessionFactory;
        this.quoteRepository = quoteRepository;
        this.personRepository = personRepository;
        this.principalService = principalService;
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


    public Quote findRandomQuote() throws ServiceException {
        Optional<Quote> randomQuote = quoteRepository.findAll().stream().findAny();
        return randomQuote.orElseThrow(ServiceException::new);
    }

    public List<Quote> findQuoteByPerson(Person person) {
        return quoteRepository.findAllByPerson(person);
    }

    public List<Quote> findTop10QuotesByRating() {

        Session session = sessionFactory.getCurrentSession();

        return session.createQuery(
                "select q from Quote q order by q.rating desc", Quote.class)
                .setMaxResults(10)
                .getResultList();
    }

    public List<Quote> findFlop10QuotesByRating() {

        Session session = sessionFactory.getCurrentSession();

        return session.createQuery(
                "select q from Quote q order by q.rating asc", Quote.class)
                .setMaxResults(10)
                .getResultList();
    }

    @Transactional
    public void createQuote(Quote quote) {

        quote.setPerson(principalService.getPerson()); //
        quote.setRating(ZERO);
        quote.setCreationDate(new Date());

        quoteRepository.save(quote);
    }
}
