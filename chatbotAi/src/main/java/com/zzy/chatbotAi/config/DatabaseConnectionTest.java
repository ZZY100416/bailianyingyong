package com.zzy.chatbotAi.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

@Component
public class DatabaseConnectionTest implements CommandLineRunner {
    
    @Autowired
    private JdbcTemplate jdbcTemplate;
    
    @Override
    public void run(String... args) throws Exception {
        try {
            // 测试数据库连接
            String result = jdbcTemplate.queryForObject("SELECT 'Database connection successful!' as message", String.class);
            System.out.println("✅ " + result);
            
            // 检查数据库版本
            String version = jdbcTemplate.queryForObject("SELECT VERSION()", String.class);
            System.out.println("📊 MySQL Version: " + version);
            
            // 检查当前数据库
            String database = jdbcTemplate.queryForObject("SELECT DATABASE()", String.class);
            System.out.println("🗄️ Current Database: " + database);
            
        } catch (Exception e) {
            System.err.println("❌ Database connection failed: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
