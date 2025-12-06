<template>
  <div class="merchant-order-detail">
    <van-nav-bar title="订单详情" left-arrow @click-left="goBack" fixed placeholder />
    <div class="page-body">
      <van-empty v-if="!order" description="暂无订单数据" />
      <div v-else>
        <div class="order-card">
          <div class="card-header">
            <div class="title">订单状态</div>
            <van-tag type="primary" plain>{{ order.status }}</van-tag>
          </div>
          <div class="meta">订单号：{{ order.id }}</div>
          <div class="meta">时间：{{ order.createTime }}</div>
          <div class="meta">桌台：{{ order.tableId || '自取' }}</div>
        </div>

        <div class="order-card">
          <div class="card-header">
            <div class="title">菜品明细</div>
          </div>
          <div v-for="item in order.items" :key="item.id" class="order-item">
            <div>
              <div class="item-name">{{ item.productName }}</div>
              <div class="item-sku">{{ item.skuName }} × {{ item.quantity }}</div>
            </div>
            <div class="item-price">¥ {{ Number(item.totalPrice).toFixed(2) }}</div>
          </div>
        </div>

        <div class="order-card">
          <div class="amount-row">
            <span>总金额</span>
            <span class="money">¥ {{ Number(order.totalAmount).toFixed(2) }}</span>
          </div>
          <div class="amount-row">
            <span>实付金额</span>
            <span class="money">¥ {{ Number(order.payAmount).toFixed(2) }}</span>
          </div>
        </div>

        <div class="action-buttons">
          <van-button
            v-if="order.status === 'CREATED'"
            type="primary"
            block
            :loading="operating"
            @click="updateStatus('confirm')"
          >
            接单
          </van-button>
          <van-button
            v-else-if="order.status === 'CONFIRMED'"
            type="success"
            block
            :loading="operating"
            @click="updateStatus('ready')"
          >
            标记出餐
          </van-button>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup>
import { onMounted, ref } from 'vue'
import { useRoute, useRouter } from 'vue-router'
import { confirmMerchantOrder, getMerchantOrderDetail, markMerchantOrderReady } from '@/api/order/merchant'
import { showFailToast, showSuccessToast } from 'vant'

const route = useRoute()
const router = useRouter()
const order = ref(null)
const operating = ref(false)

const fetchOrder = async () => {
  const orderId = route.params.id
  if (!orderId) {
    showFailToast('缺少订单ID')
    return
  }
  try {
    const res = await getMerchantOrderDetail(orderId)
    order.value = res.data
  } catch (err) {
    console.error(err)
    showFailToast('加载订单详情失败')
  }
}

const goBack = () => {
  router.back()
}

const updateStatus = async (action) => {
  if (!order.value) return
  operating.value = true
  try {
    if (action === 'confirm') {
      await confirmMerchantOrder(order.value.id)
      showSuccessToast('已接单')
    } else if (action === 'ready') {
      await markMerchantOrderReady(order.value.id)
      showSuccessToast('已出餐')
    }
    await fetchOrder()
  } catch (err) {
    console.error(err)
    showFailToast('操作失败')
  } finally {
    operating.value = false
  }
}

onMounted(() => {
  fetchOrder()
})
</script>

<style scoped>
.merchant-order-detail {
  min-height: 100vh;
  background: #f7f8fa;
}

.page-body {
  padding: 12px;
}

.order-card {
  background: #fff;
  border-radius: 10px;
  padding: 12px;
  box-shadow: 0 2px 12px rgba(0, 0, 0, 0.04);
  margin-bottom: 12px;
}

.card-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 6px;
}

.title {
  font-size: 15px;
  font-weight: 600;
}

.meta {
  font-size: 13px;
  color: #7d7e80;
  line-height: 18px;
}

.order-item {
  display: flex;
  justify-content: space-between;
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

.action-buttons {
  margin-top: 12px;
}
</style>
