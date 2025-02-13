<?php
// Database connection
$host = "localhost";
$user = "root";
$password = "";
$dbname = "learnura_courses_db";

$conn = new mysqli($host, $user, $password, $dbname);

if ($conn->connect_error) {
    die("Connection failed: " . $conn->connect_error);
}

// Get POST data
$title = $_POST['title'];
$body = $_POST['body'];
$category = $_POST['category'];

if (!empty($title) && !empty($body) && !empty($category)) {
    // Insert into database
    $stmt = $conn->prepare("INSERT INTO notifications (title, body, category) VALUES (?, ?, ?)");
    $stmt->bind_param("sss", $title, $body, $category);

    if ($stmt->execute()) {
        echo "Notification saved successfully";

        // Trigger real-time notification (Optional: Firebase Cloud Messaging or another push service)
    } else {
        echo "Failed to save notification";
    }

    $stmt->close();
} else {
    echo "All fields are required";
}

$conn->close();
?>
