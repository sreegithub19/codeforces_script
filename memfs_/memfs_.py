import subprocess

# Define the Docker command as a string
docker_command = """
docker run --rm \
  --mount type=tmpfs,dst=/tmp/my_tmpfs,tmpfs-mode=1777 \
  ubuntu bash -c "
    # Step 1: Install Node.js (if not already installed)
    apt-get update && apt-get install -y nodejs npm &&

    # Step 2: Create a directory inside tmpfs
    mkdir -p /tmp/my_tmpfs &&

    # Step 3: Create a simple text file in tmpfs
    echo 'This is a temporary file here in tmpfs' > /tmp/my_tmpfs/tempfile.txt &&
    cat /tmp/my_tmpfs/tempfile.txt &&

    # Step 4: Create a Node.js script in tmpfs
    echo -e 'console.log(\\"Hello from Node.js in tmpfs Python!\\");' > /tmp/my_tmpfs/myscript.js &&

    # Step 5: Check permissions of the script
    ls -l /tmp/my_tmpfs/myscript.js &&

    # Step 6: Execute the Node.js script using node
    node /tmp/my_tmpfs/myscript.js &&

    # Step 7: List files in tmpfs directory to confirm
    ls /tmp/my_tmpfs
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
