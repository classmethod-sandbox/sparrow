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
public class InMemoryLineMessageEntityRepositoryTest {
	
	InMemoryLineMessageEntityRepository sut = new InMemoryLineMessageEntityRepository();
	
	
	/**
	 * 計算を開始するindexを返すことを確認します
	 */
	@Test
	public void testIndexOfStart() throws StartIndexException {
		// setup
		LineEvent numberEvent1 =
				LineEventFixture.createNumberLineUserEvent("U206d25c2ea6bd87c17655609a1c37cb8", 1499378880);
		LineEvent numberEvent2 =
				LineEventFixture.createNumberLineUserEvent("U206d25c2ea6bd87c17655609a1c37cb8", 1499378881);
		LineEvent numberEvent3 =
				LineEventFixture.createNumberLineUserEvent("U206d25c2ea6bd87c17655609a1c37cb8", 1499378882);
		
		LineMessageEntity numberLineMessageEntity1 = LineMessageEntityFixture.createLineEntity(numberEvent1);
		LineMessageEntity numberLineMessageEntity2 = LineMessageEntityFixture.createLineEntity(numberEvent2);
		LineMessageEntity numberLineMessageEntity3 = LineMessageEntityFixture.createLineEntity(numberEvent3);
		
		sut.save(numberLineMessageEntity1);
		sut.save(numberLineMessageEntity2);
		sut.save(numberLineMessageEntity3);
		
		// exercise
		int actual = sut.indexOfStarting("U206d25c2ea6bd87c17655609a1c37cb8");
		
		// verify
		assertThat(actual, is(2));
	}
	
	/**
	 * Listにresetを含む場合、最新のreset後に登録したデータのindexを返すことを確認します
	 */
	@Test
	public void testIndexOfStartingContainOfReset() throws StartIndexException {
		// setup
		LineEvent numberEvent1 =
				LineEventFixture.createNumberLineUserEvent("U206d25c2ea6bd87c17655609a1c37cb8", 1499378880);
		LineEvent resetEvent =
				LineEventFixture.createResetLineUserEvent("U206d25c2ea6bd87c17655609a1c37cb8", 1499378881);
		LineEvent numberEvent2 =
				LineEventFixture.createNumberLineUserEvent("U206d25c2ea6bd87c17655609a1c37cb8", 1499378882);
		LineEvent numberEvent3 =
				LineEventFixture.createNumberLineUserEvent("U206d25c2ea6bd87c17655609a1c37cb8", 1499378883);
		
		LineMessageEntity numberLineMessageEntity1 = LineMessageEntityFixture.createLineEntity(numberEvent1);
		LineMessageEntity resetLineMessageEntity = LineMessageEntityFixture.createLineEntity(resetEvent);
		LineMessageEntity numberLineMessageEntity2 = LineMessageEntityFixture.createLineEntity(numberEvent2);
		LineMessageEntity numberLineMessageEntity3 = LineMessageEntityFixture.createLineEntity(numberEvent3);
		
		sut.save(numberLineMessageEntity1);
		sut.save(resetLineMessageEntity);
		sut.save(numberLineMessageEntity2);
		sut.save(numberLineMessageEntity3);
		
		// exercise
		int actual = sut.indexOfStarting("U206d25c2ea6bd87c17655609a1c37cb8");
		
		// verify
		assertThat(actual, is(1));
	}
	
	/**
	 * 保存データがない場合、StartIndexExceptionが発生することを確認します
	 */
	@Test(expected = StartIndexException.class)
	public void testIndexOfStartingNoData() throws StartIndexException {
		// exercise
		sut.indexOfStarting("U206d25c2ea6bd87c17655609a1c37cb8");
	}
	
	/**
	 * saveメソッドの引数と戻り値が一致することを確認します
	 */
	@Test
	public void testSave() {
		// setup
		LineEvent numberEvent =
				LineEventFixture.createNumberLineUserEvent("U206d25c2ea6bd87c17655609a1c37cb8", 1499378820);
		LineMessageEntity numberLineMessageEntity = LineMessageEntityFixture.createLineEntity(numberEvent);
		
		// exercise
		LineMessageEntity actual = sut.save(numberLineMessageEntity);
		
		// verify
		assertThat(actual, is(numberLineMessageEntity));
	}
	
