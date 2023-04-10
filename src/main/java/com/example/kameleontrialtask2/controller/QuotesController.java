package com.example.kameleontrialtask2.controller;

import com.example.kameleontrialtask2.entity.Person;
import com.example.kameleontrialtask2.entity.Quote;
import com.example.kameleontrialtask2.services.QuoteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.ModelAndView;

import static com.example.kameleontrialtask2.constant.HtmlConstant.CREATE_QUOTE;
import static com.example.kameleontrialtask2.constant.HtmlConstant.MAIN;

@Controller
public class QuotesController {

    private final MainController mainController;
    private final QuoteService quoteService;

    @Autowired
    public QuotesController(MainController mainController, QuoteService quoteService) {
        this.mainController = mainController;
        this.quoteService = quoteService;
    }

    @GetMapping("/top-10")
    public ModelAndView getTop10Quotes(ModelAndView modelAndView) {

        modelAndView.addObject("quotes", quoteService.findTop10QuotesByRating());

        return mainController.toMainPage(new Person(), false, modelAndView);
    }

    @GetMapping("/flop-10")
    public ModelAndView getFlop10Quotes(ModelAndView modelAndView) {

        modelAndView.addObject("quotes", quoteService.findFlop10QuotesByRating());
        modelAndView.setViewName(MAIN);

        return mainController.toMainPage(new Person(), false, modelAndView);
    }

    @GetMapping("/create-quote-page")
    public String createQuotePage(@ModelAttribute("user") Person person,
                                  @ModelAttribute("quote") Quote quote) {
        return CREATE_QUOTE;
    }

    @PostMapping("/create-quote")
    public ModelAndView createQuote(@ModelAttribute("quote") Quote quote,
                            ModelAndView modelAndView) {
        quoteService.createQuote(quote);
        return mainController.toMainPage(new Person(), false, modelAndView);
    }

    @GetMapping("/last-quote")
    public ModelAndView lastQuote(ModelAndView modelAndView) {

        modelAndView.addObject("mainQuote", quoteService.findLastCreatedQuote());
        modelAndView.addObject("mainQuoteType", "Last quote");

        return mainController.toMainPage(new Person(), false, modelAndView);
    }
}
