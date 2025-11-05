package com.zzy.chatbotAi.dto;

import java.time.LocalDateTime;

public class ChatMessageDto {
    private Long id;
    private String role;
    private String content;
    private LocalDateTime createdAt;
    
    // 构造函数
    public ChatMessageDto() {}
    
    public ChatMessageDto(String role, String content) {
        this.role = role;
        this.content = content;
    }
    
    public ChatMessageDto(Long id, String role, String content, LocalDateTime createdAt) {
        this.id = id;
        this.role = role;
        this.content = content;
        this.createdAt = createdAt;
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
}
