package com.zzy.chatbotAi.util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseConnectionDiagnostic {
    
    public static void testConnection() {
        String url = "jdbc:mysql://localhost:3307/chatbotai_db?useUnicode=true&characterEncoding=utf8&useSSL=false&serverTimezone=Asia/Shanghai&allowPublicKeyRetrieval=true&useAffectedRows=true";
        String username = "root";
        String password = "root123";
        
        System.out.println("🔍 Testing database connection...");
        System.out.println("📍 URL: " + url);
        System.out.println("👤 Username: " + username);
        
        try {
            // 加载驱动
            Class.forName("com.mysql.cj.jdbc.Driver");
            System.out.println("✅ MySQL Driver loaded successfully");
            
            // 测试连接
            Connection connection = DriverManager.getConnection(url, username, password);
            System.out.println("✅ Database connection successful!");
            
            // 获取数据库信息
            String version = connection.getMetaData().getDatabaseProductVersion();
            String productName = connection.getMetaData().getDatabaseProductName();
            System.out.println("📊 Database: " + productName + " " + version);
            
            // 检查数据库是否存在
            String catalog = connection.getCatalog();
            System.out.println("🗄️ Current database: " + catalog);
            
            connection.close();
            System.out.println("🔒 Connection closed successfully");
            
        } catch (ClassNotFoundException e) {
            System.err.println("❌ MySQL Driver not found: " + e.getMessage());
        } catch (SQLException e) {
            System.err.println("❌ Database connection failed:");
            System.err.println("   Error Code: " + e.getErrorCode());
            System.err.println("   SQL State: " + e.getSQLState());
            System.err.println("   Message: " + e.getMessage());
            
            // 提供解决建议
            if (e.getMessage().contains("Public Key Retrieval is not allowed")) {
                System.err.println("\n💡 Solution: Add 'allowPublicKeyRetrieval=true' to connection URL");
            } else if (e.getMessage().contains("Access denied")) {
                System.err.println("\n💡 Solution: Check username and password");
            } else if (e.getMessage().contains("Unknown database")) {
                System.err.println("\n💡 Solution: Create database 'chatbotai_db' first");
            } else if (e.getMessage().contains("Connection refused")) {
                System.err.println("\n💡 Solution: Check if MySQL server is running on port 3307");
            }
        }
    }
    
    public static void main(String[] args) {
        testConnection();
    }
}
