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
package jp.classmethod.sparrow;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Arrays;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.boot.CommandLineRunner;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowCallbackHandler;
import org.springframework.stereotype.Component;

/**
 * サンプルの {@link CommandLineRunner} 実装。
 *
 * <p>ログに {@code Hello, world!} 及び、コマンドライン引数（あれば）を出力する。</p>
 *
 * @author daisuke
 * @since #version#
 */
@Slf4j
@RequiredArgsConstructor
@Component
public class ExampleCommandlineRunner implements CommandLineRunner {
	
	private final JdbcTemplate jdbcTemplate;
	
	
	@Override
	public void run(String... args) {
		log.info("Hello, world!");
		Arrays.stream(args).forEach(log::info);
		
		jdbcTemplate.update("INSERT INTO foobar (foobar_id, foobar_str, foobar_num) VALUES (?, ?, ?)",
				"yokota", "satoshi", 456L);
		
		jdbcTemplate.query("SELECT * FROM foobar ORDER BY foobar_id", new RowCallbackHandler() {
			@Override
			public void processRow(ResultSet rs) throws SQLException {
				String id = rs.getString("foobar_id");
				String str = rs.getString("foobar_str");
				long num = rs.getLong("foobar_num");
				log.info("ID:{}, str:{}, num:{}", id, str, num);
			}
		});
	}
}
