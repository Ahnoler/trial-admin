import request from '@/utils/request'

export function listOverdueAlert(query) {
  return request({
    url: '/trial/overdueAlert/list',
    method: 'get',
    params: query
  })
}

export function getOverdueAlert(alertId) {
  return request({
    url: '/trial/overdueAlert/' + alertId,
    method: 'get'
  })
}

export function handleOverdueAlert(alertIds, handleRemark) {
  return request({
    url: '/trial/overdueAlert/handle',
    method: 'post',
    data: alertIds,
    params: { handleRemark: handleRemark }
  })
}

export function ignoreOverdueAlert(alertIds, handleRemark) {
  return request({
    url: '/trial/overdueAlert/ignore',
    method: 'post',
    data: alertIds,
    params: { handleRemark: handleRemark }
  })
}

export function delOverdueAlert(alertIds) {
  return request({
    url: '/trial/overdueAlert/' + alertIds,
    method: 'delete'
  })
}

export function countPendingAlerts() {
  return request({
    url: '/trial/overdueAlert/countPending',
    method: 'get'
  })
}

export function executeOverdueCheck() {
  return request({
    url: '/trial/overdueAlert/execute',
    method: 'post'
  })
}

export function exportOverdueAlert(query) {
  return request({
    url: '/trial/overdueAlert/export',
    method: 'post',
    params: query,
    responseType: 'blob'
  })
}
