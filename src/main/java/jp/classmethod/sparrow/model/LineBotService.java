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

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Collections;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.stereotype.Service;

import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.message.BasicHeader;
import org.apache.http.util.EntityUtils;

import com.fasterxml.jackson.databind.ObjectMapper;

import jp.classmethod.sparrow.configprops.LineBotConfigurationProperties;

/**
 * Created by mochizukimasao on 2017/03/30.
 *
 * @author mochizukimasao
 * @since version
 */

@Service
@Slf4j
@RequiredArgsConstructor
public class LineBotService {
	
	private final LineBotConfigurationProperties configurationProperties;
	
	private final ObjectMapper objectMapper;
	
	private final CloseableHttpClient httpClient;
	
	
	public void echoBot(LineEvent event) {
		if (isTextMessage(event) == false) {
			return;
		}
		try {
			HttpPost postRequest = buildMessageAPIRequest(event.getReplyToken(), event.getMessage().getText());
			CloseableHttpResponse response = httpClient.execute(postRequest);
			
			int statusCode = response.getStatusLine().getStatusCode();
			if (statusCode != 200) {
				log.error("failed to send message: {} : {}", statusCode,
						EntityUtils.toString(response.getEntity()));
				throw new LineBotAPIException("API request did not succeed.");
			}
		} catch (IOException e) {
			throw new LineBotAPIException("I/O error in LINE bot API call", e);
		}
	}
	
	private HttpPost buildMessageAPIRequest(String replyToken, String text) throws IOException {
		HttpPost postRequest = new HttpPost(configurationProperties.getMessageApiEndpoint());
		
		// header
		postRequest.setHeader(new BasicHeader("Content-Type", "application/json"));
		postRequest.setHeader(new BasicHeader("Authorization",
				"Bearer " + configurationProperties.getChannelToken()));
		
		// body
		LineMessage lineMessage = new LineMessage();
		lineMessage.setType(LineMessageType.TEXT);
		lineMessage.setText(text);
		LineMessageAPIRequest request = new LineMessageAPIRequest(replyToken, Collections.singletonList(lineMessage));
		StringEntity entity = new StringEntity(
				objectMapper.writeValueAsString(request),
				StandardCharsets.UTF_8);
		postRequest.setEntity(entity);
		
		log.info("{}", lineMessage.toString());
		return postRequest;
	}
	
	private boolean isTextMessage(LineEvent event) {
		return event.getType().equals("message") && event.getMessage().getType().equals(LineMessageType.TEXT);
	}
}
