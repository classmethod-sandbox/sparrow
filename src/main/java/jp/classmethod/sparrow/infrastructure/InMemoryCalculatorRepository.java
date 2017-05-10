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
package jp.classmethod.sparrow.infrastructure;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

import org.springframework.stereotype.Repository;

import jp.classmethod.sparrow.model.CalculatorRepository;
import jp.classmethod.sparrow.model.LineMessageEntity;

/**
 * Created by kunita.fumiko on 2017/04/13.
 */
@Repository
public class InMemoryCalculatorRepository implements CalculatorRepository {
	
	private final ConcurrentHashMap<String, List<LineMessageEntity>> map = new ConcurrentHashMap<>();
	
	
	/**
	 * mapにuIdが存在する場合は、リストにlineEntityを追加する
	 * mapにuIdが存在しない場合は、リストを新規作成してmapに追加する
	 * @param lineMessageEntity 保存したい対象
	 * @return 保存したリスト
	 */
	public List<LineMessageEntity> save(LineMessageEntity lineMessageEntity) {
		String uId = lineMessageEntity.getUserId();
		if (map.containsKey(uId)) {
			(map.get(uId)).add(lineMessageEntity);
		} else {
			ArrayList<LineMessageEntity> list = new ArrayList<>();
			list.add(lineMessageEntity);
			map.put(uId, list);
		}
		return map.get(uId);
	}
	
	/**
	 * リクエストのユーザーIDと一致するデータを抽出する
	 * @param userId ユーザーID
	 * @param offset 取得にあたって読み飛ばす
	 * @param limit　取得にあたって制限する個数
	 * @return ユーザーIDが一致するリスト（該当するIDが存在しない場合は空のリストを返す）
	 */
	public List<LineMessageEntity> findByUser(String userId, int offset, int limit) {
		//先にこっち書く。getしたりnull判定したりする
		List<LineMessageEntity> result;
		if (map.containsKey(userId)) {
			//keyが一致するリストを返す
			result = map.get(userId);
		} else {
			//空のリストを返す
			result = Collections.emptyList();
		}
		return result;
	}
}
