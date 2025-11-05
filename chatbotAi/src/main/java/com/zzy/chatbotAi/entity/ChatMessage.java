package com.zzy.chatbotAi.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "chat_message")
public class ChatMessage {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(name = "role", nullable = false, length = 20)
    private String role; // 'user' or 'assistant'
    
    @Column(name = "content", columnDefinition = "TEXT")
    private String content;
    
    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "chat_history_id", nullable = false)
    private ChatHistory chatHistory;
    
    // 构造函数
    public ChatMessage() {}
    
    public ChatMessage(String role, String content, ChatHistory chatHistory) {
        this.role = role;
        this.content = content;
        this.chatHistory = chatHistory;
        this.createdAt = LocalDateTime.now();
    }
    
    // Getters and Setters
    public Long getId() {
        return id;
    }
    
    public void setId(Long id) {
        this.id = id;
    }
    
    public String getRole() {
        return role;
    }
    
    public void setRole(String role) {
        this.role = role;
    }
    
    public String getContent() {
        return content;
    }
    
    public void setContent(String content) {
        this.content = content;
    }
    
    public LocalDateTime getCreatedAt() {
        return createdAt;
    }
    
    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }
    
    public ChatHistory getChatHistory() {
        return chatHistory;
    }
    
    public void setChatHistory(ChatHistory chatHistory) {
        this.chatHistory = chatHistory;
    }
}
