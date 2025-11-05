package com.zzy.chatbotAi.dto;

import java.time.LocalDateTime;
import java.util.List;

public class ChatHistoryDto {
    private Long id;
    private String title;
    private LocalDateTime createdAt;
    private List<ChatMessageDto> messages;
    
    // 构造函数
    public ChatHistoryDto() {}
    
    public ChatHistoryDto(Long id, String title, LocalDateTime createdAt, List<ChatMessageDto> messages) {
        this.id = id;
        this.title = title;
        this.createdAt = createdAt;
        this.messages = messages;
    }
    
    // Getters and Setters
    public Long getId() {
        return id;
    }
    
    public void setId(Long id) {
        this.id = id;
    }
    
    public String getTitle() {
        return title;
    }
    
    public void setTitle(String title) {
        this.title = title;
    }
    
    public LocalDateTime getCreatedAt() {
        return createdAt;
    }
    
    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }
    
    public List<ChatMessageDto> getMessages() {
        return messages;
    }
    
    public void setMessages(List<ChatMessageDto> messages) {
        this.messages = messages;
    }
}
