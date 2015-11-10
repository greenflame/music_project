<?php
/*	Script creates task.
	
	Post data:
		input - input data
	
	Script returns json.
	If no errors, "task_id" contains id of created task,
	else "error" contains error information.
*/

	include("db_lib.php");

	// Checking for correct post data
	if (!isset($_POST["input"]))
	{
		$resp = array("error" => "invalid post data");
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
		$resp = array("error" => "db insert error");
		die(json_encode($resp));
	}
	
	// Return result
	$resp = array("task_id" => $conn->insert_id);
	echo(json_encode($resp));

	$conn->close();
?>
