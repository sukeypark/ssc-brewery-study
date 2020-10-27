package com.hspark.brewery.security;

import javax.transaction.Transactional;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.hspark.brewery.repositories.security.UserRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
@Service
public class JpaUserDetailsService implements UserDetailsService {
	
	private final UserRepository userRepository;
	
	@Transactional
	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		
		log.debug("Getting User info via JPA");
		
		return userRepository.findByUsername(username).orElseThrow(() -> {
			return new UsernameNotFoundException("user name: " + username + " not found.");
		});
	}
}
