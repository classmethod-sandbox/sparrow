#!/bin/bash

jarFilePath=$(find /opt/sparrow/ -name "*.jar")

/usr/bin/java \
  -server \
  -Xms256m \
  -Xmx256m \
  -Dsun.net.inetaddr.ttl=60 \
  -Dfile.encoding=UTF-8 \
  -Dserver.port=8080 \
  -jar ${jarFilePath} &
