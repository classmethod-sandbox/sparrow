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

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
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
	public ResponseEntity<String> receiveWebhook(@RequestBody LineWebhookRequest webhookRequest) {
		webhookRequest.getEvents().forEach(event -> {
			log.info(event.toString());
			try {
				botService.echoBot(event);
			} catch (LineBotAPIException e) {
				// Webhook requestには200を返せというドキュメント記載があるので
				// ここでは500を返さないようにしている
				log.error("LINE API call failed : ", e);
			}
		});
		
		return ResponseEntity.ok("");
	}
}
