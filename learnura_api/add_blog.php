<?php
// Database connection
$host = "localhost";
$username = "root";
$password = "";
$dbname = "learnura_courses_db";

$conn = new mysqli($host, $username, $password, $dbname);

// Check connection
if ($conn->connect_error) {
    die("Connection failed: " . $conn->connect_error);
}

// Capture data from POST request
$title = $_POST['title'];
$detail = $_POST['detail'];
$link = $_POST['link'];
$description = $_POST['description'];

// Validate input
if (empty($title) || empty($detail) || empty($link) || empty($description)) {
    echo "All fields are required!";
    exit();
}

// Insert into database
$sql = "INSERT INTO blogs (title, detail, link, description) VALUES (?, ?, ?, ?)";
$stmt = $conn->prepare($sql);
$stmt->bind_param("ssss", $title, $detail, $link, $description);

if ($stmt->execute()) {
    echo "Blog added successfully!";
} else {
    echo "Error: " . $stmt->error;
}

$stmt->close();
$conn->close();
?>
