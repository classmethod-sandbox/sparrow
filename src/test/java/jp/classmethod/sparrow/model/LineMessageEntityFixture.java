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
 * Created by kunita.fumiko on 2017/05/12.
 */
public class LineMessageEntityFixture {
	
	public static LineMessageEntity createLineEntity(LineEvent event) {
		LineMessageEntity startLineEntity = new LineMessageEntity();
		startLineEntity.setMessageId(event.getMessage().getId());
		startLineEntity.setUserId(event.getSource().getId());
		startLineEntity.setTimestamp(event.getTimestamp());
		
		switch (event.getMessage().getText()) {
			case "start":
			case "reset":
			case "end":
				startLineEntity.setValue(0);
				break;
			default:
				startLineEntity.setValue(Integer.valueOf(event.getMessage().getText()));
				break;
		}
		return startLineEntity;
	}
}
