import request from '@/utils/request'

export function getStoreMenu(storeId) {
  return request({
    url: `/user/stores/${storeId}/menu`,
    method: 'get'
  })
}

export function createOrder(data) {
  return request({
    url: '/user/orders',
    method: 'post',
    data
  })
}

export function getOrderDetail(orderId) {
  return request({
    url: `/user/orders/${orderId}`,
    method: 'get'
  })
}
