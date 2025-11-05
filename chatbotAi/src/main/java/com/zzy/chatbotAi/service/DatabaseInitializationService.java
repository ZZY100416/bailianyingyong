package com.zzy.chatbotAi.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import jakarta.annotation.PostConstruct;

@Service
public class DatabaseInitializationService {
    
    @Autowired
    private JdbcTemplate jdbcTemplate;
    
    @PostConstruct
    public void initializeDatabase() {
        try {
            System.out.println("🔧 Initializing database tables...");
            
            // 创建聊天历史表
            String createChatHistoryTable = """
                CREATE TABLE IF NOT EXISTS chat_history (
                    id BIGINT AUTO_INCREMENT PRIMARY KEY,
                    title VARCHAR(200) NOT NULL,
                    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                    INDEX idx_created_at (created_at)
                ) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci
                """;
            
            jdbcTemplate.execute(createChatHistoryTable);
            System.out.println("✅ Created chat_history table");
            
            // 创建聊天消息表
            String createChatMessageTable = """
                CREATE TABLE IF NOT EXISTS chat_message (
                    id BIGINT AUTO_INCREMENT PRIMARY KEY,
                    role VARCHAR(20) NOT NULL,
                    content TEXT,
                    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                    chat_history_id BIGINT NOT NULL,
                    FOREIGN KEY (chat_history_id) REFERENCES chat_history(id) ON DELETE CASCADE,
                    INDEX idx_chat_history_id (chat_history_id),
                    INDEX idx_created_at (created_at)
                ) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci
                """;
            
            jdbcTemplate.execute(createChatMessageTable);
            System.out.println("✅ Created chat_message table");
            
            System.out.println("🎉 Database initialization completed successfully!");
            
        } catch (Exception e) {
            System.err.println("❌ Database initialization failed: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
