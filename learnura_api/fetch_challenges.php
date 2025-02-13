<?php
include_once 'db_config.php';

header("Access-Control-Allow-Origin: *");
header("Content-Type: application/json");

$servername = "localhost";
$username = "root";
$password = ""; // Default password for XAMPP
$dbname = "quiz_app";

$conn = new mysqli($servername, $username, $password, $dbname);

if ($conn->connect_error) {
    http_response_code(500); // Set HTTP response code
    echo json_encode(["error" => "Connection failed: " . $conn->connect_error]);
    exit; // Stop further execution
}

if (isset($_GET['course_id'])) {
    $course_id = intval($_GET['course_id']);
    $sql = "SELECT * FROM challenges WHERE course_id = $course_id";
} else {
    $sql = "SELECT * FROM challenges";
}

$result = $conn->query($sql);

if ($result && $result->num_rows > 0) {
    $challenges = $result->fetch_all(MYSQLI_ASSOC); // Fetch all rows as an associative array
    echo json_encode($challenges);
} else {
    echo json_encode(["message" => "No challenges found."]);
}

$conn->close();
?>
