package com.hspark.brewery;

import static org.hamcrest.Matchers.hasSize;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatchers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import com.hspark.brewery.domain.Beer;
import com.hspark.brewery.repositories.BeerRepository;
import com.hspark.brewery.web.controller.BeerController;

@ExtendWith(MockitoExtension.class)
class BeerControllerTest {
	
	@Mock
	private BeerRepository beerRepository;
	
	@InjectMocks
	BeerController controller;
	List<Beer> beerList;
	UUID uuid;

	MockMvc mockMvc;
	Page<Beer> beers;
	Page<Beer> pagedResponse;
	
	@BeforeEach
	void setUp() {
		beerList = new ArrayList<Beer>();
		beerList.add(Beer.builder().build());
		beerList.add(Beer.builder().build());
		pagedResponse = new PageImpl(beerList);
		
		final String id = "493410b3-dd0b-4b78-97bf-289f50f6e74f";
		uuid = UUID.fromString(id);
		
		mockMvc = MockMvcBuilders
				.standaloneSetup(controller)
				.build();
	}

	@Test
	void findBeers() throws Exception {
		mockMvc.perform(get("/beers/find"))
				.andExpect(status().isOk())
				.andExpect(view().name("beers/findBeers"))
				.andExpect(model().attributeExists("beer"));
		verifyNoMoreInteractions(beerRepository);
	}
	
	//ToDO: Mocking Page
	void processFindFormReturnMany() throws Exception {
		when(beerRepository.findAllByBeerName(anyString(), 
				PageRequest.of(0, 10, Sort.Direction.DESC, "beerName"))).thenReturn(pagedResponse);
		mockMvc.perform(get("/beers"))
				.andExpect(status().isOk())
				.andExpect(view().name("/beers/beerList"))
				.andExpect(model().attribute("selections", hasSize(2)));
	}
	
	@Test
	void initCreationForm() throws Exception {
		mockMvc.perform(get("/beers/new"))
				.andExpect(status().isOk())
				.andExpect(view().name("beers/createBeer"))
				.andExpect(model().attributeExists("beer"));
		verifyNoMoreInteractions(beerRepository);
	}
	
	@Test
	void processCreationForm() throws Exception {
		when(beerRepository.save(ArgumentMatchers.any())).thenReturn(Beer.builder().id(uuid).build());
		mockMvc.perform(post("/beers/new").with(csrf()))
				.andExpect(status().is3xxRedirection())
				.andExpect(view().name("redirect:/beers/" + uuid))
				.andExpect(model().attributeExists("beer"));
		verify(beerRepository).save(ArgumentMatchers.any());
	}
	
	@Test
	void initUpdateBeerForm() throws Exception {
		when(beerRepository.findById(uuid)).thenReturn(Optional.of(Beer.builder().id(uuid).build()));
		mockMvc.perform(get("/beers/" + uuid + "/edit"))
				.andExpect(status().isOk())
				.andExpect(view().name("beers/createOrUpdateBeer"))
				.andExpect(model().attributeExists("beer"));
		verifyNoMoreInteractions(beerRepository);
		
	}
	
	@Test
	void processUpdateBeerForm() throws Exception {
		when(beerRepository.save(ArgumentMatchers.any())).thenReturn(Beer.builder().id(uuid).build());
		mockMvc.perform(post("/beers/" + uuid + "/edit").with(csrf()))
		.andExpect(status().is3xxRedirection())
		.andExpect(view().name("redirect:/beers/" + uuid))
		.andExpect(model().attributeExists("beer"));
		verify(beerRepository).save(ArgumentMatchers.any());
		
	}

}
