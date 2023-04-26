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
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.validation.Valid;
import java.text.SimpleDateFormat;
import java.util.Optional;

import static com.example.kameleontrialtask2.constant.EntityConstant.MAIN_QUOTE;
import static com.example.kameleontrialtask2.constant.EntityConstant.MAIN_QUOTE_TYPE;
import static com.example.kameleontrialtask2.constant.HtmlConstant.*;
import static com.example.kameleontrialtask2.constant.TextConstant.RANDOM_QUOTE_TXT;

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
    public ModelAndView toMainPage(HttpServletRequest request, @ModelAttribute (name = "user") Person person,
                             @RequestParam (value = "badCredentials", required = false)
                                      boolean badCredentials,
                             ModelAndView modelAndView) {

        if (badCredentials) {
            modelAndView.addObject("loginError", true);
        }

        boolean quotesAreCreated;
        Quote quote;

        //проверка есть ли в модели уже main_qute, или, например, последняя созданная фраза
        /*Optional<Quote> mainQuote = Optional.ofNullable((Quote) modelAndView.getModel().get("mainQuote"));*/
        Optional<Object> mainQuoteSession = Optional.ofNullable(request.getSession().getAttribute(MAIN_QUOTE));

        try {
            if(mainQuoteSession.isEmpty()) {
                quote = quoteService.findRandomQuote();

                request.getSession().setAttribute(MAIN_QUOTE, quote);
                request.getSession().setAttribute(MAIN_QUOTE_TYPE, RANDOM_QUOTE_TXT);

                /*modelAndView.addObject("mainQuote", quote);
                modelAndView.addObject("mainQuoteType", RANDOM_QUOTE_TXT);*/

                /*modelAndView.addObject("randomQuoteText", quote.getQuoteText());
                modelAndView.addObject("randomQuoteOwner", quote.getPerson().getUsername());*/
            }

            quotesAreCreated = true;

        } catch (ServiceException e) {
            quotesAreCreated = false;
        }
        modelAndView.addObject("quotesAreCreated", quotesAreCreated);
        /*modelAndView.addObject("quotesAreNotCreatedText", "");*/
        modelAndView.addObject("user", new Person());
        modelAndView.setViewName(MAIN);

        return modelAndView;
    }

    /*@RequestMapping("/bad-credentials")
    public String loginError(@ModelAttribute (name = "user") Person person,
                             Model model) {
        model.addAttribute("loginError", true);
        return MAIN;
    }*/

    @GetMapping("/bad-credentials")
    public ModelAndView loginErrorSecond(HttpServletRequest request,
                                         @ModelAttribute (name = "user") Person person,
                                   ModelAndView modelAndView) {

        return toMainPage(request, person, true, modelAndView);
    }

    @GetMapping("/registration-page")
    public String registrationPage(@ModelAttribute("user") Person person) {
        return REGISTRATION;
    }

    @PostMapping("/registration")
    public ModelAndView registration(HttpServletRequest request,
                                     @ModelAttribute("user") @Valid Person person,
                               BindingResult bindingResult) {

        ModelAndView modelAndView = new ModelAndView();
        modelAndView.addObject("username", person.getUsername());
        personValidator.validate(person, bindingResult);

        if (bindingResult.hasErrors()) {
            modelAndView.setViewName(REGISTRATION);
            return modelAndView;
        }

        personService.register(person);
        modelAndView.addObject("registered_msg", REGISTERED_MSG_JS);
        modelAndView.addObject("user", new Person());
        return toMainPage(request, person, false, modelAndView);
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
