package com.hspark.brewery.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
//import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.data.repository.query.SecurityEvaluationContextExtension;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

import com.hspark.brewery.security.CustomPasswordEncoderFactories;

@EnableWebSecurity
@Configuration
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class SecurityConfig extends WebSecurityConfigurerAdapter {
	
	@Bean
	public SecurityEvaluationContextExtension securityEvaluationContextExtension() {
		return new SecurityEvaluationContextExtension();
	}
	
	@Bean
	PasswordEncoder passwordEncoder() {
		return CustomPasswordEncoderFactories.createDelegatingPasswordEncoder();
	}
	
	@Override
	protected void configure(HttpSecurity http) throws Exception {
		http
			.authorizeRequests(authorize -> {
				authorize.antMatchers("/h2-console", "/h2-console/**").permitAll(); // do not use in production
				authorize.antMatchers("/", "/webjars/**", "/login", "/resources/**").permitAll();
			})
			.authorizeRequests()
				.anyRequest().authenticated()
				.and()
			.formLogin(loginConfigurer -> {
				loginConfigurer
					.loginProcessingUrl("/login")
					.loginPage("/").permitAll()
					.successForwardUrl("/")
					.defaultSuccessUrl("/")
					.failureUrl("/?error");
			})
			.logout(logoutConfigurer -> {
				logoutConfigurer
					.logoutRequestMatcher(new AntPathRequestMatcher("/logout", "GET"))
					.logoutSuccessUrl("/?logout")
					.permitAll();
			})
			.httpBasic()
			.and().csrf().ignoringAntMatchers("/h2-console/**", "/api/**");
		
		// h2 console config
		http.headers().frameOptions().sameOrigin();
	}

}
