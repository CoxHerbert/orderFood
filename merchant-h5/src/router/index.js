import { createRouter, createWebHistory } from 'vue-router'

const routes = [
  { path: '/', redirect: '/merchant/orders' },
  { path: '/merchant/orders', component: () => import('@/views/order/merchant/MerchantOrders.vue') },
  { path: '/merchant/orders/:id', component: () => import('@/views/order/merchant/MerchantOrderDetail.vue') }
]

const router = createRouter({
  history: createWebHistory(),
  routes
})

export default router
