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

// Fetch blogs
$sql = "SELECT title, detail, link, description FROM blogs";
$result = $conn->query($sql);

if ($result->num_rows > 0) {
    $blogs = array();
    while ($row = $result->fetch_assoc()) {
        $blogs[] = $row;
    }
    echo json_encode($blogs);
} else {
    echo json_encode([]);
}

$conn->close();
?>
