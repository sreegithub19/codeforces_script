from flask import Flask, jsonify

app = Flask(__name__)

@app.route("/")
def home():
    return jsonify(message="Welcome to the Flask server!")

@app.route("/about")
def about():
    return jsonify(message="This is the about page.")

if __name__ == "__main__":
    app.run(host="0.0.0.0", port=8000)
