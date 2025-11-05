package com.zzy.chatbotAi.service;

import com.zzy.chatbotAi.dto.AuthResponse;
import com.zzy.chatbotAi.dto.LoginRequest;
import com.zzy.chatbotAi.dto.RegisterRequest;
import com.zzy.chatbotAi.dto.UserDto;
import com.zzy.chatbotAi.entity.User;
import com.zzy.chatbotAi.repository.UserRepository;
import com.zzy.chatbotAi.util.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@Transactional
public class UserService {
    
    @Autowired
    private UserRepository userRepository;
    
    @Autowired
    private PasswordEncoder passwordEncoder;
    
    @Autowired
    private JwtUtil jwtUtil;
    
    /**
     * 用户注册
     */
    public AuthResponse register(RegisterRequest request) {
        // 检查用户名是否已存在
        if (userRepository.existsByUsername(request.getUsername())) {
            throw new RuntimeException("用户名已存在");
        }
        
        // 检查邮箱是否已存在
        if (userRepository.existsByEmail(request.getEmail())) {
            throw new RuntimeException("邮箱已存在");
        }
        
        // 创建新用户
        User user = new User();
        user.setUsername(request.getUsername());
        user.setEmail(request.getEmail());
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setNickname(request.getNickname() != null ? request.getNickname() : request.getUsername());
        
        user = userRepository.save(user);
        
        // 生成JWT token
        String token = jwtUtil.generateToken(user.getUsername());
        
        // 转换为DTO
        UserDto userDto = convertToDto(user);
        
        return new AuthResponse(token, userDto, "注册成功");
    }
    
    /**
     * 用户登录
     */
    public AuthResponse login(LoginRequest request) {
        // 查找用户
        Optional<User> userOpt = userRepository.findByUsernameOrEmail(request.getUsername(), request.getUsername());
        if (userOpt.isEmpty()) {
            throw new RuntimeException("用户名或密码错误");
        }
        
        User user = userOpt.get();
        
        // 验证密码
        if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            throw new RuntimeException("用户名或密码错误");
        }
        
        // 生成JWT token
        String token = jwtUtil.generateToken(user.getUsername());
        
        // 转换为DTO
        UserDto userDto = convertToDto(user);
        
        return new AuthResponse(token, userDto, "登录成功");
    }
    
    /**
     * 根据用户名获取用户信息
     */
    public UserDto getUserByUsername(String username) {
        Optional<User> userOpt = userRepository.findByUsername(username);
        if (userOpt.isEmpty()) {
            throw new RuntimeException("用户不存在");
        }
        
        return convertToDto(userOpt.get());
    }
    
    /**
     * 验证token并获取用户信息
     */
    public UserDto validateTokenAndGetUser(String token) {
        try {
            String username = jwtUtil.extractUsername(token);
            return getUserByUsername(username);
        } catch (Exception e) {
            throw new RuntimeException("Token无效");
        }
    }
    
    /**
     * 转换为DTO
     */
    private UserDto convertToDto(User user) {
        return new UserDto(
            user.getId(),
            user.getUsername(),
            user.getEmail(),
            user.getNickname(),
            user.getAvatarUrl(),
            user.getCreatedAt()
        );
    }
}
