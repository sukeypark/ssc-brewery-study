package com.hspark.brewery.web.controller.api;

import java.util.UUID;

import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.hspark.brewery.security.perms.OrderCreatePermission;
import com.hspark.brewery.security.perms.OrderReadPermission;
import com.hspark.brewery.security.perms.OrderUpdatePermission;
import com.hspark.brewery.services.BeerOrderService;
import com.hspark.brewery.web.model.BeerOrderDto;
import com.hspark.brewery.web.model.BeerOrderPagedList;

@RequestMapping("/api/v1/customers/{customerId}")
@RestController
public class BeerOrderConstroller {
	
	private static final Integer DEFAULT_PAGE_NUMBER = 0;
	private static final Integer DEFAULT_PAGE_SIZE = 25;
	
	private final BeerOrderService beerOrderService;
	
	public BeerOrderConstroller(BeerOrderService beerOrderService) {
		this.beerOrderService = beerOrderService;
	}
	
	@OrderReadPermission
	@GetMapping("orders")
	public BeerOrderPagedList listOrders(@PathVariable("customerId") UUID customerId, 
										 @RequestParam(value="pageNumber", required = false) Integer pageNumber, 
										 @RequestParam(value="pageSize", required = false) Integer pageSize) {

		if (pageNumber == null || pageNumber < 0) {
			pageNumber = DEFAULT_PAGE_NUMBER;
		}
		
		if (pageSize == null || pageSize < 0) {
			pageSize = DEFAULT_PAGE_SIZE;
		}
		
		return beerOrderService.listOrders(customerId, PageRequest.of(pageNumber, pageSize));	
	}
	
	@OrderCreatePermission
	@PostMapping("orders")
	@ResponseStatus(HttpStatus.CREATED)
	public BeerOrderDto placeOrder(@PathVariable("customerId") UUID customerId,
								   @RequestBody BeerOrderDto beerOrderDto) {
		return beerOrderService.placeOrder(customerId, beerOrderDto);
	}
	
	@GetMapping("orders/{orderId}")
	public BeerOrderDto getOrder(@PathVariable("customerId") UUID customerId,
			                     @PathVariable("orderId") UUID orderId) {
		return beerOrderService.getOrderById(customerId, orderId);	
	}
	
	@OrderUpdatePermission
	@PutMapping("orders/{orderId}/pickup")
	@ResponseStatus(HttpStatus.NO_CONTENT)
	public void pickupOrder(@PathVariable("customerId") UUID customerId,
                            @PathVariable("orderId") UUID orderId) {
		beerOrderService.pickupOrder(customerId, orderId);
	}
}
