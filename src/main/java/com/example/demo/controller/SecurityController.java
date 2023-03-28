package com.example.demo.controller;

import javax.servlet.http.HttpSession;

import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Controller
public class SecurityController {

	@GetMapping("/login")
	public String login() {
		return "login";
	}

	@GetMapping("/")
	public String success(Authentication loginUser, HttpSession session) {
		session.setAttribute("username", loginUser.getName());
		session.setAttribute("authority", loginUser.getAuthorities());

		return "user";
	}
}
