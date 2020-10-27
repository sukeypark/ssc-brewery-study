package com.hspark.brewery.services;

import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.hspark.brewery.domain.BeerOrder;
import com.hspark.brewery.domain.Customer;
import com.hspark.brewery.domain.OrderStatusEnum;
import com.hspark.brewery.repositories.BeerOrderRepository;
import com.hspark.brewery.repositories.CustomerRepository;
import com.hspark.brewery.web.mapper.BeerOrderMapper;
import com.hspark.brewery.web.model.BeerOrderDto;
import com.hspark.brewery.web.model.BeerOrderPagedList;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@AllArgsConstructor
public class BeerOrderServiceImpl implements BeerOrderService {
	
	private final BeerOrderRepository beerOrderRepository;
	private final CustomerRepository customerRepository;
	private final BeerOrderMapper beerOrderMapper;
	
	@Override
	public BeerOrderPagedList listOrders(UUID customerId, Pageable pageable) {
		Optional<Customer> customerOptional = customerRepository.findById(customerId);
		
		if (customerOptional.isPresent()) {
			Page<BeerOrder> beerOrderPage = 
					beerOrderRepository.findAllByCustomer(customerOptional.get(), pageable);
			
			 return new BeerOrderPagedList(
					 beerOrderPage.stream()
			 			.map(beerOrderMapper::beerOrderToDto)
			 			.collect(Collectors.toList()), 
			 		PageRequest.of(beerOrderPage.getPageable().getPageNumber(),
			 			beerOrderPage.getPageable().getPageSize()),
			 		beerOrderPage.getTotalElements()
			 );
		} else {
			return null;			
		}
	}
	
	@Transactional
	@Override
	public BeerOrderDto placeOrder(UUID customerId, BeerOrderDto beerOrderDto) {
		Optional<Customer> customerOptional = customerRepository.findById(customerId);
		
		if (customerOptional.isPresent()) {
			BeerOrder beerOrder = beerOrderMapper.dtoToBeerOrder(beerOrderDto);
			beerOrder.setId(null);
			beerOrder.setCustomer(customerOptional.get());
			beerOrder.setOrderStatus(OrderStatusEnum.NEW);
			
			beerOrder.getBeerOrderLines().forEach(line -> line.setBeerOrder(beerOrder));
			
			BeerOrder savedBeerOrder = beerOrderRepository.saveAndFlush(beerOrder);
			
			log.debug("Saved Beer Order: " + beerOrder.getId());
			
			return beerOrderMapper.beerOrderToDto(savedBeerOrder);
		}
		
		// TODO: add exception type
		throw new RuntimeException("Customer Not Found");
	}
	
	@Override
	public BeerOrderDto getOrderById(UUID customerId, UUID orderId) {
		return beerOrderMapper.beerOrderToDto(getOrder(customerId, orderId));
	}

	@Override
	public void pickupOrder(UUID customerId, UUID orderId) {
		BeerOrder beerOrder = getOrder(customerId, orderId);
		beerOrder.setOrderStatus(OrderStatusEnum.PICKED_UP);
		
		beerOrderRepository.save(beerOrder);
		
	}
	
	private BeerOrder getOrder(UUID customerId, UUID orderId) {
		Optional<Customer> customerOptional = customerRepository.findById(customerId);
		
		if (customerOptional.isPresent()) {
			Optional<BeerOrder> beerOrderOptional = beerOrderRepository.findById(orderId);
			
			if (beerOrderOptional.isPresent()) {
				BeerOrder beerOrder = beerOrderOptional.get();
				
				if (beerOrder.getCustomer().getId().equals(customerId)) {
					return beerOrder;
				}
			}
			throw new RuntimeException("Beer Order Not Found.");
		}
		throw new RuntimeException("Customer Not Found.");
	}
}
