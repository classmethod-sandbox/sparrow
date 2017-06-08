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

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Repository;

import jp.classmethod.sparrow.model.LineMessageEntity;
import jp.classmethod.sparrow.model.LineMessageEntityRepository;

/**
 * Created by kunita.fumiko on 2017/04/13.
 */
@Repository
@Profile("!aws")
public class InMemoryLineMessageEntityRepository implements LineMessageEntityRepository {
	
	private final ConcurrentHashMap<String, List<LineMessageEntity>> map = new ConcurrentHashMap<>();
	
	
	public LineMessageEntity save(LineMessageEntity lineMessageEntity) {
		String userId = lineMessageEntity.getUserId();
		// LinedListを生成し、常にindex0にLineEntityを保存します
		map.computeIfAbsent(userId, k -> new LinkedList<>()).add(0, lineMessageEntity);
		return lineMessageEntity;
	}
	
	public List<LineMessageEntity> findByUser(String userId, int offset, int limit) {
		if (map.containsKey(userId)) {
			List<LineMessageEntity> list = map.get(userId);
			int listSize = list.size();
			// offsetがlistSizeより大きい場合は空リストを返す
			if (listSize < offset) {
				return Collections.emptyList();
			}
			
			int toIndex;
			if (listSize > offset + limit) {
				toIndex = offset + limit; // subListは行数ではなくindexを渡す必要があるので調整
			} else {
				toIndex = listSize;
			}
			return list.subList(offset, toIndex);
		} else {
			return Collections.emptyList();
		}
	}
}
