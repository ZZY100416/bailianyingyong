#!/bin/bash
echo "=== OpenRASP 综合攻击测试 ==="

# 获取token
echo "获取Token..."
LOGIN_RESPONSE=$(curl -s -X POST "http://localhost:9988/api/auth/login" \
  -H "Content-Type: application/json" \
  -d '{"username":"testuser2","password":"test123"}')
TOKEN=$(echo "$LOGIN_RESPONSE" | grep -o '"token":"[^"]*' | cut -d'"' -f4)

if [ -z "$TOKEN" ]; then
  echo "❌ Token获取失败"
  exit 1
fi
echo "✅ Token获取成功"

# 启动实时监控
echo "启动实时日志监控..."
tail -f rasp/logs/alarm/alarm.log &
ALARM_PID=$!
tail -f rasp/logs/rasp/rasp.log &
RASP_PID=$!

sleep 3

echo ""
echo "开始攻击测试..."

# 1. SQL注入攻击 - 多种Payload
echo "1. SQL注入攻击测试..."
echo "Payload 1: test' OR '1'='1"
curl -s -H "Authorization: Bearer $TOKEN" \
  "http://localhost:9988/api/chat-history/test-rasp-sql?input=test' OR '1'='1"
echo ""

echo "Payload 2: 1' UNION SELECT 1,2,3-- -"
curl -s -H "Authorization: Bearer $TOKEN" \
  "http://localhost:9988/api/chat-history/test-rasp-sql?input=1' UNION SELECT 1,2,3-- -"
echo ""

echo "Payload 3: 1' AND (SELECT * FROM (SELECT(SLEEP(3)))a)-- -"
curl -s -H "Authorization: Bearer $TOKEN" \
  "http://localhost:9988/api/chat-history/test-rasp-sql?input=1' AND (SELECT * FROM (SELECT(SLEEP(3)))a)-- -"
echo ""

# 2. XSS攻击 - 多种Payload
echo "2. XSS攻击测试..."
echo "Payload 1: <script>alert('xss')</script>"
curl -s -H "Authorization: Bearer $TOKEN" \
  "http://localhost:9988/api/chat-history/test-rasp-xss?input=<script>alert('xss')</script>"
echo ""

echo "Payload 2: <img src=x onerror=alert(1)>"
curl -s -H "Authorization: Bearer $TOKEN" \
  "http://localhost:9988/api/chat-history/test-rasp-xss?input=<img src=x onerror=alert(1)>"
echo ""

echo "Payload 3: <svg onload=alert(1)>"
curl -s -H "Authorization: Bearer $TOKEN" \
  "http://localhost:9988/api/chat-history/test-rasp-xss?input=<svg onload=alert(1)>"
echo ""

# 3. 命令注入攻击 - 多种Payload
echo "3. 命令注入攻击测试..."
echo "Payload 1: id;whoami;ls -la"
curl -s -H "Authorization: Bearer $TOKEN" \
  "http://localhost:9988/api/chat-history/test-rasp-command?input=id;whoami;ls -la"
echo ""

echo "Payload 2: cat /etc/passwd"
curl -s -H "Authorization: Bearer $TOKEN" \
  "http://localhost:9988/api/chat-history/test-rasp-command?input=cat /etc/passwd"
echo ""

echo "Payload 3: ping -c 1 evil.com"
curl -s -H "Authorization: Bearer $TOKEN" \
  "http://localhost:9988/api/chat-history/test-rasp-command?input=ping -c 1 evil.com"
echo ""

# 4. 文件读取攻击 - 使用存在的文件
echo "4. 文件读取攻击测试..."
echo "Payload 1: /etc/hosts (存在的文件)"
curl -s -H "Authorization: Bearer $TOKEN" \
  "http://localhost:9988/api/chat-history/test-rasp-file?filename=/etc/hosts"
echo ""

echo "Payload 2: /etc/passwd (系统文件)"
curl -s -H "Authorization: Bearer $TOKEN" \
  "http://localhost:9988/api/chat-history/test-rasp-file?filename=/etc/passwd"
echo ""

echo "Payload 3: ../../../etc/passwd (路径遍历)"
curl -s -H "Authorization: Bearer $TOKEN" \
  "http://localhost:9988/api/chat-history/test-rasp-file?filename=../../../etc/passwd"
echo ""

# 等待日志刷新
echo "等待日志刷新..."
sleep 10

# 停止监控
echo "停止日志监控..."
kill $ALARM_PID $RASP_PID 2>/dev/null

echo ""
echo "=== 攻击测试完成 ==="
