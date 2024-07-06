package com.scm.controllers;

import java.security.Principal;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/user")
public class UserController {

    private Logger logger = LoggerFactory.getLogger(UserController.class);

    // user dashboard page
    @RequestMapping(value = "/dashboard")
    public String userDashboard() {
        return "user/dashboard";
    }

    @RequestMapping(value = "/profile")
    public String userProfile(Principal principal) {
        String name = principal.getName();
        logger.info("User logged in: {}", name);
        return "user/profile";
    }

}
