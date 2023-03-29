package com.example.kameleontrialtask2.controller.main;

import com.example.kameleontrialtask2.controller.MainController;
import com.example.kameleontrialtask2.entity.Person;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;

@Controller
public class ToMainPageOverloading {

    private final MainController mainController;

    @Autowired
    public ToMainPageOverloading(MainController mainController) {
        this.mainController = mainController;
    }

    public String toMainPage(Model model) {
        return mainController.toMainPage
                (new Person(), false, model);
    }

}
