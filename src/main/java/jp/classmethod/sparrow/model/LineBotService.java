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
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import lombok.extern.slf4j.Slf4j;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import org.apache.commons.io.IOUtils;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicHeader;

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
public class LineBotService {
	
	@Autowired
	LineBotConfigurationProperties configurationProperties;
	
	@Autowired
	ObjectMapper objectMapper;
	
	private static final String MESSAGE_API_ENDPOINT = "https://api.line.me/v2/bot/message/reply";
	
	
	public void echoBot(LineEvent event) {
		if (isTextMessage(event)) {
			
			HttpPost postRequest = new HttpPost(MESSAGE_API_ENDPOINT);
			postRequest.setHeader(new BasicHeader("Content-Type", "application/json"));
			postRequest.setHeader(new BasicHeader("Authorization",
					"Bearer " + configurationProperties.getChannelToken()));
			
			try (CloseableHttpClient httpClient = HttpClients.createSystem()) {
				Map<String, String> message = new HashMap<>();
				message.put("type", "text");
				message.put("text", event.getMessage().getText());
				LineTextMessage body = new LineTextMessage(event.getReplyToken(), Collections.singletonList(message));
				
				StringEntity entity = new StringEntity(
						objectMapper.writeValueAsString(body),
						"UTF-8");
				postRequest.setEntity(entity);
				log.info("{}", IOUtils.toString(postRequest.getEntity().getContent()));
				CloseableHttpResponse response = httpClient.execute(postRequest);
				
				int statusCode = response.getStatusLine().getStatusCode();
				if (statusCode != 200) {
					log.error("failed to send message: {} : {}", statusCode,
							IOUtils.toString(response.getEntity().getContent()));
				}
				
			} catch (IOException e) {
				log.error("error in LINE bot API call", e);
			}
		}
	}
	
	private boolean isTextMessage(LineEvent event) {
		return event.getType().equals("message") && event.getMessage().getType().equals("text");
	}
}
