<!DOCTYPE html>
<html>
	<head>
		<link rel="stylesheet" type="text/css" href="style.css"> 

		<script src="js/jquery-2.1.4.min.js"></script>
		<script src="js/highcharts.js"></script>
		<script src="js/client_logic.js"></script>

	</head>
	<body>

		<h1>Ms project</h1>

		<div>
			<div class="input_block">				
				<p>Input</p>
				<textarea id="input" cols="40" rows="6"></textarea>
			</div>
	
			<div class="input_block">
				<p>Output</p>
				<textarea id="output" cols="40" rows="6" disabled></textarea>
			</div>
		</div>
		
		<button id="btn_task_create">Send to server</button><br>
		<div id="label_status"></div>
		
		<button id="btn_draw_chart">Draw chart</button>
		<div id="chart_container"></div>
		
		
	</body>
</html>
