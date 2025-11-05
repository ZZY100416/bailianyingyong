import React, { useState } from 'react'
import { useAuth } from '../contexts/AuthContext'

interface LoginFormProps {
  onSuccess: () => void
}

const LoginForm: React.FC<LoginFormProps> = ({ onSuccess }) => {
  const [username, setUsername] = useState('')
  const [password, setPassword] = useState('')
  const [loading, setLoading] = useState(false)
  const [error, setError] = useState('')
  const { login } = useAuth()

  const handleSubmit = async (e: React.FormEvent) => {
    e.preventDefault()
    setLoading(true)
    setError('')

    try {
      const success = await login(username, password)
      if (success) {
        onSuccess()
      } else {
        setError('登录失败，请检查用户名和密码')
      }
    } catch (err) {
      setError('登录时发生错误')
    } finally {
      setLoading(false)
    }
  }

  return (
    <form onSubmit={handleSubmit} className="auth-form">
      <div className="auth-header">
        <h2>登录</h2>
      </div>
      <div className="auth-form-content">
        <div className="form-group">
          <label htmlFor="username">用户名或邮箱:</label>
          <input
            type="text"
            id="username"
            value={username}
            onChange={(e) => setUsername(e.target.value)}
            required
            placeholder="请输入用户名或邮箱"
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
        {error && <div className="error-message">{error}</div>}
        <button type="submit" className="auth-submit-btn" disabled={loading}>
          {loading ? '登录中...' : '登录'}
        </button>
      </div>
    </form>
  )
}

export default LoginForm
