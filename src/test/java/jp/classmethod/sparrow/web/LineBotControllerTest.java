/*
 * Copyright 2017 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package jp.classmethod.sparrow.web;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import com.fasterxml.jackson.databind.ObjectMapper;

import jp.classmethod.sparrow.model.LineBotService;

/**
 * Test for {@link ExampleController}.
 *
 * @author daisuke
 * @since #version#
 */
@RunWith(MockitoJUnitRunner.class)
public class LineBotControllerTest {
	
	@InjectMocks
	LineBotController sut;
	
	@Mock
	LineBotService botService;
	
	private MockMvc mvc;
	
	
	@Before
	public void setup() {
		mvc = MockMvcBuilders.standaloneSetup(sut).build();
	}
	
	@Test
	public void testWebhookRequest() throws Exception {
		
		LineWebhookRequest request = LineWebhookRequestFixture.createRequest();
		ObjectMapper mapper = new ObjectMapper();
		String content = mapper.writeValueAsString(request);
		
		// exercise
		mvc.perform(post("/sparrow")
			.contentType(MediaType.APPLICATION_JSON)
			.content(content))
			// verify
			.andExpect(status().isOk());
	}
	
}
