package com.hspark.brewery.bootstrap;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import javax.transaction.Transactional;

import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import com.hspark.brewery.domain.security.Authority;
import com.hspark.brewery.domain.security.Role;
import com.hspark.brewery.domain.security.User;
import com.hspark.brewery.repositories.security.AuthorityRepository;
import com.hspark.brewery.repositories.security.RoleRepository;
import com.hspark.brewery.repositories.security.UserRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
@Component
public class UserDataLoader implements CommandLineRunner {
	
	private final UserRepository userRepository;
	private final RoleRepository roleRepository;
	private final AuthorityRepository authorityRepository;
	private final PasswordEncoder passwordEncoder;
	
	private void loadSecurityData() {
		
		// beer auths
		Authority createBeer = authorityRepository.save(Authority.builder().permission("beer.create").build());
		Authority updateBeer = authorityRepository.save(Authority.builder().permission("beer.update").build());
		Authority readBeer = authorityRepository.save(Authority.builder().permission("beer.read").build());
		Authority deleteBeer = authorityRepository.save(Authority.builder().permission("beer.delete").build());
		
		// customer auths
		Authority createCustomer = authorityRepository.save(Authority.builder().permission("customer.create").build());
		Authority updateCustomer = authorityRepository.save(Authority.builder().permission("customer.update").build());
		Authority readCustomer = authorityRepository.save(Authority.builder().permission("customer.read").build());
		Authority deleteCustomer = authorityRepository.save(Authority.builder().permission("customer.delete").build());
		
		// brewery auths
		Authority createBrewery = authorityRepository.save(Authority.builder().permission("brewery.create").build());
		Authority updateBrewery = authorityRepository.save(Authority.builder().permission("brewery.update").build());
		Authority readBrewery = authorityRepository.save(Authority.builder().permission("brewery.read").build());
		Authority deleteBrewery = authorityRepository.save(Authority.builder().permission("brewery.delete").build());
		
		// beer order
		Authority createOrder = authorityRepository.save(Authority.builder().permission("order.create").build());
		Authority updateOrder = authorityRepository.save(Authority.builder().permission("order.update").build());
		Authority readOrder = authorityRepository.save(Authority.builder().permission("order.read").build());
		Authority deleteOrder = authorityRepository.save(Authority.builder().permission("order.delete").build());
		
		// customer beer order
		Authority createOrderCustomer = authorityRepository.save(Authority.builder().permission("customer.order.create").build());
		Authority updateOrderCustomer = authorityRepository.save(Authority.builder().permission("customer.order.update").build());
		Authority readOrderCustomer = authorityRepository.save(Authority.builder().permission("customer.order.read").build());
		Authority deleteOrderCustomer = authorityRepository.save(Authority.builder().permission("customer.order.delete").build());
		
		
		Role adminRole = roleRepository.save(Role.builder().name("ADMIN").build());
		Role customerRole = roleRepository.save(Role.builder().name("CUSTOMER").build());
		Role userRole = roleRepository.save(Role.builder().name("USER").build());
		
		adminRole.setAuthorities(new HashSet<>(Set.of(
				createBeer, updateBeer, readBeer, deleteBeer,
				createCustomer, updateCustomer, readCustomer, deleteCustomer,
				createBrewery, updateBrewery, readBrewery, deleteBrewery,
				createOrder, updateOrder, readOrder, deleteOrder)));
		
		customerRole.setAuthorities(new HashSet<>(Set.of(
				readBeer, readCustomer, readBrewery,
				createOrderCustomer, updateOrderCustomer, readOrderCustomer, deleteOrderCustomer)));
		 
		userRole.setAuthorities(new HashSet<>(Set.of(readBeer)));

		roleRepository.saveAll(Arrays.asList(adminRole, customerRole, userRole));
		
		userRepository.save(
				User.builder()
				.username("admin")
				.password(passwordEncoder.encode("password"))
				.role(adminRole)
				.build()					
				);
		
		userRepository.save(
				User.builder()
				.username("customer")
				.password(passwordEncoder.encode("password"))
				.role(customerRole)
				.build()
				);
		
		userRepository.save(
				User.builder()
				.username("user")
				.password(passwordEncoder.encode("password"))
				.role(userRole)
				.build()	
				
				);
		
		log.debug("Users Loaded: " + userRepository.count());

	}
	
	@Transactional
	@Override
	public void run(String... args) throws Exception {
		if (authorityRepository.count() == 0) {
			loadSecurityData();
		}
	}

}
