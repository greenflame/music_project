<?php
/*	Script creates task.
	
	Post data:
		input - input data
	
	Script returns json.
	-"status" - "success" or "error" // request execution level
	
	-"task_id" if succes
	or
	-"error_msg" - error explanation
*/

	include("db_connection.php");

	// Checking for correct post data
	if (!isset($_POST["input"]))
	{
		$resp = array("status" => "error", "error_msg" => "Invalid post data.");
		die(json_encode($resp));
	}

	$conn = db_make_connection();
	
	// Anti injection
	$input = mysql_real_escape_string($_POST["input"]);

	// Performing query
	$sql = sprintf("INSERT INTO `tasks` (`input`) VALUES ('%s');", $input);

	$result = $conn->query($sql);
	
	if (!$result)
	{
		$resp = array("status" => "error", "error_msg" => "Db insert error.");
		die(json_encode($resp));
	}
	
	// Return result
	$resp = array("status" => "success", "task_id" => $conn->insert_id);
	echo(json_encode($resp));

	$conn->close();
?>
