import { defineConfig } from 'vite'
import react from '@vitejs/plugin-react'

// https://vite.dev/config/
export default defineConfig({
  plugins: [react()],
  server: {
    port: 3455,
    host: true, // 允许外部访问
    open: true  // 自动打开浏览器
  }
})
