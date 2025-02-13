<?php
header("Access-Control-Allow-Origin: *");
header("Access-Control-Allow-Methods: POST");
header("Content-Type: application/json");

$servername = "localhost";
$username = "root";
$password = ""; // Default password for XAMPP
$dbname = "quiz_app";

// Create connection
$conn = new mysqli($servername, $username, $password, $dbname);

// Check connection
if ($conn->connect_error) {
    die(json_encode(["status" => "error", "message" => "Connection failed: " . $conn->connect_error]));
}

// Get JSON data from POST request
$data = json_decode(file_get_contents("php://input"), true);

if (isset($data['difficulty_level']) && isset($data['question']) && isset($data['option1']) &&
    isset($data['option2']) && isset($data['option3']) && isset($data['option4']) &&
    isset($data['correctAnswer']) && isset($data['course_id'])) {

    $difficulty_level = $conn->real_escape_string($data['difficulty_level']);
    $question = $conn->real_escape_string($data['question']);
    $option1 = $conn->real_escape_string($data['option1']);
    $option2 = $conn->real_escape_string($data['option2']);
    $option3 = $conn->real_escape_string($data['option3']);
    $option4 = $conn->real_escape_string($data['option4']);
    $correctAnswer = $conn->real_escape_string($data['correctAnswer']);
    $course_id = $conn->real_escape_string($data['course_id']);

    $sql = "INSERT INTO challenges (difficulty_level, question, option1, option2, option3, option4, correctAnswer, course_id) 
            VALUES ('$difficulty_level', '$question', '$option1', '$option2', '$option3', '$option4', '$correctAnswer', '$course_id')";

    if ($conn->query($sql) === TRUE) {
        echo json_encode(["status" => "success", "message" => "Challenge added successfully"]);
    } else {
        echo json_encode(["status" => "error", "message" => "Error: " . $conn->error]);
    }
} else {
    echo json_encode(["status" => "error", "message" => "Invalid input data"]);
}

$conn->close();
?>
