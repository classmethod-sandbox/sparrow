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
		LineEvent startEvent = LineEventFixture.createLineUserEvent(LineMessageFixture.createStartLineMessage());
		// exesice
		String actual = sut.save(startEvent);
		// verify
		assertThat(actual, is("calc mode start"));
	}
	
	/**
	 * end発言時の戻り値を確認します
	 */
	@Test
	public void testEndSave() {
		// setup
		LineEvent startEvent = LineEventFixture.createLineUserEvent(LineMessageFixture.createStartLineMessage());
		LineEvent numberEvent = LineEventFixture.createLineUserEvent(LineMessageFixture.createNumberLineMessage());
		LineEvent endEvent = LineEventFixture.createLineUserEvent(LineMessageFixture.createEndLineMessage());
		sut.save(startEvent);
		sut.save(numberEvent);
		// exesice
		String actual = sut.save(endEvent);
		// verify
		assertThat(actual, is("12"));
	}
	
	/**
	 * reset発言時の戻り値を確認します
	 */
	@Test
	public void testResetSave() {
		// setup
		LineEvent resetEvent = LineEventFixture.createLineUserEvent(LineMessageFixture.createResetLineMessage());
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
		LineEvent numberEvent = LineEventFixture.createLineUserEvent(LineMessageFixture.createNumberLineMessage());
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
		LineEvent invalidEvent = LineEventFixture.createLineUserEvent(LineMessageFixture.createInvalidLineMessage());
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
		LineEvent startEvent = LineEventFixture.createLineUserEvent(LineMessageFixture.createStartLineMessage());
		LineMessageEntity startLineMessageEntity = LineMessageEntityFixture.createLineEntity(startEvent);
		LineEvent numberEvent = LineEventFixture.createLineUserEvent(LineMessageFixture.createNumberLineMessage());
		LineMessageEntity numberLineMessageEntity = LineMessageEntityFixture.createLineEntity(numberEvent);
		LineEvent endEvent = LineEventFixture.createLineUserEvent(LineMessageFixture.createEndLineMessage());
		LineMessageEntity endLineMessageEntity = LineMessageEntityFixture.createLineEntity(endEvent);
		when(inMemoryCalculatorRepository.save(startLineMessageEntity)).thenCallRealMethod();
		when(inMemoryCalculatorRepository.save(numberLineMessageEntity)).thenCallRealMethod();
		// exesice
		Integer result = sut.calculateTotal(endLineMessageEntity);
		// varify
		assertThat(result, is(12));
	}
	
	/**
	 * リスト合計値が整数のリストをリセットすることを確認します
	 */
	@Test
	public void testResetIntegerList() {
		
		// setup
		LineEvent startEvent = LineEventFixture.createLineUserEvent(LineMessageFixture.createStartLineMessage());
		LineMessageEntity startLineMessageEntity = LineMessageEntityFixture.createLineEntity(startEvent);
		LineEvent numberEvent = LineEventFixture.createLineUserEvent(LineMessageFixture.createNumberLineMessage());
		LineMessageEntity numberLineMessageEntity = LineMessageEntityFixture.createLineEntity(numberEvent);
		LineEvent resetEvent = LineEventFixture.createLineUserEvent(LineMessageFixture.createResetLineMessage());
		LineMessageEntity resetLineMessageEntity = LineMessageEntityFixture.createLineEntity(resetEvent);
		when(inMemoryCalculatorRepository.save(startLineMessageEntity)).thenCallRealMethod();
		when(inMemoryCalculatorRepository.save(numberLineMessageEntity)).thenCallRealMethod();
		// exesice
		Integer result = sut.resetList(resetLineMessageEntity);
		// verify
		assertThat(result, is(0));
	}
	
	/**
	 * リスト合計値が数のリストをリセットすることを確認します
	 */
	@Test
	public void testResetNegativeNumberList() {
		// setup
		LineEvent startEvent = LineEventFixture.createLineUserEvent(LineMessageFixture.createStartLineMessage());
		LineMessageEntity startLineMessageEntity = LineMessageEntityFixture.createLineEntity(startEvent);
		LineEvent numberEvent =
				LineEventFixture.createLineUserEvent(LineMessageFixture.createNegativeNumberLineMessage());
		LineMessageEntity numberLineMessageEntity = LineMessageEntityFixture.createLineEntity(numberEvent);
		LineEvent resetEvent = LineEventFixture.createLineUserEvent(LineMessageFixture.createResetLineMessage());
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
		LineEvent startEvent = LineEventFixture.createLineUserEvent(LineMessageFixture.createStartLineMessage());
		String messageText = startEvent.getMessage().getText();
		// exesice
		LineMessageEntity startLineMessageEntity = sut.createLineMessageEntity(startEvent, messageText);
		// velify
		assertThat(startLineMessageEntity.getMessageId(), is("325708"));
		assertThat(startLineMessageEntity.getUserId(), is("U206d25c2ea6bd87c17655609a1c37cb8"));
		assertThat(startLineMessageEntity.getTimestamp(), is(146262947912543L));
		assertThat(startLineMessageEntity.getValue(), is(0));
	}
	
	/**
	 * end発言時のLineEntity生成を確認します
	 */
	@Test
	public void testCreateEndLineMessageEntity() {
		// setup
		LineEvent endEvent = LineEventFixture.createLineUserEvent(LineMessageFixture.createEndLineMessage());
		String messageText = endEvent.getMessage().getText();
		// exesice
		LineMessageEntity endLineMessageEntity = sut.createLineMessageEntity(endEvent, messageText);
		// velify
		// getValue以外はtestCreateStartLineMessageEntityと同じなので割愛
		assertThat(endLineMessageEntity.getValue(), is(0));
	}
	
	/**
	 * reset発言時のLineEntity生成を確認します
	 */
	@Test
	public void testCreateResetLineMessageEntity() {
		// setup
		LineEvent resetEvent = LineEventFixture.createLineUserEvent(LineMessageFixture.createResetLineMessage());
		String messageText = resetEvent.getMessage().getText();
		// exesice
		LineMessageEntity resetLineMessageEntity = sut.createLineMessageEntity(resetEvent, messageText);
		// velify
		// getValue以外はtestCreateStartLineMessageEntityと同じなので割愛
		assertThat(resetLineMessageEntity.getValue(), is(0));
	}
	
	/**
	 * 数字発言時のLineEntity生成を確認します
	 */
	@Test
	public void testCreateNumberLineMessageEntity() {
		// setup
		LineEvent numberEvent = LineEventFixture.createLineUserEvent(LineMessageFixture.createNumberLineMessage());
		String messageText = numberEvent.getMessage().getText();
		// exesice
		LineMessageEntity numberLineMessageEntity = sut.createLineMessageEntity(numberEvent, messageText);
		// velify
		// getValue以外はtestCreateStartLineMessageEntityと同じなので割愛
		assertThat(numberLineMessageEntity.getValue(), is(12));
	}
}
