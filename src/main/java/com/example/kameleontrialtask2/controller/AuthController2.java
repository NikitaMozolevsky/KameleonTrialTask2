package com.example.kameleontrialtask2.controller;

import com.example.kameleontrialtask2.entity.Person;
import com.example.kameleontrialtask2.services.PersonService;
import com.example.kameleontrialtask2.util.PersonValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.validation.Valid;

@Controller
@RequestMapping("/auth")
public class AuthController2 {

    private final PersonValidator personValidator;
    private final PersonService personService;

    @Autowired
    public AuthController2(PersonValidator personValidator, PersonService personService) {
        this.personValidator = personValidator;
        this.personService = personService;
    }

    @GetMapping("/login")
    public String loginPage(@ModelAttribute(name = "user") Person person) {
        return "login";
    }

    @GetMapping("/registration")
    public String registrationPage(@ModelAttribute("person") Person person) {

        return "registration";
    }

    @PostMapping("/registration")
    public String performRegistration(@ModelAttribute("person") @Valid Person person,
                                      //an error is placed here
                                      BindingResult bindingResult) {
        personValidator.validate(person, bindingResult);

        if (bindingResult.hasErrors()) {
            return "registration";
        }

        personService.register(person);

        return "redirect:/auth/login";
    }

}
