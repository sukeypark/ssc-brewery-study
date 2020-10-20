package com.hspark.brewery.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;

@Configuration
public class SecurityConfig extends WebSecurityConfigurerAdapter {
	
	@Override
	protected void configure(HttpSecurity http) throws Exception {
		http
			.authorizeRequests(authorize -> {
				authorize.antMatchers("/", "/webjars/**", "/login", "/resources/**").permitAll();
				authorize.antMatchers(HttpMethod.GET, "/api/v1/beer").permitAll();
				authorize.mvcMatchers(HttpMethod.GET, "/api/v1/beerUpc/{upc}").permitAll();
			})
			.authorizeRequests()
				.anyRequest().authenticated()
				.and()
			.formLogin().and()
			.httpBasic();
	}
	
	@Override
	protected void configure(AuthenticationManagerBuilder auth) throws Exception {
		auth.inMemoryAuthentication()
				.withUser("spring")
				.password("{noop}guru")
				.roles("ADMIN")
				.and()
				.withUser("user")
				.password("{noop}guru")
				.roles("USER");

	
//	@Override
//	protected UserDetailsService userDetailsService() {
//		UserDetails admin = User.withDefaultPasswordEncoder()
//				.username("spring")
//				.password("guru")
//				.roles("ADMIN")
//				.build();
//		
//		UserDetails user = User.withDefaultPasswordEncoder()
//				.username("user")
//				.password("guru")
//				.roles("USER")
//				.build();
//		
//		return new InMemoryUserDetailsManager(admin, user);
	}
	
}
