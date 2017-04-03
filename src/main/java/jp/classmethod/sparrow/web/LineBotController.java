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

import java.io.IOException;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import jp.classmethod.sparrow.model.LineBotAPIException;
import jp.classmethod.sparrow.model.LineBotService;

/**
 * Created by mochizukimasao on 2017/03/30.
 *
 * @author mochizukimasao
 * @since version
 */
@RestController
@Slf4j
@RequestMapping(path = "/sparrow")
@RequiredArgsConstructor
public class LineBotController {
	
	private final LineBotService botService;
	
	
	@RequestMapping(method = RequestMethod.POST)
	public ResponseEntity<Void> receiveWebhook(@RequestHeader(value = "X-Line-Signature") String signature,
			@RequestBody String requestBody) {
		log.info("signature: {}", signature);
		if (botService.validateRequestSignature(signature, requestBody) == false) {
			log.warn("signature did not match");
			return ResponseEntity.badRequest().build();
		}
		try {
			LineWebhookRequest webhookRequest = botService.deserializeRequest(requestBody);
			webhookRequest.getEvents().stream()
				.peek(event -> log.info("{}", event))
				.forEach(event -> {
					try {
						botService.echoBot(event);
					} catch (LineBotAPIException e) {
						// Webhook requestには200を返せというドキュメント記載があるので
						// ここでは500を返さないようにしている
						log.error("LINE API call failed : ", e);
					}
				});
		} catch (IOException e) {
			log.error("error serializing input : ", e);
		}
		
		return ResponseEntity.ok().build();
	}
}
