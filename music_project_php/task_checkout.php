<?php
/*	Script checks task and returns its' output if exists.
	
	Post data:
		task_id - task to track
	
	Script returns json.
	-"status" - "success" or "error"
	
	If succes:
	-"task_status" - ...
	-"output" - task output if success
	or
	-"error_msg" - error information
*/

	include("db_connection.php");
		
	if (!isset($_POST["task_id"]) || !is_numeric($_POST["task_id"]))
	{
		$resp = array("status" => "error", "error_msg" => "Invalid post data.");
		die(json_encode($resp));
	}
	
	$conn = db_make_connection();
	
	$sql = sprintf("SELECT * FROM `tasks` WHERE `id` = %s", $_POST["task_id"]);
	$result = $conn->query($sql);
	
	if (!$result)
	{
		$resp = array("status" => "error", "error_msg" => "Db error.");
		die(json_encode($resp));
	}
	
	if ($row = $result->fetch_assoc())
	{
		$resp = array("status" => "success", "task_status" => $row["status"], "output" => $row["output"]);
		echo(json_encode($resp));
	}
	else
	{
		$resp = array("error" => "task doesn't exist");
		die(json_encode($resp));
	}
	
	$conn->close();

?>
