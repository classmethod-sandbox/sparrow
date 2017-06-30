SELECT *
FROM line_message_entity e

/*BEGIN*/
WHERE
	/*IF id != null*/
	e.message_id = /*id*/1
	/*END*/
	
	/*IF userId != null*/
	AND e.user_id = /*userId*/'foobar'
	/*END*/
/*END*/

ORDER BY timestamp DESC

/*BEGIN*/
LIMIT
  /*IF offset != null*/
  /*offset*/0,
  /*END*/

  /*IF size != null*/
  /*size*/10
  /*END*/
/*END*/
