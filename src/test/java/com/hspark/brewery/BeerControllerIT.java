package com.hspark.brewery;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.httpBasic;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.security.test.context.support.WithMockUser;

@WebMvcTest
class BeerControllerIT extends BaseIT {
	
	@WithMockUser("spring")
	@Test
	void findBeers() throws Exception {
		mockMvc.perform(get("/beers/find"))
				.andExpect(status().isOk())
				.andExpect(view().name("beers/findBeers"))
				.andExpect(model().attributeExists("beer"));
	}
	
	@Test
	void findBeersWithHttpBasic() throws Exception {
		mockMvc.perform(get("/beers/find").with(httpBasic("spring", "guru")))
				.andExpect(status().isUnauthorized());
	}
	
	@Test
	void getBeerList() throws Exception {
		mockMvc.perform(get("/api/v1/beer"))
				.andExpect(status().isOk());
	}
	
	@Test
	void findBeerByUpc() throws Exception {
		mockMvc.perform(get("/api/v1/beerUpc/0123456789101"))
				.andExpect(status().isOk());
	}

}
