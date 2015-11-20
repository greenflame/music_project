var checkout_interval, task_id;

// Hide data container
$(function(){
	$("#data_container").hide();
});

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

		if (resp.request_status == "success")	// request execution
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
    clearInterval(checkout_interval);
	console.log("Error: " + msg);
	$("#label_status").html("Error occurred.");
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
	// Status label
	$("#label_status").html("Task finished.");

	// Show data container
	$("#data_container").show();

	var result = jQuery.parseJSON(output);

	result.features[0] /= 20;

	featuresChart(result.features, "hello, world!");
}

function featuresChart(features, name) {
	$("#features_container").highcharts({
        chart: {
            polar: true,
            type: 'line'
        },

        title: {
            text: 'Track analysis result',
            x: -11,
			align: "left",
			style:
			{
				color: "#536e7b",
				fontFamily: "Roboto",
				fontWeight: "400",
				fontSize: "24px"
			},
        },

        pane: {
            size: '80%'
        },

        xAxis: {
            categories: ['Sales', 'Marketing', 'Development', 'Customer Support',
                    'Information Technology', 'Administration'],
            tickmarkPlacement: 'on',
            lineWidth: 0
        },

        yAxis: {
            gridLineInterpolation: 'polygon',
            lineWidth: 0,
            min: 0
        },

        tooltip: {
            shared: true,
            pointFormat: '<span style="color:{series.color}">{series.name}: <b>_{point.y:,.0f}</b><br/>'
        },

        legend: {
            enabled: false,
        },

        series: [{
            name: name,
			color: '#C81919',
            data: features,
            pointPlacement: 'on'
        }]

    });
}