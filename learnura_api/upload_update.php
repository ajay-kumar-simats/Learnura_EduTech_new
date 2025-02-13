<?php
header('Content-Type: application/json');

if ($_SERVER['REQUEST_METHOD'] === 'GET') {
    echo "API is working! Send a POST request to test the upload functionality.";
 
}

// Include the database connection file
include_once 'db_config.php';

// Check if the data is coming through POST
if ($_SERVER['REQUEST_METHOD'] === 'POST') {
    
    $app_name = isset($_POST['app_name']) ? $_POST['app_name'] : '';
    $version_number = isset($_POST['version_number']) ? $_POST['version_number'] : '';
    $image = isset($_FILES['image']) ? $_FILES['image'] : null;
    
    if (empty($app_name) || empty($version_number) || $image == null) {
        echo json_encode(["message" => "All fields are required!"]);
        exit;
    }
    
    // Handling the image upload
    $target_dir = "uploads/";
    $target_file = $target_dir . basename($image["name"]);
    
    if (move_uploaded_file($image["tmp_name"], $target_file)) {
        // Insert the app update info into the database
        $image_url = $target_file; // Store the image path
        
        $sql = "INSERT INTO app_updates (app_name, version_number, image_url) VALUES ('$app_name', '$version_number', '$image_url')";
        
        if ($conn->query($sql) === TRUE) {
            echo json_encode(["message" => "App update uploaded successfully!"]);
        } else {
            echo json_encode(["message" => "Error: " . $conn->error]);
        }
    } else {
        echo json_encode(["message" => "Failed to upload image."]);
    }
}
?>
