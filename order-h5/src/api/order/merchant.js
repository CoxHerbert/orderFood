import request from '@/utils/request'

export function getMerchantOrders(params) {
  return request({
    url: '/api/merchant/orders',
    method: 'get',
    params
  })
}

export function getMerchantOrderDetail(orderId) {
  return request({
    url: `/api/merchant/orders/${orderId}`,
    method: 'get'
  })
}

export function confirmMerchantOrder(orderId) {
  return request({
    url: `/api/merchant/orders/${orderId}/confirm`,
    method: 'post'
  })
}

export function markMerchantOrderReady(orderId) {
  return request({
    url: `/api/merchant/orders/${orderId}/ready`,
    method: 'post'
  })
}
