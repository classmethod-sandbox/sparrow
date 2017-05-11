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
	
	LineEventFixture lineEventFixture;
	
	LineEvent event1;
	
	LineEvent event2;
	
	LineEvent event3;
	
	LineEvent event4;
	
	LineEvent event5;
	
	LineMessageEntity lineMessageEntity1;
	
	LineMessageEntity lineMessageEntity2;
	
	LineMessageEntity lineMessageEntity3;
	
	LineMessageEntity lineMessageEntity4;
	
	@Spy
	InMemoryCalculatorRepository inMemoryCalculatorRepository;
	
	@InjectMocks
	Calculator sut;
	
	
	@Before
	public void setup() {
		// start
		event1 = lineEventFixture.createLineUserEvent();
		lineMessageEntity1 = sut.createLineMessageEntity(event1, event1.getMessage().getText());
		
		// end
		LineMessage lineMessage1 = lineEventFixture.createLineMessage();
		lineMessage1.setText("end");
		event2 = new LineEvent(
				"message",
				146262947912544L,
				lineEventFixture.createLineUserEventSource(),
				lineMessage1,
				"nHuyWiB7yP5Zw52FIkcQobQuGDXCTA",
				null,
				null);
		lineMessageEntity2 = sut.createLineMessageEntity(event2, event2.getMessage().getText());
		
		// reset
		LineMessage lineMessage2 = lineEventFixture.createLineMessage();
		lineMessage2.setText("reset");
		event3 = new LineEvent(
				"message",
				146262947912545L,
				lineEventFixture.createLineUserEventSource(),
				lineMessage2,
				"nHuyWiB7yP5Zw52FIkcQobQuGDXCTA",
				null,
				null);
		lineMessageEntity3 = sut.createLineMessageEntity(event3, event3.getMessage().getText());
		
		// 数字
		LineMessage lineMessage3 = lineEventFixture.createLineMessage();
		lineMessage3.setText("12");
		event4 = new LineEvent(
				"message",
				146262947912546L,
				lineEventFixture.createLineUserEventSource(),
				lineMessage3,
				"nHuyWiB7yP5Zw52FIkcQobQuGDXCTA",
				null,
				null);
		lineMessageEntity4 = sut.createLineMessageEntity(event4, event4.getMessage().getText());
		
		// 無効な文字列
		LineMessage lineMessage4 = lineEventFixture.createLineMessage();
		lineMessage4.setText("ああああ");
		event5 = new LineEvent(
				"message",
				146262947912547L,
				lineEventFixture.createLineUserEventSource(),
				lineMessage4,
				"nHuyWiB7yP5Zw52FIkcQobQuGDXCTA",
				null,
				null);
	}
	
	@Test
	public void testSave() {
		// setup
		when(inMemoryCalculatorRepository.save(lineMessageEntity4)).thenCallRealMethod();
		// exesice
		String result1 = sut.save(event1);  // start
		String result2 = sut.save(event2);  // end
		String result3 = sut.save(event3);  // reset
		String result4 = sut.save(event4);  // 数字
		String result5 = sut.save(event5);  // 無効な文字列
		
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
		when(inMemoryCalculatorRepository.save(lineMessageEntity1)).thenCallRealMethod();
		when(inMemoryCalculatorRepository.save(lineMessageEntity4)).thenCallRealMethod();
		// exesice
		Integer result = sut.calculateTotal(lineMessageEntity2);
		// varify
		assertThat(result, is(12));
	}
	
	@Test
	public void testResetList() {
		// setup
		// totalがプラスになる
		when(inMemoryCalculatorRepository.save(lineMessageEntity1)).thenCallRealMethod();
		when(inMemoryCalculatorRepository.save(lineMessageEntity4)).thenCallRealMethod();
		when(inMemoryCalculatorRepository.save(lineMessageEntity4)).thenCallRealMethod();
		// exesice
		Integer result = sut.resetList(lineMessageEntity3);
		// verify
		assertThat(result, is(0));
	}
	
	@Test
	public void testResetList2() {
		// setup
		lineMessageEntity4.setValue(-12);
		// totalがマイナスになる
		when(inMemoryCalculatorRepository.save(lineMessageEntity1)).thenCallRealMethod();
		when(inMemoryCalculatorRepository.save(lineMessageEntity4)).thenCallRealMethod();
		when(inMemoryCalculatorRepository.save(lineMessageEntity4)).thenCallRealMethod();
		
		// exesice
		Integer result = sut.resetList(lineMessageEntity3);
		// verify
		assertThat(result, is(0));
	}
	
	@Test
	public void testCreateLineMessageEntity() {
		// setup
		String messageText1 = event1.getMessage().getText();	// start
		String messageText2 = event2.getMessage().getText();	// end
		String messageText3 = event3.getMessage().getText();	// reset
		String messageText4 = event4.getMessage().getText();	// 数字
		// exesice
		LineMessageEntity lineMessageEntity5 = sut.createLineMessageEntity(event1, messageText1);
		LineMessageEntity lineMessageEntity6 = sut.createLineMessageEntity(event2, messageText2);
		LineMessageEntity lineMessageEntity7 = sut.createLineMessageEntity(event3, messageText3);
		LineMessageEntity lineMessageEntity8 = sut.createLineMessageEntity(event4, messageText4);
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
