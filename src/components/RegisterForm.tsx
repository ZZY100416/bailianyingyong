import React, { useState } from 'react'
import { useAuth } from '../contexts/AuthContext'

interface RegisterFormProps {
  onSuccess: () => void
}

const RegisterForm: React.FC<RegisterFormProps> = ({ onSuccess }) => {
  const [username, setUsername] = useState('')
  const [email, setEmail] = useState('')
  const [password, setPassword] = useState('')
  const [nickname, setNickname] = useState('')
  const [avatarUrl, setAvatarUrl] = useState('')
  const [loading, setLoading] = useState(false)
  const [error, setError] = useState('')
  const { register } = useAuth()

  const handleSubmit = async (e: React.FormEvent) => {
    e.preventDefault()
    setLoading(true)
    setError('')

    try {
      const success = await register(username, email, password, nickname || undefined, avatarUrl || undefined)
      if (success) {
        onSuccess()
      } else {
        setError('注册失败，请检查输入信息')
      }
    } catch (err) {
      setError('注册时发生错误')
    } finally {
      setLoading(false)
    }
  }

  return (
    <form onSubmit={handleSubmit} className="auth-form">
      <div className="auth-header">
        <h2>注册</h2>
      </div>
      <div className="auth-form-content">
        <div className="form-group">
          <label htmlFor="username">用户名:</label>
          <input
            type="text"
            id="username"
            value={username}
            onChange={(e) => setUsername(e.target.value)}
            required
            placeholder="请输入用户名"
          />
        </div>
        <div className="form-group">
          <label htmlFor="email">邮箱:</label>
          <input
            type="email"
            id="email"
            value={email}
            onChange={(e) => setEmail(e.target.value)}
            required
            placeholder="请输入邮箱"
          />
        </div>
        <div className="form-group">
          <label htmlFor="password">密码:</label>
          <input
            type="password"
            id="password"
            value={password}
            onChange={(e) => setPassword(e.target.value)}
            required
            placeholder="请输入密码"
          />
        </div>
        <div className="form-group">
          <label htmlFor="nickname">昵称 (可选):</label>
          <input
            type="text"
            id="nickname"
            value={nickname}
            onChange={(e) => setNickname(e.target.value)}
            placeholder="请输入昵称"
          />
        </div>
        <div className="form-group">
          <label htmlFor="avatarUrl">头像URL (可选):</label>
          <input
            type="url"
            id="avatarUrl"
            value={avatarUrl}
            onChange={(e) => setAvatarUrl(e.target.value)}
            placeholder="请输入头像URL"
          />
        </div>
        {error && <div className="error-message">{error}</div>}
        <button type="submit" className="auth-submit-btn" disabled={loading}>
          {loading ? '注册中...' : '注册'}
        </button>
      </div>
    </form>
  )
}

export default RegisterForm
