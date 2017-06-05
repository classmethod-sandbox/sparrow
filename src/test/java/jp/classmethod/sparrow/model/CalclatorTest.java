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

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.when;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Spy;
import org.mockito.runners.MockitoJUnitRunner;

import jp.classmethod.sparrow.infrastructure.InMemoryLineMessageEntityRepository;

/**
 * Created by kunita.fumiko on 2017/05/10.
 */
@RunWith(MockitoJUnitRunner.class)
public class CalclatorTest {
	
	@Spy
	InMemoryLineMessageEntityRepository inMemoryLineMessageEntityRepository;
	
	@InjectMocks
	Calculator sut;
	
	
	/**
	 * データが保存されている状態でtotal発言時の戻り値を確認します
	 */
	@Test
	public void testTotal() {
		// setup
		LineEvent numberEvent1 =
				LineEventFixture.createNumberLineUserEvent("U206d25c2ea6bd87c17655609a1c37cb8", 1499378880);
		LineEvent resetEvent =
				LineEventFixture.createResetLineUserEvent("U206d25c2ea6bd87c17655609a1c37cb8", 1499378881);
		LineEvent numberEvent2 =
				LineEventFixture.createNumberLineUserEvent("U206d25c2ea6bd87c17655609a1c37cb8", 1499378882);
		LineEvent numberEvent3 =
				LineEventFixture.createNumberLineUserEvent("U206d25c2ea6bd87c17655609a1c37cb8", 1499378883);
		LineEvent totalEvent =
				LineEventFixture.createTotalLineUserEvent("U206d25c2ea6bd87c17655609a1c37cb8", 1499379060);
		sut.save(numberEvent1);
		sut.save(resetEvent);
		sut.save(numberEvent2);
		sut.save(numberEvent3);
		// exesice
		int actual = sut.calculateTotal(totalEvent);
		// verify
		assertThat(actual, is(24));
	}
	
	/**
	 * データが保存されていない状態でtotal発言時の戻り値を確認します
	 */
	@Test
	public void testNoDataTotal() {
		// setup
		LineEvent totalEvent =
				LineEventFixture.createTotalLineUserEvent("U206d25c2ea6bd87c17655609a1c37cb8", 1499379060);
		// exesice
		int actual = sut.calculateTotal(totalEvent);
		// verify
		assertThat(actual, is(0));
	}
	
	/**
	 * reset発言時の戻り値を確認します
	 */
	@Test
	public void testResetSave() {
		// setup
		LineEvent resetEvent =
				LineEventFixture.createResetLineUserEvent("U206d25c2ea6bd87c17655609a1c37cb8", 1499379000);
		// exesice
		String actual = sut.save(resetEvent);
		// verify
		assertThat(actual, is("reset"));
	}
	
	/**
	 * 数字発言時の戻り値を確認します
	 */
	@Test
	public void testNumberSave() {
		// setup
		LineEvent numberEvent =
				LineEventFixture.createNumberLineUserEvent("U206d25c2ea6bd87c17655609a1c37cb8", 1499378880);
		// exesice
		String actual = sut.save(numberEvent);
		// verify
		assertThat(actual, is(""));
	}
	
	/**
	 * 仕様外の発言時の戻り値を確認します
	 */
	@Test
	public void testInvalidSave() {
		// setup
		LineEvent invalidEvent =
				LineEventFixture.createInvalidLineUserEvent("U206d25c2ea6bd87c17655609a1c37cb8", 1499379120);
		// exesice
		String actual = sut.save(invalidEvent);
		// verify
		assertThat(actual, is("error"));
	}
	
