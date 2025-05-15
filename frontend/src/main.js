import { createApp } from 'vue'
import { createPinia } from 'pinia'
import App from './App.vue'
import router from './router'
import axios from 'axios'

import './assets/main.css'

// 配置axios
axios.defaults.baseURL = import.meta.env.VITE_API_URL || '/'
axios.defaults.timeout = 10000

const app = createApp(App)

app.use(createPinia())
app.use(router)

app.mount('#app') 