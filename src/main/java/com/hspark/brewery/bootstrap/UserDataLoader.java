package com.hspark.brewery.bootstrap;

import java.util.Arrays;

import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import com.hspark.brewery.domain.security.Authority;
import com.hspark.brewery.domain.security.User;
import com.hspark.brewery.repository.security.AuthorityRepository;
import com.hspark.brewery.repository.security.UserRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
@Component
public class UserDataLoader implements CommandLineRunner {
	
	private final UserRepository userRepository;
	private final AuthorityRepository authorityRepository;
	private final PasswordEncoder passwordEncoder;
	
	@Override
	public void run(String... args) throws Exception {
		
		Authority admin = Authority.builder().role("ROLE_ADMIN").build();
		Authority customer = Authority.builder().role("ROLE_CUSTOMER").build();
		Authority user = Authority.builder().role("ROLE_USER").build();
		
		authorityRepository.saveAll(Arrays.asList(new Authority[] {admin, customer, user}));
		
		if (userRepository.count() == 0) {
			userRepository.save(
				User.builder()
					.username("admin")
					.password(passwordEncoder.encode("password"))
					.authority(admin)
					.build()					
			);
			
			userRepository.save(
				User.builder()
					.username("customer")
					.password(passwordEncoder.encode("password"))
					.authority(customer)
					.build()
			);
			
			userRepository.save(
				User.builder()
					.username("user")
					.password(passwordEncoder.encode("password"))
					.authority(user)
					.build()	
					
			);
			
			log.debug("Users Loaded: " + userRepository.count());
		}
	}

}
