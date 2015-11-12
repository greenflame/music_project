<!DOCTYPE html>
<html>
	<head>
		<link rel="stylesheet" type="text/css" href="style.css"> 

		<script src="js/jquery-2.1.4.min.js"></script>
		<script src="js/highcharts.js"></script>
		
		<script>
		
		var checkout_interval, task_id;
		
		$(document).ready(function(){
			$("#btn_task_create").click(task_create);
		});
		
		function task_create(){
			$.post("task_create.php",
			{
				input: $("#input").val()
			},
			task_crate_callback);
			
			console.log("Task create request sent.");
		}

		function task_crate_callback(data, status)
		{
			console.log("Task create response acepted.");
			
			if (status != "success")
			{
				console.log("Error request transfer.");
				return;
			}
			
			var resp = jQuery.parseJSON(data);
			
			if (resp.status != "success")
			{
				console.log("Error request execution.");
				return;
			}

			task_id = resp.task_id;
			
			console.log("Task created. id = " + task_id + ".");
			$("#label_status").html("Waiting for server...");
			
			console.log("Starting timer.");
			checkout_interval = setInterval(task_checkout, 1000);
		}
		
		function task_checkout()
		{
			$.post("task_checkout.php",
			{
				task_id: task_id
			},
			task_checkout_callback);
			
			console.log("Task checkout request sent.");
		}
		
		function task_checkout_callback(data, status)
		{
			console.log("Task checkout responce acsepted.");
			
			if (status != "success")	// transfer level
			{
				console.log("Error request transfer. Timer stoped.");
				
				clearInterval(checkout_interval);
				return;
			}
			
			var resp = jQuery.parseJSON(data);
			
			if (resp.status != "success")	// execution level
			{
				console.log("Request execution error. Timer stoped.");
				
				clearInterval(checkout_interval);
				return;
			}
			
			if (resp.task_status != "in queue")	// task manager level
			{
				clearInterval(checkout_interval);	// stop timer
				
				
				console.log("Task processed. Stopping timer.");
				
				if (resp.task_status != "success")	// task manager level
				{
					console.log("task manager level error");
					return;
				}
				
				processOutput(resp.output);
				
				$("#label_status").html("");
				return;
			}
			
			console.log("Continue waiting.");
		}
		
		function processOutput(output)
		{
			$("#output").html(output);
		}
		
		$(function () {
			$("#btn_draw_chart").click(function(){
				$('#chart_container').highcharts({
					title: {
						text: 'Title',
					},
					subtitle: {
						text: 'Subtitle',
					},
					xAxis: {
						title: {
							text: 'x axis',
						},
					},
					yAxis: {
						title: {
							text: 'y axis'
						},
					},
					legend: {
						layout: 'vertical',
						align: 'right',
						verticalAlign: 'middle',
						borderWidth: 0
					},
					series: [{
						name: 'Tokyo',
						data: [7.0, 6.9, 9.5, 14.5, 18.2, 21.5, 25.2, 26.5, 23.3, 18.3, 13.9, 9.6]
					}, {
						name: 'New York',
						data: [-0.6, 0.8, 5.7, 11.3, 17.0, 22.0, 24.8, 24.1, 20.1, 14.1, 8.6, 2.5]
					}, {
						name: 'Berlin',
						data: [-0.9, 0.6, 3.5, 8.4, 13.5, 17.0, 18.6, 17.9, 14.3, 9.0, 3.9, 1.0]
					}, {
						name: 'London',
						data: [3.9, 4.2, 5.7, 8.5, 11.9, 15.2, 17.0, 16.6, 14.2, 10.3, 6.6, 4.8]
					}]
				});
			});
		});
		
		</script>

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
