<!DOCTYPE html>
<html>
	<head>
		<link rel="stylesheet" type="text/css" href="style/fonts.css">
		<link rel="stylesheet" type="text/css" href="style/main.css">
		<link rel="stylesheet" type="text/css" href="style/normalize.css">

		<script src="js/jquery-2.1.4.min.js"></script>

		<script src="js/highcharts.js"></script>
		<script src="https://code.highcharts.com/highcharts-more.js"></script>
		<script src="https://code.highcharts.com/modules/exporting.js"></script>

		<script src="js/client_logic.js"></script>
	</head>
	<body>
		<!--form for file upload-->
		<form>
			<input class="hidden_input" id = "input_file" name="file" type="file" />
		</form>

		<div class="header">
			<image src="pictures/logo.png">
			<div class="navigation">
				<div class="navigation-item">Upload track</div>
				<div class="navigation-item">About</div>
			</div>
		</div>

		<div class="block-outer-first">
			<div class="block-inner">

				<div class="block-column-container">
					<div class="block-column block-column-left">
						<h2>About MusicSense</h2>
						<p>
							Sed ut perspiciatis unde omnis iste natus error sit voluptatem accusantium doloremque laudantium, totam rem aperiam, eaque ipsa quae ab illo inventore veritatis et quasi architecto beatae vitae dicta sunt explicabo. Nemo enim ipsam voluptatem quia voluptas sit aspernatur illum qui dolorem eum fugiat quo voluptas nulla pariatur?
						</p>
					</div>
					<div class="block-column block-column-right">
						<div class="try-area">
							<h2>Try service by uploading an audio file</h2>
							<div id="btn_send_file" class="try-button">Upload</div>
                            <div id="label_status"></div>
						</div>
					</div>
				</div>

			</div>
		</div>

		<div class="block-outer" id="data_container">
			<div class="block-inner">

				<div class="block-column-container">
					<div class="block-column block-column-left">
		                <div id="features_container"></div>
					</div>
					<div class="block-column block-column-right">
		                <h2 class="recomendations-title">Similar tracks</h2>

						<div class="track-area">
							<img class="play-button" src="pictures/play.png">
							<div class="track-name">Lodit aut fugit, sed quia consequuntur</div>
							<div class="time">12:34</div>
						</div>
						<div class="track-area">
							<img class="play-button" src="pictures/play.png">
							<div class="track-name">Rste natus error sit voluptatem</div>
							<div class="time">12:34</div>
						</div>
						<div class="track-area">
							<img class="play-button" src="pictures/play.png">
							<div class="track-name">Fugiat quo voluptas nulla pariatur</div>
							<div class="time">12:34</div>
						</div>

					</div>
				</div>

			</div>
		</div>


	</body>
</html>
