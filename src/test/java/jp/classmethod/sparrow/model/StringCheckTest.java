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
	
	
	@Test
	// 文字列が数字かどうかを確認します
	public void isNumberTest() {
		// setup
		String textNumber1 = "12";
		String textNumber2 = "-12";
		String textString = "あああああ";
		// exesice
		boolean resultNumber1 = sut.isNumber(textNumber1);
		boolean resultNumber2 = sut.isNumber(textNumber2);
		boolean resultString = sut.isNumber(textString);
		// verify
		assertThat(resultNumber1, is(true));
		assertThat(resultNumber2, is(true));
		assertThat(resultString, is(false));
	}
}
