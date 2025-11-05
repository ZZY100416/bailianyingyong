import React, { createContext, useContext, useState, useEffect, ReactNode } from 'react'

interface User {
  id: number
  username: string
  email: string
  nickname?: string
  avatarUrl?: string
}

interface AuthContextType {
  isAuthenticated: boolean
  user: User | null
  token: string | null
  login: (username: string, password: string) => Promise<boolean>
  register: (username: string, email: string, password: string, nickname?: string, avatarUrl?: string) => Promise<boolean>
  logout: () => void
}

const AuthContext = createContext<AuthContextType | undefined>(undefined)

export const useAuth = () => {
  const context = useContext(AuthContext)
  if (context === undefined) {
    throw new Error('useAuth must be used within an AuthProvider')
  }
  return context
}

interface AuthProviderProps {
  children: ReactNode
}

export const AuthProvider: React.FC<AuthProviderProps> = ({ children }) => {
  const [isAuthenticated, setIsAuthenticated] = useState(false)
  const [user, setUser] = useState<User | null>(null)
  const [token, setToken] = useState<string | null>(null)

  // 检查本地存储的token
  useEffect(() => {
    const storedToken = localStorage.getItem('token')
    const storedUser = localStorage.getItem('user')
    
    if (storedToken && storedUser) {
      try {
        const userData = JSON.parse(storedUser)
        setToken(storedToken)
        setUser(userData)
        setIsAuthenticated(true)
      } catch (error) {
        console.error('Error parsing stored user data:', error)
        localStorage.removeItem('token')
        localStorage.removeItem('user')
      }
    }
  }, [])

  const login = async (username: string, password: string): Promise<boolean> => {
    try {
      const response = await fetch('http://localhost:9988/api/auth/login', {
        method: 'POST',
        headers: {
          'Content-Type': 'application/json',
        },
        body: JSON.stringify({ username, password }),
      })

      if (response.ok) {
        const data = await response.json()
        setToken(data.token)
        setUser(data.user)
        setIsAuthenticated(true)
        
        // 保存到本地存储
        localStorage.setItem('token', data.token)
        localStorage.setItem('user', JSON.stringify(data.user))
        
        return true
      } else {
        const errorData = await response.json()
        console.error('Login failed:', errorData.message)
        return false
      }
    } catch (error) {
      console.error('Login error:', error)
      return false
    }
  }

  const register = async (username: string, email: string, password: string, nickname?: string, avatarUrl?: string): Promise<boolean> => {
    try {
      const response = await fetch('http://localhost:9988/api/auth/register', {
        method: 'POST',
        headers: {
          'Content-Type': 'application/json',
        },
        body: JSON.stringify({ username, email, password, nickname, avatarUrl }),
      })

      if (response.ok) {
        const data = await response.json()
        setToken(data.token)
        setUser(data.user)
        setIsAuthenticated(true)
        
        // 保存到本地存储
        localStorage.setItem('token', data.token)
        localStorage.setItem('user', JSON.stringify(data.user))
        
        return true
      } else {
        const errorData = await response.json()
        console.error('Registration failed:', errorData.message)
        return false
      }
    } catch (error) {
      console.error('Registration error:', error)
      return false
    }
  }

  const logout = () => {
    setToken(null)
    setUser(null)
    setIsAuthenticated(false)
    localStorage.removeItem('token')
    localStorage.removeItem('user')
  }

  const value: AuthContextType = {
    isAuthenticated,
    user,
    token,
    login,
    register,
    logout,
  }

  return <AuthContext.Provider value={value}>{children}</AuthContext.Provider>
}
