<template>
  <div id="app">
    <RouterView />
  </div>
</template>

<script setup>
import { RouterView } from 'vue-router';
import { useUserStore } from './stores/user';
import { onMounted } from 'vue';

const userStore = useUserStore();

onMounted(() => {
  // 如果存在token，初始化axios请求头
  if (userStore.token) {
    const token = userStore.token;
    if (token) {
      // 设置axios默认请求头
      import('axios').then(module => {
        const axios = module.default;
        axios.defaults.headers.common['Authorization'] = `Bearer ${token}`;
      });
    }
  }
});
</script>

<style>
#app {
  width: 100%;
  height: 100%;
}
</style> 