#!/bin/bash

# Change this directory to the folder containing your MATLAB scripts
MATLAB_DIR="files"

# Loop through all .m files in the specified directory
for matlab_file in "$MATLAB_DIR"/*.m
do
  if [[ -f "$matlab_file" ]]; then
    echo "Running $matlab_file..."
    # Run each file using MATLAB -batch
    matlab -batch "run('$matlab_file')"
  fi
done
