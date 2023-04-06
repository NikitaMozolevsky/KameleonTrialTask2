package com.example.kameleontrialtask2.controller;

import com.example.kameleontrialtask2.exceptions.ServiceException;
import com.example.kameleontrialtask2.entity.Person;
import com.example.kameleontrialtask2.entity.Quote;
import com.example.kameleontrialtask2.services.PersonService;
import com.example.kameleontrialtask2.services.PrincipalService;
import com.example.kameleontrialtask2.services.QuoteService;
import com.example.kameleontrialtask2.util.PersonValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.validation.Valid;
import java.text.SimpleDateFormat;

import static com.example.kameleontrialtask2.constant.HtmlConstant.*;

@Controller
@RequestMapping
public class MainController {

    public static final String REGISTERED_MSG_JS =
            "<script>window.alert('You were registered successful')</script>";

    private final PersonValidator personValidator;
    private final PersonService personService;
    private final QuoteService quoteService;
    private final PrincipalService principalService;

    @Autowired
    public MainController(PersonValidator personValidator,
                          PersonService personService, QuoteService quoteService, PrincipalService principalService) {
        this.personValidator = personValidator;
        this.personService = personService;
        this.quoteService = quoteService;
        this.principalService = principalService;
    }

    @GetMapping("/guest")
    public ModelAndView toMainPage(@ModelAttribute (name = "user") Person person,
                             @RequestParam (value = "badCredentials", required = false)
                                      boolean badCredentials,
                             ModelAndView modelAndView) {

        if (badCredentials) {

            modelAndView.addObject("loginError", true);
        }

        boolean quotesAreCreated;

        try {
            Quote quote = quoteService.findRandomQuote();

            quotesAreCreated = true;
            modelAndView.addObject("randomQuoteText", quote.getQuoteText());
            modelAndView.addObject("randomQuoteOwner", quote.getPerson().getUsername());
        } catch (ServiceException e) {
            quotesAreCreated = false;
        }
        modelAndView.addObject("quotesAreCreated", quotesAreCreated);
        modelAndView.addObject("quotesAreNotCreatedText", "");
        modelAndView.setViewName(MAIN);

        return modelAndView;
    }

    /*@RequestMapping("/bad-credentials")
    public String loginError(@ModelAttribute (name = "user") Person person,
                             Model model) {
        model.addAttribute("loginError", true);
        return MAIN;
    }*/

    @RequestMapping("/bad-credentials")
    public ModelAndView loginErrorSecond(@ModelAttribute (name = "user") Person person,
                                   ModelAndView modelAndView) {

        return toMainPage(person, true, modelAndView);
    }

    @GetMapping("/registration-page")
    public String registrationPage(@ModelAttribute("user") Person person) {
        return REGISTRATION;
    }

    @PostMapping("/registration")
    public ModelAndView registration(@ModelAttribute("user") @Valid Person person,
                               ModelAndView modelAndView,
                               BindingResult bindingResult) {

        personValidator.validate(person, bindingResult);

        if (bindingResult.hasErrors()) {
            modelAndView.setViewName(REGISTRATION);
            return modelAndView;
        }

        personService.register(person);
        modelAndView.addObject("registered_msg", REGISTERED_MSG_JS);
        return toMainPage(person, false, modelAndView);
    }

    @GetMapping("/profile-page")
    public ModelAndView profile() {

        ModelAndView modelAndView = new ModelAndView();
        Person person = principalService.getPerson();

        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd MMM yyyy");
        String date = simpleDateFormat.format(person.getDateOfCreation());

        modelAndView.addObject("user", person);
        modelAndView.addObject("date_of_creation", date);
        modelAndView.setViewName(PROFILE);

        return modelAndView;

        /*//достается объект который был проложен после успешной аутентификации
        //был положен в сессию, SpringSecurity положил его в контекст пользователя
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        PersonDetails personDetails = (PersonDetails) authentication.getPrincipal();
        //вывод данных пользователя
        System.out.println(personDetails.getPerson().toString());*/
    }

}
