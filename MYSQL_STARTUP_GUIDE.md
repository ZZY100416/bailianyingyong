# MySQL数据库启动指南

## 🎯 功能特点

现在您的聊天机器人支持：
- ✅ **MySQL数据库存储** - 消息持久化到MySQL
- ✅ **记忆功能** - 基于数据库的会话记忆
- ✅ **流式响应** - 实时流式聊天体验
- ✅ **会话管理** - 多会话隔离和历史查看

## 🗄️ 数据库设置

### 1. 确保MySQL运行

确保MySQL服务正在运行：
- **主机**: localhost
- **端口**: 3307
- **用户名**: root
- **密码**: root123

### 2. 创建数据库

执行以下SQL创建数据库和表：

```sql
-- 创建数据库
CREATE DATABASE IF NOT EXISTS chatbot_db CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;

-- 使用数据库
USE chatbot_db;

-- 创建messages表
CREATE TABLE IF NOT EXISTS messages (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    sender VARCHAR(50) NOT NULL,
    content TEXT NOT NULL,
    session_id VARCHAR(255),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    INDEX idx_sender (sender),
    INDEX idx_session_id (session_id),
    INDEX idx_created_at (created_at),
    INDEX idx_session_created (session_id, created_at)
);
```

### 3. 快速执行

使用提供的SQL文件：
```bash
mysql -u root -p -h localhost -P 3307 < chatbotApi/database_init.sql
```

## 🚀 启动应用

### 1. 启动后端
```bash
cd chatbotApi
mvn clean install
mvn spring-boot:run
```

### 2. 启动前端
```bash
npm run dev
```

### 3. 访问应用
打开浏览器访问：http://localhost:5173

## 🎮 测试功能

### 1. 基本聊天测试
发送消息："你好，我是小源，请记住我"

### 2. 记忆功能测试
发送消息："你还记得我是谁吗？"

### 3. 查看历史
点击"查看历史"按钮查看对话记录

### 4. 会话管理
- 点击"新会话"开始新对话
- 点击"清除历史"删除当前会话

## 📊 数据库验证

### 查看存储的消息
```sql
USE chatbot_db;
SELECT * FROM messages ORDER BY created_at DESC LIMIT 10;
```

### 查看会话历史
```sql
SELECT * FROM messages WHERE session_id = 'your_session_id' ORDER BY created_at;
```

## 🔧 API端点

- `GET /chat/simple` - 简单聊天（带数据库存储）
- `GET /chat/stream` - 流式聊天（带数据库存储）
- `POST /chat/stream` - POST流式聊天（带数据库存储）
- `GET /chat/messages` - 获取历史消息
- `GET /chat/messages/session/{sessionId}` - 获取会话历史
- `DELETE /chat/messages/session/{sessionId}` - 清除会话历史
- `POST /chat/session/new` - 创建新会话
- `GET /chat/test` - 服务测试

## 🎯 技术架构

### 后端技术栈
- Spring Boot 3.5.6
- MyBatis Plus 3.5.7
- MySQL 8.0
- WebFlux (流式响应)

### 前端技术栈
- React 18
- Vite
- @nlux/react

## ⚠️ 注意事项

1. **数据库连接** - 确保MySQL服务运行且配置正确
2. **API Key** - 确保百炼API Key有效
3. **端口冲突** - 确保5649端口未被占用
4. **数据持久化** - 所有消息都会保存到数据库

## 🐛 故障排除

### 数据库连接失败
- 检查MySQL服务状态
- 验证连接参数
- 检查防火墙设置

### 应用启动失败
- 检查依赖是否正确安装
- 查看错误日志
- 验证配置文件

### 记忆功能不工作
- 检查数据库表是否正确创建
- 验证session_id是否正确传递
- 查看应用日志

现在您的聊天机器人拥有了完整的MySQL数据库支持和记忆功能！

