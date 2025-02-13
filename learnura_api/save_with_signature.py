import tensorflow as tf

# Load the model
model = tf.keras.models.load_model("C:/xampp/htdocs/learnura_api/tf_model.h5")

# Recompile the model (optional but recommended)
model.compile(optimizer='adam', loss='binary_crossentropy', metrics=['accuracy'])

# Define the signature function
@tf.function(input_signature=[tf.TensorSpec([None, input_dim], dtype=tf.float32)])
def predict(inputs):
    return {'outputs': model(inputs)}

# Save the model with the signature
tf.saved_model.save(
    model,
    export_dir="C:/xampp/htdocs/learnura_api/saved_model",
    signatures=predict
)
print("Model saved with signature.")
