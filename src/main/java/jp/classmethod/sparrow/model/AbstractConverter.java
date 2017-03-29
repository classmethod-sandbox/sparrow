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
import java.io.IOException;
import java.io.StringReader;

public abstract class AbstractConverter implements Converter {
	
	public final String convert(String character) throws IOException {
		StringBuilder sb = new StringBuilder();
		StringReader reader = new StringReader(character);
		int c;
		while ((c = reader.read()) != -1) {
			String str = computeStringToAppend(c);
			if (str != null) {
				sb.append(str);
			}
		}
		return sb.toString();
	}
	
	protected abstract String computeStringToAppend(int c);
}