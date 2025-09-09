package com.example.commerceapi;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class RootController {

    @GetMapping({"/", "/home", "/home/"})
    public String home() {
        return "Welcome to the Commerce API";
    }
}