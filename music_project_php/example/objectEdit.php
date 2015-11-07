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

	if (!isset($_GET["id"]) || !is_numeric($_GET["id"]))
	{
		$paramsCorrect = false;
	}

	if (!$paramsCorrect)
	{
		die("Bad parameters");
	}

	// Peform query
	$sql = "SELECT * FROM `object` WHERE id = " . $_GET["id"];
	$result = $conn->query($sql);

	if($result->num_rows != 1)
	{
		die("Object does not exist.");
	}

	$row = $result->fetch_assoc();
?>



<!DOCTYPE html>

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
				<h3>Edit object</h3>

				<form action="object.php" method="GET">
					<input type="hidden" name='id' value="<?php echo $row["ID"]; ?>">
					
					<div class="form-group">
						<label for="nameInput">Name</label>
						<input type="text" name="name" value="<?php echo $row["Name"]; ?>" class="form-control" id="nameInput">
					</div>

					<div class="form-group">
						<label for="descriptionInput">Description</label>
						<input type="text" name="description" value="<?php echo $row["Description"]; ?>" class="form-control" id="descriptionInput">
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