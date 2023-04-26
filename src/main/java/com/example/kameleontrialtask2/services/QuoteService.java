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

import java.time.LocalDateTime;
import java.util.*;

import static com.example.kameleontrialtask2.constant.TextConstant.MINUS;
import static com.example.kameleontrialtask2.constant.TextConstant.PLUS;
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

        Random random = new Random();
        /*Stream<Quote> quoteList = quoteRepository.findAll().stream()
                .sorted(Comparator.comparingInt(o -> random.nextInt())).findFirst().;*/

        /*Optional<Quote> quote = quoteRepository.findAll().stream()
                .min(Comparator.comparingInt(o -> random.nextInt()));*/

        Optional<Quote> quote = quoteRepository.findAll().stream()
                .min((a, b) -> random.nextInt(3) - 1);

        return quote.orElseThrow(ServiceException::new);

        /*Optional<Quote> randomQuote = quoteRepository.findAll().stream().findAny();
        return randomQuote.orElseThrow(ServiceException::new);*/
    }

    public List<Quote> findQuoteByPerson(Person person) {
        return quoteRepository.findAllByPerson(person);
    }

    public List<Quote> findTop10QuotesByRating() {

        try(Session session = sessionFactory.openSession()) {

        return session.createQuery(
                "select q from Quote q order by q.rating desc", Quote.class)
                .setMaxResults(10)
                .getResultList();
        }
    }

    public List<Quote> findFlop10QuotesByRating() {

        try(Session session = sessionFactory.openSession()) {

            return session.createQuery(
                            "select q from Quote q order by q.rating asc", Quote.class)
                    .setMaxResults(10)
                    .getResultList();
        }
    }

    @Transactional
    public void createQuote(Quote quote) {

        quote.setPerson(principalService.getPerson()); //
        quote.setRating(ZERO);
        quote.setCreationDate(LocalDateTime.now());

        quoteRepository.save(quote);
    }

    public Quote findLastCreatedQuote() {
        return quoteRepository.findLastCreatedQuote();
    }

    @Transactional
    public void updateQuoteRating(String quoteId, String attribute) {

        Optional<Quote> optionalQuote = quoteRepository.findById(Integer.valueOf(quoteId));

        if (optionalQuote.isPresent()) {

            Quote quote = optionalQuote.get();

            if (attribute.equals(PLUS)) {

                quote.setRating(quote.getRating() + 1);
                quoteRepository.save(quote);
                
            } else if (attribute.equals(MINUS)) {

                quote.setRating(quote.getRating() - 1);
                quoteRepository.save(quote);
            }
        }
    }

    /*@Transactional
    public void increaseRating(Quote quote) {
        quote.setRating(quote.getRating() + 1);
        quoteRepository.save(quote);
    }

    @Transactional
    public void reduceRating(Quote quote) {
        quote.setRating(quote.getRating() - 1);
        quoteRepository.save(quote);
    }*/

    public Quote findQuoteById(int quoteId) {
        return quoteRepository.findQuoteById(quoteId);
    }
}
