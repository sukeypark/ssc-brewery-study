package com.hspark.brewery.api;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.transaction.annotation.Transactional;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.hspark.brewery.BaseIT;
import com.hspark.brewery.bootstrap.DefaultBreweryLoader;
import com.hspark.brewery.domain.Beer;
import com.hspark.brewery.domain.BeerOrder;
import com.hspark.brewery.domain.Customer;
import com.hspark.brewery.repositories.BeerOrderRepository;
import com.hspark.brewery.repositories.BeerRepository;
import com.hspark.brewery.repositories.CustomerRepository;

@SpringBootTest
class BeerOrderControllerTestV2 extends BaseIT {
	public static final String API_ROOT = "/api/v2/orders/";
	
	@Autowired
	CustomerRepository customerRepository;
	
	@Autowired
	BeerOrderRepository beerOrderRepository;
	
	@Autowired
	BeerRepository beerRepository;
	
	@Autowired
	ObjectMapper objectMapper;
	
	Customer stPeteCustomer;
	Customer dunedinCustomer;
	Customer keywestCustomer;
	List<Beer> loadedBeers;

	@BeforeEach
	void setUp() {
		stPeteCustomer = customerRepository.findAllByCustomerName(DefaultBreweryLoader.ST_PETE_DISTRIBUTING).orElseThrow();
		dunedinCustomer = customerRepository.findAllByCustomerName(DefaultBreweryLoader.DUNEDIN_DISTRIBUTING).orElseThrow();
		keywestCustomer = customerRepository.findAllByCustomerName(DefaultBreweryLoader.KEY_WEST_DISTRIBUTORS).orElseThrow();
		loadedBeers = beerRepository.findAll();
	}
	
	@Test
	void listOrdersNotAuth() throws Exception {
		mockMvc.perform(get(API_ROOT))
				.andExpect(status().isUnauthorized());
	}
	
	@WithUserDetails(value = "admin")
	@Test
	void listOrdersAdminAuth() throws Exception {
		mockMvc.perform(get(API_ROOT))
		.andExpect(status().isOk());
	}
	
	@WithUserDetails(value = DefaultBreweryLoader.STPETE_USER)
	@Test
	void listOrdersCustomerAuth() throws Exception {
		mockMvc.perform(get(API_ROOT))
		.andExpect(status().isOk());
	}
	
	@WithUserDetails(value = DefaultBreweryLoader.DUNEDIN_USER)
	@Test
	void listOrdersCustomerDunedinAuth() throws Exception {
		mockMvc.perform(get(API_ROOT))
		.andExpect(status().isOk());
	}
	
	@Transactional
	@Test
	void getByOrderIdNotAuth( ) throws Exception {
		BeerOrder beerOrder = stPeteCustomer.getBeerOrders().stream().findFirst().orElseThrow();
		
		mockMvc.perform(get(API_ROOT + beerOrder.getId()))
				.andExpect(status().isUnauthorized());
	}
	
	@WithUserDetails(value = "admin")
	@Transactional
	@Test
	void getByOrderIdAdminAuth( ) throws Exception {
		BeerOrder beerOrder = stPeteCustomer.getBeerOrders().stream().findFirst().orElseThrow();
		
		mockMvc.perform(get(API_ROOT + beerOrder.getId()))
		.andExpect(status().is2xxSuccessful());
	}
	
	@WithUserDetails(value = DefaultBreweryLoader.STPETE_USER)
	@Transactional
	@Test
	void getByOrderIdCustomerAuth( ) throws Exception {
		BeerOrder beerOrder = stPeteCustomer.getBeerOrders().stream().findFirst().orElseThrow();
		
		mockMvc.perform(get(API_ROOT + beerOrder.getId()))
				.andExpect(status().is2xxSuccessful());
	}
	
	@WithUserDetails(value = DefaultBreweryLoader.DUNEDIN_USER)
	@Transactional
	@Test
	void getByOrderIdCustomerNOTAuth( ) throws Exception {
		BeerOrder beerOrder = stPeteCustomer.getBeerOrders().stream().findFirst().orElseThrow();
		
		mockMvc.perform(get(API_ROOT + beerOrder.getId()))
		.andExpect(status().isNotFound());
	}

}
