#!/bin/bash

# Setup and validate arguments
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
hostname=$(hostname -f)

# Set up env var for the psql command
export PGPASSWORD=$psql_password

# PSQL command to insert data into the host_info table
insert_stmt="INSERT INTO host_info (hostname, cpu_number, cpu_architecture, cpu_model, cpu_mhz, l2_cache, timestamp, total_mem)
VALUES ('$hostname', 0, '', '', 0, 0, now(), 0) ON CONFLICT (hostname) DO NOTHING RETURNING id;"

# Connect to the database and execute the insert query
host_id=$(psql -h $psql_host -p $psql_port -d $db_name -U $psql_user -t -c "$insert_stmt")

# Check if host_id is empty (indicating a conflict or no insertion)
if [ -z "$host_id" ]; then
    # Retrieve the host_id based on the existing hostname
    host_id=$(psql -h $psql_host -p $psql_port -d $db_name -U $psql_user -t -c "SELECT id FROM host_info WHERE hostname = '$hostname';")
fi

# Output the host_id
echo "host_id: $host_id"



























