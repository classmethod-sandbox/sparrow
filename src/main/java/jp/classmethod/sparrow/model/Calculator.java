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
		String messageText = event.getMessage().getText();
		
		switch (messageText) {
			/* メッセージが"start"の場合:データを登録し、"calc mode start"を返します。 */
			case "start":
				calculatorRepository.save(createLineMessageEntity(event));
				return "calc mode start";
			
			/* メッセージが"end"の場合：計算開始状態かを確認し、以下の結果を返します。
			 * 計算を開始している場合：登録データにアクセスし、計算結果を返します。
			 * 計算を開始していない場合：""を返します。*/
			case "end":
				if (isStarted(event)) {
					calculatorRepository.save(createLineMessageEntity(event));
					return String.valueOf(calculateTotal(event));
				} else {
					return "";
				}
				
				/* メッセージが"reset"の場合：計算開始状態かを確認し、以下の結果を返します。
				 * 計算を開始している場合：登録データをリセットし、"reset"を返します。
				 * 計算を開始していない場合：""を返します。 */
			case "reset":
				if (isStarted(event)) {
					calculatorRepository.save(createLineMessageEntity(event));
					return "reset";
				} else {
					return "";
				}
				
				/* メッセージが数字または仕様で定義されていない無効な値の場合：数値かどうかを確認し、以下の結果を返します。
				 * 数値の場合：""を返します。
				 * 数値以外の場合："error"を返します */
			default:
				if (isNumber(messageText)) {
					if (isStarted(event)) {
						calculatorRepository.save(createLineMessageEntity(event));
					}
					return "";
				} else {
					return "error";
				}
		}
	}
	
	/**
	 *  引数で受けLineEventのuserが計算開始状態かを確認します
	 *
	 *  @param event LineEvent情報
	 *  @return 計算開始状態の場合はtrue、未開始の場合はfalseを返します
	 */
	public boolean isStarted(LineEvent event) {
		try {
			calculatorRepository.latestStartLine(createLineMessageEntity(event));
		} catch (StartIndexException e) {
			return false;
		}
		return true;
	}
	
	/**
	 * 取る引数で受け取るLineMessageEntityのuserIdと一致するデータを取得し、データの合計値を算出します。
	 *
	 * @param event LineEvent情報
	 * @return total userIdが一致するデータの合計値
	 */
	public Integer calculateTotal(LineEvent event) {
		int total = 0;	// 合計値
		int startLine;
		String userId = event.getSource().getId();
		// startのindex取得
		try {
			startLine = calculatorRepository.latestStartLine(createLineMessageEntity(event));
		} catch (StartIndexException e) {
			return null;
		}
		if (startLine > 0) {
			int offset = 0;	// 表示開始位置
			int limit = 5;	// 表示行数
			while (startLine >= offset) {
				// データが取得行数(limit)以下の場合、取得行数を調整
				if ((startLine - offset) < limit) {
					limit = (startLine - offset) + 1;
				}
				List<LineMessageEntity> list = calculatorRepository.findByUser(userId, offset, limit);
				for (LineMessageEntity lineMessageValue : list) {
					if (isNumber(lineMessageValue.getValue())) {
						total += Integer.valueOf(lineMessageValue.getValue());
					}
				}
				offset = offset + limit;
				limit = (startLine - offset) + 1;
			}
		}
		return total;
	}
	
	/**
	 * 引数で受け取るLineEventからLineMessageEntityを生成します
	 *
	 * @param event LineEvent情報
	 * @return lineMessageEntity
	 */
	public LineMessageEntity createLineMessageEntity(LineEvent event) {
		LineMessageEntity lineMessageEntity = new LineMessageEntity();
		lineMessageEntity.setMessageId(event.getMessage().getId());
		lineMessageEntity.setUserId(event.getSource().getId());
		lineMessageEntity.setTimestamp(event.getTimestamp());
		lineMessageEntity.setValue(event.getMessage().getText());
		return lineMessageEntity;
	}
}
