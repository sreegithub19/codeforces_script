import subprocess

# Define the Docker command as a string
docker_command = """
docker run --rm \
          --mount type=tmpfs,dst=/tmp/my_tmpfs,tmpfs-mode=1777 \
          ubuntu bash -c "
            apt-get update &&
            apt-get install -y wget unzip &&
            wget -qO- https://storage.googleapis.com/dart-archive/channels/stable/release/latest/sdk/dartsdk-linux-x64-release.zip > dart-sdk.zip &&
            unzip dart-sdk.zip -d /usr/lib &&
            export PATH=\$PATH:/usr/lib/dart-sdk/bin &&
            mkdir -p /tmp/my_tmpfs &&
            echo 'void main() { print(\\"Hello from Dart in tmpfs!\\"); }' > /tmp/my_tmpfs/myscript.dart &&
            dart /tmp/my_tmpfs/myscript.dart
          "
"""

# Run the Docker command using subprocess
def run_docker():
    try:
        print("Running Docker container...")
        subprocess.run(docker_command, shell=True, check=True)
    except subprocess.CalledProcessError as e:
        print(f"Error occurred while running Docker: {e}")

# Execute the function
if __name__ == "__main__":
    run_docker()