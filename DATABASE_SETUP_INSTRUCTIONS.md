# 数据库设置说明

## 🗄️ 数据库配置

### 1. 确保MySQL服务运行

确保MySQL服务正在运行，并且可以连接到：
- 主机：localhost
- 端口：3307
- 用户名：root
- 密码：root123

### 2. 创建数据库和表

执行以下SQL脚本创建数据库和表：

```sql
-- 创建数据库（如果不存在）
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

### 3. 执行方式

#### 方式1：使用MySQL命令行
```bash
mysql -u root -p -h localhost -P 3307 < chatbotApi/database_init.sql
```

#### 方式2：使用MySQL Workbench
1. 打开MySQL Workbench
2. 连接到您的MySQL服务器
3. 打开并执行 `chatbotApi/database_init.sql` 文件

#### 方式3：手动执行
1. 连接到MySQL服务器
2. 复制并执行上述SQL语句

### 4. 验证数据库

执行以下查询验证表是否创建成功：

```sql
USE chatbot_db;
SHOW TABLES;
DESCRIBE messages;
```

应该看到 `messages` 表及其结构。

## 🚀 启动应用

数据库设置完成后，启动应用：

```bash
cd chatbotApi
mvn clean install
mvn spring-boot:run
```

## 🔧 配置说明

当前数据库配置：
- **数据库**：chatbot_db
- **表**：messages
- **字段**：
  - `id` - 主键，自增
  - `sender` - 发送者（user/ai）
  - `content` - 消息内容
  - `session_id` - 会话ID（用于记忆功能）
  - `created_at` - 创建时间

## ⚠️ 注意事项

1. 确保MySQL服务正在运行
2. 确保端口3307可访问
3. 确保用户名和密码正确
4. 确保有创建数据库的权限

## 🐛 故障排除

### 连接失败
- 检查MySQL服务是否运行
- 检查端口号是否正确
- 检查防火墙设置

### 权限错误
- 确保用户有创建数据库的权限
- 确保用户有创建表的权限

### 字符集问题
- 确保使用utf8mb4字符集
- 确保排序规则正确

现在您的聊天机器人将使用数据库存储消息和记忆功能！
