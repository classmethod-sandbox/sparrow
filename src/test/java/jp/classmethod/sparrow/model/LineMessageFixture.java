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
 * Created by kunita.fumiko on 2017/05/11.
 */
public class LineMessageFixture {
	
	public static LineMessage createStartLineMessage() {
		return new LineMessage(LineMessageType.TEXT, "325708", "start", null, null,
				0.0, 0.0, null, null);
	}
	
	public static LineMessage createEndLineMessage() {
		return new LineMessage(LineMessageType.TEXT, "325708", "end", null, null,
				0.0, 0.0, null, null);
	}
	
	public static LineMessage createResetLineMessage() {
		return new LineMessage(LineMessageType.TEXT, "325708", "reset", null, null,
				0.0, 0.0, null, null);
	}
	
	public static LineMessage createNumberLineMessage() {
		return new LineMessage(LineMessageType.TEXT, "325708", "12", null, null,
				0.0, 0.0, null, null);
	}
	
	public static LineMessage createInvalidLineMessage() {
		return new LineMessage(LineMessageType.TEXT, "325708", "ああああ", null, null,
				0.0, 0.0, null, null);
	}
}