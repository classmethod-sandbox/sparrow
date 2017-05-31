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

/**
 * Created by kunita.fumiko on 2017/04/25.
 */
@Service
@RequiredArgsConstructor
public class Calculator {
	
	private final CalculatorRepository calculatorRepository;
	
	
	/**
	 * LineEventのtextvalueに応じて処理を行い、Linebotがユーザーに返信するメッセージを生成します。
	 *
	 * @param event LineEvent LineEvent情報
	 * @return result Linebotがユーザーに返信するメッセージ(text)
	 */
	public String save(LineEvent event) {
		String messageText;
		messageText = event.getMessage().getText();
		String userId = event.getSource().getId();
		String result = null;
		
		switch (messageText) {
			/* メッセージが"start"の場合:
			 * データを登録し、"calc mode start"を返します。
			 */
			case "start":
				calculatorRepository.save(createLineMessageEntity(event, messageText));
				result = "calc mode start";
				break;
			
			/* メッセージが"end"の場合:
			 * 計算がstartしているかを確認します。
			 * startしている場合：計算を行い、計算結果を返します。計算後は登録データをリセットします。
			 * startしていない場合：""を返します。*/
			case "end":
				if (calculatorRepository.isStarted(userId)) {
					result = String.valueOf(calculateTotal(createLineMessageEntity(event, messageText)));
					resetList(createLineMessageEntity(event, messageText));
				} else {
					result = "";
				}
				break;
			
			/* メッセージが"reset"の場合：
			 * 計算がstartしているかを確認します。
			 * startしている場合：登録データをリセットし、"reset"を返します。
			 * startしていない場合：""を返します。 */
			case "reset":
				if (calculatorRepository.isStarted(userId)) {
					resetList(createLineMessageEntity(event, messageText));
					result = "reset";
				} else {
					result = "";
				}
				break;
			
			/* メッセージが数字または仕様で定義されていない無効な値の場合：
			 * 数値かどうかを確認します。
			 * 数値の場合：計算がstartしているかを確認し、""を返します。
			 * 　startしている場合：データを登録します。
			 * 　startしていない場合：何も行いません。
			 * 数値以外の場合："error"を返します */
			default:
				if (isNumber(messageText)) {
					if (calculatorRepository.isStarted(userId)) {
						calculatorRepository.save(createLineMessageEntity(event, messageText));
					}
					result = "";
				} else {
					result = "error";
				}
				break;
		}
		return result;
	}
	
	/**
	 * 受け取ったLineMessageEntityのuserIdと一致するデータを取得し、データの合計値を算出します。
	 *
	 * @param lineMessageEntity LineMessageEntity情報
	 * @return total userIdが一致するデータの合計値
	 */
	public Integer calculateTotal(LineMessageEntity lineMessageEntity) {
		String uId = lineMessageEntity.getUserId();
		int listSize;
		int offset = 0;	// 表示開始位置
		int limit = 5;	// 表示行数
		int total = 0;	// 合計値
		do {
			List<LineMessageEntity> list = calculatorRepository.findByUser(uId, offset, limit);
			for (LineMessageEntity lineMessageValue : list) {
				total += lineMessageValue.getValue();
			}
			offset = offset + limit;
			listSize = list.size();
		} while (listSize >= offset);
		return total;
	}
	
	/**
	 * 受け取ったLineMessageEntityのuserIdと一致するデータの合計値を0にします。
	 * @param lineMessageEntity リクエストの情報
	 * @return 再計算した合計値（常に0を返します）
	 */
	public Integer resetList(LineMessageEntity lineMessageEntity) {
		Integer total = calculateTotal(lineMessageEntity);
		lineMessageEntity.setValue(-total); //符号を反転
		calculatorRepository.save(lineMessageEntity);
		return calculateTotal(lineMessageEntity);
	}
	
	/**
	 * 受け取ったLineEventからLineMessageEntityを生成します
	 * メッセージテキストの内容により異なるLineMessageEntityのValueをセットします。
	 * @param event LineEvent情報
	 * @param messageText リクエストのテキストメッセージの値
	 * @return lineMessageEntity
	 */
	public LineMessageEntity createLineMessageEntity(LineEvent event, String messageText) {
		LineMessageEntity lineMessageEntity = new LineMessageEntity();
		lineMessageEntity.setMessageId(event.getMessage().getId());
		lineMessageEntity.setUserId(event.getSource().getId());
		lineMessageEntity.setTimestamp(event.getTimestamp());
		
		switch (messageText) {
			case "start":
			case "end":
			case "reset":
				lineMessageEntity.setValue(0);
				break;
			//　数字の時
			default:
				lineMessageEntity.setValue(Integer.valueOf(event.getMessage().getText()));
				break;
		}
		return lineMessageEntity;
	}
}
