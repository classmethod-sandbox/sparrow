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

import static com.jayway.jsonassert.impl.matcher.IsCollectionWithSize.hasSize;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

import java.util.List;
import java.util.stream.Stream;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import org.junit.Test;
import org.junit.runner.RunWith;

import jp.classmethod.sparrow.model.LineEvent;
import jp.classmethod.sparrow.model.LineEventFixture;
import jp.classmethod.sparrow.model.LineMessageEntity;
import jp.classmethod.sparrow.model.LineMessageEntityFixture;

/**
 * Created by kunita.fumiko on 2017/06/06.
 */

@RunWith(SpringRunner.class)
@SpringBootTest
@ActiveProfiles("aws")
public class JdbcLineMessageRepositoryTest {
	
	@Autowired
	JdbcLineMessageRepository sut;
	
	
	/**
	 * saveメソッドの引数と戻り値が一致することを確認します
	 */
	@Test
	public void testSave() {
		// setup
		String messageId = "325709";
		String user = "U206d25c2ea6bd87c17655609a1c37cb8";
		Long timeStamp = 1499378819L;
		String number = "12";
		LineEvent numberEvent =
				LineEventFixture.createLineUserEvent(messageId, user, timeStamp, number);
		LineMessageEntity numberLineMessageEntity = LineMessageEntityFixture.createLineEntity(numberEvent);
		// exercise
		LineMessageEntity actual = sut.save(numberLineMessageEntity);
		// verify
		assertThat(actual, is(numberLineMessageEntity));
	}
	
	/**
	 * 指定のuserIdと一致するリストを返すことを確認する
	 */
	@Test
	public void testFindByUser() {
		// setup
		String user1 = "U206d25c2ea6bd87c17655609a1c37cb8";
		String user2 = "U206d25c2ea6bd87c17655609a1c37cb9";
		String number = "12";
		
		Stream<LineEvent> stream = Stream.of(
				LineEventFixture.createLineUserEvent("325710", user1, 1499378820L, number),
				LineEventFixture.createLineUserEvent("325711", user1, 1499378821L, number),
				LineEventFixture.createLineUserEvent("325712", user2, 1499378822L, number),
				LineEventFixture.createLineUserEvent("325713", user1, 1499378823L, number),
				LineEventFixture.createLineUserEvent("325714", user1, 1499378824L, number));
		
		stream.map(LineMessageEntityFixture::createLineEntity)
			.forEach(sut::save);
		
		// exercise
		List<LineMessageEntity> actual = sut.findByUser(user1, 0, 2);
		
		// verify
		assertThat(actual, hasSize(2));
		assertThat(actual.get(0).getMessageId(), is("325714"));
		assertThat(actual.get(0).getUserId(), is("U206d25c2ea6bd87c17655609a1c37cb8"));
		assertThat(actual.get(0).getTimestamp(), is(1499378824L));
		assertThat(actual.get(0).getValue(), is("12"));
		assertThat(actual.get(1).getMessageId(), is("325713"));
		assertThat(actual.get(1).getUserId(), is("U206d25c2ea6bd87c17655609a1c37cb8"));
		assertThat(actual.get(1).getTimestamp(), is(1499378823L));
		assertThat(actual.get(1).getValue(), is("12"));
	}
	
	/**
	 * 指定のuserIdと一致するリストの2ページ目を返すことを確認する
	 */
	@Test
	public void testFindByUserNextPage() {
		// setup
		String user1 = "U206d25c2ea6bd87c17655609a1c37cb8";
		String user2 = "U206d25c2ea6bd87c17655609a1c37cb9";
		String number = "12";
		
		Stream<LineEvent> stream = Stream.of(
				LineEventFixture.createLineUserEvent("325715", user1, 1499378825L, number),
				LineEventFixture.createLineUserEvent("325716", user1, 1499378826L, number),
				LineEventFixture.createLineUserEvent("325717", user2, 1499378827L, number),
				LineEventFixture.createLineUserEvent("325718", user1, 1499378828L, number),
				LineEventFixture.createLineUserEvent("325719", user1, 1499378829L, number));
		stream.map(LineMessageEntityFixture::createLineEntity)
			.forEach(sut::save);
		
		// exercise
		List<LineMessageEntity> actual = sut.findByUser(user1, 2, 2);
		
		// verify
		assertThat(actual, hasSize(2));
		assertThat(actual.get(0).getMessageId(), is("325716"));
		assertThat(actual.get(0).getUserId(), is("U206d25c2ea6bd87c17655609a1c37cb8"));
		assertThat(actual.get(0).getTimestamp(), is(1499378826L));
		assertThat(actual.get(0).getValue(), is("12"));
		assertThat(actual.get(1).getMessageId(), is("325715"));
		assertThat(actual.get(1).getUserId(), is("U206d25c2ea6bd87c17655609a1c37cb8"));
		assertThat(actual.get(1).getTimestamp(), is(1499378825L));
		assertThat(actual.get(1).getValue(), is("12"));
	}
	
	/**
	 * 存在しないuserIdで検索し、空のリストを返すことを確認する
	 */
	@Test
	public void testFindByNotExistUser() {
		
		// exercise
		List<LineMessageEntity> actual = sut.findByUser("U206d25c2ea6bd87c17655609a1c40cb2", 0, 10);
		
		// verify
		assertThat(actual, hasSize(0));
	}
}
