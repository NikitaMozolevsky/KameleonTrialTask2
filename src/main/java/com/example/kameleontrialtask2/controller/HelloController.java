package com.example.kameleontrialtask2.controller;

import com.example.kameleontrialtask2.security.PersonDetails;
import com.example.kameleontrialtask2.services.AdminService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/hello")
public class HelloController {

    private final AdminService adminService;

    @Autowired
    public HelloController(AdminService adminService) {
        this.adminService = adminService;
    }

    @GetMapping("/hello")
    public String hello() {
        return "hello";
    }

    @GetMapping("show_user")
    public String showUserInfo() {
        //достается объект который был проложен после успешной аутентификации
        //был положен в сессию, SpringSecurity положил его в контекст пользователя
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        PersonDetails personDetails = (PersonDetails) authentication.getPrincipal();
        //вывод данных пользователя
        System.out.println(personDetails.getPerson().toString());

        return "hello";
    }

    @GetMapping("/admin")
    public String adminPage() {
        //
        adminService.doAdminStaff();
        return "admin";
    }

}
