#!/bin/bash

# Change this directory to the folder containing your SCILAB scripts
SCILAB_DIR="files"

# Loop through all .m files in the specified directory
for scilab_file in "$SCILAB_DIR"/*.m
do
  if [[ -f "$scilab_file" ]]; then
    echo "Running $scilab_file..."
    # Run each file using SCILAB -batch
    scilab -no_gui -batch "exec('$scilab_file')"
  fi
done
