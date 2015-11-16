<!DOCTYPE html>
<html>
	<head>
		<link rel="stylesheet" type="text/css" href="style/fonts.css">
		<link rel="stylesheet" type="text/css" href="style/main.css">
		<link rel="stylesheet" type="text/css" href="style/normalize.css">
		
		<script src="js/jquery-2.1.4.min.js"></script>
		<script src="js/highcharts.js"></script>
		<script src="js/client_logic.js"></script>
	</head>
	<body>
		<!--form for file upload-->
		<form>
			<input class="hidden_input" id = "input_file" name="file" type="file" />
		</form>
		
		<div class="head">
			<image src="pictures/logo.png">
			<div class="nav">
				<div class="nav-item">Upload track</div>
				<div class="nav-item">About</div>
			</div>
		</div>

		<div class="content-row">
			<div class="content-column">

				<div class="column-container">
					<div class="column column-left">
						<h2>About MusicSense</h2>
						<p>
							Sed ut perspiciatis unde omnis iste natus error sit voluptatem accusantium doloremque laudantium, totam rem aperiam, eaque ipsa quae ab illo inventore veritatis et quasi architecto beatae vitae dicta sunt explicabo. Nemo enim ipsam voluptatem quia voluptas sit aspernatur aut odit aut fugit, sed quia consequuntur magni dolores eos qui ratione voluptatem sequi nesciunt. Neque porro quisquam est, qui dolorem ipsum quia dolor sit amet, consectetur, adipisci velit, sed quia non numquam eius modi tempora incidunt ut labore et dolore magnam aliquam quaerat voluptatem. Ut enim ad minima veniam, quis nostrum exercitationem ullam corporis suscipit laboriosam, nisi ut aliquid ex ea commodi consequatur? Quis autem vel eum iure reprehenderit qui in ea voluptate velit esse quam nihil molestiae consequatur, vel illum qui dolorem eum fugiat quo voluptas nulla pariatur?
						</p>
					</div>
					<div class="column column-right">
						<div class="try-area">
							<h2>Try service by uploading an audio file</h2>
							<div id="btn_send_file" class="try-button">Upload</div>
						</div>
					</div>
				</div>

			</div>
		</div>

		<div id="label_status"></div>		
		<div id="output_container"></div>

	</body>
</html>
