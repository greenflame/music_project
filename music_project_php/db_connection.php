<?php

	function db_make_connection()
	{
		// Conection info
		$servername = "localhost";
		$username = "root";
		$password = "";
		$dbname = "music_project";

		// Create connection
		$conn = new mysqli($servername, $username, $password, $dbname);
	
		// Check connection
		if ($conn->connect_error)
		{
			$resp = array("error" => "db connect error: " . mysqli_connect_error());
			die(json_encode($resp));
		}
		
		return $conn;
	}

?>
