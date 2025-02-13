<?php
include_once 'db_config.php';

include 'db_connection.php';  // Include your DB connection script

if ($_SERVER['REQUEST_METHOD'] == 'POST') {
    $courseId = $_POST['course_id'];

    // Delete the course from the database
    $query = "DELETE FROM courses WHERE course_id = '$courseId'";
    if (mysqli_query($conn, $query)) {
        echo json_encode(["status" => "success"]);
    } else {
        echo json_encode(["status" => "error", "message" => "Failed to delete course"]);
    }

    mysqli_close($conn);
}
?>
