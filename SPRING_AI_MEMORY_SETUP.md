# Spring AI Memory 记忆功能设置指南

## 概述

现在使用Spring AI官方的记忆功能，提供更稳定和强大的对话记忆能力。

## 主要改进

### 1. 使用Spring AI官方组件
- `MysqlChatMemoryRepository` - MySQL记忆存储
- `MessageWindowChatMemory` - 消息窗口记忆管理
- `MessageChatMemoryAdvisor` - 记忆顾问

### 2. 自动记忆管理
- 自动存储对话历史
- 自动管理上下文窗口
- 自动优化记忆性能

## 设置步骤

### 1. 数据库设置

执行以下SQL脚本创建Spring AI Memory所需的表：

```sql
-- 在MySQL中执行
source chatbotApi/spring_ai_memory_schema.sql
```

或者手动执行：

```sql
CREATE TABLE IF NOT EXISTS chat_memory (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    conversation_id VARCHAR(255) NOT NULL,
    message_type VARCHAR(50) NOT NULL,
    content TEXT NOT NULL,
    metadata JSON,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    INDEX idx_conversation_id (conversation_id),
    INDEX idx_created_at (created_at),
    INDEX idx_conversation_created (conversation_id, created_at)
);
```

### 2. 依赖更新

已添加以下依赖到 `pom.xml`：

```xml
<!-- Spring AI Memory JDBC -->
<dependency>
    <groupId>org.springframework.ai</groupId>
    <artifactId>spring-ai-memory-jdbc</artifactId>
</dependency>

<!-- Spring AI Chat Client -->
<dependency>
    <groupId>org.springframework.ai</groupId>
    <artifactId>spring-ai-chat-client</artifactId>
</dependency>
```

### 3. 配置更新

`application.properties` 已添加Spring AI配置：

```properties
# Spring AI 配置
spring.ai.dashscope.api-key=${bailian.api.key}
spring.ai.dashscope.chat.options.model=${bailian.chat.model}
spring.ai.dashscope.chat.options.max-tokens=${bailian.max_output_tokens}

# Spring AI Memory 配置
spring.ai.chat.memory.repository.jdbc.mysql.jdbc-url=${spring.datasource.url}
spring.ai.chat.memory.repository.jdbc.mysql.username=${spring.datasource.username}
spring.ai.chat.memory.repository.jdbc.mysql.password=${spring.datasource.password}
spring.ai.chat.memory.repository.jdbc.mysql.driver-class-name=${spring.datasource.driver-class-name}
```

## 新功能

### 1. 自动记忆
- 每次对话都会自动保存到数据库
- 自动构建上下文历史
- 智能管理记忆窗口（最多100条消息）

### 2. 会话管理
- 每个对话都有唯一的 `conversation_id`
- 支持查看历史消息
- 支持创建新会话

### 3. API端点

- `GET /chat/simple` - 简单聊天
- `GET /chat/stream` - 流式聊天
- `POST /chat/stream` - POST流式聊天（兼容前端）
- `GET /chat/messages` - 获取历史消息
- `GET /chat/test` - 测试接口

## 使用方法

### 1. 启动服务

```bash
# 启动后端
cd chatbotApi
mvn spring-boot:run

# 启动前端
npm run dev
```

### 2. 测试记忆功能

1. 发送消息："你好，我是小源，请记住我"
2. 发送消息："你还记得我是谁吗？"
3. AI应该能记住你的名字

### 3. 查看历史

点击"查看历史"按钮可以查看当前会话的所有消息。

## 技术特点

### 1. 自动上下文管理
- Spring AI会自动将历史消息作为上下文发送给AI模型
- 智能截断过长的历史记录
- 保持对话的连贯性

### 2. 高性能存储
- 使用MySQL存储记忆数据
- 优化的数据库索引
- 支持大量并发会话

### 3. 灵活配置
- 可配置最大消息数量
- 可配置记忆策略
- 支持不同的存储后端

## 故障排除

### 1. 数据库连接问题
确保MySQL服务正在运行，并且配置正确。

### 2. 记忆不工作
检查Spring AI配置是否正确，特别是API Key。

### 3. 历史消息显示异常
检查前端是否正确处理Spring AI的消息格式。

## 优势

相比之前的自定义实现：

1. **更稳定** - 使用官方组件，经过充分测试
2. **更强大** - 支持更多高级功能
3. **更易维护** - 减少自定义代码
4. **更好性能** - 优化的存储和查询
5. **更易扩展** - 支持多种存储后端

现在您的聊天机器人拥有了企业级的记忆功能！
