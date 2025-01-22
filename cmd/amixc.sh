#!/bin/bash

CACHE_DIR="$HOME/.amixc_cache"
CACHE_FILE="$CACHE_DIR/amixc.jar"
VERSION="1.0.0"

if [ ! -f "$CACHE_FILE" ]; then
  mkdir -p "$CACHE_DIR"
  echo "Downloading resources..."
  wget -q "https://github.com/AmixLang/AmixFastCompiler/releases/download/$VERSION/amixc.jar" -O "$CACHE_FILE" 2>/dev/null
fi

if [ "$#" -lt 2 ]; then
  echo "Usage: $0 -verticalroot <value> <files>"
  exit 1
fi

verticalRoot=""
files=()

while (( "$#" )); do
  case "$1" in
    -verticalroot)
      verticalRoot="$2"
      shift 2
      ;;
    *)
      files+=("$1")
      shift
      ;;
  esac
done

if [ -z "$verticalRoot" ]; then
  echo "Error: -verticalroot argument is required."
  exit 1
fi

java -jar "$CACHE_FILE" -verticalroot "$verticalRoot" "${files[@]}"