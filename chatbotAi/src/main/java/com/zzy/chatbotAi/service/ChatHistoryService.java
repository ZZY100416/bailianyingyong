package com.zzy.chatbotAi.service;

import com.zzy.chatbotAi.dto.ChatHistoryDto;
import com.zzy.chatbotAi.dto.ChatMessageDto;
import com.zzy.chatbotAi.entity.ChatHistory;
import com.zzy.chatbotAi.entity.ChatMessage;
import com.zzy.chatbotAi.entity.User;
import com.zzy.chatbotAi.repository.ChatHistoryRepository;
import com.zzy.chatbotAi.repository.ChatMessageRepository;
import com.zzy.chatbotAi.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class ChatHistoryService {
    
    @Autowired
    private ChatHistoryRepository chatHistoryRepository;
    
    @Autowired
    private ChatMessageRepository chatMessageRepository;
    
    @Autowired
    private UserRepository userRepository;
    
    /**
     * 获取用户的所有历史对话
     */
    public List<ChatHistoryDto> getAllChatHistoriesByUserId(Long userId) {
        List<ChatHistory> histories = chatHistoryRepository.findByUserIdOrderByCreatedAtDesc(userId);
        return histories.stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }
    
    /**
     * 根据ID获取历史对话
     */
    public ChatHistoryDto getChatHistoryById(Long id, Long userId) {
        ChatHistory history = chatHistoryRepository.findByIdAndUserId(id, userId).orElse(null);
        if (history == null) {
            return null;
        }
        return convertToDto(history);
    }
    
    /**
     * 保存新的对话历史
     */
    public ChatHistoryDto saveChatHistory(Long userId, String title, List<ChatMessageDto> messages) {
        User user = userRepository.findById(userId).orElseThrow(() -> new RuntimeException("用户不存在"));
        
        ChatHistory history = new ChatHistory(title, user);
        history = chatHistoryRepository.save(history);
        
        // 保存消息
        for (ChatMessageDto messageDto : messages) {
            ChatMessage message = new ChatMessage(messageDto.getRole(), messageDto.getContent(), history);
            chatMessageRepository.save(message);
        }
        
        // 重新查询以确保messages被正确加载
        ChatHistory savedHistory = chatHistoryRepository.findById(history.getId()).orElse(history);
        return convertToDto(savedHistory);
    }
    
    /**
     * 更新现有对话历史
     */
    public ChatHistoryDto updateChatHistory(Long id, Long userId, List<ChatMessageDto> messages) {
        ChatHistory history = chatHistoryRepository.findByIdAndUserId(id, userId).orElse(null);
        if (history == null) {
            return null;
        }
        
        // 删除现有消息
        chatMessageRepository.deleteByChatHistoryId(id);
        
        // 保存新消息
        for (ChatMessageDto messageDto : messages) {
            ChatMessage message = new ChatMessage(messageDto.getRole(), messageDto.getContent(), history);
            chatMessageRepository.save(message);
        }
        
        return convertToDto(history);
    }
    
    /**
     * 删除对话历史
     */
    public void deleteChatHistory(Long id, Long userId) {
        ChatHistory history = chatHistoryRepository.findByIdAndUserId(id, userId).orElse(null);
        if (history != null) {
            chatHistoryRepository.deleteById(id);
        }
    }
    
    /**
     * 搜索对话历史
     */
    public List<ChatHistoryDto> searchChatHistories(Long userId, String keyword) {
        List<ChatHistory> histories = chatHistoryRepository.findByUserIdAndTitleContainingOrderByCreatedAtDesc(userId, keyword);
        return histories.stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }
    
    /**
     * 转换为DTO
     */
    private ChatHistoryDto convertToDto(ChatHistory history) {
        List<ChatMessageDto> messageDtos = new ArrayList<>();
        if (history.getMessages() != null) {
            messageDtos = history.getMessages().stream()
                    .map(msg -> new ChatMessageDto(msg.getId(), msg.getRole(), msg.getContent(), msg.getCreatedAt()))
                    .collect(Collectors.toList());
        }
        
        return new ChatHistoryDto(history.getId(), history.getTitle(), history.getCreatedAt(), messageDtos);
    }
}
