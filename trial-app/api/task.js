import request from '@/utils/request.js'

export default {
	getTaskList(params) {
		return request.get('/trial/prod/list', params)
	},
	
	getTaskDetail(taskId) {
		return request.get(`/trial/prod/${taskId}`)
	},
	
	getTaskProcessList(taskId) {
		return request.get('/trial/prodDetail/list', { taskId })
	},
	
	getProcessDetail(id) {
		return request.get(`/trial/prodDetail/${id}`)
	},
	
	updateProcess(data) {
		return request.put('/trial/prodDetail', data)
	},
	
	scanQRCode(qrCode) {
		return request.get(`/trial/prod/scan/${qrCode}`)
	},
	
	getMyTaskList(params) {
		return request.get('/trial/prod/myTask', params)
	}
}
