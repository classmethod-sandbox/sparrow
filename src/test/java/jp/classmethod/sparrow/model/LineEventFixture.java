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

import java.util.Collections;

/**
 * Created by mochizukimasao on 2017/03/30.
 *
 * @author mochizukimasao
 * @since version
 */
public class LineEventFixture {
	
	// startLineEventを生成
	public static LineEvent createStartLineUserEvent(String userId, Integer timeStamp) {
		return new LineEvent(
				"message",
				timeStamp,
				createLineUserEventSource(userId),
				LineMessageFixture.createStartLineMessage(),
				"nHuyWiB7yP5Zw52FIkcQobQuGDXCTA",
				null,
				null);
	}
	
	// endLineEventを生成
	public static LineEvent createEndLineUserEvent(String userId, Integer timeStamp) {
		return new LineEvent(
				"message",
				timeStamp,
				createLineUserEventSource(userId),
				LineMessageFixture.createEndLineMessage(),
				"nHuyWiB7yP5Zw52FIkcQobQuGDXCTA",
				null,
				null);
	}
	
	// ResetLineEventを生成
	public static LineEvent createResetLineUserEvent(String userId, Integer timeStamp) {
		return new LineEvent(
				"message",
				timeStamp,
				createLineUserEventSource(userId),
				LineMessageFixture.createResetLineMessage(),
				"nHuyWiB7yP5Zw52FIkcQobQuGDXCTA",
				null,
				null);
	}
	
	// numberLineEventを生成
	public static LineEvent createNumberLineUserEvent(String userId, Integer timeStamp) {
		return new LineEvent(
				"message",
				timeStamp,
				createLineUserEventSource(userId),
				LineMessageFixture.createNumberLineMessage(),
				"nHuyWiB7yP5Zw52FIkcQobQuGDXCTA",
				null,
				null);
	}
	
	// NegativeNumberLineEventを生成
	public static LineEvent createNegativeNumberLineUserEvent(String userId, Integer timeStamp) {
		return new LineEvent(
				"message",
				timeStamp,
				createLineUserEventSource(userId),
				LineMessageFixture.createNegativeNumberLineMessage(),
				"nHuyWiB7yP5Zw52FIkcQobQuGDXCTA",
				null,
				null);
	}
	
	// createInvalidLineEventを生成
	public static LineEvent createInvalidLineUserEvent(String userId, Integer timeStamp) {
		return new LineEvent(
				"message",
				timeStamp,
				createLineUserEventSource(userId),
				LineMessageFixture.createInvalidLineMessage(),
				"nHuyWiB7yP5Zw52FIkcQobQuGDXCTA",
				null,
				null);
	}
	
	public static LineUserEventSource createLineUserEventSource(String userId) {
		return new LineUserEventSource(LineEventSourceType.USER, userId);
	}
	
	public static LineUserEventSource createLineUserEventSource2() {
		return new LineUserEventSource(LineEventSourceType.USER, "U206d25c2ea6bd87c17655609a1c37cb9");
	}
	
	public static LineGroupEventSource createLineGroupEventSource() {
		return new LineGroupEventSource(LineEventSourceType.GROUP, "U206d25c2ea6bd87c17655609a1c37cb8");
	}
	
	public static LineRoomEventSource createLineRoomEventSource() {
		return new LineRoomEventSource(LineEventSourceType.ROOM, "U206d25c2ea6bd87c17655609a1c37cb8");
	}
	
	public static LineMessageAPIRequest createLineMessageAPIRequest(LineMessage lineMessage) {
		return new LineMessageAPIRequest("nHuyWiB7yP5Zw52FIkcQobQuGDXCTA",
				Collections.singletonList(LineMessageFixture.createStartLineMessage()));
	}
}
