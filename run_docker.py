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

def run_docker1():
    try:
        # Combined Docker commands in a single subprocess
        subprocess.run(
            """
            #!/bin/bash
            # Generate a random container name based on the current timestamp or UUID
            CONTAINER_NAME="container_$(date +%\s)"  # Use the current timestamp
            # OR
            # CONTAINER_NAME="container_$(uuidgen)"  # Use a UUID

            docker build -t my-docker-image . && \
            docker run -d --name "$CONTAINER_NAME" my-docker-image sleep 3600 && \
            docker exec "$CONTAINER_NAME" bash -c 'echo "<html><body><h1>Hello from Docker there yet again!</h1></body></html>" > /index_container_1.html' && \
            docker cp "$CONTAINER_NAME:/index_container_1.html" ./index_1.html && \
            docker stop "$CONTAINER_NAME" && \
            docker rm "$CONTAINER_NAME"
            """, 
            shell=True, check=True
        )
    except subprocess.CalledProcessError as e:
        print(f"Error occurred while running Docker: {e}")

def run_docker2():
    try:
        # Combined Docker commands in a single subprocess
        subprocess.run(
            """
            #!/bin/bash
            docker build -t my-docker-image . && \
            #docker run -d --name "container_$(date +%\s)" my-docker-image sleep 3600 && \
            docker exec "$(docker ps -ql)" bash -c 'echo "<html><body><h1>Hello from Docker there yet again here!</h1></body></html>" > /index_container_2.html' && \
            docker cp "$(docker ps -ql):/index_container_2.html" ./index_2.html && \
            docker rm -f "$(docker ps -ql)"
            """, 
            shell=True, check=True
        )
    except subprocess.CalledProcessError as e:
        print(f"Error occurred while running Docker: {e}")

if __name__ == "__main__":
    run_docker()
    run_docker1()
    run_docker2()
