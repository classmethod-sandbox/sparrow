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

import java.io.IOException;

import lombok.extern.slf4j.Slf4j;

/**
 * Created by kunita.fumiko on 2017/03/22.
 */
@Slf4j
public class Processor {
	
	public String run(Converter converter, String request) throws IOException {
		// これから行う処理を説明する(printDescriptionメソッドを呼び出す)
		printDescription(converter.getDescription());
		//文字列の変換処理
		String dest = converter.convert(request);
		//結果の出力処理
		return dest;
	}
	
	private void printDescription(String description) {
		log.info(description);
	}
}
