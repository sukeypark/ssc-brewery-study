package com.hspark.brewery.api;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.Arrays;
import java.util.List;
import java.util.UUID;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithUserDetails;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.hspark.brewery.BaseIT;
import com.hspark.brewery.bootstrap.DefaultBreweryLoader;
import com.hspark.brewery.domain.Beer;
import com.hspark.brewery.domain.Customer;
import com.hspark.brewery.repositories.BeerOrderRepository;
import com.hspark.brewery.repositories.BeerRepository;
import com.hspark.brewery.repositories.CustomerRepository;
import com.hspark.brewery.web.model.BeerOrderDto;
import com.hspark.brewery.web.model.BeerOrderLineDto;

@SpringBootTest
class BeerOrderControllerTest extends BaseIT{
	
	public static final String API_ROOT = "/api/v1/customers/";
	
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
    Customer keyWestCustomer;
    List<Beer> loadedBeers;
    
    @BeforeEach
    void setUp() {
    	stPeteCustomer = customerRepository.findAllByCustomerName(DefaultBreweryLoader.ST_PETE_DISTRIBUTING).orElseThrow();
    	dunedinCustomer = customerRepository.findAllByCustomerName(DefaultBreweryLoader.DUNEDIN_DISTRIBUTING).orElseThrow();
    	keyWestCustomer = customerRepository.findAllByCustomerName(DefaultBreweryLoader.KEY_WEST_DISTRIBUTORS).orElseThrow();
    	loadedBeers = beerRepository.findAll();
    }
    
	@Test
	void createOrderNotAuth() throws Exception {
		BeerOrderDto beerOrderDto = buildOrderDto(stPeteCustomer, loadedBeers.get(0).getId());
		
		mockMvc.perform(post(API_ROOT + stPeteCustomer.getId() + "/orders")
				.accept(MediaType.APPLICATION_JSON)
				.characterEncoding("UTF-8")
				.contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(beerOrderDto)))
				.andExpect(status().isUnauthorized());
	}
	
	@WithUserDetails(value = "admin")
	@Test
	void createOrderUserAdmin() throws Exception {
		BeerOrderDto beerOrderDto = buildOrderDto(stPeteCustomer, loadedBeers.get(0).getId());
		
		mockMvc.perform(post(API_ROOT + stPeteCustomer.getId() + "/orders")
				.accept(MediaType.APPLICATION_JSON)
				.characterEncoding("UTF-8")
				.contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(beerOrderDto)))
				.andExpect(status().isCreated());  		
	}    	
	
	@WithUserDetails(DefaultBreweryLoader.STPETE_USER)
	@Test
	void createOrderUserAuthCustomer() throws Exception {
		BeerOrderDto beerOrderDto = buildOrderDto(stPeteCustomer, loadedBeers.get(0).getId());
		
		mockMvc.perform(post(API_ROOT + stPeteCustomer.getId() + "/orders")
				.accept(MediaType.APPLICATION_JSON)
				.characterEncoding("UTF-8")
				.contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(beerOrderDto)))
				.andExpect(status().isCreated());  		
	}    	
	
	@WithUserDetails(DefaultBreweryLoader.KEYWEST_USER)
	@Test
	void createOrderUserNOTAuthCustomer() throws Exception {
		BeerOrderDto beerOrderDto = buildOrderDto(stPeteCustomer, loadedBeers.get(0).getId());
		
		mockMvc.perform(post(API_ROOT + stPeteCustomer.getId() + "/orders")
				.accept(MediaType.APPLICATION_JSON)
				.characterEncoding("UTF-8")
				.contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(beerOrderDto)))
		.andExpect(status().isForbidden());  		
	} 

   	@Test
	void listOrdersNotAuth() throws Exception {
		mockMvc.perform(get(API_ROOT + stPeteCustomer.getId() + "/orders"))
				.andExpect(status().isUnauthorized());
	}
	
	@WithUserDetails(value = "admin")
	@Test
	void listOrdersAdminAuth() throws Exception {
		mockMvc.perform(get(API_ROOT + stPeteCustomer.getId() + "/orders"))
				.andExpect(status().isOk());
	}
	
	@WithUserDetails(value = DefaultBreweryLoader.STPETE_USER)
	@Test
	void listOrdersCustomerAuth() throws Exception {
		mockMvc.perform(get(API_ROOT + stPeteCustomer.getId() + "/orders"))
		.andExpect(status().isOk());
	}
	
	@WithUserDetails(value = DefaultBreweryLoader.DUNEDIN_USER)
	@Test
	void listOrdersCustomerNOAuth() throws Exception {
		mockMvc.perform(get(API_ROOT + stPeteCustomer.getId() + "/orders"))
		.andExpect(status().isForbidden());
	}
	
	@Disabled
	@Test
	void pickUpOrdersNotAuth() {}
	
	@Disabled
	@Test
	void pickUpOrdersAdminAuth() {}
	
	@Disabled
	@Test
	void pickUpOrdersCustomerAuth() {}
	
	@Disabled
	@Test
	void pickUpOrderNOAuth() {}

	private BeerOrderDto buildOrderDto(Customer customer, UUID beerId) {
        List<BeerOrderLineDto> orderLines = (List<BeerOrderLineDto>) Arrays.asList(BeerOrderLineDto.builder()
                .id(UUID.randomUUID())
                .beerId(beerId)
                .orderQuantity(5)
                .build());

        return BeerOrderDto.builder()
                .customerId(customer.getId())
                .customerRef("123")
                .orderStatusCallbackUrl("http://example.com")
                .beerOrderLines(orderLines)
                .build();
	}
}
