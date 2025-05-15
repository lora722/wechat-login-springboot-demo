<template>
  <div class="home-container">
    <div class="home-content">
      <h1>微信扫码登录成功</h1>
      <div class="welcome-card">
        <div class="welcome-header">
          <img class="avatar" src="https://img.wxcha.com/m00/86/59/7c6242363084032b82b4892aeeb6b8ca.jpg" alt="默认头像">
          <div class="welcome-info">
            <h2>欢迎回来</h2>
            <p>您已通过微信扫码完成登录</p>
          </div>
        </div>
        <div class="welcome-body">
          <p class="token-info">
            <span>登录凭证:</span>
            <span class="token-text">{{ tokenDisplay }}</span>
          </p>
          <p class="tip">您的登录状态将保持24小时</p>
        </div>
        <div class="welcome-footer">
          <button class="primary logout-btn" @click="handleLogout">退出登录</button>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup>
import { computed } from 'vue';
import { useUserStore } from '../stores/user';
import { useRouter } from 'vue-router';

const userStore = useUserStore();
const router = useRouter();

// 显示token的部分内容
const tokenDisplay = computed(() => {
  const token = userStore.token;
  if (!token) return '无效令牌';
  // 只显示前10个字符和后10个字符，中间用省略号替代
  if (token.length > 20) {
    return token.substring(0, 10) + '...' + token.substring(token.length - 10);
  }
  return token;
});

// 退出登录
const handleLogout = () => {
  userStore.logout();
  router.push('/login');
};
</script>

<style scoped>
.home-container {
  min-height: 100vh;
  padding: 40px 20px;
  display: flex;
  justify-content: center;
  align-items: center;
  background-color: #f0f2f5;
}

.home-content {
  max-width: 800px;
  width: 100%;
  text-align: center;
}

h1 {
  margin-bottom: 30px;
  color: var(--heading-color);
  font-size: 28px;
}

.welcome-card {
  background: #fff;
  border-radius: 8px;
  box-shadow: 0 2px 12px rgba(0, 0, 0, 0.1);
  padding: 24px;
  margin-bottom: 20px;
  text-align: left;
}

.welcome-header {
  display: flex;
  margin-bottom: 20px;
  align-items: center;
}

.avatar {
  width: 64px;
  height: 64px;
  border-radius: 50%;
  margin-right: 16px;
}

.welcome-info h2 {
  margin: 0 0 8px 0;
  font-size: 20px;
}

.welcome-info p {
  margin: 0;
  color: var(--text-color-secondary);
}

.welcome-body {
  padding: 16px 0;
  border-top: 1px solid #f0f0f0;
  border-bottom: 1px solid #f0f0f0;
}

.token-info {
  display: flex;
  flex-direction: column;
  margin-bottom: 8px;
}

.token-text {
  background-color: #f5f5f5;
  padding: 8px 12px;
  border-radius: 4px;
  font-family: monospace;
  margin-top: 8px;
  word-break: break-all;
}

.tip {
  font-size: 12px;
  color: var(--text-color-secondary);
  margin-top: 12px;
}

.welcome-footer {
  padding-top: 16px;
  display: flex;
  justify-content: flex-end;
}

.logout-btn {
  background-color: var(--primary-color);
  color: white;
  border: none;
  height: 36px;
  padding: 0 20px;
  border-radius: 4px;
  cursor: pointer;
}
</style> 