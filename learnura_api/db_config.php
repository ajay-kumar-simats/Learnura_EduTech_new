<?php
$servername = "localhost";
$username = "root";  // default username for MySQL in XAMPP
$password = "";  // default password for MySQL in XAMPP (empty)
$dbname = "app_updates_db";  // Name of the database you created earlier

// Create connection
$conn = new mysqli($servername, $username, $password, $dbname);

// Check connection
if ($conn->connect_error) {
    die("Connection failed: " . $conn->connect_error);
}
?>
