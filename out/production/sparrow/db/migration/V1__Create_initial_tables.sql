CREATE TABLE foobar (
  `foobar_id` VARCHAR(128) NOT NULL,
  `foobar_str` VARCHAR(128) NOT NULL,
  `foobar_num` BIGINT(20) NOT NULL,
  PRIMARY KEY (`foobar_id`)
)/*! CHARACTER SET utf8mb4 COLLATE utf8mb4_bin */;

INSERT INTO foobar (foobar_id, foobar_str, foobar_num) VALUES ('miyamoto', 'daisuke', 123);
