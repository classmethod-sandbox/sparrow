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

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;

import org.junit.Test;

/**
 * Created by kunita.fumiko on 2017/03/29.
 */
public class NoDigitConverterTest {
	
	NoDigitConverter sut = new NoDigitConverter();
	
	
	@Test
	public void testNoDigitConverter() throws Exception {
		// setup
		String strChar = "1Sd3w4T";
		// exercise
		String actual = sut.convert(strChar);
		// verify
		assertThat(actual, is("SdwT"));
	}
	
	@Test
	public void testDescription() throws Exception {
		// exercise
		String actual = sut.getDescription();
		// verify
		assertThat(actual, is("数字以外を除去します"));
	}
}
