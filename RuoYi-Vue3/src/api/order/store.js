import request from '@/utils/request'

// 查询门店列表
export function listStore(query) {
  return request({
    url: '/api/admin/store/list',
    method: 'get',
    params: query
  })
}

// 查询门店详情
export function getStore(id) {
  return request({
    url: `/api/admin/store/${id}`,
    method: 'get'
  })
}

// 新增门店
export function addStore(data) {
  return request({
    url: '/api/admin/store',
    method: 'post',
    data
  })
}

// 修改门店
export function updateStore(data) {
  return request({
    url: '/api/admin/store',
    method: 'put',
    data
  })
}

// 删除门店
export function delStore(id) {
  return request({
    url: `/api/admin/store/${id}`,
    method: 'delete'
  })
}
