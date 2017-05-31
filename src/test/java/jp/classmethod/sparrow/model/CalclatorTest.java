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

import jp.classmethod.sparrow.infrastructure.InMemoryCalculatorRepository;

/**
 * Created by kunita.fumiko on 2017/05/10.
 */
@RunWith(MockitoJUnitRunner.class)
public class CalclatorTest {
	
	@Spy
	InMemoryCalculatorRepository inMemoryCalculatorRepository;
	
	@InjectMocks
	Calculator sut;
	
	
	/**
	 * start発言時の戻り値を確認します
	 */
	@Test
	public void testStartSave() {
		// setup
		LineEvent startEvent =
				LineEventFixture.createStartLineUserEvent("U206d25c2ea6bd87c17655609a1c37cb8", 1499378820);
		// exesice
		String actual = sut.save(startEvent);
		// verify
		assertThat(actual, is("calc mode start"));
	}
	
	/**
	 * 計算開始状態でend発言をした時の戻り値を確認します
	 */
	@Test
	public void testEndSave() throws StartIndexException {
		// setup
		LineEvent startEvent =
				LineEventFixture.createStartLineUserEvent("U206d25c2ea6bd87c17655609a1c37cb8", 1499378820);
		LineEvent numberEvent =
				LineEventFixture.createNumberLineUserEvent("U206d25c2ea6bd87c17655609a1c37cb8", 1499378880);
		LineEvent endEvent = LineEventFixture.createEndLineUserEvent("U206d25c2ea6bd87c17655609a1c37cb8", 1499379060);
		sut.save(startEvent);
		sut.save(numberEvent);
		// exesice
		String actual = sut.save(endEvent);
		// verify
		assertThat(actual, is("12"));
	}
	
	/**
	 * 計算未開始状態でend発言をした時の戻り値を確認します
	 */
	@Test
	public void testNotStartedEndSave() {
		// setup
		LineEvent endEvent = LineEventFixture.createEndLineUserEvent("U206d25c2ea6bd87c17655609a1c37cb8", 1499379060);
		// exesice
		String actual = sut.save(endEvent);
		// verify
		assertThat(actual, is(""));
	}
	
	/**
	 * 計算開始状態でreset発言時の戻り値を確認します
	 */
	@Test
	public void testResetSave() {
		// setup
		LineEvent startEvent =
				LineEventFixture.createStartLineUserEvent("U206d25c2ea6bd87c17655609a1c37cb8", 1499378820);
		LineEvent resetEvent =
				LineEventFixture.createResetLineUserEvent("U206d25c2ea6bd87c17655609a1c37cb8", 1499379000);
		sut.save(startEvent);
		// exesice
		String actual = sut.save(resetEvent);
		// verify
		assertThat(actual, is("reset"));
	}
	
	/**
	 * 計算未開始状態でreset発言時の戻り値を確認します
	 */
	@Test
	public void testNotStartedResetSave() {
		// setup
		LineEvent resetEvent =
				LineEventFixture.createResetLineUserEvent("U206d25c2ea6bd87c17655609a1c37cb8", 1499379000);
		// exesice
		String actual = sut.save(resetEvent);
		// verify
		assertThat(actual, is(""));
	}
	
	/**
	 * 計算開始状態で数字発言時の戻り値を確認します
	 */
	@Test
	public void testNumberSave() {
		// setup
		LineEvent startEvent =
				LineEventFixture.createStartLineUserEvent("U206d25c2ea6bd87c17655609a1c37cb8", 1499378820);
		LineEvent numberEvent =
				LineEventFixture.createNumberLineUserEvent("U206d25c2ea6bd87c17655609a1c37cb8", 1499378880);
		sut.save(startEvent);
		// exesice
		String actual = sut.save(numberEvent);
		// verify
		assertThat(actual, is(""));
	}
	
