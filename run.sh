#!/bin/bash

# Delete folders to not get cached bugs
rm -r ./tempo-data
rm -r ./tempo-config
rm -r ./prometheus-data

DETACHED_MODE=false

# Check for -d and -f flags
while getopts "dfk" opt; do
  case $opt in
    d) DETACHED_MODE=true ;;
    k) docker-compose down ;;
    *) echo "Invalid option: -$OPTARG" >&2; exit 1 ;;
  esac
done

# Function to build Spring Boot applications
build_spring_boot() {
  local dir="$1"
  echo "Building the Spring Boot application in $dir"

  # Navigate to the directory
  cd "$dir"

  # Build the application with Gradle
  if ./gradlew build; then
    echo "Build successful."
  else
    echo "Build failed for $dir. Aborting."
    exit 1
  fi

  # Navigate back to the root directory
  cd ..
}

# Loop through all directories in the current folder
for dir in */; do
    # If the directory contains a Dockerfile and a build.gradle file, it's likely a Spring Boot app
    if [ -f "${dir}Dockerfile" ] && { [ -f "${dir}build.gradle" ] || [ -f "${dir}build.gradle.kts" ]; }; then
        build_spring_boot "$dir"
    fi
done

# Once all Spring Boot apps are built, rebuild the Docker images and then start the services
if [ -f "docker-compose.yml" ]; then
    echo "Rebuilding Docker images..."
    docker-compose build

    echo "Starting the Docker Compose services..."
    if $DETACHED_MODE; then
        docker-compose up -d
    else
        docker-compose up
    fi
else
    echo "docker-compose.yml not found in the root directory. Skipping the Docker Compose operations."
fi