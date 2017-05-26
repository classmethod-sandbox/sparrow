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
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import jp.classmethod.sparrow.model.CalcService;

/**
 * Created by mochizukimasao on 2017/03/30.
 *
 * @author mochizukimasao
 * @since version
 */
@RestController
@RequestMapping(path = "/sparrow")
@Slf4j
@RequiredArgsConstructor
public class LineBotController {
	
	private final CalcService calcService;
	
	
	@RequestMapping(method = RequestMethod.POST)
	public ResponseEntity<Void> receiveWebhook(@RequestHeader(value = "X-Line-Signature") String signature,
			@RequestBody String requestBody) {
		log.info("signature: {}", signature);
		try {
			calcService.process(signature, requestBody);
			return ResponseEntity.ok().build();
			
		} catch (IllegalArgumentException e) {
			log.warn("signature did not match");
			return ResponseEntity.badRequest().build();
		}
	}
}
