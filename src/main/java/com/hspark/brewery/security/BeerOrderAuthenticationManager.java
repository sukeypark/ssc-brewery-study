package com.hspark.brewery.security;

import java.util.UUID;

import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import com.hspark.brewery.domain.security.User;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class BeerOrderAuthenticationManager {
	
	public boolean customerIdMatches(Authentication authentication, UUID customerId) {
		User authenticatedUser = (User) authentication.getPrincipal();
		
		log.debug("Auth User Costomer Id: " + authenticatedUser.getCustomer().getId() + " customer Id: " + customerId);
		
		return authenticatedUser.getCustomer().getId().equals(customerId);
	}
	
}
