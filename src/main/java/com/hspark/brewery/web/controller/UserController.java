package com.hspark.brewery.web.controller;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.hspark.brewery.domain.security.User;
import com.hspark.brewery.repositories.security.UserRepository;
import com.warrenstrange.googleauth.GoogleAuthenticator;
import com.warrenstrange.googleauth.GoogleAuthenticatorQRGenerator;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Controller
@RequiredArgsConstructor
@RequestMapping("/user")
public class UserController {
	
	private final UserRepository userRepository;
	private final GoogleAuthenticator googleAuthenticator;
	
	@GetMapping("/register2fa")
	public String register2fa(Model model) {
		
		User user = getUser();
		
		String url = GoogleAuthenticatorQRGenerator.getOtpAuthURL(
				"hspark", 
				user.getUsername(), 
				googleAuthenticator.createCredentials(user.getUsername()));
		
		log.debug("Google QR URL: " + url);
		
		model.addAttribute("googleurl", url);
		
		return "user/register2fa";
	}

	@PostMapping
	public String confirm2fa(@RequestParam Integer verifyCode) {
		
		User user = getUser();
		
		log.debug("Entered Doce is: " + verifyCode);
		
		if (googleAuthenticator.authorizeUser(user.getUsername(), verifyCode) ) {
			User savedUser = userRepository.findById(user.getId()).orElseThrow();
			savedUser.setUseGoogle2fa(true );
			userRepository.save(savedUser);
			
			return "/index";
		} else {
			// bad code
			return "user/register2fa";
		}
	}
	
	@GetMapping("/verify2fa")
	public String verify2fa() {
		return "user/verify2fa";
	}
	
	@PostMapping
	public String verifyPostOf2Fa(@RequestParam Integer verifyCode) {
		
		User user = getUser();
		
		if (googleAuthenticator.authorizeUser(user.getUsername(), verifyCode) ) {
			((User) SecurityContextHolder.getContext().getAuthentication().getCredentials()).setGoogle2faRequired(false);
			
			return "/index";
		} else {
			return "user/verify2fa";			
		}
		
	}
	
	private User getUser() {
		return (User) SecurityContextHolder.getContext().getAuthentication().getCredentials();
	}
}
