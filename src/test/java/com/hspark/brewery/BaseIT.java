package com.hspark.brewery;

import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;

import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import com.hspark.brewery.services.BeerService;
import com.hspark.brewery.services.BreweryService;

public abstract class BaseIT {
	
	@Autowired
	WebApplicationContext wac;
	
	@MockBean
	BreweryService breweryService;
	
	@MockBean
	BeerService beerService;
	
	protected MockMvc mockMvc;
	
	@BeforeEach
	public void setup() {
		mockMvc = MockMvcBuilders
				.webAppContextSetup(wac)
				.apply(springSecurity())
				.build();
	}

}
