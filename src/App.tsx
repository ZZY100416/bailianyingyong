import React, { useState, useEffect, useRef } from 'react'
import { ThemeProvider } from './contexts/ThemeContext'
import { AuthProvider, useAuth } from './contexts/AuthContext'
import ThemeToggle from './components/ThemeToggle'
import AuthModal from './components/AuthModal'
import UserInfo from './components/UserInfo'

import './App.css'
import { dashscopeAiAdapter } from './apdater/dashscope'

type ChatMessage = { role: 'user' | 'assistant'; content: string }
type ChatHistory = { id: string; title: string; messages: ChatMessage[]; timestamp: Date }

function DashscopeChatDemo({ 
  chatHistories, 
  setChatHistories, 
  currentChatId, 
  setCurrentChatId,
  token
}: {
  chatHistories: ChatHistory[]
  setChatHistories: React.Dispatch<React.SetStateAction<ChatHistory[]>>
  currentChatId: string | null
  setCurrentChatId: (id: string | null) => void
  token: string | null
}) {
  const [input, setInput] = useState("")
  const [messages, setMessages] = useState<ChatMessage[]>([])
  const [loading, setLoading] = useState(false)
  // 防止一次发送流程内重复保存/重复完成
  const hasSavedRef = useRef(false)

  // 当currentChatId变化时，加载对应的历史对话
  useEffect(() => {
    if (currentChatId) {
      const history = chatHistories.find(h => h.id === currentChatId)
      if (history) {
        setMessages(history.messages)
      }
    } else {
      // 新对话时清空消息
      setMessages([])
    }
    hasSavedRef.current = false; // Reset on chat change
  }, [currentChatId, chatHistories])

  // 保存对话到历史
  const saveChatToHistory = async (messages: ChatMessage[]): Promise<boolean> => {
    console.log('=== saveChatToHistory START ===')
    console.log('Messages:', messages)
    console.log('Token:', token ? 'present' : 'missing')
    console.log('Token value:', token)
    
    if (messages.length === 0 || !token) {
      console.log('Skipping save: no messages or no token')
      return false
    }
    
    const userMessage = messages.find(m => m.role === 'user')
    if (!userMessage) {
      console.log('Skipping save: no user message found')
      return false
    }
    
    // 检查是否已经存在相同的对话
    const existingHistory = chatHistories.find(h => 
      h.messages.length === messages.length &&
      h.messages.every((msg, index) => 
        msg.role === messages[index].role && 
        msg.content === messages[index].content
      )
    )
    
    if (existingHistory) {
      console.log('Found existing history, updating current chat ID')
      setCurrentChatId(existingHistory.id)
      return true
    }
    
    const title = userMessage.content.length > 20 
      ? userMessage.content.substring(0, 20) + '...' 
      : userMessage.content
    
    console.log('Preparing to save with title:', title)
    
    try {
      const requestBody = {
        title,
        messages: messages.map(msg => ({
          role: msg.role,
          content: msg.content
        }))
      }
      
      console.log('Request body:', requestBody)
      console.log('Sending request to:', 'http://localhost:9988/api/chat-history')
      
      const response = await fetch('http://localhost:9988/api/chat-history', {
        method: 'POST',
        headers: {
          'Content-Type': 'application/json',
          'Authorization': `Bearer ${token}`
        },
        body: JSON.stringify(requestBody)
      })
      
      console.log('Response status:', response.status)
      console.log('Response headers:', Object.fromEntries(response.headers.entries()))
      
      if (response.ok) {
        const savedHistory = await response.json()
        console.log('Saved history response:', savedHistory)
        
        const newHistory: ChatHistory = {
          id: savedHistory.id.toString(),
          title: savedHistory.title,
          messages: [...messages],
          timestamp: new Date(savedHistory.createdAt)
        }
        
        console.log('Creating new history object:', newHistory)
        setChatHistories((prev: ChatHistory[]) => {
          const updated = [newHistory, ...prev]
          console.log('Updated chat histories:', updated)
          return updated
        })
        setCurrentChatId(newHistory.id)
        console.log('=== saveChatToHistory SUCCESS ===')
        return true
      } else {
        const errorText = await response.text()
        console.error('Failed to save chat history:', response.status, response.statusText)
        console.error('Error response:', errorText)
        return false
      }
    } catch (error) {
      console.error('Error saving chat history:', error)
      return false
    }
  }

  // 监听新对话事件
  useEffect(() => {
    const handleStartNewChat = async () => {
      console.log('Handling start new chat event')
      // 如果有消息且没有保存，先保存当前对话
      if (messages.length > 0 && !hasSavedRef.current) {
        console.log('Saving current conversation before starting new one')
        // 等待保存完成
        const saved = await saveChatToHistory(messages)
        if (saved) {
          console.log('Current conversation saved successfully')
        } else {
          console.log('Failed to save current conversation')
        }
      }
      setMessages([])
      setInput("")
      hasSavedRef.current = false
    }

    window.addEventListener('startNewChat', handleStartNewChat)
    return () => window.removeEventListener('startNewChat', handleStartNewChat)
  }, [messages, saveChatToHistory])

  // 功能按钮点击处理
  const handleFeatureClick = (featureText: string) => {
    setInput(featureText)
  }

  async function send() {
    if (!input.trim() || loading) return

    const userText = input
    setInput("")
    // 本轮发送开始，重置保存标记
    hasSavedRef.current = false

    // 1) 追加用户消息
    const updatedMessages = [...messages, { role: 'user' as const, content: userText }]
    setMessages(updatedMessages)

    // 2) 追加一个空的助手消息占位，用于流式拼接
    const messagesWithEmptyAssistant = [...updatedMessages, { role: 'assistant' as const, content: '' }]
    setMessages(messagesWithEmptyAssistant)

    setLoading(true)
    const adapter = dashscopeAiAdapter()
    if (!adapter || !adapter.streamText) {
      setLoading(false)
      setMessages(prev => [...prev, { role: 'assistant', content: '适配器未初始化' }])
      return
    }
    await adapter.streamText(userText, {
      next: (chunk: string) => {
        setMessages(prev => {
          if (prev.length === 0) return prev
          const lastIdx = prev.length - 1
          const last = prev[lastIdx]
          if (last.role !== 'assistant') return prev
          const updated = [...prev]
          updated[lastIdx] = { ...last, content: last.content + chunk }
          return updated
        })
      },
      error: (err: any) => {
        setMessages(prev => [...prev, { role: 'assistant', content: `出错：${err?.message ?? String(err)}` }])
        setLoading(false)
      },
      complete: async () => {
        setLoading(false)
        // 对话完成后保存到历史记录
        setMessages(prev => {
          if (!hasSavedRef.current && prev.length > 0) {
            hasSavedRef.current = true
            console.log('Attempting to save chat history with messages:', prev)
            // 延迟保存，确保消息状态已更新
            setTimeout(async () => {
              const saved = await saveChatToHistory(prev)
              if (saved) {
                console.log('Chat history saved successfully after completion')
              } else {
                console.log('Failed to save chat history after completion')
              }
            }, 100)
          }
          return prev
        })
      },
    }, { aiChatProps: {} })
  }

  return (
    <div className="chat-wrapper">
      <div className="dialog">
        {messages.length === 0 ? (
          <div className="welcome-container">
            <div className="welcome-content">
              <div className="welcome-avatar">
                <div className="welcome-avatar-img">
                  <img src="/ai.png" alt="AI Assistant" />
                </div>
              </div>
              <div className="welcome-text">
                <h3>小红书爆款文案助手</h3>
                <p>你好！我是你的专属文案创作伙伴</p>
                <div className="welcome-features">
                  <div className="feature-item" onClick={() => handleFeatureClick("帮我写一段小红书爆款文案，要求吸引眼球，容易传播")}>
                    ✨ 3秒生成高赞文案
                  </div>
                  <div className="feature-item" onClick={() => handleFeatureClick("帮我仿写/润色这段文案，让它更有小红书风格")}>
                    🎯 仿写/润色/原创
                  </div>
                  <div className="feature-item" onClick={() => handleFeatureClick("帮我优化这个标题，让它更容易成为爆款")}>
                    🔥 爆款标题优化
                  </div>
                  <div className="feature-item" onClick={() => handleFeatureClick("帮我写一段符合小红书风格的文案，包含热门标签和话题")}>
                    📝 小红书风格适配
                  </div>
                </div>
                <p className="welcome-tip">试试问我："帮我写一段苏州景点推荐的小红书文案"</p>
              </div>
            </div>
          </div>
        ) : (
          <>
            {messages.map((m, i) => (
              <div key={i} className={`msg ${m.role === 'user' ? 'msg-user' : 'msg-assistant'}`}>
                <div className="msg-avatar">
                  {m.role === 'user' ? (
                    <img 
                      src="/user-avatar.png" 
                      alt="用户头像"
                      className="avatar-img"
                    />
                  ) : (
                    <img 
                      src="/ai.png" 
                      alt="AI头像"
                      className="avatar-img"
                    />
                  )}
                </div>
                <div className="msg-bubble">
                  {m.content === '' && loading ? (
                    <div className="loading-content">
                      <div className="loading-spinner"></div>
                      <span className="loading-text">AI正在思考中...</span>
                    </div>
                  ) : (
                    m.content
                  )}
                </div>
              </div>
            ))}
          </>
        )}
      </div>

      <div className="chat-toolbar">
        <div className="chat-input-bar" style={{ flex: 1 }}>
          <input
            className="chat-input"
            value={input}
            onChange={(e) => setInput(e.target.value)}
            placeholder="输入你的问题，例如：帮我写一段小红书推广文案"
            onKeyDown={(e) => { if (e.key === 'Enter') send() }}
          />
          <button className="send-btn" onClick={send} disabled={loading}>
            {loading ? '发送中…' : '发送'}
          </button>
        </div>
      </div>
    </div>
  )
}

