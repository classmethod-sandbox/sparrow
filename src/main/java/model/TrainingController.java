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
package model;

import java.io.IOException;

import lombok.extern.slf4j.Slf4j;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import jp.xet.sparwings.spring.web.httpexceptions.HttpBadRequestException;

/**
 * Created by kunita.fumiko on 2017/03/22.
 */
@Slf4j
@Controller
public class TrainingController {
	//リクエストボディ情報の取得
	@RequestMapping(value = "/aaa", method = RequestMethod.POST)
	public ResponseEntity<String> getProcessedCharacter(@RequestParam String convertType,
			@RequestParam String character) throws IOException {
		log.debug("TrainingController");
		String[] request = {
			convertType,
			character
		};
		
		//Converterの取得
		Converter converter;
		switch (Integer.valueOf(request[0])) {
			case 0:
				converter = new DoublingConverter();
				break;
			case 1:
				converter = new NoDigitConverter();
				break;
			case 2:
				converter = new ToLowerConverter();
				break;
			default:
				throw new HttpBadRequestException();
		}
		String result = new Processor().run(converter, request[1]);
		
		return ResponseEntity.ok(result);
	}
}