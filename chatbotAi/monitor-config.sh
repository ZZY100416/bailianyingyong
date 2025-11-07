#!/bin/bash
echo "=== OpenRASP 配置监控 ==="
echo "监控时间: $(date)"

# 检查当前配置
CURRENT_CONFIG=$(grep -A5 -B5 '"action":"block"' rasp/logs/rasp/rasp.log | tail -20)

if echo "$CURRENT_CONFIG" | grep -q '"action":"block"'; then
  echo "✅ 配置处于拦截模式"
  BLOCK_COUNT=$(echo "$CURRENT_CONFIG" | grep -c '"action":"block"')
  echo "拦截算法数量: $BLOCK_COUNT"
else
  echo "❌ 配置处于记录日志模式"
fi

# 检查关键算法
echo ""
echo "关键算法状态:"
grep -E '"sql_userinput|xss_userinput|command_userinput"' rasp/logs/rasp/rasp.log | grep -o '"action":"[^"]*' | cut -d'"' -f4 | head -3

# 检查meta配置
echo ""
echo "Meta配置:"
grep '"meta"' rasp/logs/rasp/rasp.log | tail -1 | grep -o '"all_log":[^,]*'
