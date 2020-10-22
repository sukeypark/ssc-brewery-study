package com.hspark.brewery.integration_test;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.httpBasic;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.boot.test.context.SpringBootTest;

import com.hspark.brewery.BaseIT;

@SpringBootTest
class CustomerControllerIT extends BaseIT {

	@ParameterizedTest(name = "#{index} with [{arguments}]")
	@MethodSource("com.hspark.brewery.integration_test.CustomerControllerIT#getStreamAdminCustomer")
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
