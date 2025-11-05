import React, { useState, useRef, useEffect } from 'react';
import { streamText } from '@alibaba-cloud/spring-ai-alibaba-starter-dashscope';

interface Message {
  role: 'user' | 'assistant';
  content: string;
}

interface DashscopeChatDemoProps {
  token?: string;
  onSaveHistory?: (messages: Message[]) => Promise<void>;
}

const DashscopeChatDemo: React.FC<DashscopeChatDemoProps> = ({ token, onSaveHistory }) => {
  const [input, setInput] = useState('');
  const [messages, setMessages] = useState<Message[]>([]);
  const [loading, setLoading] = useState(false);
  const hasSavedRef = useRef(false);

  const handleSend = async () => {
    if (!input.trim() || loading) return;

    const userMessage: Message = { role: 'user', content: input.trim() };
    const newMessages = [...messages, userMessage];
    setMessages(newMessages);
    setInput('');
    setLoading(true);
    hasSavedRef.current = false;

    try {
      const result = await streamText({
        model: 'qwen-turbo',
        messages: newMessages,
        onChunk: (chunk) => {
          setMessages(prev => {
            const lastMessage = prev[prev.length - 1];
            if (lastMessage && lastMessage.role === 'assistant') {
              return [...prev.slice(0, -1), { ...lastMessage, content: lastMessage.content + chunk.content }];
            } else {
              return [...prev, { role: 'assistant', content: chunk.content }];
            }
          });
        },
        onComplete: async (response) => {
          setLoading(false);
          if (onSaveHistory && !hasSavedRef.current) {
            hasSavedRef.current = true;
            const finalMessages = [...newMessages, { role: 'assistant', content: response.content }];
            await onSaveHistory(finalMessages);
          }
        },
        onError: (error) => {
          console.error('Stream error:', error);
          setLoading(false);
        }
      });
    } catch (error) {
      console.error('Error:', error);
      setLoading(false);
    }
  };

  const handleKeyDown = (e: React.KeyboardEvent) => {
    if (e.key === 'Enter' && !e.shiftKey) {
      e.preventDefault();
      handleSend();
    }
  };

  // 监听自定义事件
  useEffect(() => {
    const handleStartNewChat = () => {
      setMessages([]);
    };

    const handleLoadHistory = (event: CustomEvent) => {
      setMessages(event.detail.messages || []);
    };

    window.addEventListener('startNewChat', handleStartNewChat);
    window.addEventListener('loadHistory', handleLoadHistory as EventListener);

    return () => {
      window.removeEventListener('startNewChat', handleStartNewChat);
      window.removeEventListener('loadHistory', handleLoadHistory as EventListener);
    };
  }, []);

  return (
    <div className="chat-wrapper">
      <div className="dialog">
        {messages.length === 0 ? (
          <div className="welcome-container">
            <div className="welcome-content">
              <div className="welcome-avatar">
                <div className="welcome-avatar-img">🌿</div>
              </div>
              <div className="welcome-text">
                <h3>小红书爆款文案助手</h3>
                <p>我是你的专属文案创作助手，帮你打造爆款内容！</p>
                <div className="welcome-features">
                  <div className="feature-item" onClick={() => setInput('帮我写一个关于美食的小红书文案')}>
                    美食文案
                  </div>
                  <div className="feature-item" onClick={() => setInput('帮我写一个关于旅行的种草文案')}>
                    旅行种草
                  </div>
                  <div className="feature-item" onClick={() => setInput('帮我写一个关于护肤的分享文案')}>
                    护肤分享
                  </div>
                  <div className="feature-item" onClick={() => setInput('帮我写一个关于穿搭的推荐文案')}>
                    穿搭推荐
                  </div>
                </div>
                <div className="welcome-tip">
                  💡 点击上方功能按钮快速开始，或直接输入你的需求
                </div>
              </div>
            </div>
          </div>
        ) : (
          messages.map((message, index) => (
            <div key={index} className={`msg ${message.role === 'user' ? 'msg-user' : 'msg-assistant'}`}>
              <div className="msg-avatar">
                {message.role === 'user' ? (
                  <div className="avatar-img">👤</div>
                ) : (
                  <div className="avatar-img ai-avatar">🌿</div>
                )}
              </div>
              <div className="msg-bubble">
                {message.content}
              </div>
            </div>
          ))
        )}
        {loading && (
          <div className="msg msg-assistant">
            <div className="msg-avatar">
              <div className="avatar-img ai-avatar">🌿</div>
            </div>
            <div className="msg-bubble">
              <div className="loading-content">
                <div className="loading-spinner"></div>
                <span className="loading-text">AI正在思考中...</span>
              </div>
            </div>
          </div>
        )}
      </div>
      <div className="chat-toolbar">
        <div className="chat-input-bar">
          <input
            type="text"
            className="chat-input"
            placeholder="输入你的问题..."
            value={input}
            onChange={(e) => setInput(e.target.value)}
            onKeyDown={handleKeyDown}
            disabled={loading}
          />
          <button
            className="send-btn"
            onClick={handleSend}
            disabled={loading || !input.trim()}
          >
            {loading ? '发送中...' : '发送'}
          </button>
        </div>
      </div>
    </div>
  );
};

export default DashscopeChatDemo;
