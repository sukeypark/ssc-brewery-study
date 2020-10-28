package com.hspark.brewery.security.listener;

import org.springframework.context.event.EventListener;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.authentication.event.AuthenticationSuccessEvent;
import org.springframework.security.web.authentication.WebAuthenticationDetails;
import org.springframework.stereotype.Component;

import com.hspark.brewery.domain.security.LoginSuccess;
import com.hspark.brewery.domain.security.User;
import com.hspark.brewery.repositories.security.LoginSuccessRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RequiredArgsConstructor
@Slf4j
@Component
public class AuthenticationSuccessListener {
	
	private final LoginSuccessRepository loginSuccessRepository;
	
	@EventListener
	public void listen(AuthenticationSuccessEvent event) {
		log.debug("User Logged In OK");
		
		if (event.getSource() instanceof UsernamePasswordAuthenticationToken) {
			LoginSuccess.LoginSuccessBuilder builder = LoginSuccess.builder();
			UsernamePasswordAuthenticationToken token
					= (UsernamePasswordAuthenticationToken) event.getSource();
			
			if (token.getPrincipal() instanceof User) {
				User user = (User) token.getPrincipal();
				builder.user(user);
				
				log.debug("User name logged in " + user.getUsername());
			}
			
			if (token.getDetails() instanceof WebAuthenticationDetails) {
				WebAuthenticationDetails details = (WebAuthenticationDetails) token.getDetails();
				builder.sourceIp(details.getRemoteAddress());
				
				log.debug("Source IP: " + details.getRemoteAddress());
			}
			
			LoginSuccess loginSuccess = loginSuccessRepository.save(builder.build());
			
			log.debug("Login Success saved. Id: " + loginSuccess.getId());
		}
	}

}
