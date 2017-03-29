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

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.mockito.Matchers.anyString;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.junit.Test;

/**
 * Created by kunita.fumiko on 2017/03/29.
 */
public class ProcessorTest {
	
	Processor sut = new Processor();
	
	
	@Test
	public void testRun() throws Exception {
		// setup
		Converter converter = mock(Converter.class);
		when(converter.convert(anyString())).thenReturn("def");
		when(converter.getDescription()).thenReturn("desc");
		String character = "abc";
		//exercise
		String actual = sut.run(converter, character);
		// verify
		assertThat(actual, is("def"));
		verify(converter).getDescription();
		verify(converter).convert(eq("abc"));
	}
}
