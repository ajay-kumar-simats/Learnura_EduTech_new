<?php
include_once 'db_config.php';
header("Access-Control-Allow-Origin: *");
header("Access-Control-Allow-Methods: POST");
header("Access-Control-Allow-Headers: Content-Type, Authorization");

$conn = new mysqli("localhost", "root", "", "learnura_courses_db");
if ($conn->connect_error) {
    die("Connection failed: " . $conn->connect_error);
}

// Check if required data is present
if (isset($_FILES['file']) && isset($_POST['course_name']) && isset($_POST['uploaded_by']) && isset($_POST['category'])) {
    $course_name = $_POST['course_name'];
    $uploaded_by = $_POST['uploaded_by'];
    $category = $_POST['category']; // Get the category from POST data
    $target_dir = "uploads_videos/";
    $file_name = basename($_FILES["file"]["name"]);
    $target_file = $target_dir . $file_name;

    // Move the uploaded file to the uploads directory
    if (move_uploaded_file($_FILES["file"]["tmp_name"], $target_file)) {
        // Save file info in the database, including category
        $sql = "INSERT INTO courses (course_name, file_path, uploaded_by, category) VALUES ('$course_name', '$target_file', '$uploaded_by', '$category')";
        if ($conn->query($sql) === TRUE) {
            echo json_encode(["message" => "File uploaded successfully!"]);
        } else {
            echo json_encode(["error" => "Error saving file info in database: " . $conn->error]);
        }
    } else {
        echo json_encode(["error" => "Error uploading the file."]);
    }
} else {
    if (!isset($_FILES['file'])) {
        echo json_encode(["error" => "File is missing."]);
    }
    if (!isset($_POST['course_name'])) {
        echo json_encode(["error" => "Course name is missing."]);
    }
    if (!isset($_POST['uploaded_by'])) {
        echo json_encode(["error" => "Uploaded by is missing."]);
    }
    if (!isset($_POST['category'])) {
        echo json_encode(["error" => "Category is missing."]);
    }
}

$conn->close();
?>
