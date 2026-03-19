import request from '@/utils/request.js'

export default {
	getTaskList(params) {
		return request.get('/trial/prod/list', params)
	},
	
	getTaskDetail(taskId) {
		return request.get(`/trial/prod/${taskId}`)
	},
	
	getTaskProcessList(taskId) {
		return request.get('/trial/prod/detail/list', { taskId })
	},
	
	getProcessDetail(id) {
		return request.get(`/trial/prod/detail/${id}`)
	},
	
	updateProcess(data) {
		return request.post('/trial/prod/detail/edit', data)
	},
	
	scanQRCode(qrCode) {
		return request.get(`/trial/prod/scan/${qrCode}`)
	},
	
	getMyTaskList(params) {
		return request.get('/trial/prod/myTask', params)
	}
}
