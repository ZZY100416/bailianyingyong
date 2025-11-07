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
         /**
     * OpenRASP 专用测试端点 - SQL注入测试
     */
    @GetMapping("/test-rasp-sql")
    public ResponseEntity<String> testRASPSQL(@RequestParam String input) {
        // 模拟SQL注入漏洞场景
        System.out.println("RASP SQL测试 - 输入: " + input);
        
        // 这里应该触发SQL注入检测
        String simulatedQuery = "SELECT * FROM users WHERE name = '" + input + "'";
        System.out.println("模拟SQL查询: " + simulatedQuery);
        
        return ResponseEntity.ok("SQL测试完成，输入: " + input);
    }

    /**
     * OpenRASP 专用测试端点 - XSS测试
     */
    @GetMapping("/test-rasp-xss")  
    public ResponseEntity<String> testRASPXSS(@RequestParam String input) {
        // 模拟XSS漏洞 - 直接返回用户输入
        System.out.println("RASP XSS测试 - 输入: " + input);
        
        // 这里应该触发XSS检测
        String response = "<div>用户输入: " + input + "</div>";
        return ResponseEntity.ok(response);
    }

    /**
     * OpenRASP 专用测试端点 - 命令执行测试
     */
    @GetMapping("/test-rasp-command")
    public ResponseEntity<String> testRASPCommand(@RequestParam String input) {
        // 模拟命令执行漏洞
        System.out.println("RASP 命令测试 - 输入: " + input);
        
        try {
            // 这里应该触发命令注入检测
            // 使用安全的命令执行方式
            String[] cmd = {"echo", "测试: " + input};
            Process process = Runtime.getRuntime().exec(cmd);
            process.waitFor();
        } catch (Exception e) {
            System.out.println("命令执行异常: " + e.getMessage());
        }
        
        return ResponseEntity.ok("命令测试完成，输入: " + input);
    }

    /**
     * OpenRASP 专用测试端点 - 文件读取测试
     */
    @GetMapping("/test-rasp-file")
    public ResponseEntity<String> testRASPFile(@RequestParam String filename) {
        // 模拟文件读取漏洞
        System.out.println("RASP 文件测试 - 文件名: " + filename);
        
        try {
            // 这里应该触发文件读取检测
            java.nio.file.Path path = java.nio.file.Paths.get(filename);
            if (java.nio.file.Files.exists(path)) {
                String content = new String(java.nio.file.Files.readAllBytes(path));
                return ResponseEntity.ok("文件内容: " + content.substring(0, Math.min(50, content.length())));
            } else {
                return ResponseEntity.ok("文件不存在: " + filename);
            }
        } catch (Exception e) {
            return ResponseEntity.ok("文件读取异常: " + e.getMessage());
        }
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
