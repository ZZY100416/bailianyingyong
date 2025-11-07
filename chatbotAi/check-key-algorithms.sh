#!/bin/bash
echo "=== 关键算法详细检查 ==="

# 获取最新配置
LATEST_CONFIG=$(grep "algorithm.config" rasp/logs/rasp/rasp.log | tail -1)

# 检查关键算法动作
echo "关键算法状态:"
for algo in sql_userinput xss_userinput command_userinput readFile_userinput directory_userinput; do
  ACTION=$(echo "$LATEST_CONFIG" | grep -o "\"${algo}\":{[^}]*\"action\":\"[^\"]*\"" | grep -o "\"action\":\"[^\"]*\"" | cut -d'"' -f4)
  if [ -n "$ACTION" ]; then
    echo "  ${algo}: ${ACTION}"
  else
    echo "  ${algo}: 未找到"
  fi
done

# 检查算法名称
echo ""
echo "部分设置为拦截的算法:"
echo "$LATEST_CONFIG" | grep -o '"action":"block"[^}]*"name":"[^"]*' | grep -o '"name":"[^"]*' | cut -d'"' -f4 | head -10
