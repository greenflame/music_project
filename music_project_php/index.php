<?php session_start(); ?>

<!DOCTYPE html>
<html>
	<head>
		<meta charset="utf-8">
		
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
		<form id="form_upload">
			<input type="hidden" name="PHP_SESSION_UPLOAD_PROGRESS" value="unc" />
			<input class="hidden_input" id = "input_file" name="file" type="file" />
		</form>

		<div class="header">
			<a href="index.php"><img src="pictures/logo.png"></a>
			<div class="navigation">
				<a class="navigation-item" href="index.php">Upload</a>
				<a class="navigation-item" href="howitworks.html">How it works</a>
			</div>
		</div>

		<div class="block-outer-first">
			<div class="block-inner">

				<div class="block-column-container">
					<div class="block-column block-column-left">
						<h2>About MusicSense</h2>
						<p>
							MusicSense - сервис для поиска похожих музыкальных произведений на основе анализа спектрограмм.
						</p>
					</div>
					<div class="block-column block-column-right">
						<div class="try-area">
							<h2>Try service by uploading an audio file</h2>
							<div id="btn_send_file" class="try-button">Upload</div>

                            <img id="load-animation" src="pictures/load.gif" width="40">
                            <div id="label_status">Some text</div>
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

						<audio id="player" src=""></audio>

						<div id="track-list">
						</div>

					</div>
				</div>

			</div>
		</div>


	</body>
</html>
