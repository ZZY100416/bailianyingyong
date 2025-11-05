import React from 'react'
import { useAuth } from '../contexts/AuthContext'
import '../App.css' // For user-info styling

const UserInfo: React.FC = () => {
  const { user, logout } = useAuth()

  if (!user) return null

  const displayAvatar = user.avatarUrl || '/user-avatar.png'
  const displayName = user.nickname || user.username || '用户'
  const displayEmail = user.email || ''

  return (
    <div className="user-info">
      <div className="user-avatar">
        {user.avatarUrl ? (
          <img src={displayAvatar} alt="用户头像" />
        ) : (
          <div className="default-avatar">{displayName.charAt(0).toUpperCase()}</div>
        )}
      </div>
      <div className="user-details">
        <span className="user-name">{displayName}</span>
        {displayEmail && <span className="user-email">{displayEmail}</span>}
      </div>
      <button className="logout-btn" onClick={logout}>
        退出
      </button>
    </div>
  )
}

export default UserInfo
