package com.hspark.brewery;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import com.hspark.brewery.repositories.BeerInventoryRepository;
import com.hspark.brewery.repositories.BeerRepository;
import com.hspark.brewery.repositories.CustomerRepository;
import com.hspark.brewery.services.BeerOrderService;
import com.hspark.brewery.services.BeerService;
import com.hspark.brewery.services.BreweryService;

@WebMvcTest
class IndexControllerIT extends BaseIT {
	
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
	
	@MockBean
	BeerOrderService beerOrderSerivce;

	@Test
	void testGetIndexSlash() throws Exception {
		mockMvc.perform(get("/"))
				.andExpect(status().isOk());
	}

}
