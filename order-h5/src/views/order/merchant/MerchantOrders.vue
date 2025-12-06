<template>
  <div class="merchant-orders-page">
    <van-nav-bar title="订单管理" fixed placeholder />
    <div class="page-body">
      <van-tabs v-model:active="activeStatus" @change="handleStatusChange">
        <van-tab v-for="status in statusTabs" :key="status.value" :title="status.label" :name="status.value" />
      </van-tabs>
      <div class="orders-content">
        <van-empty v-if="!orders.length && !loading" description="暂无订单" />
        <div v-else>
          <div v-for="order in orders" :key="order.id" class="order-card" @click="goDetail(order.id)">
            <div class="order-header">
              <div class="order-id">订单号：{{ order.id }}</div>
              <van-tag type="primary" plain>{{ order.status }}</van-tag>
            </div>
            <div class="order-meta">
              <div>桌台：{{ order.tableId || '自取' }}</div>
              <div>时间：{{ order.createTime }}</div>
            </div>
            <div class="order-amount">金额：¥ {{ Number(order.payAmount || order.totalAmount).toFixed(2) }}</div>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup>
import { onMounted, ref } from 'vue'
import { useRoute, useRouter } from 'vue-router'
import { getMerchantOrders } from '@/api/order/merchant'
import { showFailToast } from 'vant'

const route = useRoute()
const router = useRouter()
const orders = ref([])
const loading = ref(false)
const activeStatus = ref('CREATED')

const statusTabs = [
  { label: '待接单', value: 'CREATED' },
  { label: '已接单', value: 'CONFIRMED' },
  { label: '已出餐', value: 'READY' },
  { label: '已完成', value: 'COMPLETED' }
]

const fetchOrders = async () => {
  const storeId = route.query.storeId
  if (!storeId) {
    showFailToast('缺少 storeId 参数')
    return
  }
  loading.value = true
  try {
    const res = await getMerchantOrders({ storeId, status: activeStatus.value })
    orders.value = res.data || []
  } catch (err) {
    console.error(err)
    showFailToast('加载订单列表失败')
  } finally {
    loading.value = false
  }
}

const handleStatusChange = () => {
  fetchOrders()
}

const goDetail = (id) => {
  router.push({ path: `/merchant/orders/${id}`, query: { storeId: route.query.storeId } })
}

onMounted(() => {
  fetchOrders()
})
</script>

<style scoped>
.merchant-orders-page {
  min-height: 100vh;
  background: #f7f8fa;
}

.page-body {
  padding: 12px;
}

.orders-content {
  margin-top: 8px;
}

.order-card {
  background: #fff;
  border-radius: 10px;
  padding: 12px;
  box-shadow: 0 2px 12px rgba(0, 0, 0, 0.04);
  margin-bottom: 10px;
}

.order-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 6px;
}

.order-id {
  font-weight: 600;
  color: #111;
}

.order-meta {
  font-size: 13px;
  color: #7d7e80;
  display: flex;
  justify-content: space-between;
}

.order-amount {
  margin-top: 6px;
  font-weight: 600;
  color: #ee0a24;
}
</style>
