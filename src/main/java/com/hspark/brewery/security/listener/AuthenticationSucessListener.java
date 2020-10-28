package com.hspark.brewery.security.listener;

import org.springframework.context.event.EventListener;
import org.springframework.security.authentication.event.AuthenticationSuccessEvent;
import org.springframework.stereotype.Component;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class AuthenticationSucessListener {
	
	@EventListener
	public void listen(AuthenticationSuccessEvent event) {
		log.debug("User Logged In OK");
	}

}
