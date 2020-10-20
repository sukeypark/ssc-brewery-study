package com.hspark.brewery;

import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import com.hspark.brewery.repository.BeerInventoryRepository;
import com.hspark.brewery.repository.BeerRepository;
import com.hspark.brewery.repository.CustomerRepository;
import com.hspark.brewery.services.BeerService;
import com.hspark.brewery.services.BreweryService;

@WebMvcTest
class BeerControllerIT {

	@Autowired
	WebApplicationContext wac;
	
	MockMvc mockMvc;
	
	@MockBean
	BeerRepository beerRepository;
	
	@MockBean
	BeerInventoryRepository beerInventoryRepository;
	
	@MockBean
	BreweryService breweryService;
	
	@MockBean
	CustomerRepository customerRepository;
	
	@MockBean
	BeerService beerService;
	
	@BeforeEach
	void setUp() {
		mockMvc = MockMvcBuilders
				.webAppContextSetup(wac)
				.apply(springSecurity())
				.build();
	}
	
	@WithMockUser("spring")
	@Test
	void findBeers() throws Exception {
		mockMvc.perform(get("/beers/find"))
				.andExpect(status().isOk())
				.andExpect(view().name("beers/findBeeers"))
				.andExpect(model().attributeExists("beer"));
	}

}
