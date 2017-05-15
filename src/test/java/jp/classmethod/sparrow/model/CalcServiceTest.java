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

import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.when;

import lombok.extern.slf4j.Slf4j;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

/**
 * Created by kunita.fumiko on 2017/05/11.
 */
@Slf4j
@RunWith(MockitoJUnitRunner.class)
public class CalcServiceTest {
	
	@Mock
	LineBotService lineBotService;
	
	@Mock
	Calculator calculator;
	
	@InjectMocks
	CalcService sut;
	
	String signature;
	
	String requestBody;
	
	LineEvent event;
	
	
	@Before
	public void setup() {
		signature = "/v4Ra2mMN4ZjnjABJsDpibMaI2x8ZAg0Tl5UNDLPvjE=";
		requestBody = "{\"events\":[{\"type\":\"message\",\"timestamp\":146262947912543,"
				+ "\"source\":{\"type\":\"user\",\"type\":\"user\",\"userId\":\"U206d25c2ea6bd87c17655609a1c37cb8\"},"
				+ "\"message\":{\"type\":\"text\",\"id\":\"325708\",\"text\":\"start\",\"latitude\":0.0,\"longitude\":0.0},"
				+ "\"replyToken\":\"nHuyWiB7yP5Zw52FIkcQobQuGDXCTA\"}]}";
		event = LineEventFixture.createLineUserEvent(LineMessageFixture.createStartLineMessage());
	}
	
	/**
	 * linebotservice#validateRequestSignatureの結果がfalseの時、例外を投げることを確認する
	 */
	@Test
	public void budValidateTest() {
		// setup
		when(lineBotService.validateRequestSignature(anyString(), anyString())).thenReturn(false);
		// exesice
		// verify
		try {
			sut.process(signature, requestBody);
			fail(); //例外を投げなかった場合はテスト失敗
		} catch (IllegalArgumentException e) {
			assertTrue(true);
		}
	}
	
}
