package com.zzy.chatbotAi.controller;

import com.zzy.chatbotAi.dto.ChatHistoryDto;
import com.zzy.chatbotAi.dto.ChatMessageDto;
import com.zzy.chatbotAi.dto.UserDto;
import com.zzy.chatbotAi.service.ChatHistoryService;
import com.zzy.chatbotAi.service.UserService;
import com.zzy.chatbotAi.util.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/chat-history")
@CrossOrigin(origins = "*")
public class ChatHistoryController {
    
    @Autowired
    private ChatHistoryService chatHistoryService;
    
    @Autowired
    private UserService userService;
    
    @Autowired
    private JwtUtil jwtUtil;
    
    /**
     * 获取当前用户的所有历史对话
     */
    @GetMapping
    public ResponseEntity<List<ChatHistoryDto>> getAllChatHistories(@RequestHeader("Authorization") String token) {
        try {
            Long userId = getUserIdFromToken(token);
            List<ChatHistoryDto> histories = chatHistoryService.getAllChatHistoriesByUserId(userId);
            return ResponseEntity.ok(histories);
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }
    
    /**
     * 根据ID获取历史对话
     */
    @GetMapping("/{id}")
    public ResponseEntity<ChatHistoryDto> getChatHistoryById(@PathVariable Long id, @RequestHeader("Authorization") String token) {
        try {
            Long userId = getUserIdFromToken(token);
            ChatHistoryDto history = chatHistoryService.getChatHistoryById(id, userId);
            if (history == null) {
                return ResponseEntity.notFound().build();
            }
            return ResponseEntity.ok(history);
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }
    
    /**
     * 保存新的对话历史
     */
    @PostMapping
    public ResponseEntity<ChatHistoryDto> saveChatHistory(@RequestBody SaveChatHistoryRequest request, @RequestHeader("Authorization") String token) {
        System.out.println("=== 保存历史对话开始 ===");
        System.out.println("请求到达Controller");
        
        try {
            System.out.println("请求数据: " + request.getTitle() + ", 消息数量: " + (request.getMessages() != null ? request.getMessages().size() : 0));
            
            Long userId = getUserIdFromToken(token);
            System.out.println("用户ID: " + userId);
            
            ChatHistoryDto history = chatHistoryService.saveChatHistory(userId, request.getTitle(), request.getMessages());
            System.out.println("保存成功: " + history.getId());
            return ResponseEntity.ok(history);
        } catch (Exception e) {
            System.err.println("保存历史对话失败: " + e.getMessage());
            e.printStackTrace();
            return ResponseEntity.badRequest().body(null);
        }
    }
    
    /**
     * 更新现有对话历史
     */
    @PutMapping("/{id}")
    public ResponseEntity<ChatHistoryDto> updateChatHistory(@PathVariable Long id, @RequestBody UpdateChatHistoryRequest request, @RequestHeader("Authorization") String token) {
        try {
            Long userId = getUserIdFromToken(token);
            ChatHistoryDto history = chatHistoryService.updateChatHistory(id, userId, request.getMessages());
            if (history == null) {
                return ResponseEntity.notFound().build();
            }
            return ResponseEntity.ok(history);
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }
    
    /**
     * 删除对话历史
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteChatHistory(@PathVariable Long id, @RequestHeader("Authorization") String token) {
        try {
            Long userId = getUserIdFromToken(token);
            chatHistoryService.deleteChatHistory(id, userId);
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }
    
    /**
     * 搜索对话历史
     */
    @GetMapping("/search")
    public ResponseEntity<List<ChatHistoryDto>> searchChatHistories(@RequestParam String keyword, @RequestHeader("Authorization") String token) {
        try {
            Long userId = getUserIdFromToken(token);
            List<ChatHistoryDto> histories = chatHistoryService.searchChatHistories(userId, keyword);
            return ResponseEntity.ok(histories);
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }
    
    /**
     * 从token中获取用户ID
     */
    private Long getUserIdFromToken(String token) {
        if (token.startsWith("Bearer ")) {
            token = token.substring(7);
        }
        
        // 验证token是否有效
        if (!jwtUtil.validateToken(token, null)) {
            throw new RuntimeException("Invalid token");
        }
        
        String username = jwtUtil.extractUsername(token);
        UserDto user = userService.getUserByUsername(username);
        if (user == null) {
            throw new RuntimeException("User not found");
        }
        return user.getId();
    }
    
    // 内部类用于请求体
    public static class SaveChatHistoryRequest {
        private String title;
        private List<ChatMessageDto> messages;
        
        public String getTitle() {
            return title;
        }
        
        public void setTitle(String title) {
            this.title = title;
        }
        
        public List<ChatMessageDto> getMessages() {
            return messages;
        }
        
        public void setMessages(List<ChatMessageDto> messages) {
            this.messages = messages;
        }
    }
    
    public static class UpdateChatHistoryRequest {
        private List<ChatMessageDto> messages;
        
        public List<ChatMessageDto> getMessages() {
            return messages;
        }
        
        public void setMessages(List<ChatMessageDto> messages) {
            this.messages = messages;
        }
    }
}
