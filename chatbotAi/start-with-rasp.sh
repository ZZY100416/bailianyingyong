#!/bin/bash

# OpenRASP配置
export JAVA_OPTS="-javaagent:/home/enen/openrasp/rasp-2023-03-31/rasp/rasp.jar"
export RASP_APP_ID=bailianyingyong-backend
export RASP_BACKEND_URL=http://192.168.203.141:8086

echo "=== 启动bailianyingyong后端（带OpenRASP）==="
echo "OpenRASP Agent: /home/enen/openrasp/rasp-2023-03-31/rasp/rasp.jar"
echo "应用ID: $RASP_APP_ID"
echo "管理后台: $RASP_BACKEND_URL"
echo ""

# 启动应用
mvn spring-boot:run
