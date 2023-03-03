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
        return "guest";
    }

    @GetMapping("/authorized")
    public String toAuthorizedPage() {
        return "authorized";
    }

    @GetMapping("/registration")
    public String registrationPage(@ModelAttribute Person person) {
        return "registration";
    }

    @PostMapping("/registration")
    public String registration(@ModelAttribute("user") @Valid Person person,
                               Model model,
                               BindingResult bindingResult) {

        personValidator.validate(person, bindingResult);

        if (bindingResult.hasErrors()) {
            model.addAttribute("invalid_user", "");
            return "registration";
        }

        personService.register(person);
        model.addAttribute("registered_msg", REGISTERED_MSG_JS);
        return "guest";
    }

}
