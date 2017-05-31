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

import static org.apache.commons.lang3.math.NumberUtils.isNumber;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

import org.springframework.stereotype.Repository;

import jp.classmethod.sparrow.model.CalculatorRepository;
import jp.classmethod.sparrow.model.LineMessageEntity;
import jp.classmethod.sparrow.model.StartIndexException;

/**
 * Created by kunita.fumiko on 2017/04/13.
 */
@Repository
public class InMemoryCalculatorRepository implements CalculatorRepository {
	
	private final ConcurrentHashMap<String, List<LineMessageEntity>> map = new ConcurrentHashMap<>();
	
	
	public int indexOfLatestStart(String userId) throws StartIndexException {
		if (map.containsKey(userId)) {
			// userIdが一致するリストを取得
			List<LineMessageEntity> list = map.get(userId);
			// 降順にsort
			Collections.sort(list, (e1, e2) -> Long.compare(e2.getTimestamp(), e1.getTimestamp()));
			
			// collectionから"start"を検索する
			for (LineMessageEntity collections : list) {
				String cllectionValue = collections.getValue();
				if (isNumber(cllectionValue) == false && cllectionValue.equals("start")) {
					return map.get(userId).indexOf(collections);
				}
			}
		}
		throw new StartIndexException();
	}
	
	public LineMessageEntity save(LineMessageEntity lineMessageEntity) {
		String userId = lineMessageEntity.getUserId();
		if (map.containsKey(userId)) {
			map.get(userId).add(lineMessageEntity);
		} else {
			List<LineMessageEntity> list = new ArrayList<>();
			list.add(lineMessageEntity);
			map.put(userId, list);
		}
		return lineMessageEntity;
	}
	
	public List<LineMessageEntity> findByUser(String userId, int offset, int limit) {
		if (map.containsKey(userId)) {
			int toIndex = offset + limit; // subListは行数ではなくindexを渡す必要があるので調整
			// 降順にsort
			Collections.sort(map.get(userId), (e1, e2) -> Long.compare(e2.getTimestamp(), e1.getTimestamp()));
			return map.get(userId).subList(offset, toIndex);
		} else {
			return Collections.emptyList();
		}
	}
}
