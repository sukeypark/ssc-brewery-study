package com.hspark.brewery.bootstrap;

import java.util.Arrays;

import org.springframework.boot.CommandLineRunner;
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
	
	@Override
	public void run(String... args) throws Exception {
		
		Authority admin = Authority.builder().role("ADMIN").build();
		Authority customer = Authority.builder().role("CUSTOMER").build();
		Authority user = Authority.builder().role("USER").build();
		
		authorityRepository.saveAll(Arrays.asList(new Authority[] {admin, customer, user}));
		
		if (userRepository.count() == 0) {
			userRepository.save(
				User.builder()
					.username("admin")
					.password("{bcrypt}$2a$10$qxcEYn4r1X4P8fE9ccR77.JWPIT0HOgiyudFF5uIEIFW7thCOZWCS")
					.authority(admin)
					.build()					
			);
			
			userRepository.save(
				User.builder()
					.username("customer")
					.password("{bcrypt15}$2a$15$znAAB15Sz6UdWKnfldYsvewvAmxvct/QWamLqW7sHK8HcG60D5Eu6")
					.authority(customer)
					.build()
			);
			
			userRepository.save(
				User.builder()
					.username("user")
					.password("{ldap}{SSHA}TSFVbaInuKIE/IZxKNsSqPeEvd3rb+jAYYqPVg==")
					.authority(user)
					.build()	
					
			);
			
			log.debug("Users added successfully.");
		}
	}

}
