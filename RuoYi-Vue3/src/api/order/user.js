import request from '@/utils/request'

export function getStoreMenu(storeId) {
  return request({
    url: `/api/user/stores/${storeId}/menu`,
    method: 'get',
    headers: { isToken: false }
  })
}

export function createOrder(data) {
  return request({
    url: '/api/user/orders',
    method: 'post',
    data,
    headers: { isToken: false }
  })
}

export function getOrderDetail(orderId) {
  return request({
    url: `/api/user/orders/${orderId}`,
    method: 'get',
    headers: { isToken: false }
  })
}
