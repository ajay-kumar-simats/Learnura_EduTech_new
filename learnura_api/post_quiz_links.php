<?php
include_once 'db_config.php';  // Include your database configuration file

// Set response header to JSON format
header("Content-Type: application/json");

// Check if the required data is available
if (isset($_POST['video_identifier']) && isset($_POST['easy_link']) && isset($_POST['medium_link']) && isset($_POST['hard_link']) && isset($_POST['very_hard_link']) && isset($_POST['expert_link'])) {

    // Get data from the POST request
    $video_identifier = $_POST['video_identifier'];
    $easy_link = $_POST['easy_link'];
    $medium_link = $_POST['medium_link'];
    $hard_link = $_POST['hard_link'];
    $very_hard_link = $_POST['very_hard_link'];
    $expert_link = $_POST['expert_link'];

    // Establish database connection
    $conn = new mysqli("localhost", "root", "", "learnura_courses_db");

    // Check for connection errors
    if ($conn->connect_error) {
        die(json_encode(["error" => "Failed to connect to database: " . $conn->connect_error]));
    }

    // SQL query to insert quiz links into the table
    $sql = "INSERT INTO quiz_links (video_identifier, easy_link, medium_link, hard_link, very_hard_link, expert_link) 
            VALUES (?, ?, ?, ?, ?, ?)";

    // Prepare and bind the SQL statement
    if ($stmt = $conn->prepare($sql)) {
        $stmt->bind_param("ssssss", $video_identifier, $easy_link, $medium_link, $hard_link, $very_hard_link, $expert_link);

        // Execute the query
        if ($stmt->execute()) {
            echo json_encode(["status" => "success", "message" => "Quiz links successfully added."]);
        } else {
            echo json_encode(["status" => "error", "message" => "Failed to insert quiz links."]);
        }

        // Close the prepared statement
        $stmt->close();
    } else {
        echo json_encode(["status" => "error", "message" => "Failed to prepare SQL statement."]);
    }

    // Close the database connection
    $conn->close();
} else {
    // If any of the required parameters are missing
    echo json_encode(["status" => "error", "message" => "Missing required parameters."]);
}
?>
