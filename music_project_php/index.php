<!DOCTYPE html>
<html>
	<head>
		<link rel="stylesheet" type="text/css" href="style.css"> 
		<script src="js/jquery-2.1.4.min.js"></script>
		
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
				console.log("Error connecting to server.");
				return;
			}
			
			var resp = jQuery.parseJSON(data);
			
			if (resp.error)
			{
				console.log("Server execution error.");
				return;
			}

			task_id = resp.task_id;
			
			console.log("Task created. id = " + task_id + ".");
			$("#label_status").html("Waiting for server...");
			
			console.log("Starting timer");
			checkout_interval = setInterval(task_checkout, 1000);
		}
		
		function task_checkout()
		{
			$.post("task_checkout.php",
			{
				task_id: task_id
			},
			task_checkout_callback);
			
			console.log("Checkout request sent.");
		}
		
		function task_checkout_callback(data, status)
		{
			console.log("Checkout responce acsepted.");
			
			if (status != "success")
			{
				clearInterval(checkout_interval);
				console.log("Error connecting to server. Timer stoped.");
				return;
			}
			
			var resp = jQuery.parseJSON(data);
			
			if (resp.error)
			{
				clearInterval(checkout_interval);
				console.log("Server execution error. Timer stoped.");
				return;
			}
			
			if (resp.status != "in queue")	// todo
			{
				clearInterval(checkout_interval);
				
				console.log("Task processed. Stopping timer. Task status: " + resp.status + ".");
				$("#label_status").html("");
				$("#output").html(resp.output);
				return;
			}
			
			console.log("Continue waiting.");
		}
				
		</script>

	</head>
	<body>

		<h1>M project</h1>

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
		
	</body>
</html>
