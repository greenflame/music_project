var task_checkout_interval, task_id, upload_checkout_interval;

// Init document. Hide some parts
$(function(){
	$("#data_container").hide();
	$("#load-animation").hide();
	$("#label_status").hide();
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
	// Send form
	var formData = new FormData($('form')[0]);	// todo id
	$.ajax({
		url: 'ajax/task_create.php',
		type: 'POST',
		success: task_create_callback,
		error: function(){ onError("Error sending create request."); },
		data: formData,

		cache: false,
		contentType: false,
		processData: false
	});
	onUploadStart();

	// Start upload progress checkout
	upload_checkout_interval = setInterval(upload_checkout, 500);	// starting timer
}

function task_create_callback(data)
{
	var resp = jQuery.parseJSON(data);

	if (resp.status == "success")	// request execution
	{
		task_id = resp.task_id;

		onInfo("Task created.");
		onTaskCreated();

		task_checkout_interval = setInterval(task_checkout, 1000);	// starting timer
	}
	else
	{
		onError("Error request execution.");
	}
}

function upload_checkout()
{
	$.ajax({
		url: 'ajax/upload_checkout.php',
		type: 'POST',
		success: upload_checkout_callback,
		error: function(){ onError("Error sending process checkout request."); },

		cache: false,
		contentType: false,
		processData: false
	});
}

function upload_checkout_callback(data)
{
	var result = jQuery.parseJSON(data);

	if (result.status == "success")
	{
		onUpload(result.upload_percent);
	}
	else if (result.status == "error")
	{
		clearInterval(upload_checkout_interval);
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
	// Responce accepted. Disable upload progress checkout
	clearInterval(upload_checkout_interval);

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

			clearInterval(task_checkout_interval);	// stop timer

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
			clearInterval(task_checkout_interval);
			onError("Request execution error. Timer stoped.");
		}
	}
	else
	{
		clearInterval(task_checkout_interval);
		onError("Error request transfer.");
	}
}

// Notofications
function onError(msg)
{
    clearInterval(task_checkout_interval);
    clearInterval(upload_checkout_interval);
	console.log("Error: " + msg);
	$("#label_status").html("Error occurred.");

	// Hide progress elements
	$("#btn_send_file").show();
	$("#load-animation").hide();
	// $("#label_status").hide();
}

function onInfo(msg)
{
	console.log("Info: " + msg);
}

// Handlers
function onUploadStart()
{
	// Show progress elements
	$("#btn_send_file").hide();
	$("#load-animation").show();
	$("#label_status").show();

	$("#label_status").html("Upload started.");
}

function onUpload(percent)
{
	$("#label_status").html("Upload in progress: " + percent + "%.");
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
	// Hide progress elements
	$("#btn_send_file").show();
	$("#load-animation").hide();
	$("#label_status").hide();

	// Clear track data containers
	$("#features_container").html("");
	$("#track-list").html("");

	// Show data container
	$("#data_container").show();

	// Parse output
	var result = jQuery.parseJSON(output);

	// Plot features chart
	plotFeaturesChart(result.original_track.features, result.original_track.name);

	result.similar_tracks.forEach(function(item){
		addTrack(item.name, item.url, item.features[0]);
	});

}

function strHash(str) {
	var hash = 0, i, chr, len;
	if (str.length == 0) return hash;
	for (i = 0, len = str.length; i < len; i++) {
		chr   = str.charCodeAt(i);
		hash  = ((hash << 5) - hash) + chr;
		hash |= 0; // Convert to 32bit integer
	}
	return hash;
};

function addTrack(name, url, secDuration) {
	var mins = Math.floor(secDuration / 60);
	var secs = Math.floor(secDuration % 60);

	secs = String(secs);
	if (secs.length < 2) {
		secs = "0" + secs;
	}

	var strDuration = mins + ":" + secs;

	var trackCode = '\
		<div class="track-area">\
		<div id="' + strHash(url) + '" class="play-stop">\
			<img class="play-button" src="pictures/play.png" onclick="onPlayClick(\'' + url + '\')">\
			<img class="stop-button" src="pictures/stop.png" onclick="onStopClick(\'' + url + '\')" style="display:none">\
		</div>\
		<div class="track-name">' + name + '</div>\
		<div class="time">' + strDuration + '</div>\
		</div>';

	$("#track-list").append(trackCode);
}


function onPlayClick(url){
	$(".stop-button").trigger("click");	// Clicking all stop buttons
	$("#" + strHash(url)).find('img').toggle();	// Switching to stop button

	var player = $("#player");
	player.attr("src", url);	// Loading track
	player.trigger("play");		// Start playing
}

function onStopClick(url) {
	$("#player").trigger("pause");	// Stopping player
	$("#" + strHash(url)).find('.play-button').show();	// Switching to play button
	$("#" + strHash(url)).find('.stop-button').hide();
}

function plotFeaturesChart(features, name) {
	$("#features_container").highcharts({
        chart: {
            polar: true,
            type: 'line'
        },

        title: {
            text: name,
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
            categories: ['Duration', 'Spectrogram density', 'Feature 3', 'Feature 4',
                    'Feature 5', 'Feature 6'],
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