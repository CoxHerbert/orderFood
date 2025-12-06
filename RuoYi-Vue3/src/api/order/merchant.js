import request from '@/utils/request'

export function getMerchantOrders(query) {
  return request({
    url: '/api/merchant/orders',
    method: 'get',
    params: query,
    headers: { isToken: false }
  })
}

export function getMerchantOrderDetail(orderId) {
  return request({
    url: `/api/merchant/orders/${orderId}`,
    method: 'get',
    headers: { isToken: false }
  })
}

export function confirmMerchantOrder(orderId) {
  return request({
    url: `/api/merchant/orders/${orderId}/confirm`,
    method: 'post',
    headers: { isToken: false }
  })
}

export function markMerchantOrderReady(orderId) {
  return request({
    url: `/api/merchant/orders/${orderId}/ready`,
    method: 'post',
    headers: { isToken: false }
  })
}
