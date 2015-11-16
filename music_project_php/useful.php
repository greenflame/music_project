<?php
	include("settings.php");

	function db_make_connection()
	{
		include("settings.php");

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
	
	function generateRandomString($length = 10)
	{
		$characters = '0123456789abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ';
		$charactersLength = strlen($characters);
		$randomString = '';

		for ($i = 0; $i < $length; $i++) {
			$randomString .= $characters[rand(0, $charactersLength - 1)];
		}

		return $randomString;
	}

?>
