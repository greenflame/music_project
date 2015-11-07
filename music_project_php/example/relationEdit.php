<?php
	// Conection info
	$servername = "localhost";
	$username = "root";
	$password = "";
	$dbname = "dbtask";

	// Create connection
	$conn = new mysqli($servername, $username, $password, $dbname);

	// Check connection
	if ($conn->connect_error)
	{
	    die("Connection failed: " . mysqli_connect_error() . "<br>");
	}

	// Check parameters
	$paramsCorrect = true;

	if (!isset($_GET["action"]) || $_GET["action"] != "edit")
	{
		$paramsCorrect = false;
	}

	if (!isset($_GET["Object1_ID"]))
	{
		$paramsCorrect = flase;
	}

	if (!isset($_GET["Object2_ID"]))
	{
		$paramsCorrect = flase;
	}

	if (!isset($_GET["Relation_description"]))
	{
		$paramsCorrect = flase;
	}

	if (!$paramsCorrect)
	{
		die("Bad parameters");
	}

	// Peform query
	$sql = "SELECT * FROM `relation` WHERE `Object1_ID` = ".$_GET["Object1_ID"]." AND `Object2_ID` = "
		.$_GET["Object2_ID"]." AND `Relation_description` = '".$_GET["Relation_description"]."'";
	$result = $conn->query($sql);

	if($result->num_rows != 1)
	{
		die("Object does not exist.");
	}

	$row = $result->fetch_assoc();
?>


<!-- Latest compiled and minified CSS -->
<link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.4/css/bootstrap.min.css">

<html lang="en">
	<div class="container">

		<div class="row">
			<div class="col-md-12">
				<h1>DB task</h1>
			</div>
		</div>


		<div class="row">
			<div class="col-md-3">
				<h3>Navigation</h3>

				<a href="object.php">Object manager</a><br>
				<a href="relation.php">Relation manager</a><br>
			</div>

			<div class="col-md-9">
				<h3>Edit relation</h3>


				<form action="relation.php" method="GET">
					<input type="hidden" name='Relation_description_old' value="<?php echo $row["Relation_description"]; ?>">
					<input type="hidden" name='Object1_ID' value="<?php echo $row["Object1_ID"]; ?>">
					<input type="hidden" name='Object2_ID' value="<?php echo $row["Object2_ID"]; ?>">

					<div class="form-group">
						<label for="relstionEdit">Relation description</label>
						<input type="text" name="Relation_description_new" value="<?php echo $row["Relation_description"]; ?>" class="form-control" id="relstionEdit">
					</div>

					<input type="hidden" name="action" value="update">
					<input type="submit" value="Update" class="btn btn-default"><br>
				</form>


			</div>
		</div>


	</div>
</html>


<?php
	$conn->close();
?>