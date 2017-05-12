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

import org.springframework.boot.test.json.JacksonTester;
import org.springframework.boot.test.json.ObjectContent;

import org.junit.Before;
import org.junit.Test;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * Created by mochizukimasao on 2017/03/30.
 *
 * @author mochizukimasao
 * @since version
 */
public class LineEventTest {
	
	JacksonTester<LineEvent> json;
	
	private ObjectMapper mapper;
	
	private static final String USER_EVENT_JSON =
			"{"
					+ "  'replyToken': 'nHuyWiB7yP5Zw52FIkcQobQuGDXCTA',"
					+ "  'type': 'message',"
					+ "  'timestamp': 146262947912543,"
					+ "  'source': {"
					+ "    'type': 'user',"
					+ "    'userId': 'U206d25c2ea6bd87c17655609a1c37cb8'"
					+ "  },"
					+ "  'message': {"
					+ "    'id': '325708',"
					+ "    'type': 'text',"
					+ "    'text': 'start'"
					+ "  }"
					+ "}";
	
	private static final String GROUP_EVENT_JSON =
			"{"
					+ "  'replyToken': 'nHuyWiB7yP5Zw52FIkcQobQuGDXCTA',"
					+ "  'type': 'join',"
					+ "  'timestamp': 146262947912543,"
					+ "  'source': {"
					+ "    'type': 'group',"
					+ "    'groupId': 'U206d25c2ea6bd87c17655609a1c37cb8'"
					+ "  }"
					+ "}";
	
	private static final String ROOM_EVENT_JSON =
			"{"
					+ "  'replyToken': 'nHuyWiB7yP5Zw52FIkcQobQuGDXCTA',"
					+ "  'type': 'join',"
					+ "  'timestamp': 146262947912543,"
					+ "  'source': {"
					+ "    'type': 'room',"
					+ "    'roomId': 'U206d25c2ea6bd87c17655609a1c37cb8'"
					+ "  }"
					+ "}";
	
	
	@Before
	public void setup() throws Exception {
		mapper = new ObjectMapper();
		mapper.configure(JsonParser.Feature.ALLOW_SINGLE_QUOTES, true);
		JacksonTester.initFields(this, mapper);
		assert json != null; // required for FindBugs NP_UNWRITTEN_FIELD
	}
	
	@Test
	public void testJson2Value() throws Exception {
		//setup
		LineEvent expect = LineEventFixture.createLineUserEvent(LineMessageFixture.createStartLineMessage());
		
		// exercise
		ObjectContent<LineEvent> actual = json.parse(USER_EVENT_JSON);
		
		//verify
		actual.assertThat().isEqualTo(expect);
	}
	
	@Test
	public void testJsonValidity() throws Exception {
		
		// exercise 1 : user event
		LineEvent event1 = mapper.readValue(USER_EVENT_JSON, LineEvent.class);
		assertThat(event1.getSource().getClass().getName(), is(LineUserEventSource.class.getName()));
		assertThat(event1.getReplyToken(), is("nHuyWiB7yP5Zw52FIkcQobQuGDXCTA"));
		assertThat(event1.getType(), is("message"));
		assertThat(event1.getMessage().getClass().getName(), is(LineMessage.class.getName()));
		
		// exercise 2 : group event
		LineEvent event2 = mapper.readValue(GROUP_EVENT_JSON, LineEvent.class);
		assertThat(event2.getSource().getClass().getName(), is(LineGroupEventSource.class.getName()));
		
		// exercise 3 : room event
		LineEvent event3 = mapper.readValue(ROOM_EVENT_JSON, LineEvent.class);
		assertThat(event3.getSource().getClass().getName(), is(LineRoomEventSource.class.getName()));
	}
}
