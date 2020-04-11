package com.mealjournal.app.controller;

import org.springframework.stereotype.Controller;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Controller
@RequestMapping("/")
public class DashboardController {

    @GetMapping("/dashboard")
    public String index() {
        return "index.html";
    }

    @GetMapping("/error")
    public String error() {
        return "error-page";
        // todo: a basic error page
    }
}
