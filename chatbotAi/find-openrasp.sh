#!/bin/bash
echo "=== 查找OpenRASP安装路径 ==="
echo ""
echo "1. 查找rasp.jar文件："
find /opt /usr/local /home -name "rasp.jar" 2>/dev/null | head -5
echo ""
echo "2. 查找rasp-java目录："
find /opt /usr/local /home -type d -name "rasp-java" 2>/dev/null
echo ""
echo "3. 查找openrasp目录："
find /opt /usr/local /home -type d -name "*openrasp*" 2>/dev/null
echo ""
echo "4. 检查常见安装位置："
for dir in /opt/rasp-java /opt/openrasp /usr/local/rasp-java /usr/local/openrasp ~/rasp-java ~/openrasp; do
    if [ -d "$dir" ]; then
        echo "✓ 找到: $dir"
        ls -la "$dir" | head -5
    fi
done
echo ""
echo "5. 检查Docker容器："
docker ps -a | grep -i rasp 2>/dev/null || echo "未找到OpenRASP Docker容器"
echo ""
echo "6. 检查端口8086："
netstat -tlnp 2>/dev/null | grep 8086 || ss -tlnp 2>/dev/null | grep 8086 || echo "端口8086未监听"
