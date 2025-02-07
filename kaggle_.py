import kagglehub

# Download latest version
path = kagglehub.dataset_download("imsparsh/gesture-recognition")

print("Path to dataset files:", path)