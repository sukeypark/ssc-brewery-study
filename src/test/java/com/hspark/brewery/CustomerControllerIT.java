package com.hspark.brewery;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.httpBasic;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;

@SpringBootTest
class CustomerControllerIT extends BaseIT {
	
	@DisplayName("List Customers")
	@Nested
	class ListCustomers {
		@ParameterizedTest(name = "#{index} with [{arguments}]")
		@MethodSource("com.hspark.brewery.CustomerControllerIT#getStreamAdminCustomer")
		void testListCustomersAUTH(String user, String pwd) throws Exception {
			mockMvc.perform(get("/customers")
					.with(httpBasic(user, pwd)))
					.andExpect(status().isOk());
		}
		
		@Test
		void testListCustomersNOTAUTH() throws Exception {
			mockMvc.perform(get("/customers")
					.with(httpBasic("user", "password")))
					.andExpect(status().isForbidden());
		}
		
		@Test
		void testListCustomersNOTLOGGEDIN() throws Exception {
			mockMvc.perform(get("/customers"))
					.andExpect(status().isUnauthorized());
		}
	}
	
	@DisplayName("Add Customers")
	@Nested
	class AddCustomers {
		
		@Rollback
		@Test
		void processCreationForm() throws Exception {
			mockMvc.perform(post("/customers/new")
					.param("customerName", "Foo Customer")
					.with(httpBasic("admin", "password"))
					.with(csrf()))
					.andExpect(status().is3xxRedirection());
			
		}
		
		@Rollback
		@ParameterizedTest(name = "#{index} with [{arguments}]")
		@MethodSource("com.hspark.brewery.CustomerControllerIT#getStreamNotAdmin")
		void processCreationFormNOTAUTH(String user, String pwd) throws Exception {
			mockMvc.perform(post("/customers/new")
					.param("customerName", "Foo Customer")
					.with(httpBasic(user, pwd))
					.with(csrf()))
					.andExpect(status().isForbidden());
			
		}
		
		@Test
		void processCreationFormNOAUTH() throws Exception {
			mockMvc.perform(post("/customers/new").param("customerName", "Foo Customer")
					.with(csrf()))
					.andExpect(status().isUnauthorized());
			
		}
	}
}
