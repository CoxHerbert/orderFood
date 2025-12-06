<template>
  <div class="order-confirm-page">
    <van-nav-bar title="确认订单" left-arrow @click-left="goBack" fixed placeholder />
    <div class="order-content">
      <van-empty v-if="!cartItems.length" description="购物车为空">
        <template #bottom>
          <van-button type="primary" size="small" @click="goMenu">返回菜单</van-button>
        </template>
      </van-empty>
      <div v-else>
        <div class="order-section">
          <div class="section-title">菜品明细</div>
          <div v-for="item in cartItems" :key="item.skuId" class="cart-item">
            <div class="item-main">
              <div>
                <div class="item-name">{{ item.productName }}</div>
                <div class="item-sku">{{ item.skuName }}</div>
              </div>
              <div class="item-price">¥ {{ item.price.toFixed(2) }}</div>
            </div>
            <div class="item-actions">
              <van-stepper
                v-model="editableQuantities[item.skuId]"
                min="0"
                integer
                @change="(val) => updateQuantity(item.skuId, val)"
              />
              <div class="item-subtotal">小计 ¥ {{ (item.price * item.quantity).toFixed(2) }}</div>
            </div>
          </div>
        </div>
        <div class="order-section">
          <div class="section-title">订单信息</div>
          <van-cell-group inset>
            <van-cell title="门店" :value="storeId || '-'" />
            <van-cell title="桌台" :value="tableId || '自取'" />
            <van-cell title="合计" :value="`¥ ${totalAmount}`" />
          </van-cell-group>
        </div>
      </div>
    </div>
    <van-action-bar>
      <van-action-bar-icon icon="gold-coin-o" text="合计" :badge="cartItems.length ? `¥${totalAmount}` : ''" />
      <van-action-bar-button
        type="danger"
        text="提交订单"
        :disabled="!cartItems.length || submitting"
        :loading="submitting"
        @click="submitOrder"
      />
    </van-action-bar>
  </div>
</template>

<script setup>
import { computed, onMounted, reactive, ref } from 'vue'
import { useRoute, useRouter } from 'vue-router'
import { createOrder } from '@/api/order/user'
import { showFailToast, showSuccessToast } from 'vant'

const route = useRoute()
const router = useRouter()
const submitting = ref(false)
const cart = reactive({})
const editableQuantities = reactive({})

const storeId = computed(() => Number(route.query.storeId))
const tableId = computed(() => route.query.tableId)
const cartStorageKey = computed(() => `order-cart-${storeId.value || 'unknown'}-${tableId.value || 'none'}`)

const cartItems = computed(() => Object.values(cart))
const totalAmount = computed(() => cartItems.value.reduce((sum, item) => sum + item.price * item.quantity, 0).toFixed(2))

const loadCart = () => {
  const saved = localStorage.getItem(cartStorageKey.value)
  Object.keys(cart).forEach((key) => delete cart[key])
  Object.keys(editableQuantities).forEach((key) => delete editableQuantities[key])
  if (saved) {
    try {
      const parsed = JSON.parse(saved)
      Object.keys(parsed).forEach((key) => {
        cart[key] = parsed[key]
        editableQuantities[key] = parsed[key].quantity
      })
    } catch (e) {
      console.warn('Failed to parse cart cache', e)
    }
  }
}

const persistCart = () => {
  localStorage.setItem(cartStorageKey.value, JSON.stringify(cart))
}

const goMenu = () => {
  router.push({ path: '/menu', query: { storeId: storeId.value, tableId: tableId.value || '' } })
}

const goBack = () => {
  router.back()
}

const updateQuantity = (skuId, value) => {
  const quantity = Number(value)
  if (!quantity || quantity <= 0) {
    delete cart[skuId]
    delete editableQuantities[skuId]
  } else {
    if (cart[skuId]) {
      cart[skuId].quantity = quantity
      editableQuantities[skuId] = quantity
    }
  }
  persistCart()
}

const submitOrder = async () => {
  if (!storeId.value) {
    showFailToast('缺少 storeId')
    return
  }
  if (!cartItems.value.length) {
    showFailToast('请先选择菜品')
    return
  }
  submitting.value = true
  try {
    const payload = {
      storeId: storeId.value,
      tableId: tableId.value || undefined,
      userId: 'mock-user-1',
      items: cartItems.value.map((item) => ({
        skuId: item.skuId,
        quantity: item.quantity
      }))
    }
    const res = await createOrder(payload)
    localStorage.removeItem(cartStorageKey.value)
    showSuccessToast('下单成功')
    router.replace({ path: `/order/${res.data.id}` })
  } catch (err) {
    console.error(err)
    showFailToast('提交订单失败')
  } finally {
    submitting.value = false
  }
}

onMounted(() => {
  loadCart()
})
</script>

<style scoped>
.order-confirm-page {
  min-height: 100vh;
  background: #f7f8fa;
  padding-bottom: 80px;
}

.order-content {
  padding: 12px;
}

.order-section {
  margin-bottom: 16px;
}

.section-title {
  font-size: 15px;
  font-weight: 600;
  margin-bottom: 8px;
  color: #111;
}

.cart-item {
  background: #fff;
  border-radius: 8px;
  padding: 12px;
  box-shadow: 0 2px 12px rgba(0, 0, 0, 0.04);
  margin-bottom: 10px;
}

.item-main {
  display: flex;
  justify-content: space-between;
  align-items: center;
}

.item-name {
  font-size: 15px;
  font-weight: 600;
  color: #111;
}

.item-sku {
  font-size: 13px;
  color: #7d7e80;
}

.item-price {
  color: #ee0a24;
  font-weight: 600;
}

.item-actions {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-top: 10px;
}

.item-subtotal {
  font-size: 13px;
  color: #7d7e80;
}
</style>
