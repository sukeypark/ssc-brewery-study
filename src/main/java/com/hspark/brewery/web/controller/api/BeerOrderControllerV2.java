package com.hspark.brewery.web.controller.api;

import java.util.UUID;

import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import com.hspark.brewery.domain.security.User;
import com.hspark.brewery.security.perms.BeerOrderReadPermissionV2;
import com.hspark.brewery.services.BeerOrderService;
import com.hspark.brewery.web.model.BeerOrderDto;
import com.hspark.brewery.web.model.BeerOrderPagedList;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v2/orders/")
public class BeerOrderControllerV2 {
	
	private static final Integer DEFAULT_PAGE_NUMBER = 0;
	private static final Integer DEFAULT_PAGE_SIZE = 25;
	
	private final BeerOrderService beerOrderService;
	
	@BeerOrderReadPermissionV2
	@GetMapping
	public BeerOrderPagedList listOrders(@AuthenticationPrincipal User user, 
										 @RequestParam(value="pageNumber", required = false) Integer pageNumber, 
										 @RequestParam(value="pageSize", required = false) Integer pageSize) {

		if (pageNumber == null || pageNumber < 0) {
			pageNumber = DEFAULT_PAGE_NUMBER;
		}
		
		if (pageSize == null || pageSize < 0) {
			pageSize = DEFAULT_PAGE_SIZE;
		}
		
		if (user.getCustomer() != null) {
			return beerOrderService.listOrders(user.getCustomer().getId(), PageRequest.of(pageNumber, pageSize));			
		} else {
			return beerOrderService.listOrders(PageRequest.of(pageNumber, pageSize));
		}
		
	}

	@BeerOrderReadPermissionV2
	@GetMapping("{orderId}")
	public BeerOrderDto getOrder(@PathVariable("orderId") UUID orderId) {
		BeerOrderDto beerOrderDto = beerOrderService.getOrderById(orderId);
		
		if (beerOrderDto == null) {
			throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Order Not Found.");
		}
		
		log.debug("Found Order: " + beerOrderDto);
		
		return beerOrderDto;
	}

}
