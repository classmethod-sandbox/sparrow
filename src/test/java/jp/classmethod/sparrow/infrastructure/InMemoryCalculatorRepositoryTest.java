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
import jp.classmethod.sparrow.model.StartIndexException;

/**
 * Created by kunita.fumiko on 2017/05/08.
 */
public class InMemoryCalculatorRepositoryTest {
	
	InMemoryCalculatorRepository sut = new InMemoryCalculatorRepository();
	
	
	/**
	 * 計算開始状態の時、startのindex(1)を返すことを確認します
	 */
	@Test
	public void testLatestStartLine() throws StartIndexException {
		// setup
		LineEvent startEvent =
				LineEventFixture.createStartLineUserEvent("U206d25c2ea6bd87c17655609a1c37cb8", 1499378820);
		LineEvent numberEvent1 =
				LineEventFixture.createNumberLineUserEvent("U206d25c2ea6bd87c17655609a1c37cb8", 1499378880);
		LineEvent numberEvent2 =
				LineEventFixture.createNumberLineUserEvent("U206d25c2ea6bd87c17655609a1c37cb8", 1499378881);
		LineMessageEntity startLineMessageEntity = LineMessageEntityFixture.createLineEntity(startEvent);
		LineMessageEntity numberLineMessageEntity1 = LineMessageEntityFixture.createLineEntity(numberEvent1);
		LineMessageEntity numberLineMessageEntity2 = LineMessageEntityFixture.createLineEntity(numberEvent2);
		sut.save(startLineMessageEntity);
		sut.save(numberLineMessageEntity1);
		
		// exercise
		int actual = sut.latestStartLine(numberLineMessageEntity2);
		
		// verify
		assertThat(actual, is(1));
	}
	
	/**
	 * 計算未開始状態の時、StartIndexExceptionが発生することを確認します
	 */
	@Test(expected = StartIndexException.class)
	public void testNoLatestStartLine() throws StartIndexException {
		// setup
		LineEvent numberEvent =
				LineEventFixture.createNumberLineUserEvent("U206d25c2ea6bd87c17655609a1c37cb8", 1499378880);
		LineMessageEntity numberLineMessageEntity = LineMessageEntityFixture.createLineEntity(numberEvent);
		
		// exercise
		sut.latestStartLine(numberLineMessageEntity);
	}
	
	/**
	 * saveメソッドの引数と戻り値が一致していることを確認します
	 */
	@Test
	public void testSave() {
		// setup
		LineEvent startEvent =
				LineEventFixture.createStartLineUserEvent("U206d25c2ea6bd87c17655609a1c37cb8", 1499378820);
		LineMessageEntity startLineMessageEntity = LineMessageEntityFixture.createLineEntity(startEvent);
		
		// exercise
		LineMessageEntity actual = sut.save(startLineMessageEntity);
		
		// verify
		assertThat(actual, is(startLineMessageEntity));
	}
	
	/**
	 *  指定のuserIdと一致しているリストを返すことを確認する
	 */
	@Test
	public void testFindByUser() throws StartIndexException {
		// setup
		LineEvent startEventUser1 =
				LineEventFixture.createStartLineUserEvent("U206d25c2ea6bd87c17655609a1c37cb8", 1499378820);
		LineEvent numberEvent1 =
				LineEventFixture.createNumberLineUserEvent("U206d25c2ea6bd87c17655609a1c37cb8", 1499378880);
		LineEvent numberEvent2 =
				LineEventFixture.createNumberLineUserEvent("U206d25c2ea6bd87c17655609a1c37cb8", 1499378881);
		LineEvent endEvent =
				LineEventFixture.createEndLineUserEvent("U206d25c2ea6bd87c17655609a1c37cb8", 1499378886);
		LineEvent startEventUser2 =
				LineEventFixture.createStartLineUserEvent("U206d25c2ea6bd87c17655609a1c37cb9", 1499378821);
		
		LineMessageEntity startLineMessageEntityUser1 = LineMessageEntityFixture.createLineEntity(startEventUser1);
		LineMessageEntity numberLineMessageEntity1 = LineMessageEntityFixture.createLineEntity(numberEvent1);
		LineMessageEntity numberLineMessageEntity2 = LineMessageEntityFixture.createLineEntity(numberEvent2);
		LineMessageEntity endLineMessageEntity = LineMessageEntityFixture.createLineEntity(endEvent);
		LineMessageEntity startLineMessageEntityUser2 = LineMessageEntityFixture.createLineEntity(startEventUser2);
		
		sut.save(startLineMessageEntityUser1);
		sut.save(numberLineMessageEntity1);
		sut.save(numberLineMessageEntity2);
		sut.save(endLineMessageEntity);
		sut.latestStartLine(endLineMessageEntity);
		sut.save(startLineMessageEntityUser2);
		
		// exercise
		List<LineMessageEntity> actual = sut.findByUser("U206d25c2ea6bd87c17655609a1c37cb8", 0, 2);
		
		// verify
		assertThat(actual, hasSize(2));
		assertThat(actual.get(0).getMessageId(), is("325709"));
		assertThat(actual.get(0).getUserId(), is("U206d25c2ea6bd87c17655609a1c37cb8"));
		assertThat(actual.get(0).getTimestamp(), is(1499378886L));
		assertThat(actual.get(0).getValue(), is("end"));
		assertThat(actual.get(1).getMessageId(), is("325711"));
		assertThat(actual.get(1).getUserId(), is("U206d25c2ea6bd87c17655609a1c37cb8"));
		assertThat(actual.get(1).getTimestamp(), is(1499378881L));
		assertThat(actual.get(1).getValue(), is("12"));
	}
	
