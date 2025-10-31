# 甜品店管理系统 - 前端

基于 Vue 3 + Vite 开发的现代化甜品店管理系统前端应用。

## 功能特性

- 🍰 甜品管理 - 添加、编辑、删除甜品信息
- 🏢 部门管理 - 组织架构管理
- 🏷️ 分类管理 - 甜品分类管理
- 👤 用户注册 - 用户注册功能
- 📱 响应式设计 - 适配移动端和桌面端
- 🎨 现代化UI - 美观的用户界面

## 技术栈

- **前端框架**: Vue 3
- **构建工具**: Vite
- **路由**: Vue Router
- **HTTP请求**: Axios
- **UI组件**: 自定义组件
- **样式**: CSS3

## 项目结构

```
prj-fronted/
├── src/
│   ├── api/          # API接口调用
│   ├── assets/       # 静态资源
│   ├── components/   # 公共组件
│   ├── router/       # 路由配置
│   ├── utils/        # 工具函数
│   ├── views/        # 页面组件
│   ├── App.vue       # 根组件
│   ├── main.js       # 入口文件
│   └── style.css     # 全局样式
├── public/           # 公共资源
└── package.json      # 项目配置
```

## 快速开始

### 环境要求

- Node.js >= 14.0.0
- npm >= 6.0.0

### 安装依赖

```bash
cd prj-fronted
npm install
```

### 开发环境运行

```bash
npm run dev
```

访问 <http://localhost:5173> 查看应用

### 生产环境构建

```bash
npm run build
```

构建产物将在 `dist/` 目录中

### 预览构建结果

```bash
npm run preview
```

## API接口

前端通过 `/api` 路径访问后端服务，主要包含以下模块：

- **甜品管理**: `/api/desserts`
- **部门管理**: `/api/departments`
- **分类管理**: `/api/categories`

## 开发建议

### 推荐的IDE配置

- [VS Code](https://code.visualstudio.com/) + [Volar](https://marketplace.visualstudio.com/items?itemName=Vue.volar)
- 禁用 Vetur 插件
- 安装 [TypeScript Vue Plugin (Volar)](https://marketplace.visualstudio.com/items?itemName=Vue.vscode-typescript-vue-plugin)

### 代码规范

- 使用 Vue 3 Composition API
- 组件命名采用 PascalCase
- 文件命名采用 kebab-case
- 保持代码注释清晰

## 相关项目

- [甜品店管理系统后端](https://github.com/your-username/dessert-management-backend) - Spring Boot 后端服务

## 许可证

MIT License
