<?php
header("Content-Type: application/json");

// Database connection
$host = "localhost";
$username = "root";
$password = "";
$dbname = "learnura_courses_db";

$conn = new mysqli($host, $username, $password, $dbname);

// Check connection
if ($conn->connect_error) {
    die(json_encode(["status" => "error", "message" => "Database connection failed"]));
}

// Fetch categorized courses
$sql = "SELECT course_id, course_name, file_path, category FROM courses"; // Match column names exactly
$result = $conn->query($sql);

if ($result->num_rows > 0) {
    // Initialize categories
    $categories = ["Arduino" => [], "IoT" => [], "Programming" => []];

    while ($row = $result->fetch_assoc()) {
        // Ensure category exists in the predefined categories array
        if (array_key_exists($row['category'], $categories)) {
            $categories[$row['category']][] = [
                "course_id" => $row["course_id"],
                "course_name" => $row["course_name"],
                "file_path" => $row["file_path"]
            ];
        }
    }

    // Output JSON response
    echo json_encode(["status" => "success", "categories" => $categories]);
} else {
    echo json_encode(["status" => "error", "message" => "No courses found"]);
}

$conn->close();
?>
