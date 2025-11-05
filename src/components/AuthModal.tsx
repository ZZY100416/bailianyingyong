import React, { useState } from 'react'
import LoginForm from './LoginForm'
import RegisterForm from './RegisterForm'
import '../App.css' // For modal styling

interface AuthModalProps {
  isOpen: boolean
  onClose: () => void
}

const AuthModal: React.FC<AuthModalProps> = ({ isOpen, onClose }) => {
  const [isLogin, setIsLogin] = useState(true)

  if (!isOpen) return null

  const handleSuccess = () => {
    alert(isLogin ? '登录成功！' : '注册成功！')
    onClose()
  }

  return (
    <div className="auth-modal-overlay" onClick={onClose}>
      <div className="auth-modal" onClick={(e) => e.stopPropagation()}>
        <button className="auth-close-btn" onClick={onClose}>
          &times;
        </button>
        {isLogin ? (
          <LoginForm onSuccess={handleSuccess} />
        ) : (
          <RegisterForm onSuccess={handleSuccess} />
        )}
        <div className="auth-switch">
          <p>
            {isLogin ? '还没有账号？' : '已经有账号了？'}
            <span className="switch-link" onClick={() => setIsLogin(!isLogin)}>
              {isLogin ? '立即注册' : '立即登录'}
            </span>
          </p>
        </div>
      </div>
    </div>
  )
}

export default AuthModal
