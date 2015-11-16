var checkout_interval, task_id;

// Button click - show file dialog
$(function(){
	$("#btn_send_file").click(function(){
		$("#input_file").trigger("click");
	});
});

// File dialog callback - file selected
$(function(){
	$("#input_file").change(function(){
		task_create();
	});
});

function task_create()
{
	var formData = new FormData($('form')[0]);	// todo id 
	$.ajax({
		url: 'ajax/task_create.php',
		type: 'POST',
		success: task_crate_callback,
		error: function(){ onError("Error sending create request."); },
		data: formData,
		
		cache: false,
		contentType: false,
		processData: false
	});
	onUploadStart();
}

function task_crate_callback(data)
{			
	var resp = jQuery.parseJSON(data);
	
	if (resp.status == "success")	// request execution
	{	
		task_id = resp.task_id;

		onInfo("Task created.");
		onTaskCreated();
		
		checkout_interval = setInterval(task_checkout, 1000);	// starting timer
	}
	else
	{
		onError("Error request execution.");
	}		
}

function task_checkout()
{
	$.post("ajax/task_checkout.php",
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
function onUploadStart()
{
	$("#label_status").html("Upload started.");
}

function onTaskCreated()
{
	$("#label_status").html("Task created.");
}

function onTaskInQueue()
{
	$("#label_status").html("Task in queue.");
}

function onTaskInProgress()
{
	$("#label_status").html("Task in progress.");
}

function onTaskFinished(output)
{
	$("#label_status").html("Task finished.");
	$("#output_container").html(output);
	
	var result = jQuery.parseJSON(output);
	
	if (result.status == "success")
	{
		// drawSpectrum(result.spectrum);
	}
	else
	{
		onError("Task executor error.");
	}
}

function drawSpectrum(data){
	$('#spectrum_container').highcharts({
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
			name: 'data',
			data: data
		}]
	});
};
