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

import java.util.stream.Stream;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Spy;
import org.mockito.runners.MockitoJUnitRunner;

import jp.classmethod.sparrow.infrastructure.InMemoryLineMessageEntityRepository;

/**
 * Created by kunita.fumiko on 2017/05/10.
 */
@RunWith(MockitoJUnitRunner.class)
public class CalculatorTest {
	
	@Spy
	LineMessageEntityRepository inMemoryLineMessageEntityRepository = new InMemoryLineMessageEntityRepository();
	
	@InjectMocks
	Calculator sut;
	
	
	/**
	 * データが保存されている状態でtotal発言時の戻り値を確認します
	 */
	@Test
	public void testTotal() {
		// setup
		String userId = "U206d25c2ea6bd87c17655609a1c37cb8";
		Stream<LineEvent> stream = Stream.of(
				LineEventFixture.createLineUserEvent("325710", userId, 1499378880L, "12"),
				LineEventFixture.createLineUserEvent("325711", userId, 1499378881L, "reset"),
				LineEventFixture.createLineUserEvent("325712", userId, 1499378882L, "12"),
				LineEventFixture.createLineUserEvent("325713", userId, 1499378883L, "12"));
		stream.forEach(sut::save);
		
		LineEvent totalEvent = LineEventFixture.createLineUserEvent("325714", userId, 1499378882L, "total");
		// exercise
		String actual = sut.save(totalEvent);
		
		// verify
		assertThat(actual, is("24"));
	}
	
	/**
	 * データが保存されていない状態でtotal発言時の戻り値を確認します
	 */
	@Test
	public void testNoDataTotal() {
		// setup
		LineEvent totalEvent =
				LineEventFixture.createLineUserEvent("325710", "U206d25c2ea6bd87c17655609a1c37cb8", 1499379060L,
						"total");
		// exercise
		String actual = sut.save(totalEvent);
		// verify
		assertThat(actual, is("0"));
	}
	
	/**
	 * reset発言時の戻り値を確認します
	 */
	@Test
	public void testResetSave() {
		// setup
		LineEvent resetEvent =
				LineEventFixture.createLineUserEvent("325710", "U206d25c2ea6bd87c17655609a1c37cb8", 1499379000L,
						"reset");
		// exercise
		String actual = sut.save(resetEvent);
		// verify
		assertThat(actual, is("reset"));
	}
	
	/**
	 * 数字発言時の戻り値を確認します
	 */
	@Test
	public void testNumberSave() {
		// setup
		LineEvent numberEvent =
				LineEventFixture.createLineUserEvent("325710", "U206d25c2ea6bd87c17655609a1c37cb8", 1499378880L, "12");
		// exercise
		String actual = sut.save(numberEvent);
		// verify
		assertThat(actual, is(""));
	}
	
	/**
	 * 仕様外の発言時の戻り値を確認します
	 */
	@Test
	public void testInvalidSave() {
		// setup
		LineEvent invalidEvent =
				LineEventFixture.createLineUserEvent("325710", "U206d25c2ea6bd87c17655609a1c37cb8", 1499379120L, "あああ");
		// exercise
		String actual = sut.save(invalidEvent);
		// verify
		assertThat(actual, is("error"));
	}
	
	/**
	 * 引数で渡すLineMessageEntityのuIdと一致するリストの合計値を返すことを確認します
	 */
	@Test
	public void testCalculateTotal() {
		// setup
		String user1 = "U206d25c2ea6bd87c17655609a1c37cb8";
		String user2 = "U206d25c2ea6bd87c17655609a1c38cb9";
		
		Stream<LineEvent> stream = Stream.of(
				LineEventFixture.createLineUserEvent("325710", user1, 1499378821L, "12"),
				LineEventFixture.createLineUserEvent("325711", user1, 1499378822L, "reset"),
				LineEventFixture.createLineUserEvent("325712", user1, 1499378823L, "12"),
				LineEventFixture.createLineUserEvent("325713", user1, 1499378824L, "12"),
				LineEventFixture.createLineUserEvent("325714", user1, 1499378825L, "12"),
				LineEventFixture.createLineUserEvent("325715", user2, 1499378826L, "12"),
				LineEventFixture.createLineUserEvent("325716", user1, 1499378827L, "12"),
				LineEventFixture.createLineUserEvent("325717", user1, 1499378828L, "12"));
		
		stream.map(LineMessageEntityFixture::createLineEntity)
			.forEach((lineEntity) -> when(inMemoryLineMessageEntityRepository.create(lineEntity)).thenCallRealMethod());
		
		LineEvent totalEvent =
				LineEventFixture.createLineUserEvent("325718", user1, 1499378829L, "total");
		
		// exercise
		int result = sut.calculateTotal(totalEvent);
		// verify
		assertThat(result, is(60));
	}
	
	/**
	 * 数字発言時のLineEntity生成を確認します
	 */
	@Test
	public void testCreateLineMessageEntity() {
		// setup
		LineEvent numberEvent =
				LineEventFixture.createLineUserEvent("325710", "U206d25c2ea6bd87c17655609a1c37cb8", 1499378820L, "12");
		// exercise
		LineMessageEntity numberLineMessageEntity = sut.createLineMessageEntity(numberEvent);
		// verify
		assertThat(numberLineMessageEntity.getMessageId(), is("325710"));
		assertThat(numberLineMessageEntity.getUserId(), is("U206d25c2ea6bd87c17655609a1c37cb8"));
		assertThat(numberLineMessageEntity.getTimestamp(), is(1499378820L));
		assertThat(numberLineMessageEntity.getValue(), is("12"));
	}
}
