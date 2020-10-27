package com.hspark.brewery.unit_test;

import static org.hamcrest.Matchers.hasProperty;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;
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
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatchers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import com.hspark.brewery.domain.Customer;
import com.hspark.brewery.repositories.CustomerRepository;
import com.hspark.brewery.web.controller.CustomerController;

@ExtendWith(MockitoExtension.class)
public class CustomerControllerTest {

	@Mock
	CustomerRepository customerRepository;
	
	@InjectMocks
	CustomerController controller;
	List<Customer> customerList;
	UUID uuid;
	Customer customer;
	
	MockMvc mockMvc;
	
	@BeforeEach
	void setUp() {
		customerList = new ArrayList<Customer>();
		customerList.add(Customer.builder().customerName("John Doe").build());
		customerList.add(Customer.builder().customerName("John Doe").build());
		
		final String id = "493410b3-dd0b-4b78-97bf-289f50f6e74f";
		uuid = UUID.fromString(id);
		
		mockMvc = MockMvcBuilders
				.standaloneSetup(controller)
				.build();
	}
	
	@Test
	void findCustomer() throws Exception {
		mockMvc.perform(get("/customers/find"))
				.andExpect(status().isOk())
				.andExpect(view().name("customers/findCustomers"))
				.andExpect(model().attributeExists("customer"));
		
		verifyNoInteractions(customerRepository);
	}
	
	@Test
	@Disabled
	void processFindFormReturnMany() throws Exception {
		when(customerRepository.findAllByCustomerNameLike("John Doe")).thenReturn(customerList);
		
		mockMvc.perform(get("/customers"))
				.andExpect(status().isOk())
				.andExpect(view().name("customers/customerList"))
				.andExpect(model().attribute("selections", hasSize(2)));
	}
	
	@Test
	void showCustomer() throws Exception {
		when(customerRepository.findById(uuid)).thenReturn(Optional.of(Customer.builder().id(uuid).build()));
		mockMvc.perform(get("/customers/" + uuid))
				.andExpect(status().isOk())
				.andExpect(view().name("customers/customerDetails"))
				.andExpect(model().attribute("customer", hasProperty("id", is(uuid))));
	}
	
	@Test
	void initCreationForm() throws Exception {
		mockMvc.perform(get("/customers/new"))
				.andExpect(status().isOk())
				.andExpect(view().name("customers/createCustomer"))
				.andExpect(model().attributeExists("customer"));
		verifyNoInteractions(customerRepository);
	}
	
	@Test
	void processCreationForm() throws Exception {
		when(customerRepository.save(ArgumentMatchers.any())).thenReturn(Customer.builder().id(uuid).build());
		
		mockMvc.perform(post("/customers/new"))
				.andExpect(status().is3xxRedirection())
				.andExpect(view().name("redirect:/customers/" + uuid))
				.andExpect(model().attributeExists("customer"));
		
		verify(customerRepository).save(ArgumentMatchers.any());
	}
	
	@Test
	void initUpdateCustomerForm() throws Exception {
		when(customerRepository.findById(uuid)).thenReturn(Optional.of(Customer.builder().id(uuid).build()));
		mockMvc.perform(get("/customers/" + uuid + "/edit"))
				.andExpect(status().isOk())
				.andExpect(view().name("customers/createOrUpdateCustomer"))
				.andExpect(model().attributeExists("customer"));
		
		verifyNoMoreInteractions(customerRepository);
	}
	
	@Test
	void processUpdateForm() throws Exception {
		when(customerRepository.save(ArgumentMatchers.any())).thenReturn(Customer.builder().id(uuid).build());
		
		mockMvc.perform(post("/customers/" + uuid + "/edit"))
				.andExpect(status().is3xxRedirection())
				.andExpect(view().name("redirect:/customers/" + uuid))
				.andExpect(model().attributeExists("customer"));
		
		verify(customerRepository).save(ArgumentMatchers.any());
	}
}
