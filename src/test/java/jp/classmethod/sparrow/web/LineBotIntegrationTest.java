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
import static org.mockito.Matchers.anyString;

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
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;

import jp.classmethod.sparrow.model.CalcService;

/**
 *
 * @author daisuke
 * @since #version#
 */
@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
public class LineBotIntegrationTest {
	
	@Autowired
	TestRestTemplate restTemplate;
	
	@SpyBean
	CalcService calcService;
	
	
	@Test
	public void testWebhookRequest() throws Exception {
		// setup
		// skip signature validation
		Mockito.doNothing().when(calcService).process(anyString(), anyString());
		
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);
		headers.set("X-Line-Signature", "/v4Ra2mMN4ZjnjABJsDpibMaI2x8ZAg0Tl5UNDLPvjE=");
		LineWebhookRequest data = LineWebhookRequestFixture.createRequest();
		HttpEntity<LineWebhookRequest> entity = new HttpEntity<>(data, headers);
		
		// exercise
		ResponseEntity<Void> actual = restTemplate.exchange("/sparrow/", HttpMethod.POST, entity, Void.class);
		// verify
		assertThat(actual.getStatusCode()).isEqualTo(HttpStatus.OK);
	}
}
