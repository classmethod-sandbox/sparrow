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

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.runners.MockitoJUnitRunner;

/**
 * Test for {@link ExampleController}.
 *
 * @author daisuke
 * @since #version#
 */
@RunWith(MockitoJUnitRunner.class)
public class ExampleControllerTest {
	
	@InjectMocks
	ExampleController sut;
	
	private MockMvc mvc;
	
	
	@Before
	public void setup() {
		mvc = MockMvcBuilders.standaloneSetup(sut).build();
	}
	
	@Test
	public void testGetIndex() throws Exception {
		// exercise
		mvc.perform(get("/"))
			// verify
			.andExpect(status().isOk())
			.andExpect(content().contentTypeCompatibleWith(MediaType.TEXT_PLAIN))
			.andExpect(content().string("Hello, world!"));
	}
	
	// GETでクエリを取得する練習
	@Test
	public void testGetCalc() throws Exception {
		// exercise
		mvc.perform(get("/calc?x=1&y=2"))
			// verify
			.andExpect(status().isOk())
			.andExpect(content().contentTypeCompatibleWith(MediaType.TEXT_PLAIN))
			.andExpect(content().string("3"));
	}
	
	// POSTでリクエストボディを取得する練習
	@Test
	public void testPostClac() throws Exception {
		// exercise
		mvc.perform(post("/calc")
			.param("x", "1")
			.param("y", "2"))	//paramメソッドを使ってリクエストパラメータの指定
			// verify
			.andExpect(status().isOk())
			.andExpect(content().contentTypeCompatibleWith(MediaType.TEXT_PLAIN))
			.andExpect(content().string("3"));
	}
	
}
