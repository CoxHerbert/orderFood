import { createApp } from 'vue'
import App from './App.vue'
import router from './router'
import setupVant from './plugins/vant'
import 'vant/lib/index.css'
import './styles/index.css'

const app = createApp(App)
setupVant(app)
app.use(router)
app.mount('#app')