	/**
	 * 計算未開始状態で数字発言時の戻り値を確認します
	 */
	@Test
	public void testNotStartedNumberSave() {
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
	 * startを発言している時、isStartedがtrueを返すことを確認します
	 */
	@Test
	public void testIsStarted() {
		// setup
		LineEvent startEvent =
				LineEventFixture.createStartLineUserEvent("U206d25c2ea6bd87c17655609a1c37cb8", 1499378820);
		LineEvent numberEvent =
				LineEventFixture.createNumberLineUserEvent("U206d25c2ea6bd87c17655609a1c37cb8", 1499378821);
		LineMessageEntity startLineMessageEntity = LineMessageEntityFixture.createLineEntity(startEvent);
		when(inMemoryCalculatorRepository.save(startLineMessageEntity)).thenCallRealMethod();
		
		// exesice
		boolean actual = sut.isStarted(numberEvent);
		
		//verify
		assertThat(actual, is(true));
	}
	
	/**
	 * startを発言していない時、isStartedがfalseを返すことを確認します
	 */
	@Test
	public void testIsNotStarted() {
		// setup
		LineEvent numberEvent =
				LineEventFixture.createNumberLineUserEvent("U206d25c2ea6bd87c17655609a1c37cb8", 1499378821);
		
		// exesice
		boolean actual = sut.isStarted(numberEvent);
		
		//verify
		assertThat(actual, is(false));
	}
	
	/**
	 * 引数で渡すLineMessegaEntityのuIdと一致するリストの合計値を返すことを確認します
	 */
	@Test
	public void testCalculateTotal() {
		// setup
		LineEvent startEvent1 =
				LineEventFixture.createStartLineUserEvent("U206d25c2ea6bd87c17655609a1c37cb8", 1499378820);
		LineEvent numberEvent1 =
				LineEventFixture.createNumberLineUserEvent("U206d25c2ea6bd87c17655609a1c37cb8", 1499378821);
		LineEvent resetEvent =
				LineEventFixture.createResetLineUserEvent("U206d25c2ea6bd87c17655609a1c37cb8", 1499378822);
		LineEvent startEvent2 =
				LineEventFixture.createStartLineUserEvent("U206d25c2ea6bd87c17655609a1c37cb8", 1499378823);
		LineEvent numberEvent2 =
				LineEventFixture.createNumberLineUserEvent("U206d25c2ea6bd87c17655609a1c37cb8", 1499378824);
		LineEvent numberEvent3 =
				LineEventFixture.createNumberLineUserEvent("U206d25c2ea6bd87c17655609a1c37cb8", 1499378825);
		LineEvent numberEvent4 =
				LineEventFixture.createNumberLineUserEvent("U206d25c2ea6bd87c17655609a1c37cb8", 1499378826);
		LineEvent numberEvent5 =
				LineEventFixture.createNumberLineUserEvent("U206d25c2ea6bd87c17655609a1c37cb8", 1499378827);
		LineEvent numberEvent6 =
				LineEventFixture.createNumberLineUserEvent("U206d25c2ea6bd87c17655609a1c37cb8", 1499378828);
		LineEvent endEvent = LineEventFixture.createEndLineUserEvent("U206d25c2ea6bd87c17655609a1c37cb8", 1499378829);
		
		LineMessageEntity startLineMessageEntity1 = LineMessageEntityFixture.createLineEntity(startEvent1);
		LineMessageEntity numberLineMessageEntity1 = LineMessageEntityFixture.createLineEntity(numberEvent1);
		LineMessageEntity startLineMessageEntity2 = LineMessageEntityFixture.createLineEntity(startEvent2);
		LineMessageEntity resetLineMessageEntity = LineMessageEntityFixture.createLineEntity(resetEvent);
		LineMessageEntity numberLineMessageEntity2 = LineMessageEntityFixture.createLineEntity(numberEvent2);
		LineMessageEntity numberLineMessageEntity3 = LineMessageEntityFixture.createLineEntity(numberEvent3);
		LineMessageEntity numberLineMessageEntity4 = LineMessageEntityFixture.createLineEntity(numberEvent4);
		LineMessageEntity numberLineMessageEntity5 = LineMessageEntityFixture.createLineEntity(numberEvent5);
		LineMessageEntity numberLineMessageEntity6 = LineMessageEntityFixture.createLineEntity(numberEvent6);
		LineMessageEntity endLineMessageEntity = LineMessageEntityFixture.createLineEntity(endEvent);
		
		when(inMemoryCalculatorRepository.save(startLineMessageEntity1)).thenCallRealMethod();
		when(inMemoryCalculatorRepository.save(numberLineMessageEntity1)).thenCallRealMethod();
		when(inMemoryCalculatorRepository.save(startLineMessageEntity2)).thenCallRealMethod();
		when(inMemoryCalculatorRepository.save(resetLineMessageEntity)).thenCallRealMethod();
		when(inMemoryCalculatorRepository.save(numberLineMessageEntity2)).thenCallRealMethod();
		when(inMemoryCalculatorRepository.save(numberLineMessageEntity3)).thenCallRealMethod();
		when(inMemoryCalculatorRepository.save(numberLineMessageEntity4)).thenCallRealMethod();
		when(inMemoryCalculatorRepository.save(numberLineMessageEntity5)).thenCallRealMethod();
		when(inMemoryCalculatorRepository.save(numberLineMessageEntity6)).thenCallRealMethod();
		when(inMemoryCalculatorRepository.save(endLineMessageEntity)).thenCallRealMethod();
		
		// exesice
		Integer result = sut.calculateTotal(endEvent);
		
		// varify
		assertThat(result, is(60));
	}
	
	/**
	 * start発言時のLineEntity生成を確認します
	 */
	@Test
	public void testcreateLineMessageEntity() {
		// setup
		LineEvent startEvent =
				LineEventFixture.createStartLineUserEvent("U206d25c2ea6bd87c17655609a1c37cb8", 1499378820);
		// exesice
		LineMessageEntity startLineMessageEntity = sut.createLineMessageEntity(startEvent);
		// velify
		assertThat(startLineMessageEntity.getMessageId(), is("325708"));
		assertThat(startLineMessageEntity.getUserId(), is("U206d25c2ea6bd87c17655609a1c37cb8"));
		assertThat(startLineMessageEntity.getTimestamp(), is(1499378820L));
		assertThat(startLineMessageEntity.getValue(), is("start"));
	}
}
