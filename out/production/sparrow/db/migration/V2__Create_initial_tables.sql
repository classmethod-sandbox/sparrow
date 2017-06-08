CREATE TABLE line_message_entity (
  `user_id` VARCHAR(128) NOT NULL,
  `message_id` VARCHAR(128) NOT NULL,
  `timestamp` BIGINT(20) NOT NULL,
  `value` VARCHAR(128) NOT NULL,
  PRIMARY KEY (`message_id`)
)/*! CHARACTER SET utf8mb4 COLLATE utf8mb4_bin */;

INSERT INTO line_message_entity (user_id, message_id, timestamp, value) VALUES ('123', 'daisuke', 123, '12');
