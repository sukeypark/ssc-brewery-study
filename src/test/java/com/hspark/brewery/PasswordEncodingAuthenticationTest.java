package com.hspark.brewery;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.httpBasic;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;

@WebMvcTest
class PasswordEncodingAuthenticationTest extends BaseIT {

	static final String PASSWORD = "password";
	
	@Test
	void testAdminMd5() throws Exception {
		mockMvc.perform(get("/beers/find").with(httpBasic("admin_md5", PASSWORD)))
				.andExpect(status().isOk());
	}
	
	@Test
	void testUserLdap() throws Exception {
		mockMvc.perform(get("/beers/find").with(httpBasic("user_ldap", PASSWORD)))
		.andExpect(status().isOk());
	}
	
	@Test
	void testUserSha256() throws Exception {
		mockMvc.perform(get("/beers/find").with(httpBasic("user_sha256", PASSWORD)))
		.andExpect(status().isOk());
	}
	
	@Test
	void testUserBcrypt() throws Exception {
		mockMvc.perform(get("/beers/find").with(httpBasic("user_bcrypt", PASSWORD)))
		.andExpect(status().isOk());
	}

}
