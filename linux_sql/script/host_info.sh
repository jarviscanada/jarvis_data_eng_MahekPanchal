#!/bin/bash

#Setup and validate arguments
psql_host=$1
psql_port=$2
db_name=$3
psql_user=$4
psql_password=$5

# Check the number of arguments
if [ "$#" -ne 5 ]; then
    echo "Illegal number of parameters"
    exit 1
fi

# Save the hostname to a variable
hostname=$(hostname)

# Set up env var for the psql command
export PGPASSWORD=$psql_password 

# PSQL command to select data from the host_info table
select_stmt="SELECT * FROM host_info;"

# Connect to the database and execute the query
psql -h $psql_host -p $psql_port -d $db_name -U $psql_user -c "$select_stmt"

