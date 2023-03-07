package com.example.kameleontrialtask2.controller;

import com.example.kameleontrialtask2.entity.Quote;
import com.example.kameleontrialtask2.repository.QuoteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jmx.export.annotation.ManagedAttribute;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;

import java.util.List;

@Controller
public class QuotesController {

    public final QuoteRepository quoteRepository;

    @Autowired
    public QuotesController(QuoteRepository quoteRepository) {
        this.quoteRepository = quoteRepository;
    }

    @GetMapping("/top-10")
    public final String getTop10Quotes(@ModelAttribute List<Quote> quotes) {



        return "main";
    }
}
