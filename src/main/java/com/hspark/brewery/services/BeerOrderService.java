package com.hspark.brewery.services;

import java.util.UUID;

import org.springframework.data.domain.Pageable;

import com.hspark.brewery.web.model.BeerOrderDto;
import com.hspark.brewery.web.model.BeerOrderPagedList;

public interface BeerOrderService {

	public BeerOrderPagedList listOrders(UUID customerId, Pageable pageable);

	public BeerOrderPagedList listOrders(Pageable pageable);

	public BeerOrderDto placeOrder(UUID customerId, BeerOrderDto beerOrderDto);

	public BeerOrderDto getOrderById(UUID customerId, UUID orderId);
	
	public BeerOrderDto getOrderById(UUID orderId);

	public void pickupOrder(UUID customerId, UUID orderId);

}
