package com.hspark.brewery.security.listener;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.List;

import org.springframework.context.event.EventListener;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.authentication.event.AuthenticationFailureBadCredentialsEvent;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.web.authentication.WebAuthenticationDetails;
import org.springframework.stereotype.Component;

import com.hspark.brewery.domain.security.LoginFailure;
import com.hspark.brewery.domain.security.User;
import com.hspark.brewery.repositories.security.LoginFailureRepository;
import com.hspark.brewery.repositories.security.UserRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RequiredArgsConstructor
@Slf4j
@Component
public class AuthenticationFailureListener {
	
	private final LoginFailureRepository loginFailureRepository;
	private final UserRepository userRepository;
	
	@EventListener
	public void listen(AuthenticationFailureBadCredentialsEvent event) {
		log.debug("User Logged In Failed");
		
		if (event.getSource() instanceof UsernamePasswordAuthenticationToken) {
			LoginFailure.LoginFailureBuilder builder = LoginFailure.builder();
			
			UsernamePasswordAuthenticationToken token
					= (UsernamePasswordAuthenticationToken) event.getSource();
			
			if (token.getPrincipal() instanceof String) {
				String username = (String) token.getPrincipal();
				builder.username(username);
				
				try {
					userRepository.findByUsername(username).ifPresent(builder::user);
				} catch(UsernameNotFoundException e) {
					log.debug(e.getMessage());
				}
				
				log.debug("Attempted Username: " + username);				
			}
			
			if (token.getDetails() instanceof WebAuthenticationDetails) {
				WebAuthenticationDetails details = (WebAuthenticationDetails) token.getDetails();
				builder.sourceIp(details.getRemoteAddress());
				
				log.debug("Source IP: " + details.getRemoteAddress());
			}
			
			LoginFailure loginFailure = loginFailureRepository.save(builder.build());
			
			log.debug("Login Success saved. Id: " + loginFailure.getId());
			
			if (loginFailure.getUser() != null) {
				lockUserAccount(loginFailure.getUser());
			}
		}
	}

	private void lockUserAccount(User user) {
		List<LoginFailure> failures = loginFailureRepository.findAllByUserAndCreatedDateIsAfter(user,
				Timestamp.valueOf(LocalDateTime.now().minusDays(1)));
		
		if(failures.size() > 3) {
			log.debug("Locking User Account...");
			user.setAccountNonLocked(false);
			userRepository.save(user);
		}
	}

}
