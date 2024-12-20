import subprocess

def run_docker():
    try:
        # Combined Docker commands in a single subprocess
        subprocess.run(
            """
            docker build -t my-docker-image . && \
            docker run -d --name my-container --mount type=tmpfs,dst=/tmp/my_tmpfs my-docker-image sleep 3600 && \
            docker exec my-container bash -c 'echo "<html><body><h1>Hello from Docker here in memory!</h1></body></html>" > /tmp/my_tmpfs/index1_container.html' && \
            sleep 10 && \
            docker exec -it my-container bash && ls -l /tmp/my_tmpfs/ && \
            docker cp my-container:/tmp/my_tmpfs/index1_container.html ./index1.html && \
            docker stop my-container && \
            docker rm my-container
            """, 
            shell=True, check=True
        )
    except subprocess.CalledProcessError as e:
        print(f"Error occurred while running Docker: {e}")

if __name__ == "__main__":
    run_docker()
