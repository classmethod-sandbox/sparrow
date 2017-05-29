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
				LineEventFixture.createStartLineUserEvent("U206d25c2ea6bd87c17655609a1c37cb8", 2017052600);
		// exesice
		String actual = sut.save(startEvent);
		// verify
		assertThat(actual, is("calc mode start"));
	}
	
	/**
	 * 計算開始状態でend発言をした時の戻り値を確認します
	 */
	@Test
	public void testEndSave() {
		// setup
		LineEvent startEvent =
				LineEventFixture.createStartLineUserEvent("U206d25c2ea6bd87c17655609a1c37cb8", 2017052600);
		LineEvent numberEvent =
				LineEventFixture.createNumberLineUserEvent("U206d25c2ea6bd87c17655609a1c37cb8", 2017052610);
		LineEvent endEvent = LineEventFixture.createEndLineUserEvent("U206d25c2ea6bd87c17655609a1c37cb8", 2017052640);
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
		LineEvent endEvent = LineEventFixture.createEndLineUserEvent("U206d25c2ea6bd87c17655609a1c37cb8", 2017052640);
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
				LineEventFixture.createStartLineUserEvent("U206d25c2ea6bd87c17655609a1c37cb8", 2017052600);
		LineEvent resetEvent =
				LineEventFixture.createResetLineUserEvent("U206d25c2ea6bd87c17655609a1c37cb8", 2017052630);
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
				LineEventFixture.createResetLineUserEvent("U206d25c2ea6bd87c17655609a1c37cb8", 2017052630);
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
				LineEventFixture.createStartLineUserEvent("U206d25c2ea6bd87c17655609a1c37cb8", 2017052600);
		LineEvent numberEvent =
				LineEventFixture.createNumberLineUserEvent("U206d25c2ea6bd87c17655609a1c37cb8", 2017052610);
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
				LineEventFixture.createNumberLineUserEvent("U206d25c2ea6bd87c17655609a1c37cb8", 2017052610);
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
				LineEventFixture.createInvalidLineUserEvent("U206d25c2ea6bd87c17655609a1c37cb8", 2017052650);
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
		LineEvent startEvent =
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
		LineEvent endEvent = LineEventFixture.createEndLineUserEvent("U206d25c2ea6bd87c17655609a1c37cb8", 2017052640);
		
		LineMessageEntity startLineMessageEntity = LineMessageEntityFixture.createLineEntity(startEvent);
		LineMessageEntity numberLineMessageEntity1 = LineMessageEntityFixture.createLineEntity(numberEvent1);
		LineMessageEntity numberLineMessageEntity2 = LineMessageEntityFixture.createLineEntity(numberEvent2);
		LineMessageEntity numberLineMessageEntity3 = LineMessageEntityFixture.createLineEntity(numberEvent3);
		LineMessageEntity numberLineMessageEntity4 = LineMessageEntityFixture.createLineEntity(numberEvent4);
		LineMessageEntity numberLineMessageEntity5 = LineMessageEntityFixture.createLineEntity(numberEvent5);
		LineMessageEntity numberLineMessageEntity6 = LineMessageEntityFixture.createLineEntity(numberEvent6);
		LineMessageEntity endLineMessageEntity = LineMessageEntityFixture.createLineEntity(endEvent);
		
		when(inMemoryCalculatorRepository.save(startLineMessageEntity)).thenCallRealMethod();
		when(inMemoryCalculatorRepository.save(numberLineMessageEntity1)).thenCallRealMethod();
		when(inMemoryCalculatorRepository.save(numberLineMessageEntity2)).thenCallRealMethod();
		when(inMemoryCalculatorRepository.save(numberLineMessageEntity3)).thenCallRealMethod();
		when(inMemoryCalculatorRepository.save(numberLineMessageEntity4)).thenCallRealMethod();
		when(inMemoryCalculatorRepository.save(numberLineMessageEntity5)).thenCallRealMethod();
		when(inMemoryCalculatorRepository.save(numberLineMessageEntity6)).thenCallRealMethod();
		
		// exesice
		Integer result = sut.calculateTotal(endLineMessageEntity);
		
		// varify
		assertThat(result, is(72));
	}
	
	/**
	 * リスト合計値が正数のリストをリセットすることを確認します
	 */
	@Test
	public void testResetIntegerList() {
		// setup
		LineEvent startEvent =
				LineEventFixture.createStartLineUserEvent("U206d25c2ea6bd87c17655609a1c37cb8", 2017052600);
		LineEvent numberEvent =
				LineEventFixture.createNumberLineUserEvent("U206d25c2ea6bd87c17655609a1c37cb8", 2017052610);
		LineEvent resetEvent =
				LineEventFixture.createResetLineUserEvent("U206d25c2ea6bd87c17655609a1c37cb8", 2017052630);
		
		LineMessageEntity startLineMessageEntity = LineMessageEntityFixture.createLineEntity(startEvent);
		LineMessageEntity numberLineMessageEntity = LineMessageEntityFixture.createLineEntity(numberEvent);
		LineMessageEntity resetLineMessageEntity = LineMessageEntityFixture.createLineEntity(resetEvent);
		
		when(inMemoryCalculatorRepository.save(startLineMessageEntity)).thenCallRealMethod();
		when(inMemoryCalculatorRepository.save(numberLineMessageEntity)).thenCallRealMethod();
		
		// exesice
		Integer result = sut.resetList(resetLineMessageEntity);
		// verify
		assertThat(result, is(0));
	}
	
	/**
	 * リスト合計値が負数のリストをリセットすることを確認します
	 */
	@Test
	public void testResetNegativeNumberList() {
		// setup
		LineEvent startEvent =
				LineEventFixture.createStartLineUserEvent("U206d25c2ea6bd87c17655609a1c37cb8", 2017052600);
		LineEvent negativeNumberEvent =
				LineEventFixture.createNegativeNumberLineUserEvent("U206d25c2ea6bd87c17655609a1c37cb8", 2017052620);
		LineEvent resetEvent =
				LineEventFixture.createResetLineUserEvent("U206d25c2ea6bd87c17655609a1c37cb8", 2017052630);
		
		LineMessageEntity startLineMessageEntity = LineMessageEntityFixture.createLineEntity(startEvent);
		LineMessageEntity numberLineMessageEntity = LineMessageEntityFixture.createLineEntity(negativeNumberEvent);
		LineMessageEntity resetLineMessageEntity = LineMessageEntityFixture.createLineEntity(resetEvent);
		
		when(inMemoryCalculatorRepository.save(startLineMessageEntity)).thenCallRealMethod();
		when(inMemoryCalculatorRepository.save(numberLineMessageEntity)).thenCallRealMethod();
		
		// exesice
		Integer result = sut.resetList(resetLineMessageEntity);
		// verify
		assertThat(result, is(0));
	}
	
	/**
	 * start発言時のLineEntity生成を確認します
	 */
	@Test
	public void testCreateStartLineMessageEntity() {
		// setup
		LineEvent startEvent =
				LineEventFixture.createStartLineUserEvent("U206d25c2ea6bd87c17655609a1c37cb8", 2017052600);
		String messageText = startEvent.getMessage().getText();
		// exesice
		LineMessageEntity startLineMessageEntity = sut.createLineMessageEntity(startEvent, messageText);
		// velify
		assertThat(startLineMessageEntity.getMessageId(), is("325708"));
		assertThat(startLineMessageEntity.getUserId(), is("U206d25c2ea6bd87c17655609a1c37cb8"));
		assertThat(startLineMessageEntity.getTimestamp(), is(2017052600L));
		assertThat(startLineMessageEntity.getValue(), is(0));
	}
	
	/**
	 * end発言時のLineEntity生成を確認します
	 */
	@Test
	public void testCreateEndLineMessageEntity() {
		// setup
		LineEvent endEvent = LineEventFixture.createEndLineUserEvent("U206d25c2ea6bd87c17655609a1c37cb8", 2017052640);
		String messageText = endEvent.getMessage().getText();
		// exesice
		LineMessageEntity endLineMessageEntity = sut.createLineMessageEntity(endEvent, messageText);
		// velify
		assertThat(endLineMessageEntity.getMessageId(), is("325709"));
		assertThat(endLineMessageEntity.getUserId(), is("U206d25c2ea6bd87c17655609a1c37cb8"));
		assertThat(endLineMessageEntity.getTimestamp(), is(2017052640L));
		assertThat(endLineMessageEntity.getValue(), is(0));
	}
	
	/**
	 * reset発言時のLineEntity生成を確認します
	 */
	@Test
	public void testCreateResetLineMessageEntity() {
		// setup
		LineEvent resetEvent =
				LineEventFixture.createResetLineUserEvent("U206d25c2ea6bd87c17655609a1c37cb8", 2017052630);
		String messageText = resetEvent.getMessage().getText();
		// exesice
		LineMessageEntity resetLineMessageEntity = sut.createLineMessageEntity(resetEvent, messageText);
		// velify
		assertThat(resetLineMessageEntity.getMessageId(), is("325710"));
		assertThat(resetLineMessageEntity.getUserId(), is("U206d25c2ea6bd87c17655609a1c37cb8"));
		assertThat(resetLineMessageEntity.getTimestamp(), is(2017052630L));
		assertThat(resetLineMessageEntity.getValue(), is(0));
	}
	
	/**
	 * 数字発言時のLineEntity生成を確認します
	 */
	@Test
	public void testCreateNumberLineMessageEntity() {
		// setup
		LineEvent numberEvent =
				LineEventFixture.createNumberLineUserEvent("U206d25c2ea6bd87c17655609a1c37cb8", 2017052610);
		String messageText = numberEvent.getMessage().getText();
		// exesice
		LineMessageEntity numberLineMessageEntity = sut.createLineMessageEntity(numberEvent, messageText);
		// velify
		assertThat(numberLineMessageEntity.getMessageId(), is("325711"));
		assertThat(numberLineMessageEntity.getUserId(), is("U206d25c2ea6bd87c17655609a1c37cb8"));
		assertThat(numberLineMessageEntity.getTimestamp(), is(2017052610L));
		assertThat(numberLineMessageEntity.getValue(), is(12));
	}
}
