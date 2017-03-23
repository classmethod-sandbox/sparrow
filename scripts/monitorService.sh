#!/bin/bash

ret=1
while [ ${ret} -ne 0 ]; do
    echo "trying to connect localhost:8080 ..."
    curl -s http://localhost:8080/ -o /dev/null
    ret=$?
    sleep 5
done

echo "connection succeeded."
