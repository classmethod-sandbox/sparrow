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

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.when;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringRunner;

import org.junit.Test;
import org.junit.runner.RunWith;

import jp.classmethod.sparrow.model.LineBotService;

/**
 * Test for {@link ExampleController}.
 *
 * @author daisuke
 * @since #version#
 */
@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class LineBotIntegrationTest {
	
	@Autowired
	TestRestTemplate restTemplate;
	
	@SpyBean
	LineBotService lineBotService;
	
	
	@Test
	public void testWebhookRequest() throws Exception {
		
		// setup
		// skip signature validation
		when(lineBotService.validateRequestSignature(anyString(), any(LineWebhookRequest.class)))
			.thenReturn(true);
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);
		headers.set("X-Line-Signature", "/v4Ra2mMN4ZjnjABJsDpibMaI2x8ZAg0Tl5UNDLPvjE=");
		LineWebhookRequest data = LineWebhookRequestFixture.createRequest();
		HttpEntity<LineWebhookRequest> entity = new HttpEntity<>(data, headers);
		
		// exercise
		ResponseEntity<String> actual = restTemplate.exchange("/sparrow/", HttpMethod.POST, entity, String.class);
		// verify
		assertThat(actual.getStatusCode()).isEqualTo(HttpStatus.OK);
	}
	
}
