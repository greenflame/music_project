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


	// Insert action
	if (isset($_GET["action"]) && $_GET["action"] == "insert")
	{
		if (isset($_GET["name"]) && isset($_GET["description"]))
		{
			$sql = "INSERT INTO `object` (`ID`, `Name`, `Description`)
				VALUES (NULL, '" . addslashes ($_GET["name"]) . "', '" .
					addslashes($_GET["description"]) . "')";
			$result = $conn->query($sql);
		}
		else
		{
			echo "Bad parameters for insert operation<br>";
		}
	}	

	// Delete action
	if (isset($_GET["action"]) && $_GET["action"] == "delete")
	{
		if (isset($_GET["id"]) && is_numeric($_GET["id"]))
		{
			$sql = "DELETE FROM `object` WHERE `ID` = " . $_GET["id"];
			$result = $conn->query($sql);
		}
		else
		{
			echo "Bad parameters for delete operation<br>";
		}
	}

	// Update action
	if (isset($_GET["action"]) && $_GET["action"] == "update")
	{
		if (isset($_GET["id"]) && is_numeric($_GET["id"]) && isset($_GET["name"]) && isset($_GET["description"]))
		{
			$sql = "UPDATE `Object` SET `Name` = '".$_GET["name"]
				."', `Description` = '".$_GET["description"]
				."' WHERE `object`.`ID` = ".$_GET["id"];
			$result = $conn->query($sql);
		}
		else
		{
			echo "Bad parameters for update operation<br>";
		}
	}
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

				<h3>Objects</h3>

				<table class="table">
					<tr>
						<!-- <td>Id</td> -->
						<td>Name</td>
						<td>Description</td>
						<td>Delete</td>
						<td>Edit</td>
					</tr>

					<?php
						$sql = "SELECT * FROM `object`";
						$result = $conn->query($sql);

					    while($row = $result->fetch_assoc())
					    {
					    	$deleteForm = '
								<form action="object.php" method="GET">
									<input type="hidden" name="action" value="delete">
									<input type="hidden" name="id" value="' . $row["ID"] . '">
									<input type="submit" value="delete" class="btn btn-default">
								</form>
					    	';

					    	$editForm = '
								<form action="objectEdit.php" method="GET">
									<input type="hidden" name="action" value="edit">
									<input type="hidden" name="id" value="' . $row["ID"] . '">
									<input type="submit" value="edit" class="btn btn-default">
								</form>
					    	';

							// echo "<tr> <td>" . $row["ID"]. "</td>
							echo "<tr>
								<td>" . $row["Name"]. "</td>
								<td>" . $row["Description"]. "</td>
								<td>" . $deleteForm. "</td>
								<td>" . $editForm . "</td> </tr>";
					    }
					?>

					
				</table>


				<h3>Add new object</h3>

				<form action="object.php" method="GET">
					<div class="form-group">
						<label for="nameInput">Name</label>
						<input type="text" name="name" class="form-control" id="nameInput">
					</div>

					<div class="form-group">
						<label for="descriptionInput">Description</label>
						<input type="text" name="description" class="form-control" id="descriptionInput">
					</div>

					<input type="hidden" name="action" value="insert">
					
					<button type="submit" class="btn btn-default">Insert</button>
				</form>
			</div>
		</div>


	</div>
</html>


<?php
	$conn->close();
?>