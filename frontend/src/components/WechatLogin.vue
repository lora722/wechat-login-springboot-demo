<template>
  <div class="wechat-login">
    <h2>微信扫码登录</h2>
    <div class="qrcode-container">
      <canvas ref="qrCanvas" width="200" height="200"></canvas>
      <div v-if="loadingQr" class="loading-overlay">
        <span>加载中...</span>
      </div>
      <div v-if="errorMsg" class="error-msg">{{ errorMsg }}</div>
    </div>
    <div class="status-tips">
      <p v-if="scanStatus === 'pending'">请使用微信扫码登录</p>
      <p v-if="scanStatus === 'timeout'" class="timeout">
        <span>二维码已失效</span>
        <button @click="refreshQrCode" class="refresh-btn">刷新</button>
      </p>
      <p v-if="scanStatus === 'done'">登录成功，正在跳转...</p>
    </div>
  </div>
</template>

<script setup>
import { ref, onMounted, onBeforeUnmount } from 'vue';
import axios from 'axios';
import { v4 as uuidv4 } from 'uuid';
import QRCode from 'qrcode';
import { useUserStore } from '../stores/user';
import { useRouter } from 'vue-router';

const qrCanvas = ref(null);
const loginUuid = ref(uuidv4());
const scanStatus = ref('pending');
const errorMsg = ref('');
const loadingQr = ref(true);
const pollInterval = ref(null);
const timeoutTimer = ref(null);

const userStore = useUserStore();
const router = useRouter();

// 生成二维码
async function generateQrCode() {
  try {
    loadingQr.value = true;
    scanStatus.value = 'pending';
    errorMsg.value = '';
    
    // 获取二维码URL
    const res = await axios.get(`/api/wechat/qr?uuid=${loginUuid.value}`);
    const qrUrl = res.data.data.qrUrl;
    
    // 使用qrcode生成二维码
    await QRCode.toCanvas(qrCanvas.value, qrUrl, {
      width: 200,
      margin: 1,
      color: {
        dark: '#000000',
        light: '#ffffff'
      }
    });
    
    loadingQr.value = false;
    
    // 开始轮询登录状态
    startPolling();
    
    // 设置二维码过期时间（5分钟）
    clearTimeout(timeoutTimer.value);
    timeoutTimer.value = setTimeout(() => {
      scanStatus.value = 'timeout';
      stopPolling();
    }, 5 * 60 * 1000);
  } catch (error) {
    loadingQr.value = false;
    errorMsg.value = '生成二维码失败，请刷新重试';
    console.error('生成二维码失败:', error);
  }
}

// 开始轮询登录状态
function startPolling() {
  stopPolling(); // 先停止之前的轮询
  
  pollInterval.value = setInterval(async () => {
    try {
      const loginSuccess = await userStore.checkLoginStatus(loginUuid.value);
      
      if (loginSuccess) {
        scanStatus.value = 'done';
        stopPolling();
        clearTimeout(timeoutTimer.value);
        // 登录成功后跳转到首页
        setTimeout(() => {
          router.push({ name: 'home' });
        }, 1000);
      }
    } catch (error) {
      console.error('检查登录状态失败:', error);
    }
  }, 1500);
}

// 停止轮询
function stopPolling() {
  if (pollInterval.value) {
    clearInterval(pollInterval.value);
    pollInterval.value = null;
  }
}

// 刷新二维码
function refreshQrCode() {
  loginUuid.value = uuidv4();
  generateQrCode();
}

onMounted(() => {
  generateQrCode();
});

onBeforeUnmount(() => {
  stopPolling();
  clearTimeout(timeoutTimer.value);
});
</script>

<style scoped>
.wechat-login {
  display: flex;
  flex-direction: column;
  align-items: center;
  padding: 20px;
  border-radius: 8px;
  background-color: #fff;
  box-shadow: 0 2px 10px rgba(0, 0, 0, 0.1);
  width: 320px;
}

h2 {
  margin-bottom: 20px;
  color: var(--heading-color);
}

.qrcode-container {
  position: relative;
  width: 200px;
  height: 200px;
  margin: 20px 0;
  border: 1px solid #eee;
}

.loading-overlay {
  position: absolute;
  top: 0;
  left: 0;
  width: 100%;
  height: 100%;
  display: flex;
  align-items: center;
  justify-content: center;
  background-color: rgba(255, 255, 255, 0.8);
}

.status-tips {
  text-align: center;
  margin-top: 15px;
  height: 24px;
}

.timeout {
  color: var(--warning-color);
  display: flex;
  align-items: center;
  justify-content: center;
}

.refresh-btn {
  margin-left: 10px;
  font-size: 12px;
  height: 24px;
  padding: 0 8px;
}

.error-msg {
  color: var(--error-color);
  position: absolute;
  top: 50%;
  left: 50%;
  transform: translate(-50%, -50%);
  width: 90%;
  text-align: center;
}
</style> 