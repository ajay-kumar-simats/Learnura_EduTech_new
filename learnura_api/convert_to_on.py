import joblib
from skl2onnx import convert_sklearn
from skl2onnx.common.data_types import FloatTensorType

# Load the trained model
model = joblib.load("C:\\xampp\\htdocs\\learnura_api\\user_behavior_model.pkl")

# Define input dimensions (4 features: difficulty_level, time_spent, correct_answers, learning_pace)
input_type = [("float_input", FloatTensorType([None, 4]))]

# Convert the model to ONNX format
onnx_model = convert_sklearn(model, initial_types=input_type)

# Save the ONNX model
with open("user_behavior_model.onnx", "wb") as f:
    f.write(onnx_model.SerializeToString())

print("Model successfully converted to ONNX!")
