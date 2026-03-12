import request from '@/utils/request'

// 查询试制任务信息列表
export function listProd(query) {
  return request({
    url: '/trial/prod/detail/list',
    method: 'get',
    params: query
  })
}

// 查询试制任务信息详细
export function getProd(taskId) {
  return request({
    url: '/trial/prod/detail/' + taskId,
    method: 'get'
  })
}

// 新增试制任务信息
export function addProd(data) {
  return request({
    url: '/trial/prod/detail',
    method: 'post',
    data: data
  })
}

// 修改试制任务信息
export function updateProd(data) {
  return request({
    url: '/trial/prod/detail',
    method: 'put',
    data: data
  })
}

// 申请试制任务信息
export function applyProd(data) {
  return request({
    url: '/trial/prod/detail/apply',
    method: 'post',
    data: data
  })
}

// 审核试制任务信息
export function approveProd(data) {
  return request({
    url: '/trial/prod/detail/approve',
    method: 'post',
    data: data
  })
}

// 删除试制任务信息
export function delProd(taskId) {
  return request({
    url: '/trial/prod/detail/' + taskId,
    method: 'delete'
  })
}
