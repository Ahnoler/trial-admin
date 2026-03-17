import request from '@/utils/request.js'

export default {
	login(data) {
		return request.post('/login', data)
	},
	
	getUserInfo() {
		return request.get('/getInfo')
	},
	
	logout() {
		return request.post('/logout')
	},
	
	updateUserInfo(data) {
		return request.put('/system/user/profile', data)
	}
}
