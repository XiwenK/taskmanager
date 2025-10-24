#!/bin/bash

set -e

PROJECT_ROOT=$(cd "$(dirname "$0")" && pwd)
APP_NAME="taskmanager"
JAR_FILE="build/libs/${APP_NAME}-0.0.1-SNAPSHOT.jar"
DOCKER_COMPOSE_FILE="${PROJECT_ROOT}/docker-compose.yml"
LOG_FILE="${PROJECT_ROOT}/taskmanager.log"

DB_CONTAINER_NAME="taskmanager-mysql"
DB_HOST="localhost"
DB_USER="taskuser"
DB_PASSWD="taskpass"

cleanup() {
  echo "Clean up existing docker resources ..."
  docker compose down -v
  echo "Cleanup completed."
}

# once catch any error do resource cleanup
trap cleanup ERR

echo "Booting up mysql docker container ..."
docker compose -f "$DOCKER_COMPOSE_FILE" up -d

CNT=0
MAX_CNT=15
until docker exec "$DB_CONTAINER_NAME" mysqladmin ping -h "$DB_HOST" -u "$DB_USER" -p"$DB_PASSWD" --silent; do
  CNT=$((CNT+1))
  if [ $CNT -ge $MAX_CNT ]; then
    echo "Failed to boot up mysql docker container, shutdown ..."
    cleanup
    exit 1
  fi
  sleep 2
done
echo "Mysql is ready !"

echo "Building springboot project ..."
./gradlew clean build

CNT=0
until [ -f "$JAR_FILE" ]; do
  CNT=$((CNT+1))
  if [ $CNT -ge $MAX_CNT]; then
    echo "Failed to build springboot project, shutdown ..."
    cleanup
    exit 1
  fi
  sleep 2
done

echo "Running up project ..."
java -jar "$JAR_FILE" # Test on local
#nohup java -jar "$JAR_FILE" > "$LOG_FILE" 2>&1 & # Test on Github Actions


