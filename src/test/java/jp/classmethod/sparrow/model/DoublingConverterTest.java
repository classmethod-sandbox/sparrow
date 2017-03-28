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

import static junit.framework.TestCase.assertEquals;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.runners.MockitoJUnitRunner;

/**
 * Created by kunita.fumiko on 2017/03/28.
 */
@RunWith(MockitoJUnitRunner.class)
public class DoublingConverterTest {
	
	@InjectMocks
	DoublingConverter sut;
	
	
	@Test
	public void testDoublingConverter() throws Exception {
		// exercise
		String strChar = "Qx3";
		String sb = sut.convert(strChar);
		// verify
		assertEquals("QQxx33", sb);
	}
	
	@Test
	public void testGetDescription() throws Exception {
		// exercise
		String description = sut.getDescription();
		// verify
		assertEquals("文字をダブらせます", description);
	}
}
