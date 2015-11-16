<?php
/*	Script checks task and returns its' status and output.
	
	Post data:
		-"task_id" - task to track
	
	Script returns json.
		-"status" - "success" or "error"
		
		If succes:
		-"task_status" - ...
		-"output" - task output
		else
		-"error_msg" - error information
*/

	include("useful.php");
		
	if (isset($_POST["task_id"]) && is_numeric($_POST["task_id"]))	// Post correct
	{
		$conn = db_make_connection();
		
		$sql = sprintf("SELECT * FROM `tasks` WHERE `id` = %s", $_POST["task_id"]);
		$result = $conn->query($sql);
		
		if ($result)	// Select success
		{
			if ($row = $result->fetch_assoc())	// Task exsists
			{
				$resp = array("status" => "success", "task_status" => $row["status"], "output" => $row["output"]);
				echo(json_encode($resp));
				$conn->close();			
			}
			else
			{
				$conn->close();			
				$resp = array("status" => "error", "error_msg" => "Task doesn't exist.");
				die(json_encode($resp));
			}
		}
		else
		{
			$conn->close();
			$resp = array("status" => "error", "error_msg" => "Task select error.");
			die(json_encode($resp));
		}
	}
	else
	{
		$resp = array("status" => "error", "error_msg" => "Invalid post data.");
		die(json_encode($resp));
	}
?>
