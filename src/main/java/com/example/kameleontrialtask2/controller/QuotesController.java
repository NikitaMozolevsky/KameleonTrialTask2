package com.example.kameleontrialtask2.controller;

import com.example.kameleontrialtask2.constant.HtmlConstant;
import com.example.kameleontrialtask2.entity.Person;
import com.example.kameleontrialtask2.entity.Quote;
import com.example.kameleontrialtask2.services.QuoteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.List;

import static com.example.kameleontrialtask2.constant.HtmlConstant.CREATE_QUOTE;

@Controller
public class QuotesController {

    private final QuoteService quoteService;

    @Autowired
    public QuotesController(QuoteService quoteService) {
        this.quoteService = quoteService;
    }

    @GetMapping("/top-10")
    public final String getTop10Quotes(@ModelAttribute List<Quote> quotes) {



        return "main";
    }

    @GetMapping("/create-quote-page")
    public String createQuotePage(@ModelAttribute("user") Person person,
                                  @ModelAttribute("quote") Quote quote) {
        return CREATE_QUOTE;
    }

    @PostMapping("/create-quote")
    public String createQuote(@ModelAttribute("quote") Quote quote) {
        quoteService.createQuote(quote);
        return HtmlConstant.MAIN;
    }
}
