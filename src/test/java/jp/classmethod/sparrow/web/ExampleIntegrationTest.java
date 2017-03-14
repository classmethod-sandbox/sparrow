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

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringRunner;

import org.junit.Test;
import org.junit.runner.RunWith;

/**
 * Integration test.
 *
 * @author daisuke
 * @since #version#
 */
@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class ExampleIntegrationTest {
	
	@Autowired
	TestRestTemplate restTemplate;
	
	
	@Test
	public void testGetIndex() {
		// setup
		HttpHeaders headers = new HttpHeaders();
		HttpEntity<Object> entity = new HttpEntity<>(headers);
		// exercise
		ResponseEntity<String> actual = restTemplate.exchange("/", HttpMethod.GET, entity, String.class);
		// verify
		assertThat(actual.getStatusCode()).isEqualTo(HttpStatus.OK);
		assertThat(actual.getBody()).isEqualTo("Hello, world!");
	}
}
