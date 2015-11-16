<?php
/*	Script creates task.
	
	Post data:
		- audiofile
	
	Script returns json.
		-"status" - "success" or "error" // request execution level
		
		If success
		-"task_id"
		else
		-"error_msg" - error info
*/

	include("useful.php");
	include("settings.php");

    if (isset($_FILES['file']['error']) && !is_array($_FILES['file']['error']))	// Accepting file
	{
		// Moving file to storage
		$storageName = generateRandomString() . ".mp3";
		$realName = $_FILES['file']['name'];
		move_uploaded_file($_FILES['file']['tmp_name'], $storagePath . $storageName);
	
		$conn = db_make_connection();
		
		// Add record about file to database
		$sql = sprintf("INSERT INTO `audiofiles` (`storage_name`, `real_name`)
			VALUES ('%s', '%s')", $storageName, $realName);
		
		$result = $conn->query($sql);
		
		if ($result)	// File record add success
		{
			$filedId = $conn->insert_id;
			
			// Add task
			$task = array("task" => "analyse", "file_id" => $filedId);
			$sql = sprintf("INSERT INTO `tasks` (`input`) VALUES ('%s');", json_encode($task));
			$result = $conn->query($sql);
			
			if ($result)	// Task created
			{
				// Return result
				$resp = array("status" => "success", "task_id" => $conn->insert_id);
				echo(json_encode($resp));			
			}
			else
			{
				$conn->close();
				$resp = array("status" => "error", "error_msg" => "Error inserting task record.");
				die(json_encode($resp));				
			}
		}
		else
		{
			$conn->close();
			$resp = array("status" => "error", "error_msg" => "Error inserting file record.");
			die(json_encode($resp));
		}
	
		$conn->close();
	}
	else
    {
		$resp = array("status" => "error", "error_msg" => "Error accepting file.");
		die(json_encode($resp));
    }

	
?>
