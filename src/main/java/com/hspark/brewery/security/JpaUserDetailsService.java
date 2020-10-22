package com.hspark.brewery.security;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

import javax.transaction.Transactional;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.hspark.brewery.domain.security.Authority;
import com.hspark.brewery.domain.security.User;
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
		
		User user = userRepository.findByUsername(username).orElseThrow(() -> {
			return new UsernameNotFoundException("user name: " + username + " not found.");
		});
		
		return new org.springframework.security.core.userdetails.User(
				username, user.getPassword(), user.isEnabled(),
				user.isAccountNonExpired(), user.isCredentialsNonExpired(),
				user.isAccountNonLocked(),
				convertToSpringAuthorities(user.getAuthorities())
		);
	}

	private Collection<? extends GrantedAuthority> convertToSpringAuthorities(Set<Authority> authorities) {
		if (authorities != null && authorities.size() > 0) {
			return authorities.stream()
					.map(Authority::getRole)
					.map(SimpleGrantedAuthority::new)
					.collect(Collectors.toSet());
		} else {
			return new HashSet<>();
		}
	}

}
