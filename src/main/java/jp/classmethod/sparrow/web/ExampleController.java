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
package jp.classmethod.sparrow.web;

import java.util.HashMap;
import java.util.Map;

import lombok.extern.slf4j.Slf4j;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * サンプルのcontroller実装。
 *
 * <p>{@code GET /} のリクエストに対して、{@code 200 OK Hello, world!}をレスポンスする。</p>
 *
 * @author daisuke
 * @since #version#
 */
@Slf4j
@Controller
public class ExampleController {
	
	@RequestMapping(value = "/", method = RequestMethod.GET)
	public ResponseEntity<String> index() {
		log.debug("index");
		return ResponseEntity.ok("Hello, world!");
	}
	
	// POSTでクエリを取得する練習
	@RequestMapping(value = "/calc", method = RequestMethod.POST)
	public ResponseEntity<String> calcByPost(@RequestParam int x, @RequestParam int y) {
		String result = Integer.toString(x + y);
		return ResponseEntity.ok(result);
	}
	
	// GETでクエリを取得する練習
	// valueにはパスを記述する（？以降は書かない）
	@RequestMapping(value = "/calc", method = RequestMethod.GET)
	public ResponseEntity<?> calcByGet(@RequestParam int x, @RequestParam int y) {
		String result = Integer.toString(x + y);
		return ResponseEntity.ok(result);
	}
	
	// Json
	@RequestMapping(value = "/calc-json", method = RequestMethod.GET)
	public ResponseEntity<String> calcJson(@RequestParam int x, @RequestParam int y)
			throws JsonProcessingException {
		
		log.debug("calcJson");
		ObjectMapper mapper = new ObjectMapper();
		String json = mapper.writeValueAsString(getMap(x, y));
		
		HttpHeaders headers = new HttpHeaders();
		headers.add("content-type", "application/json;" + "charset=utf-8");
		
		return new ResponseEntity<String>(json, headers, HttpStatus.OK);
	}
	
	public Map<String, Integer> getMap(int x, int y) {
		HashMap<String, Integer> map = new HashMap<String, Integer>();
		map.put("add", x + y);
		map.put("subtract", x - y);
		map.put("multiply", y / x);
		return map;
	}
}
