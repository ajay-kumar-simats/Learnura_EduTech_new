<?php
include_once 'db_config.php';

header("Access-Control-Allow-Origin: *");
header("Content-Type: application/json");

$servername = "localhost";
$username = "root";
$password = ""; // Default password for XAMPP
$dbname = "learnura_courses_db";

$conn = new mysqli($servername, $username, $password, $dbname);

if ($conn->connect_error) {
    http_response_code(500); // Set HTTP response code
    echo json_encode(["error" => "Connection failed: " . $conn->connect_error]);
    exit; // Stop further execution
}

if (isset($_GET['course_id'])) {
    $course_id = intval($_GET['course_id']);
    $sql = "SELECT file_path FROM courses WHERE course_id = $course_id";
} else {
    echo json_encode(["error" => "Course ID not provided"]);
    exit;
}

$result = $conn->query($sql);

if ($result && $result->num_rows > 0) {
    $row = $result->fetch_assoc();
    echo json_encode(["file_path" => $row['file_path']]);
} else {
    echo json_encode(["message" => "No video found for this course"]);
}

$conn->close();
?>
