# AI 聊天助手

基于 React + TypeScript + Vite + NLUX 构建的智能聊天应用。

## 功能特性

- 🤖 基于 NLUX 框架的现代化聊天界面
- 🎨 美观的 Luna 主题设计
- 💬 支持流式对话和实时响应
- 🎯 语法高亮支持
- 📱 响应式设计，支持移动端
- 🔧 可配置的聊天选项

## 技术栈

- **前端框架**: React 18 + TypeScript
- **构建工具**: Vite
- **聊天框架**: NLUX
- **样式**: CSS3 + 自定义主题
- **后端端口**: 5645

## 快速开始

### 安装依赖

```bash
npm install
```

### 启动开发服务器

```bash
npm run dev
```

应用将在 `http://localhost:3455` 上运行

### 构建生产版本

```bash
npm run build
```

## 配置说明

### 后端配置

项目默认连接到 `http://localhost:5645/chat/stream` 端点。如需修改，请编辑 `src/apdater/openai.tsx` 文件中的 URL 配置。

### 聊天配置

可以在 `src/apdater/openai.tsx` 中自定义以下选项：

- **API 密钥**: 修改 `apiKey` 字段
- **主题**: 修改 `themeId` 和 `colorScheme`
- **头像**: 修改 `personaOptions` 中的头像设置
- **输入框**: 修改 `composerOptions` 中的占位符和快捷键

## 项目结构

```
src/
├── apdater/
│   └── openai.tsx      # NLUX 适配器配置
├── App.tsx             # 主应用组件
├── App.css             # 应用样式
├── main.tsx            # 应用入口
└── index.css           # 全局样式
```

## 开发说明

1. 确保后端服务运行在端口 5645
2. 后端应支持流式响应格式
3. 如需自定义主题，可以修改 `@nlux/themes` 导入
4. 支持自定义渲染器和高级配置

## 许可证

MIT License