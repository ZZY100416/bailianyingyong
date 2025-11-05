# 数据库设置说明

## 问题描述

当前系统报错：`Unknown column 'session_id' in 'field list'`

这是因为数据库中的 `messages` 表还没有 `session_id` 字段。

## 解决方案

### 方法1：手动执行SQL（推荐）

1. 连接到您的MySQL数据库：
   - 主机：localhost
   - 端口：3307
   - 用户名：root
   - 密码：root123
   - 数据库：chatbot_db

2. 执行以下SQL语句：

```sql
-- 添加session_id字段
ALTER TABLE messages ADD COLUMN session_id VARCHAR(255);

-- 添加索引以提高查询性能
CREATE INDEX idx_messages_session_id ON messages(session_id);
CREATE INDEX idx_messages_created_at ON messages(created_at);
CREATE INDEX idx_messages_session_created ON messages(session_id, created_at);
```

### 方法2：使用MySQL Workbench

1. 打开MySQL Workbench
2. 连接到您的数据库
3. 在SQL编辑器中执行上述SQL语句
4. 点击执行按钮

### 方法3：使用命令行（如果已安装MySQL客户端）

```bash
mysql -u root -p -h localhost -P 3307 chatbot_db
```

然后执行上述SQL语句。

## 验证

执行完SQL后，可以通过以下方式验证：

```sql
-- 查看表结构
DESCRIBE messages;

-- 应该能看到session_id字段
```

## 临时解决方案

如果暂时无法修改数据库，系统已经添加了错误处理机制：

1. 消息仍然会保存，但不会包含session_id
2. 上下文功能暂时不可用
3. 系统会继续正常工作，只是没有记忆功能

## 重启服务

数据库修改完成后，重启Spring Boot应用：

1. 停止当前运行的应用
2. 重新启动应用
3. 测试聊天功能

现在应该可以正常使用记忆上下文功能了！
