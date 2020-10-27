package com.hspark.brewery.security.perms;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

import org.springframework.security.access.prepost.PreAuthorize;

@Retention(RetentionPolicy.RUNTIME)
@PreAuthorize("hasAuthority('order.create') OR "
		+ "hasAuthority('customer.order.create') AND "
		+ "@beerOrderAuthenticationManager.customerIdMatches(authentication, #customerId)")
public @interface OrderCreatePermission {
	
}
