package com.example.kameleontrialtask2.controller.main;

import com.example.kameleontrialtask2.controller.MainController;
import com.example.kameleontrialtask2.entity.Person;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;

@Controller
public class ToMainPageOverloading {

    private final MainController mainController;

    @Autowired
    public ToMainPageOverloading(MainController mainController) {
        this.mainController = mainController;
    }


    public ModelAndView toMainPage(HttpServletRequest request,
                                   ModelAndView modelAndView) {
        return mainController.toMainPage
                (request, new Person(), false, modelAndView);
    }

}
