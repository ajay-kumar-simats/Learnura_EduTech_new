import joblib
import tensorflow as tf
from tensorflow import keras
import numpy as np

# Load the trained model from .pkl
model = joblib.load("C:\\xampp\\htdocs\\learnura_api\\user_behavior_model.pkl")

# Define a wrapper for the scikit-learn model
class SKLearnWrapper(tf.keras.Model):
    def __init__(self, model):
        super(SKLearnWrapper, self).__init__()
        self.model = model

    def call(self, inputs):
        return tf.convert_to_tensor(self.model.predict(inputs))

# Wrap the model
tf_model = SKLearnWrapper(model)

# Save as a TensorFlow model
tf.saved_model.save(tf_model, "tf_model")
