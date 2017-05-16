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

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.runners.MockitoJUnitRunner;

/**
 * Created by kunita.fumiko on 2017/05/12.
 */
@RunWith(MockitoJUnitRunner.class)
public class StringCheckTest {
	
	@InjectMocks
	StringCheck sut;
	
	
	/**
	 * 値が整数の時、trueを返すことを確認します
	 */
	@Test
	public void integerNumberTest() {
		// setup
		String textMessage = "12";
		// exesice
		boolean actual = sut.isNumber(textMessage);
		// verify
		assertThat(actual, is(true));
	}
	
	/**
	 * 値が負数の時、trueを返すことを確認します
	 */
	@Test
	public void negativeNumberTest() {
		// setup
		String textMessage = "-12";
		// exesice
		boolean actual = sut.isNumber(textMessage);
		// verify
		assertThat(actual, is(true));
	}
	
	/**
	 * 値が数字以外の時、falseを返すことを確認します
	 */
	@Test
	public void stringTest() {
		// setup
		String textMessage = "start";
		// exesice
		boolean actual = sut.isNumber(textMessage);
		// verify
		assertThat(actual, is(false));
	}
}
