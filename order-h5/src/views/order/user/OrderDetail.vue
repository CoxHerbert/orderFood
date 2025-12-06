<template>
  <div class="order-detail-page">
    <van-nav-bar title="订单详情" left-arrow @click-left="goBack" fixed placeholder />
    <div class="order-detail-body">
      <van-empty v-if="!order" description="暂无订单信息" />
      <div v-else>
        <div class="order-card">
          <div class="order-status">订单状态：{{ order.status }}</div>
          <div class="order-meta">
            <div>订单号：{{ order.id }}</div>
            <div>创建时间：{{ order.createTime }}</div>
            <div>桌台：{{ order.tableId || '自取' }}</div>
          </div>
        </div>
        <div class="order-card">
          <div class="card-title">菜品明细</div>
          <div v-for="item in order.items" :key="item.id" class="order-item">
            <div>
              <div class="item-name">{{ item.productName }}</div>
              <div class="item-sku">{{ item.skuName }} × {{ item.quantity }}</div>
            </div>
            <div class="item-price">¥ {{ Number(item.totalPrice).toFixed(2) }}</div>
          </div>
        </div>
        <div class="order-card">
          <div class="card-title">金额</div>
          <div class="amount-row">
            <span>总金额</span>
            <span class="money">¥ {{ Number(order.totalAmount).toFixed(2) }}</span>
          </div>
          <div class="amount-row">
            <span>实付金额</span>
            <span class="money">¥ {{ Number(order.payAmount).toFixed(2) }}</span>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup>
import { onMounted, ref } from 'vue'
import { useRoute, useRouter } from 'vue-router'
import { getOrderDetail } from '@/api/order/user'
import { showFailToast } from 'vant'

const route = useRoute()
const router = useRouter()
const order = ref(null)
const loading = ref(false)

const fetchOrder = async () => {
  const orderId = route.params.id
  if (!orderId) {
    showFailToast('缺少订单ID')
    return
  }
  loading.value = true
  try {
    const res = await getOrderDetail(orderId)
    order.value = res.data
  } catch (err) {
    console.error(err)
    showFailToast('获取订单详情失败')
  } finally {
    loading.value = false
  }
}

const goBack = () => {
  router.back()
}

onMounted(() => {
  fetchOrder()
})
</script>

<style scoped>
.order-detail-page {
  min-height: 100vh;
  background: #f7f8fa;
}

.order-detail-body {
  padding: 12px;
  padding-bottom: 24px;
}

.order-card {
  background: #fff;
  border-radius: 10px;
  padding: 14px 12px;
  box-shadow: 0 2px 12px rgba(0, 0, 0, 0.04);
  margin-bottom: 12px;
}

.order-status {
  font-size: 16px;
  font-weight: 600;
  margin-bottom: 8px;
}

.order-meta {
  font-size: 13px;
  color: #7d7e80;
  line-height: 20px;
}

.card-title {
  font-size: 15px;
  font-weight: 600;
  margin-bottom: 8px;
}

.order-item {
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding: 8px 0;
  border-bottom: 1px solid #f5f6f7;
}

.order-item:last-of-type {
  border-bottom: none;
}

.item-name {
  font-size: 14px;
  color: #111;
}

.item-sku {
  font-size: 13px;
  color: #7d7e80;
}

.item-price {
  font-weight: 600;
  color: #ee0a24;
}

.amount-row {
  display: flex;
  justify-content: space-between;
  padding: 6px 0;
}

.money {
  font-weight: 600;
  color: #111;
}
</style>
