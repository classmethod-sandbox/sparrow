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
package jp.classmethod.sparrow.model;

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
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import org.junit.Test;
import org.junit.runner.RunWith;

/**
 * Created by kunita.fumiko on 2017/03/27.
 */
@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class TrainingIntegrationTest {
	
	@Autowired
	TestRestTemplate restTemplate;
	
	
	@Test
	public void testCharacterProcessing() {
		// setup
		HttpHeaders headers = new HttpHeaders();
		// 追加
		MultiValueMap<String, String> data = new LinkedMultiValueMap<>();
		data.add("convertType", "1");
		data.add("character", "aDt112dQ");
		HttpEntity<Object> entity = new HttpEntity<>(data, headers);
		// exercise
		ResponseEntity<String> actual = restTemplate.exchange("/converter", HttpMethod.POST, entity, String.class);
		// verify
		assertThat(actual.getStatusCode()).isEqualTo(HttpStatus.OK);
		assertThat(actual.getBody()).isEqualTo("aDtdQ");
	}
}
