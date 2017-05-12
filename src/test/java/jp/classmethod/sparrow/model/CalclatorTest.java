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

import org.junit.Before;
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

	LineEvent startEvent;

	LineEvent endEvent;

	LineEvent resetEvent;

	LineEvent numberEvent;

	LineEvent invalidEvent;

	LineMessageEntity startLineMessageEntity;
	
	LineMessageEntity endLineMessageEntity;
	
	LineMessageEntity resetLineMessageEntity;
	
	LineMessageEntity numberLineMessageEntity;
	
	@Spy
	InMemoryCalculatorRepository inMemoryCalculatorRepository;
	
	@InjectMocks
	Calculator sut;
	
	
	@Before
	public void setup() {
		// start
		startEvent = LineEventFixture.createLineUserEvent(LineMessageFixture.createStartLineMessage());
		startLineMessageEntity = LineMessageEntityFixture.createLineEntity(startEvent);
		
		// end
		endEvent = LineEventFixture.createLineUserEvent(LineMessageFixture.createEndLineMessage());
		endLineMessageEntity = LineMessageEntityFixture.createLineEntity(endEvent);
		
		// reset
		resetEvent = LineEventFixture.createLineUserEvent(LineMessageFixture.createResetLineMessage());
		resetLineMessageEntity = LineMessageEntityFixture.createLineEntity(resetEvent);
		
		// 数字
		numberEvent = LineEventFixture.createLineUserEvent(LineMessageFixture.createNumberLineMessage());
		numberLineMessageEntity = LineMessageEntityFixture.createLineEntity(numberEvent);

		// 無効な文字列
		invalidEvent = LineEventFixture.createLineUserEvent(LineMessageFixture.createInvalidLineMessage());
	}
	
	@Test
	public void testSave() {
		// setup
		when(inMemoryCalculatorRepository.save(startLineMessageEntity)).thenCallRealMethod();
		when(inMemoryCalculatorRepository.save(numberLineMessageEntity)).thenCallRealMethod();

		// exesice
		String result1 = sut.save(startEvent);		// start
		String result2 = sut.save(endEvent);  		// end
		String result3 = sut.save(resetEvent);  	// reset
		String result4 = sut.save(numberEvent);		// 数字
		String result5 = sut.save(invalidEvent);	// 無効な文字列
		
		// verify
		assertThat(result1, is("calc mode start"));
		assertThat(result2, is("12"));
		assertThat(result3, is(""));
		assertThat(result4, is(""));
		assertThat(result5, is(""));
	}
	
	@Test
	public void testCalculateTotal() {
		// setup
		when(inMemoryCalculatorRepository.save(startLineMessageEntity)).thenCallRealMethod();
		when(inMemoryCalculatorRepository.save(numberLineMessageEntity)).thenCallRealMethod();
		// exesice
		Integer result = sut.calculateTotal(endLineMessageEntity);
		// varify
		assertThat(result, is(12));
	}
	
	@Test
	public void testResetList() {
		// setup
		// totalがプラスになる
		when(inMemoryCalculatorRepository.save(startLineMessageEntity)).thenCallRealMethod();
		when(inMemoryCalculatorRepository.save(numberLineMessageEntity)).thenCallRealMethod();
		when(inMemoryCalculatorRepository.save(numberLineMessageEntity)).thenCallRealMethod();
		// exesice
		Integer result = sut.resetList(resetLineMessageEntity);
		// verify
		assertThat(result, is(0));
	}
	
	@Test
	public void testResetList2() {
		// setup
		numberLineMessageEntity.setValue(-12);
		// totalがマイナスになる
		when(inMemoryCalculatorRepository.save(startLineMessageEntity)).thenCallRealMethod();
		when(inMemoryCalculatorRepository.save(numberLineMessageEntity)).thenCallRealMethod();
		when(inMemoryCalculatorRepository.save(numberLineMessageEntity)).thenCallRealMethod();
		
		// exesice
		Integer result = sut.resetList(resetLineMessageEntity);
		// verify
		assertThat(result, is(0));
	}
	
	@Test
	public void testCreateLineMessageEntity() {
		// setup
		String messageText1 = startEvent.getMessage().getText();	// start
		String messageText2 = endEvent.getMessage().getText();		// end
		String messageText3 = resetEvent.getMessage().getText();	// reset
		String messageText4 = numberEvent.getMessage().getText();	// 数字
		// exesice
		LineMessageEntity lineMessageEntity5 = sut.createLineMessageEntity(startEvent, messageText1);
		LineMessageEntity lineMessageEntity6 = sut.createLineMessageEntity(endEvent, messageText2);
		LineMessageEntity lineMessageEntity7 = sut.createLineMessageEntity(resetEvent, messageText3);
		LineMessageEntity lineMessageEntity8 = sut.createLineMessageEntity(numberEvent, messageText4);
		// velify
		assertThat(lineMessageEntity5.getMessageId(), is("325708"));
		assertThat(lineMessageEntity5.getUserId(), is("U206d25c2ea6bd87c17655609a1c37cb8"));
		assertThat(lineMessageEntity5.getTimestamp(), is(146262947912543L));
		// messageTextによって値が変わるので全パターンテスト
		assertThat(lineMessageEntity5.getValue(), is(0));
		assertThat(lineMessageEntity6.getValue(), is(0));
		assertThat(lineMessageEntity7.getValue(), is(0));
		assertThat(lineMessageEntity8.getValue(), is(12));
	}
	
}
