import subprocess

def run_docker():
    try:
        # Step 1: Build the Docker image
        print("Building Docker image...")
        subprocess.run("docker build -t my-docker-image .", shell=True, check=True)

        # Step 2: Run the Docker container in detached mode (so it stays alive)
        print("Running Docker container...")
        subprocess.run("docker run -d --name my-container my-docker-image", shell=True, check=True)

        # Step 3: Create the index.html file inside the container using a subprocess
        print("Creating index.html inside the Docker container...")
        create_file_command = """
        docker exec my-container bash -c 'echo "<html><body><h1>Hello from Docker!</h1></body></html>" > /index.html'
        """
        subprocess.run(create_file_command, shell=True, check=True)

        # Step 4: Copy the index.html file from the container to the host
        print("Copying index.html from Docker container...")
        subprocess.run("docker cp my-container:/index.html ./index.html", shell=True, check=True)

        # Step 5: Clean up the Docker container
        print("Cleaning up Docker container...")
        subprocess.run("docker rm my-container", shell=True, check=True)

        # # Check if index.html is successfully copied
        # if os.path.exists('./index.html'):
        #     print("index.html has been successfully extracted!")
        # else:
        #     print("index.html not found!")

    except subprocess.CalledProcessError as e:
        print(f"Error occurred while running Docker: {e}")

if __name__ == "__main__":
    run_docker()
