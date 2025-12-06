import { createRouter, createWebHistory } from 'vue-router'

const routes = [
  { path: '/', redirect: '/menu' },
  { path: '/menu', component: () => import('@/views/order/user/Menu.vue') },
  { path: '/order/confirm', component: () => import('@/views/order/user/OrderConfirm.vue') },
  { path: '/order/:id', component: () => import('@/views/order/user/OrderDetail.vue') },
  { path: '/merchant/orders', component: () => import('@/views/order/merchant/MerchantOrders.vue') },
  { path: '/merchant/orders/:id', component: () => import('@/views/order/merchant/MerchantOrderDetail.vue') }
]

const router = createRouter({
  history: createWebHistory(),
  routes
})

export default router
