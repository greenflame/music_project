<?php
/*	Script checks upload progress and returns its' status.

	Script returns json.
		-"status" - "success" or "error"

		If succes:
		-"upload_percent" - ...
		else
		-"error_msg" - error information
*/

	include("useful.php");

	session_start();

	if (isset($_SESSION["upload_progress_unc"])) {
		$progress = $_SESSION["upload_progress_unc"];
		$percent = round(100 * $progress['bytes_processed'] / $progress['content_length']);

		$resp = array("status" => "success", "upload_percent" => $percent);
		die(json_encode($resp));
	}
	else
	{
		$resp = array("status" => "error", "error_msg" => "No uploads.");
		die(json_encode($resp));
	}

?>
