# 百炼智能体应用集成指南

基于 [百炼控制台](https://bailian.console.aliyun.com/?tab=doc#/doc/?type=app&url=2974821) 的官方文档，本指南将帮助您正确集成百炼智能体应用。

## 1. 百炼平台配置

### 1.1 获取必要信息
在百炼控制台中，您需要获取以下信息：
- **API Key**: 用于身份验证
- **App ID**: 您的智能体应用ID
- **模型配置**: 选择合适的模型（如qwen-turbo, qwen-plus等）

### 1.2 应用配置
```properties
# 百炼平台配置
spring.ai.dashscope.apikey=your-api-key-here
spring.ai.dashscope.agent.app-id=your-app-id-here
spring.ai.dashscope.chat.options.model=qwen-turbo
spring.ai.dashscope.chat.options.temperature=0.7
spring.ai.dashscope.chat.options.max-tokens=2000
```

## 2. 技术实现

### 2.1 依赖配置
确保在 `pom.xml` 中包含正确的依赖：

```xml
<!-- Spring AI DashScope -->
<dependency>
    <groupId>org.springframework.ai</groupId>
    <artifactId>spring-ai-dashscope-spring-boot-starter</artifactId>
    <version>${spring-ai.version}</version>
</dependency>
```

### 2.2 智能体服务实现
使用百炼平台的Agent API进行智能体调用：

```java
@Service
public class BailianAgentService {
    
    @Autowired
    private DashScopeAgentApi dashScopeAgentApi;
    
    @Value("${spring.ai.dashscope.agent.app-id}")
    private String appId;
    
    public Flux<String> streamAgent(String query) {
        // 使用百炼智能体API进行流式调用
        return dashScopeAgentApi.streamAgent(appId, query);
    }
}
```

## 3. API接口设计

### 3.1 流式对话接口
```http
POST /bailian/agent/stream
Content-Type: application/json

{
    "query": "用户问题"
}
```

### 3.2 响应格式
- **流式响应**: 实时返回AI生成的文本片段
- **错误处理**: 统一的错误响应格式
- **超时控制**: 合理的请求超时设置

## 4. 前端集成

### 4.1 流式数据处理
```javascript
async function callBailianAgent(query) {
    const response = await fetch('/bailian/agent/stream', {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json'
        },
        body: JSON.stringify({ query })
    });
    
    const reader = response.body.getReader();
    const decoder = new TextDecoder();
    
    while (true) {
        const { done, value } = await reader.read();
        if (done) break;
        
        const chunk = decoder.decode(value);
        // 处理流式数据
        displayChunk(chunk);
    }
}
```

## 5. 最佳实践

### 5.1 错误处理
- 实现重试机制
- 优雅的错误提示
- 日志记录和监控

### 5.2 性能优化
- 连接池管理
- 请求限流
- 缓存策略

### 5.3 安全考虑
- API密钥保护
- 请求验证
- 访问控制

## 6. 测试和调试

### 6.1 本地测试
使用提供的测试页面验证功能：
- 健康检查
- 流式API测试
- 错误处理测试

### 6.2 生产环境
- 监控和告警
- 性能指标
- 用户反馈

## 7. 常见问题

### 7.1 认证问题
- 检查API Key是否正确
- 确认App ID是否有效
- 验证权限设置

### 7.2 网络问题
- 检查网络连接
- 确认防火墙设置
- 验证代理配置

### 7.3 性能问题
- 调整超时设置
- 优化请求频率
- 监控资源使用

## 8. 参考资源

- [百炼控制台](https://bailian.console.aliyun.com/?tab=doc#/doc/?type=app&url=2974821)
- [Spring AI 官方文档](https://spring.io/projects/spring-ai)
- [DashScope API 文档](https://help.aliyun.com/zh/dashscope/)