	/**
	 *  指定のuserIdと一致するリストを返すことを確認する
	 */
	@Test
	public void testFindByUser() {
		// setup
		LineEvent numberEvent1 =
				LineEventFixture.createNumberLineUserEvent("U206d25c2ea6bd87c17655609a1c37cb8", 1499378880);
		LineEvent numberEvent2 =
				LineEventFixture.createNumberLineUserEvent("U206d25c2ea6bd87c17655609a1c37cb8", 1499378881);
		LineEvent numberEvent3 =
				LineEventFixture.createNumberLineUserEvent("U206d25c2ea6bd87c17655609a1c37cb9", 1499378882);
		LineEvent numberEvent4 =
				LineEventFixture.createNumberLineUserEvent("U206d25c2ea6bd87c17655609a1c37cb8", 1499378883);
		LineEvent numberEvent5 =
				LineEventFixture.createNumberLineUserEvent("U206d25c2ea6bd87c17655609a1c37cb8", 1499378884);
		
		LineMessageEntity numberLineMessageEntity1 = LineMessageEntityFixture.createLineEntity(numberEvent1);
		LineMessageEntity numberLineMessageEntity2 = LineMessageEntityFixture.createLineEntity(numberEvent2);
		LineMessageEntity numberLineMessageEntity3 = LineMessageEntityFixture.createLineEntity(numberEvent3);
		LineMessageEntity numberLineMessageEntity4 = LineMessageEntityFixture.createLineEntity(numberEvent4);
		LineMessageEntity numberLineMessageEntity5 = LineMessageEntityFixture.createLineEntity(numberEvent5);
		
		sut.save(numberLineMessageEntity1);
		sut.save(numberLineMessageEntity2);
		sut.save(numberLineMessageEntity3);
		sut.save(numberLineMessageEntity4);
		sut.save(numberLineMessageEntity5);
		
		// exercise
		List<LineMessageEntity> actual = sut.findByUser("U206d25c2ea6bd87c17655609a1c37cb8", 0, 2);
		
		// verify
		assertThat(actual, hasSize(2));
		assertThat(actual.get(0).getMessageId(), is("325711"));
		assertThat(actual.get(0).getUserId(), is("U206d25c2ea6bd87c17655609a1c37cb8"));
		assertThat(actual.get(0).getTimestamp(), is(1499378884L));
		assertThat(actual.get(0).getValue(), is("12"));
		assertThat(actual.get(1).getMessageId(), is("325711"));
		assertThat(actual.get(1).getUserId(), is("U206d25c2ea6bd87c17655609a1c37cb8"));
		assertThat(actual.get(1).getTimestamp(), is(1499378883L));
		assertThat(actual.get(1).getValue(), is("12"));
	}
	
	/**
	 *  指定のuserIdと一致するリストの2ページ目を返すことを確認する
	 */
	@Test
	public void testFindByUserNextPage() {
		// setup
		LineEvent numberEvent1 =
				LineEventFixture.createNumberLineUserEvent("U206d25c2ea6bd87c17655609a1c37cb8", 1499378880);
		LineEvent numberEvent2 =
				LineEventFixture.createNumberLineUserEvent("U206d25c2ea6bd87c17655609a1c37cb8", 1499378881);
		LineEvent numberEvent3 =
				LineEventFixture.createNumberLineUserEvent("U206d25c2ea6bd87c17655609a1c37cb9", 1499378882);
		LineEvent numberEvent4 =
				LineEventFixture.createNumberLineUserEvent("U206d25c2ea6bd87c17655609a1c37cb8", 1499378883);
		LineEvent numberEvent5 =
				LineEventFixture.createNumberLineUserEvent("U206d25c2ea6bd87c17655609a1c37cb8", 1499378884);
		
		LineMessageEntity numberLineMessageEntity1 = LineMessageEntityFixture.createLineEntity(numberEvent1);
		LineMessageEntity numberLineMessageEntity2 = LineMessageEntityFixture.createLineEntity(numberEvent2);
		LineMessageEntity numberLineMessageEntity3 = LineMessageEntityFixture.createLineEntity(numberEvent3);
		LineMessageEntity numberLineMessageEntity4 = LineMessageEntityFixture.createLineEntity(numberEvent4);
		LineMessageEntity numberLineMessageEntity5 = LineMessageEntityFixture.createLineEntity(numberEvent5);
		
		sut.save(numberLineMessageEntity1);
		sut.save(numberLineMessageEntity2);
		sut.save(numberLineMessageEntity3);
		sut.save(numberLineMessageEntity4);
		sut.save(numberLineMessageEntity5);
		
		// exercise
		List<LineMessageEntity> actual = sut.findByUser("U206d25c2ea6bd87c17655609a1c37cb8", 2, 2);
		
		// verify
		assertThat(actual, hasSize(2));
		assertThat(actual.get(0).getMessageId(), is("325711"));
		assertThat(actual.get(0).getUserId(), is("U206d25c2ea6bd87c17655609a1c37cb8"));
		assertThat(actual.get(0).getTimestamp(), is(1499378881L));
		assertThat(actual.get(0).getValue(), is("12"));
		assertThat(actual.get(1).getMessageId(), is("325711"));
		assertThat(actual.get(1).getUserId(), is("U206d25c2ea6bd87c17655609a1c37cb8"));
		assertThat(actual.get(1).getTimestamp(), is(1499378880L));
		assertThat(actual.get(1).getValue(), is("12"));
		
	}
	
	/**
	 * 存在しないuserIdで検索し、空のリストを返すことを確認する
	 */
	@Test
	public void testFindByNotExistUser() throws StartIndexException {
		
		// exercise
		List<LineMessageEntity> actual = sut.findByUser("U206d25c2ea6bd87c17655609a1c40cb2", 0, 5);
		
		// verify
		assertThat(actual, hasSize(0));
	}
}
