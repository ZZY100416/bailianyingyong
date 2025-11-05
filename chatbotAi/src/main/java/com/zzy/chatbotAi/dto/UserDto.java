package com.zzy.chatbotAi.dto;

import java.time.LocalDateTime;

public class UserDto {
    private Long id;
    private String username;
    private String email;
    private String nickname;
    private String avatarUrl;
    private LocalDateTime createdAt;
    
    // 构造函数
    public UserDto() {}
    
    public UserDto(Long id, String username, String email, String nickname, String avatarUrl, LocalDateTime createdAt) {
        this.id = id;
        this.username = username;
        this.email = email;
        this.nickname = nickname;
        this.avatarUrl = avatarUrl;
        this.createdAt = createdAt;
    }
    
    // Getters and Setters
    public Long getId() {
        return id;
    }
    
    public void setId(Long id) {
        this.id = id;
    }
    
    public String getUsername() {
        return username;
    }
    
    public void setUsername(String username) {
        this.username = username;
    }
    
    public String getEmail() {
        return email;
    }
    
    public void setEmail(String email) {
        this.email = email;
    }
    
    public String getNickname() {
        return nickname;
    }
    
    public void setNickname(String nickname) {
        this.nickname = nickname;
    }
    
    public String getAvatarUrl() {
        return avatarUrl;
    }
    
    public void setAvatarUrl(String avatarUrl) {
        this.avatarUrl = avatarUrl;
    }
    
    public LocalDateTime getCreatedAt() {
        return createdAt;
    }
    
    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }
}
