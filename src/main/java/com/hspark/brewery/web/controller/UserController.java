package com.hspark.brewery.web.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.hspark.brewery.repositories.security.UserRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Controller
@RequiredArgsConstructor
@RequestMapping("/user")
public class UserController {
	
	private final UserRepository userRepository;
	
	@GetMapping("/register2fa")
	public String register2fa(Model model) {
		
		model.addAttribute("googleurl", "todo");
		
		return "user/register2fa";
	}
	
	@PostMapping("/register2fa")
	public String confirm2fa(@RequestParam Integer verifyCode) {
		// TODO
		return "index";
	}
}
