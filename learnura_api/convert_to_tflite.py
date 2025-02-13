import tensorflow as tf

# Path to the SavedModel directory
saved_model_dir = "C:/xampp/htdocs/learnura_api/tf_model"

# Path to save the TFLite model
tflite_model_path = "C:/xampp/htdocs/learnura_api/user_behavior_model.tflite"

# Convert the model to TFLite format
converter = tf.lite.TFLiteConverter.from_saved_model(saved_model_dir)
tflite_model = converter.convert()

# Save the TFLite model to the specified path
with open(tflite_model_path, "wb") as f:
    f.write(tflite_model)

print(f"TFLite model successfully saved at {tflite_model_path}")
