#!/bin/bash
echo "=== OpenRASP 最新配置监控 ==="
echo "监控时间: $(date)"

# 获取最近一次的算法配置
LATEST_CONFIG=$(grep "algorithm.config" rasp/logs/rasp/rasp.log | tail -1)

# 检查是否包含block
if echo "$LATEST_CONFIG" | grep -q '"action":"block"'; then
  echo "✅ 最新配置中包含拦截模式"
  BLOCK_COUNT=$(echo "$LATEST_CONFIG" | grep -o '"action":"block"' | wc -l)
  echo "拦截算法数量: $BLOCK_COUNT"
else
  echo "❌ 最新配置中未找到拦截模式"
fi

# 检查关键算法
echo ""
echo "关键算法状态:"
echo "$LATEST_CONFIG" | grep -o '"sql_userinput":{"[^}]*"action":"[^"]*' | grep -o '"action":"[^"]*' | cut -d'"' -f4
echo "$LATEST_CONFIG" | grep -o '"xss_userinput":{"[^}]*"action":"[^"]*' | grep -o '"action":"[^"]*' | cut -d'"' -f4
echo "$LATEST_CONFIG" | grep -o '"command_userinput":{"[^}]*"action":"[^"]*' | grep -o '"action":"[^"]*' | cut -d'"' -f4

# 检查meta配置
echo ""
echo "Meta配置:"
echo "$LATEST_CONFIG" | grep -o '"meta":{"[^}]*}' | head -1
