
if [ $# -lt 1 ]; then
  echo "Usage: $0 <create|start|stop> [db_username] [db_password]"
  exit 1
fi

# Call psql_container.sh script with arguments
./script/psql_container.sh "$@"

