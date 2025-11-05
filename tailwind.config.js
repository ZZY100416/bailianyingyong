/** @type {import('tailwindcss').Config} */
export default {
  content: [
    "./index.html",
    "./src/**/*.{js,ts,jsx,tsx}",
  ],
  darkMode: 'class', // 启用class-based dark模式
  theme: {
    extend: {
      colors: {
        // 自定义颜色主题
        primary: {
          50: '#e8f5e8',
          100: '#c8e6c9',
          200: '#a5d6a7',
          300: '#81c784',
          400: '#66bb6a',
          500: '#4caf50',
          600: '#45a049',
          700: '#388e3c',
          800: '#2e7d32',
          900: '#1b5e20',
        },
        // Dark mode colors
        dark: {
          50: '#f8f9fa',
          100: '#e9ecef',
          200: '#dee2e6',
          300: '#ced4da',
          400: '#adb5bd',
          500: '#6c757d',
          600: '#495057',
          700: '#343a40',
          800: '#212529',
          900: '#121212',
        }
      },
      fontFamily: {
        sans: ['Inter', 'system-ui', 'sans-serif'],
      },
      boxShadow: {
        'primary': '0 4px 20px rgba(76, 175, 80, 0.3)',
        'primary-lg': '0 8px 32px rgba(76, 175, 80, 0.2)',
        'dark': '0 4px 20px rgba(0, 0, 0, 0.3)',
        'dark-lg': '0 8px 32px rgba(0, 0, 0, 0.2)',
      }
    },
  },
  plugins: [],
}
