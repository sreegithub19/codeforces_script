import subprocess

def run_docker():
    try:
        # Combined Docker commands in a single subprocess
        subprocess.run(
            """
            docker build -t my-docker-image . && \
            docker run -d --name my-container my-docker-image sleep 3600 && \
            docker exec my-container bash -c 'echo "<html><body><h1>Hello from Docker there!</h1></body></html>" > /index_container.html' && \
            docker cp my-container:/index_container.html ./index.html && \
            docker stop my-container && \
            docker rm my-container
            """, 
            shell=True, check=True
        )
    except subprocess.CalledProcessError as e:
        print(f"Error occurred while running Docker: {e}")

if __name__ == "__main__":
    run_docker()
