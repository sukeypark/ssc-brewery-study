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
import com.hspark.brewery.repositories.CustomerRepository;

@SpringBootTest
class CustomerControllerIT extends BaseIT {
	
	@Autowired
	CustomerRepository customerRepository;
	
	@DisplayName("Init new Form")
	@Nested
	class InitNewForm {
		@ParameterizedTest(name = "#{index} with [{arguments}]")
		@MethodSource("com.hspark.brewery.integration_test.CustomerControllerIT#getStreamNotAdmin")
		void initCreationFormNotAuth(String user, String pwd) throws Exception {
			mockMvc.perform(get("/customers/new").with(httpBasic(user, pwd)))
					.andExpect(status().isForbidden());
		}
		
		@Test
		void initCreationFormAdmin() throws Exception {
			mockMvc.perform(get("/customers/new").with(httpBasic("admin", "password")))
			.andExpect(status().isOk())
			.andExpect(view().name("customers/createCustomer"))
			.andExpect(model().attributeExists("customer"));			
		}
		
		@Test
		void initCreationFormWithAnonymous() throws Exception {
			mockMvc.perform(get("/customers/new").with(anonymous()))
					.andExpect(status().isUnauthorized());
		}
	}
	
	@DisplayName("Init Find Customer Form")
	@Nested
	class FindForm {
		@ParameterizedTest(name = "#{index} with [{arguments}]")
		@MethodSource("com.hspark.brewery.integration_test.CustomerControllerIT#getStreamAdminCustomer")
		void findCustomerFormNotAuth(String user, String pwd) throws Exception {
			mockMvc.perform(get("/customers/find").with(httpBasic(user, pwd)))
					.andExpect(status().isOk())
					.andExpect(view().name("customers/findCustomers"))
					.andExpect(model().attributeExists("customer"));			
		}
		
		@Test
		void findCustomerNotAuth() throws Exception {
			mockMvc.perform(get("/customers/find").with(httpBasic("user", "password")))
					.andExpect(status().isForbidden());
		}
		
		@Test
		void findCustomerFormWithAnonymous() throws Exception {
			mockMvc.perform(get("/customers/find").with(anonymous()))
					.andExpect(status().isUnauthorized());
		}
	}
	
	@DisplayName("Get Customer By Id")
	@Nested
	class GetById {
		@ParameterizedTest(name = "#{index} with [{arguments}]")
		@MethodSource("com.hspark.brewery.integration_test.CustomerControllerIT#getStreamAdminCustomer")
		void findCustomerFormNotAuth(String user, String pwd) throws Exception {
			mockMvc.perform(get("/customers/find").with(httpBasic(user, pwd)))
					.andExpect(status().isOk())
					.andExpect(view().name("customers/findCustomers"))
					.andExpect(model().attributeExists("customer"));			
		}
		
		@Test
		void findCustomerNotAuth() throws Exception {
			mockMvc.perform(get("/customers/find").with(httpBasic("user", "password")))
					.andExpect(status().isForbidden());
		}
		
		@Test
		void findCustomerFormWithAnonymous() throws Exception {
			mockMvc.perform(get("/customers/find").with(anonymous()))
					.andExpect(status().isUnauthorized());
		}
	}
	
	@DisplayName("Process Creation Form")
	@Nested
	class ProcessCreationForm {
		
	}
	
	@DisplayName("Init Update Customer Form")
	class ProcessUpcateForm {
		
	}

}
