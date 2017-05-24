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

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;

import java.util.List;

import org.junit.Test;

import jp.classmethod.sparrow.model.LineEvent;
import jp.classmethod.sparrow.model.LineEventFixture;
import jp.classmethod.sparrow.model.LineMessageEntity;
import jp.classmethod.sparrow.model.LineMessageEntityFixture;
import jp.classmethod.sparrow.model.LineMessageFixture;

/**
 * Created by kunita.fumiko on 2017/05/08.
 */
public class InMemoryCalculatorRepositoryTest {
	
	InMemoryCalculatorRepository sut = new InMemoryCalculatorRepository();
	
	
	/**
	 * saveメソッドの引数と戻り値が一致していることを確認します
	 */
	@Test
	public void testSave() {
		// setup
		LineEvent startEvent = LineEventFixture.createLineUserEvent(LineMessageFixture.createStartLineMessage());
		LineMessageEntity startLineMessageEntity = LineMessageEntityFixture.createLineEntity(startEvent);
		// exercise
		LineMessageEntity actual = sut.save(startLineMessageEntity);
		// verify
		assertThat(actual, is(startLineMessageEntity));
	}
	
	/**
	 *  リスト検索し、指定のuIdと一致するリストのみ返すことを確認する
	 */
	@Test
	public void testFindByUser() {
		// setup
		LineEvent startEvent = LineEventFixture.createLineUserEvent(LineMessageFixture.createStartLineMessage());
		LineMessageEntity startLineMessageEntity = LineMessageEntityFixture.createLineEntity(startEvent);
		LineEvent startEvent2 = LineEventFixture.createLineUserEvent2(LineMessageFixture.createStartLineMessage());
		LineMessageEntity startLineMessageEntity2 = LineMessageEntityFixture.createLineEntity(startEvent2);
		sut.save(startLineMessageEntity);
		sut.save(startLineMessageEntity2);
		// exercise
		List<LineMessageEntity> actual = sut.findByUser("U206d25c2ea6bd87c17655609a1c37cb8", 1, 1);
		// verify
		assertThat(actual, hasSize(1));
		assertThat(actual.get(0).getMessageId(), is("325708"));
		assertThat(actual.get(0).getUserId(), is("U206d25c2ea6bd87c17655609a1c37cb8"));
		assertThat(actual.get(0).getTimestamp(), is(146262947912543L));
		assertThat(actual.get(0).getValue(), is(0));
	}
	
	/**
	 * 存在しないuIdでリスト検索し、空のリストを返すことを確認する
	 */
	@Test
	public void testFindByNotExistUser() {
		// setup
		LineEvent startEvent = LineEventFixture.createLineUserEvent(LineMessageFixture.createStartLineMessage());
		LineMessageEntity startLineMessageEntity = LineMessageEntityFixture.createLineEntity(startEvent);
		sut.save(startLineMessageEntity);
		// exercise
		List<LineMessageEntity> actual = sut.findByUser("U206d25c2ea6bd87c17655609a1c40cb2", 1, 1);
		// verify
		assertThat(actual, hasSize(0));
	}
}
