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
package jp.classmethod.sparrow.infrastructure;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import lombok.RequiredArgsConstructor;

import org.springframework.context.annotation.Profile;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import jp.classmethod.sparrow.model.LineMessageEntity;
import jp.classmethod.sparrow.model.LineMessageEntityRepository;

/**
 * Created by kunita.fumiko on 2017/06/06.
 */
@RequiredArgsConstructor
@Repository
@Profile("aws")
public class JdbcLineMessageRepository implements LineMessageEntityRepository {
	
	private final JdbcTemplate jdbcTemplate;
	
	
	public LineMessageEntity save(LineMessageEntity lineMessageEntity) {
		String sql = "INSERT INTO line_message_entity (user_id, message_id, timestamp, value) VALUES (?, ?, ?, ?)";
		jdbcTemplate.update(sql, new Object[] {
			lineMessageEntity.getUserId(),
			lineMessageEntity.getMessageId(),
			lineMessageEntity.getTimestamp(),
			lineMessageEntity.getValue()
		});
		return lineMessageEntity;
	}
	
	public List<LineMessageEntity> findByUser(String userId, int offset, int limit) {
		
		String sql = "SELECT * FROM line_message_entity WHERE user_id = ? ORDER BY timestamp DESC LIMIT ? OFFSET ?";
		
		List<LineMessageEntity> list = jdbcTemplate.query(sql,
				new Object[] {
					userId,
					limit,
					offset
				},
				new RowMapper<LineMessageEntity>() {
					public LineMessageEntity mapRow(ResultSet rs, int num) throws SQLException {
						LineMessageEntity lineMessageEntity = new LineMessageEntity();
						lineMessageEntity.setMessageId(rs.getString("message_id"));
						lineMessageEntity.setUserId(rs.getString("user_id"));
						lineMessageEntity.setTimestamp(rs.getLong("timestamp"));
						lineMessageEntity.setValue(rs.getString("value"));
						return lineMessageEntity;
					}
				});
		return list;
	}
}
