package com.example.kameleontrialtask2.controller;

import com.example.kameleontrialtask2.entity.Person;
import com.example.kameleontrialtask2.security.PersonDetails;
import com.example.kameleontrialtask2.services.PersonService;
import com.example.kameleontrialtask2.util.PersonValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

@Controller
@RequestMapping
public class MainController {

    public static final String MAIN = "main";
    public static final String PROFILE = "profile";
    public static final String REGISTRATION = "registration";

    public static final String REGISTERED_MSG_JS =
            "<script>window.alert('You were registered successful')</script>";

    private final PersonValidator personValidator;
    private final PersonService personService;

    @Autowired
    public MainController(PersonValidator personValidator, PersonService personService) {
        this.personValidator = personValidator;
        this.personService = personService;
    }

    @GetMapping("/guest")
    public String toGuestPage(@ModelAttribute (name = "user") Person person) {
        return MAIN;
    }

    @RequestMapping("/guest-bad-credentials")
    public String loginError(Model model, @ModelAttribute (name = "user") Person person) {
        model.addAttribute("loginError", true);
        return MAIN;
    }

    @GetMapping("/authorized")
    public String toAuthorizedPage() {
        return MAIN;
    }

    @GetMapping("/registration-page")
    public String registrationPage(@ModelAttribute("user") Person person) {
        return REGISTRATION;
    }

    @PostMapping("/registration")
    public String registration(@ModelAttribute("user") @Valid Person person,
                               Model model,
                               BindingResult bindingResult) {

        personValidator.validate(person, bindingResult);

        if (bindingResult.hasErrors()) {
            return REGISTRATION;
        }

        personService.register(person);
        model.addAttribute("registered_msg", REGISTERED_MSG_JS);
        return MAIN;
    }

    @GetMapping("/profile-page")
    public String profile(HttpServletRequest request) {
        //достается объект который был проложен после успешной аутентификации
        //был положен в сессию, SpringSecurity положил его в контекст пользователя
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        PersonDetails personDetails = (PersonDetails) authentication.getPrincipal();
        //вывод данных пользователя
        System.out.println(personDetails.getPerson().toString());

        Person person = personDetails.getPerson();

        request.setAttribute("user", person);

        return PROFILE;
    }

}
