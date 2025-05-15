<template>
  <div class="not-found">
    <div class="not-found-content">
      <h1>404</h1>
      <h2>页面未找到</h2>
      <p>您访问的页面不存在或已被删除</p>
      <RouterLink to="/" class="home-link">返回首页</RouterLink>
    </div>
  </div>
</template>

<script setup>
import { RouterLink } from 'vue-router';
</script>

<style scoped>
.not-found {
  display: flex;
  justify-content: center;
  align-items: center;
  height: 100vh;
  background-color: #f0f2f5;
}

.not-found-content {
  text-align: center;
  padding: 48px;
  background: #fff;
  border-radius: 8px;
  box-shadow: 0 2px 10px rgba(0, 0, 0, 0.1);
}

h1 {
  font-size: 72px;
  color: var(--heading-color);
  margin: 0;
}

h2 {
  margin: 16px 0;
  color: var(--heading-color);
}

p {
  margin-bottom: 24px;
  color: var(--text-color-secondary);
}

.home-link {
  display: inline-block;
  padding: 8px 24px;
  background-color: var(--primary-color);
  color: #fff;
  border-radius: 4px;
  text-decoration: none;
  transition: background-color 0.3s;
}

.home-link:hover {
  background-color: #40a9ff;
}
</style> 