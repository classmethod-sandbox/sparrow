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

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

import org.springframework.stereotype.Repository;

import jp.classmethod.sparrow.model.LineMessageEntity;
import jp.classmethod.sparrow.model.LineMessageEntityRepository;
import jp.classmethod.sparrow.model.StartIndexException;

/**
 * Created by kunita.fumiko on 2017/04/13.
 */
@Repository
public class InMemoryLineMessageEntityRepository implements LineMessageEntityRepository {
	
	private final ConcurrentHashMap<String, List<LineMessageEntity>> map = new ConcurrentHashMap<>();
	
	
	public int indexOfStarting(String userId) throws StartIndexException {
		if (map.containsKey(userId)) {
			// Listから"reset"を検索する
			for (LineMessageEntity collections : map.get(userId)) {
				String cllectionValue = collections.getValue();
				if (isNumber(cllectionValue) == false && cllectionValue.equals("reset")) {
					return map.get(userId).indexOf(collections) - 1;
				}
			}
			// resetが見つからなかった時
			return map.get(userId).size() - 1;
		}
		throw new StartIndexException();
	}
	
	public LineMessageEntity save(LineMessageEntity lineMessageEntity) {
		String userId = lineMessageEntity.getUserId();
		// LinedListを生成し、常にindex0にLineEntityを保存します
		if (map.containsKey(userId)) {
			map.get(userId).add(0, lineMessageEntity);
		} else {
			List<LineMessageEntity> list = new LinkedList<>();
			list.add(0, lineMessageEntity);
			map.put(userId, list);
		}
		return lineMessageEntity;
	}
	
	public List<LineMessageEntity> findByUser(String userId, int offset, int limit) {
		if (map.containsKey(userId)) {
			int toIndex = offset + limit; // subListは行数ではなくindexを渡す必要があるので調整
			return map.get(userId).subList(offset, toIndex);
		} else {
			return Collections.emptyList();
		}
	}
}
