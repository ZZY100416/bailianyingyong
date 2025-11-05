package com.zzy.chatbotAi.dto;

public class RegisterRequest {
    private String username;
    private String email;
    private String password;
    private String nickname;
    
    public RegisterRequest() {}
    
    public RegisterRequest(String username, String email, String password, String nickname) {
        this.username = username;
        this.email = email;
        this.password = password;
        this.nickname = nickname;
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
    
    public String getPassword() {
        return password;
    }
    
    public void setPassword(String password) {
        this.password = password;
    }
    
    public String getNickname() {
        return nickname;
    }
    
    public void setNickname(String nickname) {
        this.nickname = nickname;
    }
}
