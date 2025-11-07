#!/bin/bash
echo "=== OpenRASP 完整测试 ==="

# 1. 登录获取token
echo "1. 用户登录..."
LOGIN_RESPONSE=$(curl -s -X POST "http://localhost:9988/api/auth/login" \
  -H "Content-Type: application/json" \
  -d '{"username":"testuser2","password":"test123"}')
echo "登录响应: $LOGIN_RESPONSE"

TOKEN=$(echo "$LOGIN_RESPONSE" | grep -o '"token":"[^"]*' | cut -d'"' -f4)
if [ -z "$TOKEN" ]; then
  echo "错误: 无法获取token"
  exit 1
fi
echo "Token获取成功"

# 2. 测试正常请求（基线）
echo ""
echo "2. 测试正常请求..."
curl -s -H "Authorization: Bearer $TOKEN" "http://localhost:9988/api/chat-history"
echo ""

# 3. SQL注入测试
echo "3. SQL注入测试..."
echo "测试1: 搜索接口SQL注入"
curl -s -H "Authorization: Bearer $TOKEN" \
  "http://localhost:9988/api/chat-history/search?keyword=test' UNION SELECT 1,2,3-- -"
echo ""

echo "测试2: ID参数SQL注入"  
curl -s -H "Authorization: Bearer $TOKEN" \
  "http://localhost:9988/api/chat-history/1' OR '1'='1"
echo ""

# 4. XSS测试
echo "4. XSS测试..."
echo "测试1: 搜索接口XSS"
curl -s -H "Authorization: Bearer $TOKEN" \
  "http://localhost:9988/api/chat-history/search?keyword=<script>alert('XSS')</script>"
echo ""

echo "测试2: 复杂XSS"
curl -s -H "Authorization: Bearer $TOKEN" \
  "http://localhost:9988/api/chat-history/search?keyword=<img src=x onerror=alert(1)>"
echo ""

# 5. 路径遍历测试
echo "5. 路径遍历测试..."
curl -s -H "Authorization: Bearer $TOKEN" \
  "http://localhost:9988/api/chat-history/../../../etc/passwd"
echo ""

# 6. 命令注入测试（AI接口）
echo "6. AI接口命令注入测试..."
curl -s -X POST "http://localhost:9988/bailian/agent/stream" \
  -H "Content-Type: application/json" \
  -d '{"query":"hello; id; whoami; ls -la"}'
echo ""

# 7. 注册接口SQL注入测试
echo "7. 注册接口SQL注入测试..."
curl -s -X POST "http://localhost:9988/api/auth/register" \
  -H "Content-Type: application/json" \
  -d '{"username":"admin'' OR ''1''=''1","password":"test","email":"sql@test.com","nickname":"sqltest"}'
echo ""

echo "=== 测试完成 ==="
echo "请检查 OpenRASP 日志查看是否有攻击事件"
