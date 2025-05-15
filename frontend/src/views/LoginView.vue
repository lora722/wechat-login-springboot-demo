<template>
  <div class="login-container">
    <div class="login-header">
      <h1>微信扫码登录示例</h1>
      <p class="description">扫描下面的二维码完成登录</p>
    </div>
    
    <WechatLogin />
    
    <div class="login-tips">
      <p>注意：本应用仅用于演示微信扫码登录功能</p>
      <p>完成登录后将跳转到首页</p>
    </div>
  </div>
</template>

<script setup>
import WechatLogin from '../components/WechatLogin.vue';
</script>

<style scoped>
.login-container {
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;
  min-height: 100vh;
  background-color: #f0f2f5;
  padding: 20px;
}

.login-header {
  text-align: center;
  margin-bottom: 30px;
}

.login-header h1 {
  font-size: 28px;
  color: var(--heading-color);
  margin-bottom: 10px;
}

.description {
  color: var(--text-color-secondary);
  font-size: 14px;
}

.login-tips {
  margin-top: 30px;
  color: var(--text-color-secondary);
  font-size: 13px;
  text-align: center;
}

.login-tips p {
  margin: 5px 0;
}
</style>