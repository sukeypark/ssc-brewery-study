package com.hspark.brewery.security.filter;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.util.matcher.RequestMatcher;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class RestHeaderAuthFilter extends AbstractAuthFilter {
	
	public RestHeaderAuthFilter(RequestMatcher requiresAuthenticationRequestMatcher) {
		super(requiresAuthenticationRequestMatcher);
	}

	@Override
	public void doFilter(ServletRequest req, ServletResponse res, FilterChain chain)
			throws IOException, ServletException {

		HttpServletRequest request = (HttpServletRequest) req;
		HttpServletResponse response = (HttpServletResponse) res;

		if (log.isDebugEnabled()) {
			log.debug("Request is to process authentication");
		}
		
		try {
			Authentication authResult = attemptAuthentication(request, response);
			
			if (authResult != null) {
				successfulAuthentication(request, response, chain, authResult);				
			} else {
				chain.doFilter(request, response);
			}	
		} catch(AuthenticationException e) {
			log.error("AuthenticationFailed", e);
			unsuccessfulAuthentication(request, response, e);
		}

	}

	@Override
	protected String getUsername(HttpServletRequest request) {
		return request.getHeader("Api-Key");
	}

	@Override
	protected String getPassword(HttpServletRequest request) {
		return request.getHeader("Api-Secret");
	}

}
