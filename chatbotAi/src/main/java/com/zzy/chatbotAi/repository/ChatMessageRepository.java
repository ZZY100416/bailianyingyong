package com.zzy.chatbotAi.repository;

import com.zzy.chatbotAi.entity.ChatMessage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ChatMessageRepository extends JpaRepository<ChatMessage, Long> {
    
    @Query("SELECT cm FROM ChatMessage cm WHERE cm.chatHistory.id = :chatHistoryId ORDER BY cm.createdAt ASC")
    List<ChatMessage> findByChatHistoryIdOrderByCreatedAtAsc(@Param("chatHistoryId") Long chatHistoryId);
    
    @Query("SELECT cm FROM ChatMessage cm WHERE cm.chatHistory.id = :chatHistoryId AND cm.role = :role ORDER BY cm.createdAt ASC")
    List<ChatMessage> findByChatHistoryIdAndRoleOrderByCreatedAtAsc(@Param("chatHistoryId") Long chatHistoryId, @Param("role") String role);
    
    @Modifying
    @Query("DELETE FROM ChatMessage cm WHERE cm.chatHistory.id = :chatHistoryId")
    void deleteByChatHistoryId(@Param("chatHistoryId") Long chatHistoryId);
}
