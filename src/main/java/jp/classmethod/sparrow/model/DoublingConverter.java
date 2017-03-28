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
 * Created by kunita.fumiko on 2017/03/22.
 */
public class DoublingConverter extends AbstractConverter {
	
	@Override
	protected String computeStringToAppend(int c) {
		StringBuilder sb = new StringBuilder();
		sb.append((char) c).append((char) c);
		return sb.toString();
	}
	
	//処理説明
	public String getDescription() {
		return "文字をダブらせます";
	}
}
