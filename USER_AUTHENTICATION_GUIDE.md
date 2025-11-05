# 用户注册登录和历史记录持久化指南

## 问题解决

### 原问题
- 页面刷新后历史记录消失
- 需要用户注册登录功能
- 历史记录需要持久化保存

### 解决方案
✅ **完整的用户认证系统**
✅ **数据库持久化存储**
✅ **JWT Token会话管理**
✅ **用户隔离的历史记录**

## 后端功能

### 1. 数据库结构
```sql
-- 用户表
users (id, username, email, password, nickname, avatar_url, created_at, updated_at)

-- 聊天历史表 (关联用户)
chat_history (id, user_id, title, created_at, updated_at)

-- 聊天消息表
chat_message (id, chat_history_id, role, content, created_at)
```

### 2. API接口

#### 认证接口
- `POST /api/auth/register` - 用户注册
- `POST /api/auth/login` - 用户登录
- `GET /api/auth/me` - 获取当前用户信息

#### 历史记录接口 (需要认证)
- `GET /api/chat-history` - 获取用户历史对话
- `GET /api/chat-history/{id}` - 获取特定历史对话
- `POST /api/chat-history` - 保存新对话
- `PUT /api/chat-history/{id}` - 更新对话
- `DELETE /api/chat-history/{id}` - 删除对话
- `GET /api/chat-history/search?keyword=关键词` - 搜索对话

### 3. 安全特性
- **密码加密**: BCrypt加密存储
- **JWT Token**: 无状态认证
- **用户隔离**: 每个用户只能访问自己的历史记录
- **CORS支持**: 跨域请求支持

## 前端集成指南

### 1. 添加认证状态管理
```typescript
// 在App.tsx中添加
const [user, setUser] = useState(null)
const [token, setToken] = useState(localStorage.getItem('token'))
const [isAuthenticated, setIsAuthenticated] = useState(!!token)
```

### 2. 登录注册组件
```tsx
// 登录表单
const LoginForm = () => {
  const [username, setUsername] = useState('')
  const [password, setPassword] = useState('')
  
  const handleLogin = async () => {
    const response = await fetch('/api/auth/login', {
      method: 'POST',
      headers: { 'Content-Type': 'application/json' },
      body: JSON.stringify({ username, password })
    })
    const data = await response.json()
    if (data.token) {
      localStorage.setItem('token', data.token)
      setToken(data.token)
      setIsAuthenticated(true)
    }
  }
}
```

### 3. API请求添加认证头
```typescript
// 获取历史记录
const fetchChatHistories = async () => {
  const response = await fetch('/api/chat-history', {
    headers: {
      'Authorization': `Bearer ${token}`
    }
  })
  return response.json()
}
```

### 4. 保存历史记录到后端
```typescript
// 保存对话
const saveChatHistory = async (messages) => {
  const response = await fetch('/api/chat-history', {
    method: 'POST',
    headers: {
      'Content-Type': 'application/json',
      'Authorization': `Bearer ${token}`
    },
    body: JSON.stringify({
      title: messages[0]?.content?.substring(0, 20) || '新对话',
      messages: messages
    })
  })
  return response.json()
}
```

## 启动步骤

### 1. 更新数据库
```bash
# 运行新的数据库初始化脚本
mysql -u root -p -P 3307 < chatbotAi/database_init.sql
```

### 2. 启动后端
```bash
cd chatbotAi
mvn clean install
mvn spring-boot:run
```

### 3. 测试API
```bash
# 注册用户
curl -X POST http://localhost:9988/api/auth/register \
  -H "Content-Type: application/json" \
  -d '{"username":"testuser","email":"test@example.com","password":"123456","nickname":"测试用户"}'

# 登录
curl -X POST http://localhost:9988/api/auth/login \
  -H "Content-Type: application/json" \
  -d '{"username":"testuser","password":"123456"}'

# 获取历史记录 (需要token)
curl -X GET http://localhost:9988/api/chat-history \
  -H "Authorization: Bearer YOUR_TOKEN"
```

## 功能特性

### ✅ 已实现
- 用户注册/登录
- JWT Token认证
- 密码加密存储
- 用户隔离的历史记录
- 数据库持久化
- 跨域支持
- 搜索功能

### 🔄 前端待集成
- 登录注册界面
- Token管理
- 认证状态管理
- API请求认证头
- 历史记录同步

## 安全说明

1. **密码安全**: 使用BCrypt加密，不可逆
2. **Token安全**: JWT Token包含用户信息，24小时过期
3. **数据隔离**: 用户只能访问自己的历史记录
4. **SQL注入防护**: 使用JPA参数化查询
5. **CORS配置**: 允许跨域请求

现在历史记录会永久保存到数据库中，用户登录后可以查看所有历史对话，页面刷新不会丢失数据！
