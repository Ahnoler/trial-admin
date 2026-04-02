import request from '@/utils/request'

// 查询试制任务信息列表
export function listProd(query) {
  return request({
    url: '/trial/prod/list',
    method: 'get',
    params: query
  })
}

// 查询试制任务信息详细
export function getProd(taskId) {
  return request({
    url: '/trial/prod/' + taskId,
    method: 'get'
  })
}

// 新增试制任务信息
export function addProd(data) {
  return request({
    url: '/trial/prod',
    method: 'post',
    data: data
  })
}

// 修改试制任务信息
export function updateProd(data) {
  return request({
    url: '/trial/prod',
    method: 'put',
    data: data
  })
}

// 变更程序试制任务信息
export function flowProd(data) {
  return request({
    url: '/trial/prod/flow',
    method: 'post',
    data: data
  })
}

// 零件分流试制任务信息
export function forkProd(data) {
  return request({
    url: '/trial/prod/fork',
    method: 'post',
    data: data
  })
}

// 结束试制任务信息
export function overProd(data) {
  return request({
    url: '/trial/prod/over',
    method: 'post',
    data: data
  })
}

// 停用试制任务信息
export function disableProd(data) {
  return request({
    url: '/trial/prod/disable',
    method: 'post',
    data: data
  })
}

// 启用试制任务信息
export function enableProd(data) {
  return request({
    url: '/trial/prod/enable',
    method: 'post',
    data: data
  })
}

// 删除试制任务信息
export function delProd(taskId) {
  return request({
    url: '/trial/prod/' + taskId,
    method: 'delete'
  })
}

// 根据关联任务ID查询试制任务信息列表
export function listRelatedProd(relatedTaskId, query) {
  return request({
    url: '/trial/prod/related/' + relatedTaskId,
    method: 'get',
    params: query
  })
}

// 导出任务内容为PDF文件
export function exportProdPdf(id) {
  return request({
    url: '/trial/prod/exportPdf/' + id,
    method: 'get',
    responseType: 'blob'
  })
}

// 打印项目所有零件的电子流转卡
export function printDetail(id) {
  return request({
    url: '/trial/prod/printDetail/' + id,
    method: 'get',
  })
}
