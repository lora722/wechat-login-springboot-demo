import { defineStore } from 'pinia'
import axios from 'axios'
import { ref } from 'vue'

export const useUserStore = defineStore('user', () => {
  const token = ref(localStorage.getItem('token') || '')
  const userInfo = ref(null)

  // 设置令牌
  function setToken(newToken) {
    token.value = newToken
    localStorage.setItem('token', newToken)
    // 设置axios默认请求头
    axios.defaults.headers.common['Authorization'] = `Bearer ${newToken}`
  }

  // 清空令牌
  function clearToken() {
    token.value = ''
    localStorage.removeItem('token')
    // 移除axios默认请求头
    delete axios.defaults.headers.common['Authorization']
  }

  // 退出登录
  function logout() {
    clearToken()
    userInfo.value = null
  }

  // 检查登录状态
  function checkLoginStatus(uuid) {
    return new Promise((resolve, reject) => {
      axios.get(`/api/wechat/status?uuid=${uuid}`)
        .then(response => {
          const data = response.data.data
          if (data && data.status === 'done' && data.jwt) {
            setToken(data.jwt)
            resolve(true)
          } else {
            resolve(false)
          }
        })
        .catch(err => {
          console.error('检查登录状态失败:', err)
          reject(err)
        })
    })
  }

  return {
    token,
    userInfo,
    setToken,
    clearToken,
    logout,
    checkLoginStatus
  }
}) 