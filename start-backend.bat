@echo off
echo 启动Spring Boot后端应用...
cd bialianyingyongapikey
echo 当前目录: %CD%
echo.
echo 正在编译项目...
call mvnw clean compile
if %ERRORLEVEL% neq 0 (
    echo 编译失败！
    pause
    exit /b 1
)
echo.
echo 编译成功！正在启动应用...
echo 后端将在端口 7765 上运行
echo 按 Ctrl+C 停止应用
echo.
call mvnw spring-boot:run
pause
