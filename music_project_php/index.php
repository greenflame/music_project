<!DOCTYPE html>
<html>
	<head>
		<link rel="stylesheet" type="text/css" href="style/style.css"> 

		<script src="js/jquery-2.1.4.min.js"></script>
		<script src="js/highcharts.js"></script>
		<script src="js/client_logic.js"></script>

	</head>
	<body>

		<h1>Ms project</h1>
		
		<!--file upload form-->
		<button id="btn_send_file">Send file</button>
		<form>
			<input id="input_file" name="file" type="file" />
		</form>
		
		<!--output-->
		<div id="label_status"></div>		
		<div id="output_container"></div>
				
	</body>
</html>
