import request from '@/utils/request'

// 查询 打印记录列表
export function listLog(query) {
  return request({
    url: '/trial/log/list',
    method: 'get',
    params: query
  })
}

// 查询 打印记录详细
export function getLog(id) {
  return request({
    url: '/trial/log/' + id,
    method: 'get'
  })
}

// 新增 打印记录
export function addLog(data) {
  return request({
    url: '/trial/log',
    method: 'post',
    data: data
  })
}

// 修改 打印记录
export function updateLog(data) {
  return request({
    url: '/trial/log',
    method: 'put',
    data: data
  })
}

// 删除 打印记录
export function delLog(id) {
  return request({
    url: '/trial/log/' + id,
    method: 'delete'
  })
}
