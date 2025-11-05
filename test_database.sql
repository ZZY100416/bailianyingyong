-- 测试数据库连接和表结构
-- 请在MySQL中执行此脚本

-- 1. 查看当前数据库
SELECT DATABASE();

-- 2. 查看messages表结构
DESCRIBE messages;

-- 3. 检查是否存在session_id字段
SELECT COLUMN_NAME, DATA_TYPE, IS_NULLABLE 
FROM INFORMATION_SCHEMA.COLUMNS 
WHERE TABLE_SCHEMA = 'chatbot_db' 
AND TABLE_NAME = 'messages' 
AND COLUMN_NAME = 'session_id';

-- 4. 如果session_id字段不存在，执行以下语句添加
-- ALTER TABLE messages ADD COLUMN session_id VARCHAR(255);

-- 5. 添加索引（可选，但推荐）
-- CREATE INDEX idx_messages_session_id ON messages(session_id);
-- CREATE INDEX idx_messages_created_at ON messages(created_at);
-- CREATE INDEX idx_messages_session_created ON messages(session_id, created_at);

-- 6. 验证修改结果
DESCRIBE messages;
