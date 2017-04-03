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

import static org.hamcrest.Matchers.equalTo;
import static org.junit.Assert.assertThat;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.isA;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.Collections;

import org.apache.http.HttpEntity;
import org.apache.http.HttpStatus;
import org.apache.http.HttpVersion;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.message.BasicStatusLine;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.runners.MockitoJUnitRunner;

import com.fasterxml.jackson.databind.ObjectMapper;

import jp.classmethod.sparrow.configprops.LineBotConfigurationProperties;

/**
 * Created by mochizukimasao on 2017/03/30.
 *
 * @author mochizukimasao
 * @since version
 */
@RunWith(MockitoJUnitRunner.class)
public class LineBotServiceTest {
	
	@Spy
	LineBotConfigurationProperties configurationProperties = new LineBotConfigurationProperties()
		.setChannelSecret("channel_secret")
		.setChannelToken("channel_token")
		.setMessageApiEndpoint("http://localhost:8080/");
	
	@Mock
	CloseableHttpClient httpClient;
	
	@Spy
	ObjectMapper objectMapper;
	
	private LineMessageAPIRequest request;
	
	@InjectMocks
	LineBotService sut;
	
	
	@Before
	public void setup() throws Exception {
		LineMessage message = new LineMessage();
		message.setType(LineMessageType.TEXT);
		message.setText("text body");
		request = spy(new LineMessageAPIRequest("fffff", Collections.singletonList(message)));
		when(objectMapper.writeValueAsString(any())).thenReturn("");
		
	}
	
	@Test
	public void testMessageIsProcessed() throws Exception {
		// setup
		LineEvent event = LineEventFixture.createLineUserEvent();
		CloseableHttpResponse response = mock(CloseableHttpResponse.class);
		HttpEntity entity = mock(HttpEntity.class);
		when(response.getStatusLine()).thenReturn(new BasicStatusLine(HttpVersion.HTTP_1_1, HttpStatus.SC_OK, ""));
		when(entity.getContent()).thenReturn(new ByteArrayInputStream("contents".getBytes()));
		when(response.getEntity()).thenReturn(entity);
		when(httpClient.execute(any())).thenReturn(response);
		
		// exercise
		sut.echoBot(event);
		
		verify(httpClient, times(1)).execute(isA(HttpUriRequest.class));
	}
	
	@Test(expected = LineBotAPIException.class)
	public void testAPIReturnCodeIsNot200() throws Exception {
		// setup
		LineEvent event = LineEventFixture.createLineUserEvent();
		CloseableHttpResponse response = mock(CloseableHttpResponse.class);
		HttpEntity entity = mock(HttpEntity.class);
		when(response.getStatusLine())
			.thenReturn(new BasicStatusLine(HttpVersion.HTTP_1_1, HttpStatus.SC_BAD_REQUEST, ""));
		when(entity.getContent()).thenReturn(new ByteArrayInputStream("contents".getBytes()));
		when(response.getEntity()).thenReturn(entity);
		when(httpClient.execute(any())).thenReturn(response);
		
		// exercise
		sut.echoBot(event);
	}
	
	@Test(expected = LineBotAPIException.class)
	public void testLineBotAPIFailed() throws Exception {
		// setup
		LineEvent event = LineEventFixture.createLineUserEvent();
		when(httpClient.execute(any())).thenThrow(new IOException());
		
		// exercise
		sut.echoBot(event);
	}
	
	@Test
	public void testSignatureCalculationValidity() throws Exception {
		String expected = "2uK7wt4xHTJk7iFSil4GB4oC48Zk4ljXddiGxrvcsG0=";
		String result = sut.calculateRequestSignature("{\"key\": \"value\"}");
		assertThat(result, equalTo(expected));
	}
}
