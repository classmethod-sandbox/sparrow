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

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.stereotype.Service;

import jp.classmethod.sparrow.web.LineWebhookRequest;

/**
 * Created by kunita.fumiko on 2017/04/13.
 */
@Slf4j
@RequiredArgsConstructor
@Service
public class CalcService {
	
	private final Calculator calculator;
	
	private final LineBotService lineBotService;
	
	
	/**
	 * リクエスト本体を処理する
	 * 署名検証して、おかしい場合はIllegalArgumentExceptionを投げる
	 * JSON文字列をWebhookリクエスト型に変換する
	 * リクエストメッセージの内容を検証し、内容に応じた処理をする
	 * TODO どんな処理するの？（あとで書く）
	 *
	 * @param signature   リクエストの署名
	 * @param requestBody リクエストの本体
	 * @throws IllegalArgumentException 署名検証しておかしい場合
	 */
	public void process(String signature, String requestBody) {
		if (lineBotService.validateRequestSignature(signature, requestBody) == false) {
			throw new IllegalArgumentException();
		}
		try {
			LineWebhookRequest webhookRequest = lineBotService.deserializeRequest(requestBody);
			webhookRequest.getEvents().stream()
				.peek(event -> log.info("{}", event))
				.forEach((LineEvent event) -> {
					String responseText = calculator.save(event);
					if (responseText.isEmpty() == false) {
						//レスポンスを返す
						lineBotService.echoBot(event, responseText);
					}
				});
		} catch (LineBotAPIException e) {
			// Webhook requestには200を返せというドキュメント記載があるので
			// ここでは500を返さないようにしている
			log.error("LINE API call failed : ", e);
			
		} catch (IOException e) {
			log.error("error serializing input : ", e);
		}
	}
}
