package com.hspark.brewery.integration_test;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.anonymous;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.httpBasic;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.hspark.brewery.BaseIT;
import com.hspark.brewery.domain.Beer;
import com.hspark.brewery.repositories.BeerRepository;

@SpringBootTest
class BeerControllerIT extends BaseIT {
	
	@Autowired
	BeerRepository beerRepository;
	
	@DisplayName("Init New Form")
	@Nested
	class InitNewForm {
		@ParameterizedTest(name = "#{index} with [{arguments}]")
		@MethodSource("com.hspark.brewery.integration_test.BeerControllerIT#getStreamAllUsers")
		void initCreationFormAuth(String user, String pwd) throws Exception {
			mockMvc.perform(get("/beers/new").with(httpBasic(user, pwd)))
					.andExpect(status().isOk())
					.andExpect(view().name("beers/createBeer"))
					.andExpect(model().attributeExists("beer"));
		}
		
		@Test
		void initCreationFormNotAuth() throws Exception {
			mockMvc.perform(get("/beers/new"))
					.andExpect(status().isUnauthorized());
		}
		
	}
	
	@DisplayName("Init Find Beer Form")
	@Nested
	class FindForm {
		@ParameterizedTest(name = "#{index} with [{arguments}]")
		@MethodSource("com.hspark.brewery.integration_test.BeerControllerIT#getStreamAllUsers")
		void findBeersFormAUTH(String user, String pwd) throws Exception {
			mockMvc.perform(get("/beers/find").with(httpBasic(user, pwd)))
			.andExpect(status().isOk())
			.andExpect(view().name("beers/findBeers"))
			.andExpect(model().attributeExists("beer"));
		}
		
		@Test
		void findBeersWithAnonymous() throws Exception {
			mockMvc.perform(get("/beers/find").with(anonymous()))
					.andExpect(status().isUnauthorized());
		}
		
	}
	
	@DisplayName("Process Find Beer Form")
	@Nested
	class ProcessFindForm {
		
		@Test
		void findBeerForm() throws Exception {
			mockMvc.perform(get("/beers").param("beerName", ""))
					.andExpect(status().isUnauthorized());
		}
		
		@ParameterizedTest(name = "#{index} with [{arguments}]")
		@MethodSource("com.hspark.brewery.integration_test.BeerControllerIT#getStreamAllUsers")
		void findBeerFormAUTH(String user, String pwd) throws Exception {
			mockMvc.perform(get("/beers").param("beerName", "")
					.with(httpBasic(user, pwd)))
				.andExpect(status().isOk());
		}
	}
	
	@DisplayName("Get Beer By Id")
	@Nested
	class GetById {
		
		@ParameterizedTest(name = "#{index} with [{arguments}]")
		@MethodSource("com.hspark.brewery.integration_test.BeerControllerIT#getStreamAllUsers")
		void getBeerByIdAUTH(String user, String pwd) throws Exception {
			Beer beer = beerRepository.findAll().get(0);
			
			mockMvc.perform(get("/beers/" + beer.getId()).param("beerName", "")
					.with(httpBasic(user, pwd)))
					.andExpect(status().isOk())
					.andExpect(view().name("beers/beerDetails"))
					.andExpect(model().attributeExists("beer"));
		}
		@Test
		void getBeerByIdNoAuth() throws Exception {
			Beer beer = beerRepository.findAll().get(0);
			
			mockMvc.perform(get("/beers/" + beer.getId()))
					.andExpect(status().isUnauthorized());
		}
		
	}
}
