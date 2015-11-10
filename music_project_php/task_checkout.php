<?php
/*	Script checks task and returns its' output if exists.
	
	Post data:
		task_id - task to track
	
	Script returns json.
	If no errors, "status" contains task status, "output"(if exists) contains task output,
	else "error" contains error information.
*/

	include("db_lib.php");
		
	if (!isset($_POST["task_id"]) || !is_numeric($_POST["task_id"]))
	{
		$resp = array("error" => "invalid post data");
		die(json_encode($resp));
	}
	
	$conn = db_make_connection();
	
	$sql = sprintf("SELECT * FROM `tasks` WHERE `id` = %s", $_POST["task_id"]);
	$result = $conn->query($sql);
	
	if (!$result)
	{
		$resp = array("error" => "db error");
		die(json_encode($resp));
	}
	
	if ($row = $result->fetch_assoc())
	{
		$resp = array("status" => $row["status"]);
		
		if ($row["output"] != NULL)
		{
			$resp["output"] = $row["output"];
		}
		
		echo(json_encode($resp));
	}
	else
	{
		$resp = array("error" => "task doesn't exist");
		die(json_encode($resp));
	}
	
	$conn->close();

?>
