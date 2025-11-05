package com.zzy.chatbotAi.repository;

import com.zzy.chatbotAi.entity.ChatHistory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ChatHistoryRepository extends JpaRepository<ChatHistory, Long> {
    
    @Query("SELECT ch FROM ChatHistory ch WHERE ch.user.id = :userId ORDER BY ch.createdAt DESC")
    List<ChatHistory> findByUserIdOrderByCreatedAtDesc(@Param("userId") Long userId);
    
    @Query("SELECT ch FROM ChatHistory ch WHERE ch.id = :id AND ch.user.id = :userId")
    Optional<ChatHistory> findByIdAndUserId(@Param("id") Long id, @Param("userId") Long userId);
    
    @Query("SELECT ch FROM ChatHistory ch WHERE ch.user.id = :userId AND ch.title LIKE %:keyword% ORDER BY ch.createdAt DESC")
    List<ChatHistory> findByUserIdAndTitleContainingOrderByCreatedAtDesc(@Param("userId") Long userId, @Param("keyword") String keyword);
}
