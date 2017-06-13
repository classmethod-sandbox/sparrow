#!/bin/bash

# download configuration from S3
export AWS_DEFAULT_REGION=$(curl -s http://169.254.169.254/latest/meta-data/placement/availability-zone | sed -e 's/.$//')
INSTANCE_ID=$(curl -s http://169.254.169.254/latest/meta-data/instance-id)

getTagValue() {
    tagName=$1
    ret=$(aws ec2 describe-instances --instance-id ${INSTANCE_ID} \
        | jq -r '.Reservations[].Instances[].Tags[] | select(.Key == "'${tagName}'") | .Value')
    echo ${ret}
}
s3Bucket=$(getTagValue sparrow_configuration_bucket)
s3Prefix=$(getTagValue sparrow_configuration_prefix)
aws s3 cp s3://${s3Bucket}/${s3Prefix}/application.json /opt/sparrow/application.json

sudo chown ec2-user:ec2-user -R /opt/sparrow/*

jarFilePath=$(find /opt/sparrow/ -name "*.jar")
chmod a+x ${jarFilePath} && sudo ln -sf ${jarFilePath} /etc/init.d/sparrow

# 実行ファイルの拡張子を除いた名前を取得
# 同名の実行ファイルを作成する必要があるため
# https://docs.spring.io/spring-boot/docs/current/reference/html/deployment-install.html
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

export SPRING_APPLICATION_JSON=$(cat /opt/sparrow/application.json)
export SPRING_PROFILES_ACTIVE=aws
EOF
