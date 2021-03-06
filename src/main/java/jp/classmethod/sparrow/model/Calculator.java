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

import static org.apache.commons.lang3.math.NumberUtils.isNumber;

import java.util.List;

import lombok.RequiredArgsConstructor;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Created by kunita.fumiko on 2017/04/25.
 */
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class Calculator {
	
	/**
	 * 引数で受け取るLineEventからLineMessageEntityを生成します
	 *
	 * @param event LineEvent情報
	 * @return lineMessageEntity
	 */
	static LineMessageEntity createLineMessageEntity(LineEvent event) {
		LineMessageEntity lineMessageEntity = new LineMessageEntity();
		lineMessageEntity.setMessageId(event.getMessage().getId());
		lineMessageEntity.setUserId(event.getSource().getId());
		lineMessageEntity.setTimestamp(event.getTimestamp());
		lineMessageEntity.setValue(event.getMessage().getText());
		return lineMessageEntity;
	}
	
	
	private final LineMessageEntityRepository lineMessageEntityRepository;
	
	
	/**
	 * LineEventのtextvalueに応じて処理を行い、Linebotがユーザーに返信するメッセージを生成します。
	 *
	 * @param event LineEvent LineEvent情報
	 * @return result Linebotがユーザーに返信するメッセージ(text)
	 */
	@Transactional
	public String save(LineEvent event) {
		String messageText = event.getMessage().getText();
		
		switch (messageText) {
			// メッセージが"total"の場合：最新のresetまでの計算結果を返します。
			case "total":
				return String.valueOf(calculateTotal(event));
			
			// メッセージが"reset"の場合：データベースにLineEntityを保存し、"reset"を返します
			case "reset":
				lineMessageEntityRepository.create(createLineMessageEntity(event));
				return "reset";
			
			// メッセージが数字の場合：データベースにLineEntityを保存し、""を返します
			// 仕様で定義されていない無効な値の場合："error"を返します。
			default:
				if (isNumber(messageText)) {
					lineMessageEntityRepository.create(createLineMessageEntity(event));
					return "";
				} else {
					return "error";
				}
		}
	}
	
	/**
	 * 引数で受け取るLineEventのuserIdと一致するデータの合計値を算出します。
	 *
	 * @param event LineEvent情報
	 * @return total userIdが一致するデータの合計値
	 */
	public int calculateTotal(LineEvent event) {
		int total = 0;	// 合計値
		int offset = 0;	// 表示開始位置
		int limit = 5;	// 表示行数
		String userId = event.getSource().getId();
		
		while (true) {
			List<LineMessageEntity> list = lineMessageEntityRepository.findByUser(userId, offset, limit);
			
			// 空リストの場合はbreak
			if (list.isEmpty()) {
				break;
			}
			
			// resetまで足していく
			for (LineMessageEntity lineMessageValue : list) {
				if (lineMessageValue.getValue().equals("reset")) {
					break;
				} else {
					total += Integer.valueOf(lineMessageValue.getValue());
				}
			}
			
			offset = offset + limit;
		}
		return total;
	}
}
