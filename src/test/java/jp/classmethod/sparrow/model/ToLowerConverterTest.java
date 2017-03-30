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
public class ToLowerConverterTest {
	
	ToLowerConverter sut = new ToLowerConverter();
	
	
	@Test
	public void testTolowerConverter() throws Exception {
		// setup
		String strChar = "Ayt3WR";
		// exercise
		String actual = sut.convert(strChar);
		// verify
		assertThat(actual, is("ayt3wr"));
	}
	
	@Test
	public void testDescription() throws Exception {
		// exercise
		String actual = sut.getDescription();
		//verify
		assertThat(actual, is("すべて小文字に変換します"));
	}
}
