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
		if (isset($_GET["Object1_ID"]) && isset($_GET["Object2_ID"]) && isset($_GET["Relation_description"])
			&& is_numeric($_GET["Object1_ID"]) && is_numeric($_GET["Object2_ID"]))
		{
			$sql = "INSERT INTO `dbtask`.`relation` (`Object1_ID`, `Object2_ID`, `Relation_description`)
			VALUES ('" . $_GET["Object1_ID"] . "', '" . $_GET["Object2_ID"] . "', '" . $_GET["Relation_description"] . "');";
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
		if (isset($_GET["Object1_ID"]) && is_numeric($_GET["Object1_ID"])
			&& isset($_GET["Object2_ID"]) && is_numeric($_GET["Object2_ID"])
			&& isset($_GET["Relation_description"]))
		{
			$sql = "DELETE FROM `relation`
			WHERE `Object1_ID` = " . $_GET["Object1_ID"] . "
			AND `Object2_ID` = " . $_GET["Object2_ID"] . "
			AND `Relation_description` = '" . $_GET["Relation_description"] . "'";
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
		if (isset($_GET["Object1_ID"]) && is_numeric($_GET["Object1_ID"])
			&& isset($_GET["Object2_ID"]) && is_numeric($_GET["Object2_ID"])
			&& isset($_GET["Relation_description_old"])
			&& isset($_GET["Relation_description_new"]))
		{
			$sql = "UPDATE `relation` SET `Relation_description` = '".$_GET["Relation_description_new"]."'
			WHERE `Object1_ID` = " . $_GET["Object1_ID"] . "
			AND `Object2_ID` = " . $_GET["Object2_ID"] . "
			AND `Relation_description` = '" . $_GET["Relation_description_old"] . "'";
			$result = $conn->query($sql);
		}
		else
		{
			echo "Bad parameters for delete operation<br>";
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

				<h3>Relations</h3>

				<table class="table">
					<tr>
						<td>Object 1</td>
						<td>Relation</td>
						<td>Object 2</td>
						<td>Delete</td>
						<td>Edit</td>
					</tr>

					<?php
						$sql = "SELECT
							r.Object1_ID,
							o1.name AS name1,
							r.Object2_ID,
							o2.name AS name2,
							r.Relation_description


							FROM relation AS r
							LEFT JOIN object AS o1
							on r.Object1_ID = o1.id
							LEFT JOIN object AS o2
							on r.Object2_ID = o2.id";

						$result = $conn->query($sql);

					    while($row = $result->fetch_assoc())
					    {
					    	$deleteForm = '
								<form action="relation.php" method="GET">
									<input type="hidden" name="Object1_ID" value="' . $row["Object1_ID"] . '">
									<input type="hidden" name="Object2_ID" value="' . $row["Object2_ID"] . '">
									<input type="hidden" name="Relation_description" value="' . $row["Relation_description"] . '">

									<input type="hidden" name="action" value="delete">
									<input type="submit" value="delete" class="btn btn-default">
								</form>
					    	';

					    	$editForm = '
								<form action="relationEdit.php" method="GET">
									<input type="hidden" name="Object1_ID" value="' . $row["Object1_ID"] . '">
									<input type="hidden" name="Object2_ID" value="' . $row["Object2_ID"] . '">
									<input type="hidden" name="Relation_description" value="' . $row["Relation_description"] . '">

									<input type="hidden" name="action" value="edit">
									<input type="submit" value="edit" class="btn btn-default">
								</form>
					    	';

					    	$obj1str = $row["name1"] . " [id = " . $row["Object1_ID"] . "]";
					    	$obj2str = $row["name2"] . " [id = " . $row["Object2_ID"] . "]";
					    	$relstr = $row["Relation_description"];
						
							echo "<tr> <td>" . $obj1str . "</td>
								<td>" . $relstr . "</td>
								<td>" . $obj2str . "</td>
								<td>" . $deleteForm . "</td>
								<td>" . $editForm . "</td> </tr>";
					    }
					?>
					
				</table>


				<h3>Add new relation</h3>


				<form action="relation.php" method="GET">

					<div class="form-group">
						<label for="object1input">Object 1</label>

						<select name="Object1_ID" class="form-control" id="object1input">

						<?php
							$sql = "SELECT * FROM `object`";
							$result = $conn->query($sql);

						    while($row = $result->fetch_assoc())
						    {
						    	$value = $row["ID"];
						    	$caption = $row["Name"] . " [id = " . $row["ID"] . "]";

						        echo "<option value=" . $value . ">" . $caption . "</option>";
						    }
						?>

						</select>
					</div>

					<div class="form-group">
						<label for="relationInput">Relation</label>
						<input type="text" name="Relation_description" class="form-control" id="relationInput">
					</div>

					<div class="form-group">
						<label for="object2input">Object 2</label>

						<select name="Object2_ID" class="form-control" id="object2input">

						<?php
							$sql = "SELECT * FROM `object`";
							$result = $conn->query($sql);

						    while($row = $result->fetch_assoc())
						    {
						    	$value = $row["ID"];
						    	$caption = $row["Name"] . " [id = " . $row["ID"] . "]";

						        echo "<option value=" . $value . ">" . $caption . "</option>";
						    }
						?>

						</select>
					</div>

					<input type="hidden" name="action" value="insert">
					
					<input type="submit" value="Insert" class="btn btn-default"><br>
				</form>


			</div>
		</div>


	</div>
</html>



<?php
	$conn->close();
?>	