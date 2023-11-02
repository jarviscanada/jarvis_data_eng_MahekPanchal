#!/bin/bash

# Capture CLI arguments
cmd=$1
db_username=$2
db_password=$3

# Function to check if the container exists
container_exists() {
  docker inspect jrvs-psql > /dev/null 2>&1
  return $?
}

# Start Docker if not running
sudo systemctl is-active --quiet docker || sudo systemctl start docker

# Check container status
container_exists
container_status=$?

# User switch case to handle create|stop|start options
case $cmd in 
  create)
    # Check if the container is already created
    if [ $container_status -eq 0 ]; then
      echo 'Container already exists'
      exit 1
    fi

    # Check the number of CLI arguments
    if [ $# -ne 3 ]; then
      echo 'Create requires username and password'
      exit 1
    fi

    # Create container and start it
    docker run --name jrvs-psql -e POSTGRES_PASSWORD=$db_password -e POSTGRES_USER=$db_username -d -p 5432:5432 postgres
    exit $?
    ;;

  start|stop) 
    # Check container status; exit 1 if container has not been created
    if [ $container_status -ne 0 ]; then
      echo 'Container does not exist. Use "create" to create it.'
      exit 1
    fi

    # Start or stop the container
    docker container $cmd jrvs-psql
    exit $?
    ;;	
  
  *)
    echo 'Illegal command'
    echo 'Commands: start|stop|create'
    exit 1
    ;;
esac
if [ $# -lt 1 ]; then
  echo "Usage: $0 <create|start|stop> [db_username] [db_password]"
  exit 1
fi

# Call psql_container.sh script with arguments
.script/psql_docker.sh "@"
