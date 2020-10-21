package com.hspark.brewery.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
//import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.hspark.brewery.security.CustomPasswordEncoderFactories;

@Configuration
public class SecurityConfig extends WebSecurityConfigurerAdapter {
	
//	@Bean
//	PasswordEncoder passwordEncoder() {
//		return PasswordEncoderFactories.createDelegatingPasswordEncoder();
//	}
	
	@Bean
	PasswordEncoder passwordEncoder() {
		return CustomPasswordEncoderFactories.createDelegatingPasswordEncoder();
	}
	
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
				.withUser("admin_md5")
				.password("{MD5}5f4dcc3b5aa765d61d8327deb882cf99")
				.roles("ADMIN")
				.and()
				.withUser("user_ldap")
				.password("{ldap}{SSHA}+llyeWsvbnbK4gAwPtnOH9CEgXG9iJpDTMxDYg==")
				.roles("USER")
				.and()
				.withUser("user_sha256")
				.password("{sha256}a4118abd76a7ff9150fe7dd5fc0092445032cce368eca41ce7bfa8de33ac0815886d28276b5c34da")
				.roles("USER")
				.and()
				.withUser("user_bcrypt")
				.password("{bcrypt}$2a$10$9htW1cH6gbtgz/pK5hvNwOYAbfDv4Rbs8wrvfHCTbBT.OZpB/cKy6")
				.roles("USER")
				.and()
				.withUser("user_bcrypt15")
				.password("{bcrypt15}$2a$15$WDPinCqETpFV6mdDYsEnYu9mVUaDdVEhcqm6nQ3DgZ369lguCWxxi")
				.roles("USER")
				;

	
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
