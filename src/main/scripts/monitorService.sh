#!/bin/bash

status=down
while [ "${status}" != "UP" ]; do
    echo "trying to connect localhost:8080 ..."
    status=$(curl -s http://localhost:8080/health | jq -r '.status')
    sleep 5
    echo "current status: ${status}."
done

echo "connection succeeded."
