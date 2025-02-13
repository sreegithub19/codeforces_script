import requests
import time
import json
import os

def run_colab_notebook(notebook_path, output_path, email, password):
    session = requests.Session()

    # Authenticate with Google
    login_payload = {
        'Email': email,
        'Passwd': password
    }
    session.post('https://accounts.google.com/ServiceLoginAuth', data=login_payload)

    # Create a new Colab notebook session
    create_notebook_payload = {
        'path': notebook_path
    }
    response = session.post('https://colab.research.google.com/v2/sessions', data=create_notebook_payload)
    response_data = response.json()
    notebook_id = response_data['id']

    # Run the notebook
    run_notebook_url = f'https://colab.research.google.com/v2/sessions/{notebook_id}:run'
    session.post(run_notebook_url)

    # Wait for the notebook to finish running
    while True:
        status_response = session.get(f'https://colab.research.google.com/v2/sessions/{notebook_id}')
        status_data = status_response.json()
        if status_data['state'] == 'IDLE':
            break
        time.sleep(10)

    # Save the output notebook
    output_notebook_url = f'https://colab.research.google.com/v2/sessions/{notebook_id}:download'
    output_response = session.get(output_notebook_url)
    with open(output_path, 'wb') as output_file:
        output_file.write(output_response.content)

if __name__ == '__main__':
    notebook_path = 'execute/ipython_.ipynb'
    output_path = 'output_colab.ipynb'
    email = os.getenv('GOOGLE_EMAIL')
    password = os.getenv('GOOGLE_PASSWORD')
    run_colab_notebook(notebook_path, output_path, email, password)