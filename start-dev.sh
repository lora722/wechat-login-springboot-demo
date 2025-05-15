#!/bin/bash
echo "正在启动微信扫码登录示例项目开发环境..."

# 启动后端服务
echo "正在启动后端服务..."
(cd backend && mvn spring-boot:run) &

# 启动前端服务
echo "正在启动前端服务..."
(cd frontend && npm install && npm run dev) &

echo "服务启动中，请稍候..."
echo "前端地址: http://localhost:5173"
echo "后端地址: http://localhost:8080" 