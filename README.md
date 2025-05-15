# 微信扫码登录示例项目

这是一个基于Spring Boot + Vue 3实现微信扫码登录的完整示例项目。

## 技术栈

### 后端
- Spring Boot 2.7.8
- Spring Data Redis
- Java JWT

### 前端
- Vue 3.3.4
- Vue Router 4
- Pinia
- Axios
- QRCode
- UUID

## 功能特性

- 微信扫码登录
- JWT令牌鉴权
- Redis存储登录状态
- 前端轮询检查登录状态
- 自动刷新过期二维码
- 响应式布局设计

## 项目结构

```
├── backend/                  # 后端项目
│   ├── src/                  # 源代码
│   │   ├── main/
│   │   │   ├── java/
│   │   │   │   └── com/example/wechatlogin/
│   │   │   │       ├── config/       # 配置类
│   │   │   │       ├── controller/   # 控制器
│   │   │   │       ├── dto/          # 数据传输对象
│   │   │   │       ├── service/      # 服务接口和实现
│   │   │   │       └── util/         # 工具类
│   │   │   └── resources/
│   │   │       └── application.yml   # 应用配置
│   └── pom.xml                       # Maven配置
│
└── frontend/                # 前端项目
    ├── public/              # 静态资源
    ├── src/                 # 源代码
    │   ├── assets/          # 资源文件
    │   ├── components/      # 组件
    │   ├── router/          # 路由
    │   ├── stores/          # 状态管理
    │   ├── views/           # 视图
    │   ├── App.vue          # 根组件
    │   └── main.js          # 入口文件
    ├── index.html           # HTML模板
    ├── vite.config.js       # Vite配置
    └── package.json         # NPM配置
```

## 如何运行

### 前提条件

- Java 11+
- Node.js 16+
- Redis 服务器
- 微信开发者账号和应用

### 配置微信应用

1. 在微信开放平台注册应用并获取AppID和AppSecret
2. 配置回调域名为你的服务器域名或本地开发地址

### 后端配置

1. 修改 `backend/src/main/resources/application.yml` 中的微信配置：
   ```yaml
   wechat:
     appid: 你的微信AppID
     secret: 你的微信AppSecret
     redirectUri: 你的回调地址
   ```

2. 确保Redis服务器已启动，并根据需要修改Redis配置

### 启动后端

```bash
cd backend
mvn spring-boot:run
```

### 启动前端

```bash
cd frontend
npm install
npm run dev
```

## 浏览器访问

打开浏览器访问 `http://localhost:5173`，即可看到登录页面。使用微信扫描二维码完成登录。

## 注意事项

- 本项目仅用于演示，生产环境中应当增强安全措施
- 微信扫码登录需要公网域名和SSL证书才能在真实环境中使用
- 开发测试时可以使用内网穿透工具（如ngrok）来获取公网访问能力 