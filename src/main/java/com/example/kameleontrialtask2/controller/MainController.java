package com.example.kameleontrialtask2.controller;

import com.example.kameleontrialtask2.entity.Person;
import com.example.kameleontrialtask2.services.PersonService;
import com.example.kameleontrialtask2.util.PersonValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.validation.Valid;

@Controller
@RequestMapping
public class MainController {

    public static final String GUEST_HTML = "guest";
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
        return GUEST_HTML;
    }

    @RequestMapping("/guest_bad_credentials")
    public String loginError(Model model, @ModelAttribute (name = "user") Person person) {
        model.addAttribute("loginError", true);
        return GUEST_HTML;
    }

    @GetMapping("/authorized")
    public String toAuthorizedPage() {
        return "authorized";
    }

    @GetMapping("/registration_page")
    public String registrationPage(@ModelAttribute("user") Person person, Model model) {
        return "registration";
    }

    @PostMapping("/registration")
    public String registration(@ModelAttribute("user") @Valid Person person,
                               Model model,
                               BindingResult bindingResult) {

        personValidator.validate(person, bindingResult);

        if (bindingResult.hasErrors()) {
            return "/registration";
        }

        personService.register(person);
        model.addAttribute("registered_msg", REGISTERED_MSG_JS);
        return GUEST_HTML;
    }

}
