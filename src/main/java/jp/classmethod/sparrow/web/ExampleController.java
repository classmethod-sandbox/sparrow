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

import java.util.List;
import java.util.Map;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.http.ResponseEntity;
import org.springframework.jdbc.core.ColumnMapRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

/**
 * Example controller
 *
 * @author daisuke
 * @since "version"
 */
@RestController
@RequestMapping(path = "/example")
@Slf4j
@RequiredArgsConstructor
public class ExampleController {
	
	private static final ColumnMapRowMapper MAPPER = new ColumnMapRowMapper();
	
	private final JdbcTemplate jdbcTemplate;
	
	
	@RequestMapping(method = RequestMethod.GET)
	public ResponseEntity<?> handle() {
		log.info("example");
		List<Map<String, Object>> result =
				jdbcTemplate.query("SELECT * FROM foobar ORDER BY foobar_id LIMIT 10", MAPPER);
		return ResponseEntity.ok(result);
		
	}
}
