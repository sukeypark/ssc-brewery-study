package com.hspark.brewery.security.filter;

import javax.servlet.http.HttpServletRequest;

import org.springframework.security.web.util.matcher.RequestMatcher;

public class UrlParamAuthFilter extends AbstractAuthFilter {

	public UrlParamAuthFilter(RequestMatcher requiresAuthenticationRequestMatcher) {
		super(requiresAuthenticationRequestMatcher);
	}

	@Override
	protected String getPassword(HttpServletRequest request) {
		return request.getParameter("apiKey");
	}

	@Override
	protected String getUsername(HttpServletRequest request) {
		return request.getParameter("apiSecret");
	}

}
