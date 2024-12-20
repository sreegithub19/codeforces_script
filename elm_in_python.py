import subprocess
import os
import time

# Define the Docker command to start the container (in detached mode)
docker_command = """
docker run --name elm_container -d \
          ubuntu bash -c "
            apt-get update &&
            apt-get install -y curl npm &&
            npm install -g elm &&
            mkdir -p /tmp/elm_artifacts/elm_project/src /tmp/elm_artifacts/elm_project/html &&
            
            # Create Elm source file (Main1.elm) using cat with a here document
            cat <<EOF > /tmp/elm_artifacts/elm_project/src/Main1.elm
module Main exposing (..)

import Browser
import Html exposing (Html, text, div, h1, p)

-- The main function to run the application
main : Program () () ()
main =
    Browser.sandbox
        { init = ()
        , update = \\_ model -> model
        , view = view
        }

-- Define the view function to render HTML
view : () -> Html msg
view _ =
    div []
        [ h1 [] [ text "Hello there, Elm World!" ]
        , p [] [ text "This is a simple Elm application rendering HTML from Docker container." ]
        ]
EOF
            &&
            
            # Create an index.html file to load the compiled Elm JavaScript using cat with a here document
            cat <<EOF > /tmp/elm_artifacts/elm_project/html/index.html
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Elm Hello World</title>
</head>
<body>
    <h1>Elm Hello World App</h1>
    <div id="elm"></div>
    <script src="/tmp/elm_artifacts/elm_project/js/Main1.js"></script>
</body>
</html>
EOF
            &&
            
            # Compile Elm project to JavaScript
            elm make /tmp/elm_artifacts/elm_project/src/Main1.elm --output /tmp/elm_artifacts/elm_project/js/Main1.js &&
            
            echo 'Elm project compiled successfully. You can find the JS at /tmp/elm_artifacts/elm_project/js/Main1.js'
          "
"""

# Define the target path on the host where to save artifacts
output_directory = 'docker_'

def run_docker_and_copy():
    try:
        print("Running Docker container to build Elm project and generate HTML...")
        
        # Step 1: Run Docker container in detached mode
        result = subprocess.run(docker_command, shell=True, capture_output=True, text=True, check=True)
        
        # Capture the container ID from the result of the docker run command
        container_id = result.stdout.strip()
        print(f"Docker container {container_id} started in detached mode.")

        # Step 2: Wait for the container to finish processing
        # In real scenarios, you might want to implement a more robust waiting mechanism, 
        # like checking for specific file existence or sleeping for a few seconds.
        time.sleep(5)  # Adjust as necessary to ensure the container has finished work

        print("Docker container finished running. Now copying artifacts...")
        
        # Step 3: Copy the artifact (the JS file in this case) from the container to the host
        container_output_js_path = "/tmp/elm_artifacts/elm_project/js/Main1.js"
        container_output_html_path = "/tmp/elm_artifacts/elm_project/html/index.html"
        
        # Run docker cp command to copy from the container to the local machine
        copy_command_js = f"docker cp {container_id}:{container_output_js_path} {output_directory}/Main1.js"
        subprocess.run(copy_command_js, shell=True, check=True)
        
        copy_command_html = f"docker cp {container_id}:{container_output_html_path} {output_directory}/index.html"
        subprocess.run(copy_command_html, shell=True, check=True)
        
        print(f"Elm artifacts have been copied: JS - {output_directory}/Main1.js, HTML - {output_directory}/index.html")
    
    except subprocess.CalledProcessError as e:
        print(f"Error occurred: {e}")

if __name__ == "__main__":
    run_docker_and_copy()
