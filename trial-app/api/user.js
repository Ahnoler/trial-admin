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
	
	getProfile() {
		return request.get('/system/user/profile')
	},
	
	updateProfile(data) {
		return request.put('/system/user/profile', data)
	},
	
	updatePwd(oldPassword, newPassword) {
		return request.put(`/system/user/profile/updatePwd?oldPassword=${encodeURIComponent(oldPassword)}&newPassword=${encodeURIComponent(newPassword)}`)
	}
}
