# Use a base image
FROM ubuntu:20.04

# Set working directory
WORKDIR /app

# Create an output directory
RUN mkdir -p /app/output

# Generate a file in the output directory
RUN echo "Hello, this is the output file!" > /app/output/output.txt

# Default command
CMD ["sleep", "infinity"]