	/**
	 *  指定のuserIdと一致しているリストの2ページ目を返すことを確認する
	 */
	@Test
	public void testFindByUserNextPage() throws StartIndexException {
		// setup
		LineEvent startEventUser1 =
				LineEventFixture.createStartLineUserEvent("U206d25c2ea6bd87c17655609a1c37cb8", 1499378820);
		LineEvent numberEvent1 =
				LineEventFixture.createNumberLineUserEvent("U206d25c2ea6bd87c17655609a1c37cb8", 1499378880);
		LineEvent numberEvent2 =
				LineEventFixture.createNumberLineUserEvent("U206d25c2ea6bd87c17655609a1c37cb8", 1499378881);
		LineEvent endEvent =
				LineEventFixture.createEndLineUserEvent("U206d25c2ea6bd87c17655609a1c37cb8", 1499378886);
		LineEvent startEventUser2 =
				LineEventFixture.createStartLineUserEvent("U206d25c2ea6bd87c17655609a1c37cb9", 1499378821);
		
		LineMessageEntity startLineMessageEntityUser1 = LineMessageEntityFixture.createLineEntity(startEventUser1);
		LineMessageEntity numberLineMessageEntity1 = LineMessageEntityFixture.createLineEntity(numberEvent1);
		LineMessageEntity numberLineMessageEntity2 = LineMessageEntityFixture.createLineEntity(numberEvent2);
		LineMessageEntity endLineMessageEntity = LineMessageEntityFixture.createLineEntity(endEvent);
		LineMessageEntity startLineMessageEntityUser2 = LineMessageEntityFixture.createLineEntity(startEventUser2);
		
		sut.save(startLineMessageEntityUser1);
		sut.save(numberLineMessageEntity1);
		sut.save(numberLineMessageEntity2);
		sut.save(endLineMessageEntity);
		sut.latestStartLine(endLineMessageEntity);
		sut.save(startLineMessageEntityUser2);
		
		// exercise
		List<LineMessageEntity> actual = sut.findByUser("U206d25c2ea6bd87c17655609a1c37cb8", 2, 2);
		
		// verify
		assertThat(actual, hasSize(2));
		assertThat(actual.get(0).getMessageId(), is("325711"));
		assertThat(actual.get(0).getUserId(), is("U206d25c2ea6bd87c17655609a1c37cb8"));
		assertThat(actual.get(0).getTimestamp(), is(1499378880L));
		assertThat(actual.get(0).getValue(), is("12"));
		assertThat(actual.get(1).getMessageId(), is("325708"));
		assertThat(actual.get(1).getUserId(), is("U206d25c2ea6bd87c17655609a1c37cb8"));
		assertThat(actual.get(1).getTimestamp(), is(1499378820L));
		assertThat(actual.get(1).getValue(), is("start"));
		
	}
	
	/**
	 * 存在しないuserIdで検索し、空のリストを返すことを確認する
	 */
	@Test
	public void testFindByNotExistUser() throws StartIndexException {
		// setup
		LineEvent startEvent =
				LineEventFixture.createStartLineUserEvent("U206d25c2ea6bd87c17655609a1c37cb8", 1499378820);
		LineMessageEntity startLineMessageEntity = LineMessageEntityFixture.createLineEntity(startEvent);
		sut.save(startLineMessageEntity);
		sut.latestStartLine(startLineMessageEntity);
		
		// exercise
		List<LineMessageEntity> actual = sut.findByUser("U206d25c2ea6bd87c17655609a1c40cb2", 0, 5);
		
		// verify
		assertThat(actual, hasSize(0));
	}
}
