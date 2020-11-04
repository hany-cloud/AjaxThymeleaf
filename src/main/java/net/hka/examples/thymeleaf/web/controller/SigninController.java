package net.hka.examples.thymeleaf.web.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class SigninController {

	@RequestMapping(value = "signin")
	String signin() {
		return "signin/signin";
    }
}
