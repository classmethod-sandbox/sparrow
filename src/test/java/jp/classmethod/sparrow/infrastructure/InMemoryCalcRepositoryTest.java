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
package jp.classmethod.sparrow.infrastructure;

import static org.hamcrest.Matchers.hasSize;
import static org.junit.Assert.assertThat;

import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.runners.MockitoJUnitRunner;

import jp.classmethod.sparrow.model.LineMessageEntity;

/**
 * Created by kunita.fumiko on 2017/05/08.
 */
@RunWith(MockitoJUnitRunner.class)
public class InMemoryCalcRepositoryTest {
	
	@InjectMocks
	InMemoryCalculatorRepository sut;
	
	LineMessageEntity lineMessageEntity1 = new LineMessageEntity();
	
	LineMessageEntity lineMessageEntity2 = new LineMessageEntity();
	
	LineMessageEntity lineMessageEntity3 = new LineMessageEntity();
	
	
	@Before
	public void setup() {
		lineMessageEntity1.setMessageId("325708");
		lineMessageEntity1.setUserId("U206d25c2ea6bd87c17655609a1c37cb8");
		lineMessageEntity1.setTimestamp(1462629479859L);
		lineMessageEntity1.setValue(0);
		
		lineMessageEntity2.setMessageId("325709");
		lineMessageEntity2.setUserId("U206d25c2ea6bd87c17655609a1c37cb8");
		lineMessageEntity2.setTimestamp(1462629479860L);
		lineMessageEntity2.setValue(10);
		
		lineMessageEntity3.setMessageId("325710");
		lineMessageEntity3.setUserId("U206d25c2ea6bd87c17655609a1c38cb9");
		lineMessageEntity3.setTimestamp(1462629479862L);
		lineMessageEntity3.setValue(0);
	}
	
	@Test
	public void testSave() {
		//setup
		//exercise
		sut.save(lineMessageEntity1);
		List<LineMessageEntity> list1 = sut.save(lineMessageEntity2);
		List<LineMessageEntity> list2 = sut.save(lineMessageEntity3);
		//verify
		assertThat(list1, hasSize(2));
		assertThat(list2, hasSize(1));
	}
	
	/**
	 *
	 */
	@Test
	public void testFindByUser() {
		//setup
		sut.save(lineMessageEntity1);
		sut.save(lineMessageEntity2);
		sut.save(lineMessageEntity3);
		//exercise
		List<LineMessageEntity> list1 = sut.findByUser("U206d25c2ea6bd87c17655609a1c37cb8", 1, 1);
		List<LineMessageEntity> list2 = sut.findByUser("U206d25c2ea6bd87c17655609a1c40cb2", 1, 1);
		//verify
		assertThat(list1, hasSize(2));
		assertThat(list2, hasSize(0));
		
	}
	
}
