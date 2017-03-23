#!/bin/bash

pid=$(jps | grep -i sparrow | awk '{print $1}')
if [ -n "${pid}" ]; then
    kill ${pid} || true
fi
