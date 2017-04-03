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
import java.security.GeneralSecurityException;
import java.util.Collections;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.stereotype.Service;

import org.apache.commons.codec.binary.Base64;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.message.BasicHeader;
import org.apache.http.util.EntityUtils;

import com.fasterxml.jackson.databind.ObjectMapper;

import jp.classmethod.sparrow.configprops.LineBotConfigurationProperties;
import jp.classmethod.sparrow.web.LineWebhookRequest;

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
	
	
	/**
	 * Lineの署名検証を行う
	 * https://devdocs.line.me/ja/#webhooks
	 * @param expected HTTPヘッダ X-Line-Signature の値
	 * @param request Body部
	 * @return ヘッダと計算結果が一致していればtrue. 不一致や計算失敗の場合はfalse
	 */
	public boolean validateRequestSignature(String expected, LineWebhookRequest request) {
		try {
			String result = calculateRequestSignature(objectMapper.writeValueAsString(request));
			return expected.equals(result);
		} catch (IOException | GeneralSecurityException e) {
			log.warn("failed to calculate signature : ", e);
			return false;
		}
	}
	
	String calculateRequestSignature(String requestBody) throws IOException, GeneralSecurityException {
		String channelSecret = configurationProperties.getChannelSecret();
		SecretKeySpec key = new SecretKeySpec(channelSecret.getBytes(StandardCharsets.UTF_8), "HmacSHA256");
		Mac mac = Mac.getInstance("HmacSHA256");
		mac.init(key);
		byte[] source = requestBody.getBytes(StandardCharsets.UTF_8);
		String result = Base64.encodeBase64String(mac.doFinal(source));
		log.debug("signature calculation result : {}", result);
		return result;
	}
	
	public void echoBot(LineEvent event) {
		if (isTextMessage(event) == false) {
			return;
		}
		try {
			HttpPost postRequest = buildMessageAPIRequest(event.getReplyToken(), event.getMessage().getText());
			try (CloseableHttpResponse response = httpClient.execute(postRequest)) {
				int statusCode = response.getStatusLine().getStatusCode();
				if (statusCode != 200) {
					log.error("failed to send message: {} : {}", statusCode,
							EntityUtils.toString(response.getEntity()));
					throw new LineBotAPIException("API request did not succeed.");
				}
			} catch (IOException e) {
				throw new LineBotAPIException("I/O error in LINE bot API call", e);
			}
		} catch (IOException e) {
			log.error("failed to build API request : ", e);
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
