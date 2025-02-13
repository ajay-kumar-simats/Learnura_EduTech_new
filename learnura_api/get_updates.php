<?php
header('Content-Type: application/json');
include_once 'db_config.php';

$sql = "SELECT app_name, version_number, image_url FROM app_updates";
$result = $conn->query($sql);

$app_updates = [];

if ($result->num_rows > 0) {
    while ($row = $result->fetch_assoc()) {
        $app_updates[] = [
            "appName" => $row['app_name'],
            "version" => $row['version_number'],
            "imageUrl" => $row['image_url'],
        ];
    }
    echo json_encode($app_updates);
} else {
    echo json_encode(["message" => "No updates found."]);
}
$conn->close();
?>