function AppContent() {
  const [chatHistories, setChatHistories] = useState<ChatHistory[]>([])
  const [currentChatId, setCurrentChatId] = useState<string | null>(null)
  const [showAuthModal, setShowAuthModal] = useState(false)
  const { isAuthenticated, token } = useAuth()

  // 从后端加载历史对话
  const loadChatHistoriesFromBackend = async () => {
    if (!isAuthenticated || !token) {
      console.log('Not authenticated or no token, skipping history load')
      return
    }
    
    console.log('Loading chat histories from backend...')
    try {
      const response = await fetch('http://localhost:9988/api/chat-history', {
        headers: {
          'Authorization': `Bearer ${token}`
        }
      })
      
      console.log('History API response status:', response.status)
      
      if (response.ok) {
        const histories = await response.json()
        console.log('Loaded histories from backend:', histories)
        // 转换后端数据格式到前端格式
        const convertedHistories: ChatHistory[] = histories.map((h: any) => ({
          id: h.id.toString(),
          title: h.title,
          messages: h.messages || [],
          timestamp: new Date(h.createdAt)
        }))
        console.log('Converted histories:', convertedHistories)
        setChatHistories(convertedHistories)
      } else {
        console.error('Failed to load histories, status:', response.status)
      }
    } catch (error) {
      console.error('Failed to load chat histories:', error)
    }
  }

  // 当用户登录状态变化时，加载历史对话
  useEffect(() => {
    console.log('=== Auth state changed ===')
    console.log('isAuthenticated:', isAuthenticated)
    console.log('token:', token ? 'present' : 'missing')
    
    if (isAuthenticated) {
      console.log('User is authenticated, loading chat histories...')
      loadChatHistoriesFromBackend()
    } else {
      console.log('User not authenticated, clearing chat histories')
      setChatHistories([])
      setCurrentChatId(null)
    }
  }, [isAuthenticated, token])

  // 开始新对话
  const startNewChat = () => {
    console.log('Starting new chat...')
    setCurrentChatId(null)
    // 通知聊天组件清空消息
    window.dispatchEvent(new CustomEvent('startNewChat'))
    console.log('New chat started')
  }

  // 加载历史对话
  const loadChatHistory = (chatId: string) => {
    setCurrentChatId(chatId)
  }

  // 删除历史对话
  const deleteChatHistory = async (chatId: string, event: React.MouseEvent) => {
    event.stopPropagation() // 阻止事件冒泡，避免触发加载历史对话
    
    if (!token) {
      console.error('No token available for deletion')
      return
    }

    if (!confirm('确定要删除这条历史对话吗？')) {
      return
    }

    try {
      const response = await fetch(`http://localhost:9988/api/chat-history/${chatId}`, {
        method: 'DELETE',
        headers: {
          'Authorization': `Bearer ${token}`
        }
      })

      if (response.ok) {
        console.log('History deleted successfully')
        // 从本地状态中移除删除的历史对话
        setChatHistories(prev => prev.filter(h => h.id !== chatId))
        
        // 如果删除的是当前对话，清空当前对话
        if (currentChatId === chatId) {
          setCurrentChatId(null)
          // 通知聊天组件清空消息
          window.dispatchEvent(new CustomEvent('startNewChat'))
        }
      } else {
        console.error('Failed to delete history:', response.status)
        alert('删除失败，请重试')
      }
    } catch (error) {
      console.error('Error deleting history:', error)
      alert('删除失败，请重试')
    }
  }

  // 处理登录按钮点击
  const handleLoginClick = () => {
    setShowAuthModal(true)
  }

  return (
      <div className="app">
        <header className="app-header">
          <h1>小红书爆款文案助手</h1>
        </header>
        <main className="app-main">
        {/* 左侧边栏 - 历史消息 */}
        <div className="sidebar">
          <div className="sidebar-header">
            <h3>💬 历史对话</h3>
            <button className="new-chat-btn" onClick={startNewChat}>
              ➕ 新对话
            </button>
          </div>
          <div className="sidebar-content">
            {chatHistories.length === 0 ? (
              <div className="empty-history">
                <p>暂无历史对话</p>
                <p className="empty-tip">开始新的对话吧！</p>
              </div>
            ) : (
              <div className="history-list">
                {chatHistories.map((history) => (
                  <div 
                    key={history.id} 
                    className={`history-item ${currentChatId === history.id ? 'active' : ''}`}
                    onClick={() => loadChatHistory(history.id)}
                  >
                    <div className="history-content">
                      <div className="history-title">{history.title}</div>
                      <div className="history-time">
                        {history.timestamp.toLocaleDateString()} {history.timestamp.toLocaleTimeString()}
                      </div>
                    </div>
                    <button 
                      className="delete-history-btn"
                      onClick={(e) => deleteChatHistory(history.id, e)}
                      title="删除此对话"
                    >
                      🗑️
                    </button>
                  </div>
                ))}
              </div>
            )}
          </div>
          
          {/* 侧边栏底部认证区域 */}
          <div className="sidebar-footer">
            {isAuthenticated ? (
              <UserInfo />
            ) : (
              <button className="sidebar-login-btn" onClick={handleLoginClick}>
                🔐 登录
              </button>
            )}
          </div>
        </div>
        
          {/* 聊天容器 */}
          <div className="chat-container">
          <DashscopeChatDemo 
            chatHistories={chatHistories}
            setChatHistories={setChatHistories}
            currentChatId={currentChatId}
            setCurrentChatId={setCurrentChatId}
            token={token}
          />
          </div>
        </main>
        
        {/* 主题切换按钮 */}
        <ThemeToggle />
      
      {/* 认证模态框 */}
      <AuthModal 
        isOpen={showAuthModal} 
        onClose={() => setShowAuthModal(false)} 
      />
      </div>
  )
}

function App() {
  return (
    <ThemeProvider>
      <AuthProvider>
        <AppContent />
      </AuthProvider>
    </ThemeProvider>
  )
}

export default App
