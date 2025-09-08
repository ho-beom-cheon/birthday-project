import { createApp } from 'vue'
import App from './App.vue'
import router from './router' // 1. 라우터 import

const app = createApp(App)

app.use(router) // 2. 라우터 사용 설정

app.mount('#app')
