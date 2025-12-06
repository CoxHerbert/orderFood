<template>
  <div class="menu-page">
    <van-nav-bar title="点餐" fixed placeholder />
    <div class="menu-body">
      <van-sidebar v-model="activeCategory" class="menu-categories">
        <van-sidebar-item
          v-for="(category, index) in categories"
          :key="category.categoryId"
          :title="category.categoryName"
          :dot="index === activeCategory"
        />
      </van-sidebar>
      <div class="menu-products">
        <div v-if="loading" class="menu-loading">
          <van-loading size="24px" type="spinner" />
        </div>
        <van-empty v-else-if="!categories.length" description="暂无菜单" />
        <div v-else class="menu-list">
          <div v-for="category in displayedCategories" :key="category.categoryId" class="menu-category-block">
            <div class="category-title">{{ category.categoryName }}</div>
            <div v-for="product in category.products" :key="product.spuId" class="product-card">
              <div class="product-header">
                <div class="product-name">{{ product.name }}</div>
                <van-tag v-if="product.onSale === false" type="danger" plain>停售</van-tag>
              </div>
              <div class="product-desc" v-if="product.description">{{ product.description }}</div>
              <div
                v-for="sku in product.skus"
                :key="sku.skuId"
                class="sku-row"
              >
                <div class="sku-info">
                  <div class="sku-name">{{ sku.skuName }}</div>
                  <div class="sku-price">¥ {{ Number(sku.price).toFixed(2) }}</div>
                </div>
                <van-stepper
                  v-model="cartQuantities[sku.skuId]"
                  min="0"
                  integer
                  @change="(value) => handleQuantityChange(sku, product, value)"
                />
              </div>
            </div>
          </div>
        </div>
      </div>
    </div>
    <van-action-bar>
      <van-action-bar-icon icon="cart-o" text="购物车" :badge="cartCount || ''" />
      <van-action-bar-button type="danger" text="去下单" :disabled="!cartItems.length" @click="goToConfirm" />
    </van-action-bar>
  </div>
</template>

<script setup>
import { computed, onMounted, reactive, ref, watch } from 'vue'
import { useRoute, useRouter } from 'vue-router'
import { getStoreMenu } from '@/api/order/user'
import { showFailToast } from 'vant'

const route = useRoute()
const router = useRouter()

const loading = ref(false)
const categories = ref([])
const activeCategory = ref(0)
const cart = reactive({})
const cartQuantities = reactive({})

const storeId = computed(() => Number(route.query.storeId))
const tableId = computed(() => route.query.tableId)
const cartStorageKey = computed(() => `order-cart-${storeId.value || 'unknown'}-${tableId.value || 'none'}`)

const cartItems = computed(() => Object.values(cart))
const cartCount = computed(() => cartItems.value.reduce((sum, item) => sum + item.quantity, 0))

const displayedCategories = computed(() => {
  if (!categories.value.length) return []
  const current = categories.value[activeCategory.value] || categories.value[0]
  return current ? [current] : []
})

const syncQuantitiesFromCart = () => {
  cartQuantities.clear?.()
  Object.keys(cartQuantities).forEach((key) => delete cartQuantities[key])
  Object.values(cart).forEach((item) => {
    cartQuantities[item.skuId] = item.quantity
  })
}

const persistCart = () => {
  localStorage.setItem(cartStorageKey.value, JSON.stringify(cart))
}

const restoreCart = () => {
  const saved = localStorage.getItem(cartStorageKey.value)
  if (saved) {
    try {
      const parsed = JSON.parse(saved)
      Object.keys(parsed).forEach((key) => {
        cart[key] = parsed[key]
      })
    } catch (e) {
      console.warn('Failed to parse cart cache', e)
    }
  }
  syncQuantitiesFromCart()
}

const handleQuantityChange = (sku, product, value) => {
  if (!value || value <= 0) {
    delete cart[sku.skuId]
    delete cartQuantities[sku.skuId]
  } else {
    cart[sku.skuId] = {
      skuId: sku.skuId,
      productName: product.name,
      skuName: sku.skuName,
      price: Number(sku.price),
      quantity: Number(value),
      productId: product.spuId,
      categoryId: product.categoryId
    }
    cartQuantities[sku.skuId] = Number(value)
  }
  persistCart()
}

const fetchMenu = async () => {
  if (!storeId.value) {
    showFailToast('缺少 storeId')
    return
  }
  loading.value = true
  try {
    const res = await getStoreMenu(storeId.value)
    categories.value = res.data || []
    if (categories.value.length === 0) {
      activeCategory.value = 0
    } else {
      activeCategory.value = 0
    }
  } catch (err) {
    console.error(err)
    showFailToast('加载菜单失败')
  } finally {
    loading.value = false
  }
}

const goToConfirm = () => {
  persistCart()
  router.push({
    path: '/order/confirm',
    query: {
      storeId: storeId.value,
      tableId: tableId.value || ''
    }
  })
}

watch(
  () => cartStorageKey.value,
  () => {
    Object.keys(cart).forEach((key) => delete cart[key])
    restoreCart()
  }
)

onMounted(() => {
  restoreCart()
  fetchMenu()
})
</script>

<style scoped>
.menu-page {
  min-height: 100vh;
  background: #f7f8fa;
}

.menu-body {
  display: flex;
  padding-bottom: 90px;
}

.menu-categories {
  width: 110px;
  background: #fff;
  border-right: 1px solid #f2f3f5;
}

.menu-products {
  flex: 1;
  padding: 12px;
  overflow-y: auto;
  min-height: 80vh;
}

.menu-loading {
  display: flex;
  justify-content: center;
  align-items: center;
  padding: 40px 0;
}

.menu-category-block + .menu-category-block {
  margin-top: 12px;
}

.category-title {
  font-weight: 600;
  font-size: 15px;
  margin-bottom: 8px;
}

.product-card {
  background: #fff;
  border-radius: 8px;
  padding: 10px 12px;
  box-shadow: 0 2px 12px rgba(0, 0, 0, 0.04);
}

.product-card + .product-card {
  margin-top: 10px;
}

.product-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 4px;
}

.product-name {
  font-weight: 600;
  font-size: 16px;
  color: #111;
}

.product-desc {
  font-size: 13px;
  color: #7d7e80;
  margin-bottom: 8px;
}

.sku-row {
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding: 8px 0;
  border-top: 1px solid #f5f6f7;
}

.sku-row:first-of-type {
  border-top: none;
}

.sku-info {
  display: flex;
  flex-direction: column;
  gap: 4px;
}

.sku-name {
  font-size: 14px;
  color: #323233;
}

.sku-price {
  font-size: 14px;
  font-weight: 600;
  color: #ee0a24;
}
</style>
