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

/**
 * Created by mochizukimasao on 2017/03/30.
 *
 * @author mochizukimasao
 * @since version
 */
public class LineEventFixture {
	
	public static LineEvent createLineUserEvent() {
		return new LineEvent(
				"message",
				146262947912543L,
				createLineUserEventSource(),
				createLineMessage(),
				"nHuyWiB7yP5Zw52FIkcQobQuGDXCTA",
				null,
				null);
	}
	
	public static LineUserEventSource createLineUserEventSource() {
		return new LineUserEventSource(LineEventSourceType.USER, "U206d25c2ea6bd87c17655609a1c37cb8");
	}
	
	public static LineGroupEventSource createLineGroupEventSource() {
		return new LineGroupEventSource(LineEventSourceType.GROUP, "U206d25c2ea6bd87c17655609a1c37cb8");
	}
	
	public static LineRoomEventSource createLineRoomEventSource() {
		return new LineRoomEventSource(LineEventSourceType.ROOM, "U206d25c2ea6bd87c17655609a1c37cb8");
	}
	
	public static LineMessage createLineMessage() {
		return new LineMessage(LineMessageType.TEXT, "325708", "Hello, world", null, null,
				0.0, 0.0, null, null);
	}
	
}
