# Use an official Ubuntu image as the base
FROM ubuntu:latest

# Install necessary dependencies
RUN apt-get update && \
    apt-get install -y wget unzip

# The index.html file will now be created dynamically by the Python script