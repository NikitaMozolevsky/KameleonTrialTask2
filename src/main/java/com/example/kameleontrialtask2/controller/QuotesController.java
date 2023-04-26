package com.example.kameleontrialtask2.controller;

import com.example.kameleontrialtask2.entity.Person;
import com.example.kameleontrialtask2.entity.Quote;
import com.example.kameleontrialtask2.exceptions.ServiceException;
import com.example.kameleontrialtask2.services.QuoteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;

import static com.example.kameleontrialtask2.constant.EntityConstant.*;
import static com.example.kameleontrialtask2.constant.EntityConstant.MAIN_QUOTE_TYPE;
import static com.example.kameleontrialtask2.constant.HtmlConstant.CREATE_QUOTE;
import static com.example.kameleontrialtask2.constant.HtmlConstant.MAIN;
import static com.example.kameleontrialtask2.constant.TextConstant.LAST_QUOTE_TXT;
import static com.example.kameleontrialtask2.constant.TextConstant.RANDOM_QUOTE_TXT;

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
    public ModelAndView getTop10Quotes(HttpServletRequest request,
                                       ModelAndView modelAndView) {

        modelAndView.addObject("quotes", quoteService.findTop10QuotesByRating());

        return mainController.toMainPage(request, new Person(), false, modelAndView);
    }

    @GetMapping("/flop-10")
    public ModelAndView getFlop10Quotes(HttpServletRequest request,
                                        ModelAndView modelAndView) {

        modelAndView.addObject("quotes", quoteService.findFlop10QuotesByRating());
        modelAndView.setViewName(MAIN);

        return mainController.toMainPage(request, new Person(), false, modelAndView);
    }

    @GetMapping("/create-quote-page")
    public String createQuotePage(@ModelAttribute("user") Person person,
                                  @ModelAttribute("quote") Quote quote) {
        return CREATE_QUOTE;
    }

    @PostMapping("/create-quote")
    public ModelAndView createQuote(HttpServletRequest request,
                                    @ModelAttribute("quote") Quote quote,
                            ModelAndView modelAndView) {
        quoteService.createQuote(quote);
        return mainController.toMainPage(request, new Person(), false, modelAndView);
    }

    @GetMapping("/last-quote")
    public ModelAndView lastQuote(HttpServletRequest request,
                                  ModelAndView modelAndView) {

        request.getSession().setAttribute(MAIN_QUOTE, quoteService.findLastCreatedQuote());
        request.getSession().setAttribute(MAIN_QUOTE_TYPE, LAST_QUOTE_TXT);

        return mainController.toMainPage(request, new Person(), false, modelAndView);
    }

    @PostMapping("/change-rating")
    public ModelAndView updateRating(HttpServletRequest request,
                                     ModelAndView modelAndView) {

        String quoteId = request.getParameter(QUOTE_ID);
        String attribute = request.getParameter(ATTRIBUTE);

        quoteService.updateQuoteRating(quoteId, attribute);

        request.getSession().setAttribute(MAIN_QUOTE,
                quoteService.findQuoteById(Integer.parseInt(quoteId)));
        request.getSession().setAttribute(MAIN_QUOTE_TYPE, RANDOM_QUOTE_TXT);

        return mainController.toMainPage(request, new Person(), false, modelAndView);

    }

    @GetMapping("/random-quote") //лениво обработано исключение!!!
    public ModelAndView getRandomQuote(HttpServletRequest request, ModelAndView modelAndView) throws ServiceException {

        request.getSession().setAttribute(MAIN_QUOTE, quoteService.findRandomQuote());
        request.getSession().setAttribute(MAIN_QUOTE_TYPE, RANDOM_QUOTE_TXT);

        return mainController.toMainPage(request, new Person(), false, modelAndView);

    }
}
