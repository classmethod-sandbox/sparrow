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
	 * リクエストのテキストメッセージに応じて処理を振り分けます。
	 * start:repositoryのsaveメソッドを起動してmapを作成します。返り値は"calc message start".
	 * 数字:repositoryのsaveメソッドを起動して値を保存します。返り値は"".
	 * end:calculateTotalメソッドを起動して保存リストの合計を算出します。返り値は計算結果.
	 * reset:保存リストの合計を算出し、合計が0になる値をsaveします。返り値は"".
	 * それ以外：無効な値として何も処理しません。返り値は"".
	 *
	 * @param event リクエストの内容
	 * @return result レスポンスするテキストメッセージ（リクエストのテキストメッセージに応じて異なる値を返す）
	 */
	public String save(LineEvent event) {
		String messageText;
		messageText = event.getMessage().getText();
		String result = null;
		
		switch (messageText) {
			case "end":
				//計算結果を代入する
				result = String.valueOf(calculateTotal(createLineMessageEntity(event, messageText)));
				resetList(createLineMessageEntity(event, messageText));
				break;
			
			case "reset":
				resetList(createLineMessageEntity(event, messageText));
				//resetの場合は発言しない
				result = "reset";
				break;
			
			default:
				if (messageText.equals("start") || isNumber(messageText)) {
					if (messageText.equals("start")) {
						calculatorRepository.save(createLineMessageEntity(event, messageText));
						result = "calc mode start";
					} else {
						//mapが既に存在するかFindByUserで確認する
						List<LineMessageEntity> list = calculatorRepository.findByUser(event.getSource().getId(), 1, 2);
						if (list.isEmpty() == false) {
							//数字の時はmapが存在した時のみsaveする
							calculatorRepository.save(createLineMessageEntity(event, messageText));
						}
						result = "";
					}
				} else {
					result = "error";
				}
				break;
		}
		return result;
	}
	
	/**
	 * repositoryからユーザーIDが一致するリストを取得します。
	 * 取得したリストからvalueの合計を算出します。
	 * @param lineMessageEntity リクエストの情報
	 * @return 計算結果
	 */
	public Integer calculateTotal(LineMessageEntity lineMessageEntity) {
		String uId = lineMessageEntity.getUserId();
		List<LineMessageEntity> list = calculatorRepository.findByUser(uId, 1, 2);
		Integer total = 0;
		for (LineMessageEntity lineMessageValue : list) {
			total += lineMessageValue.getValue();
		}
		return total;
	}
	
	/**
	 * uIdが一致するリストの合計結果を算出します。
	 * リスト合計が0になるよう符号を反転し、LineEntityのValueを再セットします。
	 * LineEntityをリストに追加します。（リスト合計は0になります）
	 * @param lineMessageEntity リクエストの情報
	 * @return 再計算した合計結果
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
