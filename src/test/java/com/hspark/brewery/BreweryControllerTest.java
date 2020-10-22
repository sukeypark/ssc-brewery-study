package com.hspark.brewery;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.httpBasic;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class BreweryControllerTest extends BaseIT {

	@Test
	void testBreweriesWithCustomerRole() throws Exception {
		mockMvc.perform(get("/brewery/breweries").with(httpBasic("customer", "password")))
				.andExpect(status().isOk());
	}
	
	@Test
	void testBreweriesWithAdminRole() throws Exception {
		mockMvc.perform(get("/brewery/breweries").with(httpBasic("admin", "password")))
		.andExpect(status().isOk());
	}
	
	@Test
	void testBreweriesWithoutCustomerRole() throws Exception {
		mockMvc.perform(get("/brewery/breweries").with(httpBasic("user", "password")))
		.andExpect(status().isForbidden());
	}
	
	@Test
	void testBreweriesAPIWithCustomerRole() throws Exception {
		mockMvc.perform(get("/brewery/api/v1/breweries").with(httpBasic("customer", "password")))
		.andExpect(status().isOk());
	}
	
	@Test
	void testBreweriesAPIWithoutCustomerRole() throws Exception {
		mockMvc.perform(get("/brewery/api/v1/breweries").with(httpBasic("user", "password")))
		.andExpect(status().isForbidden());
	}
	
	@Test
	void testBreweriesAPINoAuth() throws Exception {
		mockMvc.perform(get("/brewery/api/v1/breweries"))
		.andExpect(status().isUnauthorized());
	}
	
	@Test
	void testBreweriesNoAuth() throws Exception {
		mockMvc.perform(get("/brewery/breweries"))
		.andExpect(status().isUnauthorized());
	}

}
