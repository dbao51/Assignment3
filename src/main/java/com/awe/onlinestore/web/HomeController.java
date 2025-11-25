// src/main/java/com/awe/onlinestore/web/HomeController.java
package com.awe.onlinestore.web;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HomeController {
    
    @GetMapping("/")
    public String home() {
        return "index";
    }
}