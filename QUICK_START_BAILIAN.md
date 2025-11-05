# 百炼智能体快速启动指南

基于 [百炼控制台](https://bailian.console.aliyun.com/?tab=doc#/doc/?type=app&url=2974821) 的官方文档，本指南将帮助您快速启动百炼智能体应用。

## 🚀 快速开始

### 1. 配置百炼平台

在 [百炼控制台](https://bailian.console.aliyun.com/?tab=doc#/doc/?type=app&url=2974821) 中：

1. **获取API Key**: 在控制台中找到您的API密钥
2. **获取App ID**: 创建或选择一个智能体应用，获取App ID
3. **配置模型**: 选择合适的模型（推荐qwen-turbo或qwen-plus）

### 2. 更新配置

编辑 `bialianyingyongapikey/src/main/resources/application.properties`：

```properties
# 百炼平台配置
spring.ai.dashscope.apikey=your-api-key-here
spring.ai.dashscope.agent.app-id=your-app-id-here
spring.ai.dashscope.chat.options.model=qwen-turbo
spring.ai.dashscope.chat.options.temperature=0.7
spring.ai.dashscope.chat.options.max-tokens=2000
```

### 3. 启动后端

```bash
# Windows
start-backend.bat

# Linux/Mac
cd bialianyingyongapikey
./mvnw spring-boot:run
```

### 4. 测试连接

打开 `test-connection.html` 文件，测试以下功能：
- ✅ 健康检查
- ✅ 智能体信息
- ✅ 流式对话
- ✅ 简单对话

### 5. 启动前端

```bash
npm run dev
```

访问 http://localhost:5173

## 📋 API 接口

### 百炼智能体接口

| 接口 | 方法 | 描述 |
|------|------|------|
| `/bailian/agent/stream` | POST | 流式对话 |
| `/bailian/agent/chat` | POST | 简单对话 |
| `/bailian/agent/health` | GET | 健康检查 |
| `/bailian/agent/info` | GET | 智能体信息 |

### 传统接口

| 接口 | 方法 | 描述 |
|------|------|------|
| `/bailian/agent/chat-model` | POST | ChatModel对话 |
| `/bailian/agent/agent-chat` | POST | 传统Agent对话 |

## 🔧 技术架构

```
前端 (React + Vite)
    ↓ HTTP请求
后端 (Spring Boot + Spring AI)
    ↓ API调用
百炼平台 (DashScope)
    ↓ AI模型
通义千问 (qwen-turbo)
```

## 🐛 故障排除

### 常见问题

1. **API Key错误**
   - 检查API Key是否正确
   - 确认API Key权限

2. **App ID无效**
   - 检查App ID是否存在
   - 确认应用状态正常

3. **网络连接问题**
   - 检查网络连接
   - 确认防火墙设置

4. **依赖问题**
   - 运行 `mvn clean install`
   - 检查Java版本（需要17+）

### 日志查看

```bash
# 查看详细日志
tail -f bialianyingyongapikey/logs/application.log
```

## 📚 参考资源

- [百炼控制台](https://bailian.console.aliyun.com/?tab=doc#/doc/?type=app&url=2974821)
- [Spring AI 文档](https://spring.io/projects/spring-ai)
- [DashScope API 文档](https://help.aliyun.com/zh/dashscope/)

## 🎯 下一步

1. 自定义智能体提示词
2. 添加更多功能（如文件上传、图片生成等）
3. 集成更多百炼平台功能
4. 部署到生产环境
