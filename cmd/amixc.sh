#!/bin/bash

CACHE_DIR="$HOME/.amixc_cache"
CACHE_FILE="$CACHE_DIR/amixc.jar"

if [ ! -f "$CACHE_FILE" ]; then
  mkdir -p "$CACHE_DIR"
  wget https://github.com/trindadedev13/AmixC/releases/download/1.0.0/amixc.jar -O "$CACHE_FILE"
fi

echo "Enter the verticalroot:"
read verticalRoot

echo "Enter the files (separate them with spaces):"
read -a files

java -jar "$CACHE_FILE" -verticalroot "$verticalRoot" "${files[@]}"