package com.hspark.brewery;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.UUID;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;

@WebMvcTest
class BeerRestControllerIT extends BaseIT {
	
	static UUID UUID_ = UUID.randomUUID();
	static String PASSWORD = "password";
	
	@Test
	void deleteBeerWithUrlParamAuth() throws Exception {
		mockMvc.perform(delete("/api/v1/beer/" + UUID_)
				.param("apiKey", "user_ldap").param("apiSecret", PASSWORD))
				.andExpect(status().is2xxSuccessful());		
	}

	@Test
	void deleteBeer() throws Exception {
		mockMvc.perform(delete("/api/v1/beer/" + UUID_)
				.header("Api-Key", "user_ldap").header("Api-Secret", PASSWORD))
				.andExpect(status().is2xxSuccessful());
	}
	
	@Test
	void getBeerListWithNoAuth() throws Exception {
		mockMvc.perform(get("/api/v1/beer"))
				.andExpect(status().isUnauthorized());
	}
	
	@Test
	void findBeerByUpcWithNoAuth() throws Exception {
		mockMvc.perform(get("/api/v1/beerUpc/0123456789101"))
				.andExpect(status().isUnauthorized());
	}

}