	/**
	 * 引数で渡すLineMessegaEntityのuIdと一致するリストの合計値を返すことを確認します
	 */
	@Test
	public void testCalculateTotal() {
		// setup
		LineEvent numberEvent1 =
				LineEventFixture.createNumberLineUserEvent("U206d25c2ea6bd87c17655609a1c37cb8", 1499378821);
		LineEvent resetEvent =
				LineEventFixture.createResetLineUserEvent("U206d25c2ea6bd87c17655609a1c37cb8", 1499378822);
		LineEvent numberEvent2 =
				LineEventFixture.createNumberLineUserEvent("U206d25c2ea6bd87c17655609a1c37cb8", 1499378823);
		LineEvent numberEvent3 =
				LineEventFixture.createNumberLineUserEvent("U206d25c2ea6bd87c17655609a1c37cb8", 1499378824);
		LineEvent numberEvent4 =
				LineEventFixture.createNumberLineUserEvent("U206d25c2ea6bd87c17655609a1c37cb8", 1499378825);
		LineEvent numberEvent5 =
				LineEventFixture.createNumberLineUserEvent("U206d25c2ea6bd87c17655609a1c37cb8", 1499378826);
		LineEvent numberEvent6 =
				LineEventFixture.createNumberLineUserEvent("U206d25c2ea6bd87c17655609a1c37cb8", 1499378827);
		LineEvent numberEvent =
				LineEventFixture.createNumberLineUserEvent("U206d25c2ea6bd87c17655609a1c38cb9", 1499378828);
		LineEvent totalEvent =
				LineEventFixture.createTotalLineUserEvent("U206d25c2ea6bd87c17655609a1c37cb8", 1499378829);
		
		LineMessageEntity numberLineMessageEntity1 = LineMessageEntityFixture.createLineEntity(numberEvent1);
		LineMessageEntity resetLineMessageEntity = LineMessageEntityFixture.createLineEntity(resetEvent);
		LineMessageEntity numberLineMessageEntity2 = LineMessageEntityFixture.createLineEntity(numberEvent2);
		LineMessageEntity numberLineMessageEntity3 = LineMessageEntityFixture.createLineEntity(numberEvent3);
		LineMessageEntity numberLineMessageEntity4 = LineMessageEntityFixture.createLineEntity(numberEvent4);
		LineMessageEntity numberLineMessageEntity5 = LineMessageEntityFixture.createLineEntity(numberEvent5);
		LineMessageEntity numberLineMessageEntity6 = LineMessageEntityFixture.createLineEntity(numberEvent6);
		LineMessageEntity numberLineMessageEntity = LineMessageEntityFixture.createLineEntity(numberEvent);
		
		when(inMemoryLineMessageEntityRepository.save(numberLineMessageEntity1)).thenCallRealMethod();
		when(inMemoryLineMessageEntityRepository.save(resetLineMessageEntity)).thenCallRealMethod();
		when(inMemoryLineMessageEntityRepository.save(numberLineMessageEntity2)).thenCallRealMethod();
		when(inMemoryLineMessageEntityRepository.save(numberLineMessageEntity3)).thenCallRealMethod();
		when(inMemoryLineMessageEntityRepository.save(numberLineMessageEntity4)).thenCallRealMethod();
		when(inMemoryLineMessageEntityRepository.save(numberLineMessageEntity5)).thenCallRealMethod();
		when(inMemoryLineMessageEntityRepository.save(numberLineMessageEntity6)).thenCallRealMethod();
		when(inMemoryLineMessageEntityRepository.save(numberLineMessageEntity)).thenCallRealMethod();
		
		// exesice
		int result = sut.calculateTotal(totalEvent);
		
		// verify
		assertThat(result, is(60));
	}
	
	/**
	 *  数字発言時のLineEntity生成を確認します
	 */
	@Test
	public void testcreateLineMessageEntity() {
		// setup
		LineEvent startEvent =
				LineEventFixture.createNumberLineUserEvent("U206d25c2ea6bd87c17655609a1c37cb8", 1499378820);
		// exesice
		LineMessageEntity startLineMessageEntity = sut.createLineMessageEntity(startEvent);
		// velify
		assertThat(startLineMessageEntity.getMessageId(), is("325711"));
		assertThat(startLineMessageEntity.getUserId(), is("U206d25c2ea6bd87c17655609a1c37cb8"));
		assertThat(startLineMessageEntity.getTimestamp(), is(1499378820L));
		assertThat(startLineMessageEntity.getValue(), is("12"));
	}
}
