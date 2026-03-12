import request from '@/utils/request'

// 查询项目管理列表
export function listProjects(query) {
  return request({
    url: '/trial/projects/list',
    method: 'get',
    params: query
  })
}

// 查询项目管理详细
export function getProjects(id) {
  return request({
    url: '/trial/projects/' + id,
    method: 'get'
  })
}

// 新增项目管理
export function addProjects(data) {
  return request({
    url: '/trial/projects',
    method: 'post',
    data: data
  })
}

// 修改项目管理
export function updateProjects(data) {
  return request({
    url: '/trial/projects',
    method: 'put',
    data: data
  })
}

// 删除项目管理
export function delProjects(id) {
  return request({
    url: '/trial/projects/' + id,
    method: 'delete'
  })
}

// 打印项目基本信息
export function printProjects(id) {
  return request({
    url: '/trial/projects/print/' + id,
    method: 'get',
    responseType: 'blob'
  })
}

// 导出项目内容为PDF文件
export function exportProjectPdf(id) {
  return request({
    url: '/trial/projects/exportPdf/' + id,
    method: 'get',
    responseType: 'blob'
  })
}

// 打印项目所有零件的电子流转卡
export function printAllProjectsCards(id) {
  return request({
    url: '/trial/projects/printAllCards/' + id,
    method: 'get',
  })
}
