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
 * @author mochizukimasao
 * @since version
 */
public interface SparrowCalculatorRepository {

	/**
	 * ユーザの発言一覧を返す。
	 * リストはtimestampの降順になるように実装する
	 * @param userId ユーザーID
	 * @param offset
	 * @param limit
	 * @return
	 */
	List<LineMessageEntity> findByUser(String userId, int offset, int limit);
	
	LineMessageEntity save(LineMessageEntity messageEntity);
}
