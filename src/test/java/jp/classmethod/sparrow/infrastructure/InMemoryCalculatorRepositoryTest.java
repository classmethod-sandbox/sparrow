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

/**
 * Created by kunita.fumiko on 2017/05/08.
 */
public class InMemoryCalculatorRepositoryTest {
	
	InMemoryCalculatorRepository sut = new InMemoryCalculatorRepository();
	
	
	/**
	 * 計算がstartしている時にisStartedメソッドがtrueを返すことを確認します
	 */
	@Test
	public void testIsStarted() {
		// setup
		LineEvent startEvent =
				LineEventFixture.createStartLineUserEvent("U206d25c2ea6bd87c17655609a1c37cb8", 2017052600);
		LineMessageEntity startLineMessageEntity = LineMessageEntityFixture.createLineEntity(startEvent);
		sut.save(startLineMessageEntity);
		
		// exercise
		boolean actual = sut.isStarted("U206d25c2ea6bd87c17655609a1c37cb8");
		
		// verify
		assertThat(actual, is(true));
	}
	
	/**
	 * 計算がstartしていない時にisStartedメソッドがfalseを返すことを確認します
	 */
	@Test
	public void testIsNotStarted() {
		// exercise
		boolean actual = sut.isStarted("U206d25c2ea6bd87c17655609a1c37cb8");
		
		// verify
		assertThat(actual, is(false));
	}
	
	/**
	 * saveメソッドの引数と戻り値が一致していることを確認します
	 */
	@Test
	public void testSave() {
		// setup
		LineEvent startEvent =
				LineEventFixture.createStartLineUserEvent("U206d25c2ea6bd87c17655609a1c37cb8", 2017052600);
		LineMessageEntity startLineMessageEntity = LineMessageEntityFixture.createLineEntity(startEvent);
		
		// exercise
		LineMessageEntity actual = sut.save(startLineMessageEntity);
		
		// verify
		assertThat(actual, is(startLineMessageEntity));
	}
	
	/**
	 *  指定のuIdと一致しているoffset,limitで指定された要素で構成されたリストを返すことを確認する
	 */
	@Test
	public void testFindByUser() {
		// setup
		LineEvent startEventUser1 =
				LineEventFixture.createStartLineUserEvent("U206d25c2ea6bd87c17655609a1c37cb8", 2017052600);
		LineEvent numberEvent1 =
				LineEventFixture.createNumberLineUserEvent("U206d25c2ea6bd87c17655609a1c37cb8", 2017052610);
		LineEvent numberEvent2 =
				LineEventFixture.createNumberLineUserEvent("U206d25c2ea6bd87c17655609a1c37cb8", 2017052611);
		LineEvent numberEvent3 =
				LineEventFixture.createNumberLineUserEvent("U206d25c2ea6bd87c17655609a1c37cb8", 2017052612);
		LineEvent numberEvent4 =
				LineEventFixture.createNumberLineUserEvent("U206d25c2ea6bd87c17655609a1c37cb8", 2017052613);
		LineEvent numberEvent5 =
				LineEventFixture.createNumberLineUserEvent("U206d25c2ea6bd87c17655609a1c37cb8", 2017052614);
		LineEvent numberEvent6 =
				LineEventFixture.createNumberLineUserEvent("U206d25c2ea6bd87c17655609a1c37cb8", 2017052615);
		LineEvent startEventUser2 =
				LineEventFixture.createStartLineUserEvent("U206d25c2ea6bd87c17655609a1c37cb9", 2017052601);
		
		LineMessageEntity startLineMessageEntityUser1 = LineMessageEntityFixture.createLineEntity(startEventUser1);
		LineMessageEntity numberLineMessageEntity1 = LineMessageEntityFixture.createLineEntity(numberEvent1);
		LineMessageEntity numberLineMessageEntity2 = LineMessageEntityFixture.createLineEntity(numberEvent2);
		LineMessageEntity numberLineMessageEntity3 = LineMessageEntityFixture.createLineEntity(numberEvent3);
		LineMessageEntity numberLineMessageEntity4 = LineMessageEntityFixture.createLineEntity(numberEvent4);
		LineMessageEntity numberLineMessageEntity5 = LineMessageEntityFixture.createLineEntity(numberEvent5);
		LineMessageEntity numberLineMessageEntity6 = LineMessageEntityFixture.createLineEntity(numberEvent6);
		LineMessageEntity startLineMessageEntityUser2 = LineMessageEntityFixture.createLineEntity(startEventUser2);
		sut.save(startLineMessageEntityUser1);
		sut.save(numberLineMessageEntity1);
		sut.save(numberLineMessageEntity2);
		sut.save(numberLineMessageEntity3);
		sut.save(numberLineMessageEntity4);
		sut.save(numberLineMessageEntity5);
		sut.save(numberLineMessageEntity6);
		sut.save(startLineMessageEntityUser2);
		
		// exercise
		List<LineMessageEntity> actual = sut.findByUser("U206d25c2ea6bd87c17655609a1c37cb8", 5, 5);
		
		// verify
		assertThat(actual, hasSize(2));
		assertThat(actual.get(0).getMessageId(), is("325711"));
		assertThat(actual.get(0).getUserId(), is("U206d25c2ea6bd87c17655609a1c37cb8"));
		assertThat(actual.get(0).getTimestamp(), is(2017052614L));
		assertThat(actual.get(0).getValue(), is(12));
		assertThat(actual.get(1).getMessageId(), is("325711"));
		assertThat(actual.get(1).getUserId(), is("U206d25c2ea6bd87c17655609a1c37cb8"));
		assertThat(actual.get(1).getTimestamp(), is(2017052615L));
		assertThat(actual.get(1).getValue(), is(12));
	}
	
	/**
	 * 存在しないuIdでリスト検索し、空のリストを返すことを確認する
	 */
	@Test
	public void testFindByNotExistUser() {
		// setup
		LineEvent startEvent =
				LineEventFixture.createStartLineUserEvent("U206d25c2ea6bd87c17655609a1c37cb8", 2017052600);
		LineMessageEntity startLineMessageEntity = LineMessageEntityFixture.createLineEntity(startEvent);
		sut.save(startLineMessageEntity);
		
		// exercise
		List<LineMessageEntity> actual = sut.findByUser("U206d25c2ea6bd87c17655609a1c40cb2", 0, 5);
		
		// verify
		assertThat(actual, hasSize(0));
	}
}
