#!/bin/bash

jarFilePath=$(find /opt/sparrow/ -name "*.jar")
sudo ln -sf ${jarFilePath} /etc/init.d/sparrow

confFilename="${jarFilePath%.*}"

cat <<'EOF' > ${confFilename}.conf
JAVA_OPTS="-server"
JAVA_OPTS="${JAVA_OPTS} -Xms256m"
JAVA_OPTS="${JAVA_OPTS} -Xmx256m"
JAVA_OPTS="${JAVA_OPTS} -XX:+UseCompressedOops"
JAVA_OPTS="${JAVA_OPTS} -XX:+UseConcMarkSweepGC -XX:+CMSParallelRemarkEnabled"
JAVA_OPTS="${JAVA_OPTS} -XX:+UseCMSInitiatingOccupancyOnly -XX:CMSInitiatingOccupancyFraction=70"
JAVA_OPTS="${JAVA_OPTS} -XX:+ScavengeBeforeFullGC -XX:+CMSScavengeBeforeRemark"
JAVA_OPTS="${JAVA_OPTS} -XX:+PrintGCDateStamps -verbose:gc -XX:+PrintGCDetails -Xloggc:/opt/sparrow/gc_%t_pid%p.log"
JAVA_OPTS="${JAVA_OPTS} -XX:+UseGCLogFileRotation -XX:NumberOfGCLogFiles=10 -XX:GCLogFileSize=100M"
JAVA_OPTS="${JAVA_OPTS} -XX:+HeapDumpOnOutOfMemoryError -XX:HeapDumpPath=/var/eb_log/"
JAVA_OPTS="${JAVA_OPTS} -XX:ErrorFile=/opt/sparrow/hs_err_pid%p.log"
JAVA_OPTS="${JAVA_OPTS} -Dsun.net.inetaddr.ttl=60"
JAVA_OPTS="${JAVA_OPTS} -Dfile.encoding=UTF-8"
JAVA_OPTS="${JAVA_OPTS} -Dserver.port=8080"
EOF

service sparrow start
