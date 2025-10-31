import { createApp } from 'vue'
import './style.css'
import App from './App.vue'
import ElementPlus from 'element-plus'
import 'element-plus/theme-chalk/index.css' 
import router from './router/index'
import locale from 'element-plus/dist/locale/zh-cn'

const app=createApp(App)
app.use(ElementPlus,{locale}) 
app.use(router) 
app.mount('#app')
