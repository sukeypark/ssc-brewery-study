package com.hspark.brewery;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;

@WebMvcTest
class IndexControllerIT extends BaseIT {

	@Test
	void testGetIndexSlash() throws Exception {
		mockMvc.perform(get("/"))
				.andExpect(status().isOk());
	}

}
