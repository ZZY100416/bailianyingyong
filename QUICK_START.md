# Spring AI Memory 快速启动指南

## 🚀 快速开始

### 1. 数据库准备

确保MySQL数据库正在运行，并且可以连接到：
- 主机：localhost
- 端口：3307
- 用户名：root
- 密码：root123
- 数据库：chatbot_db

### 2. 启动后端服务

```bash
cd chatbotApi
mvn clean install
mvn spring-boot:run
```

### 3. 启动前端服务

```bash
npm run dev
```

### 4. 测试记忆功能

1. 打开浏览器访问：http://localhost:5173
2. 发送消息："你好，我是小源，请记住我"
3. 发送消息："你还记得我是谁吗？"
4. 点击"查看历史"按钮查看对话记录

## 🔧 配置说明

### 自动数据库初始化

Spring AI会自动创建所需的数据库表，无需手动执行SQL脚本。

### 记忆功能特点

- ✅ 自动保存对话历史
- ✅ 智能上下文管理（最多100条消息）
- ✅ 支持多会话隔离
- ✅ 高性能MySQL存储
- ✅ 流式响应支持

## 🐛 故障排除

### 1. 数据库连接失败
- 检查MySQL服务是否运行
- 验证数据库连接配置
- 确保数据库用户权限正确

### 2. 记忆功能不工作
- 检查Spring AI配置
- 查看应用日志
- 验证API Key是否正确

### 3. 前端无法连接后端
- 检查CORS配置
- 验证端口号是否正确
- 查看浏览器控制台错误

## 📝 API端点

- `GET /test/memory` - 测试记忆功能
- `GET /chat/simple` - 简单聊天
- `GET /chat/stream` - 流式聊天
- `POST /chat/stream` - POST流式聊天
- `GET /chat/messages` - 获取历史消息

## 🎯 功能验证

### 测试步骤

1. **基本聊天测试**
   ```
   GET /chat/simple?query=你好&conversation_id=test1
   ```

2. **记忆功能测试**
   ```
   GET /chat/simple?query=我是张三&conversation_id=test1
   GET /chat/simple?query=你还记得我是谁吗&conversation_id=test1
   ```

3. **历史查询测试**
   ```
   GET /chat/messages?conversation_id=test1
   ```

现在您的聊天机器人已经具备了完整的记忆功能！
