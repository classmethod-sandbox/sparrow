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

import java.util.List;

/** 
 * Created by mochizukimasao on 2017/04/11.
 * 
 * Calculator repository interface.
 *
 * @author mochizukimasao
 * @since version
 */
public interface CalculatorRepository {
	
	/**
	 * userIdが一致するデータにアクセスし、indexを返します。
	 * @param lineMessageEntity
	 * @return 最新の"start"のindexを返します、startしていない場合はindexは-1を返します
	 * @throws
	 */
	Integer latestStartLine(LineMessageEntity lineMessageEntity) throws StartIndexException;
	
	/**
	 * LineMessageEntityを保存します。
	 *
	 * @param lineMessageEntity
	 * @return 引数で受け取ったLineMessageEntityをそのまま返します
	 */
	LineMessageEntity save(LineMessageEntity lineMessageEntity);
	
	/**
	 * 引数で受け取るuserIdと一致するoffsetの位置(0始まり)からlimitに指定した数以下の要素を降順で返します
	 *
	 * @param userId ユーザーID
	 * @param offset 読み飛ばす行数
	 * @param limit	取得行数
	 * @return offsetからlimitに指定した数以下の要素を返します。一致するデータがない場合は空のコレクションを返します。
	 */
	List<LineMessageEntity> findByUser(String userId, int offset, int limit);
}
