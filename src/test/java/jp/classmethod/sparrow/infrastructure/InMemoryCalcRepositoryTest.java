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

import jp.classmethod.sparrow.model.*;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.runners.MockitoJUnitRunner;

/**
 * Created by kunita.fumiko on 2017/05/08.
 */
@RunWith(MockitoJUnitRunner.class)
public class InMemoryCalcRepositoryTest {
	
	@InjectMocks
	InMemoryCalculatorRepository sut;

	LineEvent startEvent;

	LineEvent startEvent2;

	LineEvent numberEvent;

	LineMessageEntity startLineMessageEntity;

	LineMessageEntity startLineMessageEntity2;

	LineMessageEntity numberLineMessageEntity;
	
	// 事前準備（LineMessageEntityの生成）
	@Before
	public void setup() {
		// start
		startEvent = LineEventFixture.createLineUserEvent(LineMessageFixture.createStartLineMessage());
		startLineMessageEntity = LineMessageEntityFixture.createLineEntity(startEvent);
		// start（uIdが異なる）
		startEvent2 = LineEventFixture.createLineUserEvent2(LineMessageFixture.createStartLineMessage());
		startLineMessageEntity2 = LineMessageEntityFixture.createLineEntity(startEvent2);

		// 数字
		numberEvent = LineEventFixture.createLineUserEvent(LineMessageFixture.createNumberLineMessage());
		numberLineMessageEntity = LineMessageEntityFixture.createLineEntity(numberEvent);
	}

	// 異なるuIdでLinentityをsaveし、uId別にリスト作成＆追加しているかを確認します
	@Test
	public void testSave() {
		// setup
		// exercise
		sut.save(startLineMessageEntity);
		List<LineMessageEntity> list1 = sut.save(numberLineMessageEntity);
		List<LineMessageEntity> list2 = sut.save(startLineMessageEntity2);
		// verify
		assertThat(list1, hasSize(2));
		assertThat(list2, hasSize(1));
	}

	/* 存在しているuIdと存在しないuIdでリスト検索し、
	　　存在しているuIdでは指定したuIdのリスト（LineEntity）のみを返し、
	　　存在していないuIdではリストを持っていないことを確認します。*/
	@Test
	public void testFindByUser() {
		// setup
		sut.save(startLineMessageEntity);
		sut.save(numberLineMessageEntity);
		sut.save(startLineMessageEntity2);
		// exercise
		List<LineMessageEntity> list1 = sut.findByUser("U206d25c2ea6bd87c17655609a1c37cb8", 1, 1);
		List<LineMessageEntity> list2 = sut.findByUser("U206d25c2ea6bd87c17655609a1c40cb2", 1, 1);
		// verify
		assertThat(list1, hasSize(2));
		assertThat(list2, hasSize(0));
	}
}
