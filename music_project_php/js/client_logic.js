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
}

function task_crate_callback(data, status)
{			
	if (status == "success")	// request transfer
	{
		var resp = jQuery.parseJSON(data);
		
		if (resp.status == "success")	// request execution
		{	
			onInfo("Task created.");
			
			task_id = resp.task_id;
			onTaskCreated();
			
			checkout_interval = setInterval(task_checkout, 1000);	// starting timer
		}
		else
		{
			onError("Error request execution.");
		}
	}
	else
	{
		onError("Error request transfer.");
	}			
}

function task_checkout()
{
	$.post("task_checkout.php",
	{
		task_id: task_id
	},
	task_checkout_callback);
}

function task_checkout_callback(data, status)
{
	if (status == "success")	// request transfer
	{	
		var resp = jQuery.parseJSON(data);
		
		if (resp.status == "success")	// request execution
		{
			// task manager level
			if (resp.task_status == "in queue")
			{
				onTaskInQueue();
				return;	// continue waiting
			}
			
			if (resp.task_status == "in progress")
			{
				onTaskInProgress();
				return;
			}

			clearInterval(checkout_interval);	// stop timer

			if (resp.task_status == "success")
			{
				onInfo("Task processed successfully.");
				onTaskFinished(resp.output);
				return;
			}
			else
			{
				onError("Task manager level error.");
				return;
			}
		}
		else
		{
			clearInterval(checkout_interval);
			onError("Request execution error. Timer stoped.");
		}
	}
	else
	{
		clearInterval(checkout_interval);
		onError("Error request transfer.");		
	}
}

// Notofications
function onError(msg)
{
	console.log("Error: " + msg);
}

function onInfo(msg)
{
	console.log("Info: " + msg);
}

// Handlers
function onTaskCreated()
{
	console.log("Task created.");
}

function onTaskInQueue()
{
	console.log("Task in queue.");
}

function onTaskInProgress()
{
	console.log("Task in progress.");
}

function onTaskFinished(output)
{
	console.log("Task finished.");
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
