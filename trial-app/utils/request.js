import config from '@/config/index.js'

class Request {
	constructor() {
		this.baseURL = config.BASE_URL
		this.timeout = config.TIMEOUT
	}
	
	request(options = {}) {
		return new Promise((resolve, reject) => {
			const token = uni.getStorageSync(config.TOKEN_KEY)
			
			uni.request({
				url: this.baseURL + options.url,
				method: options.method || 'GET',
				data: options.data || {},
				header: {
					'Content-Type': 'application/json',
					'Authorization': token ? `Bearer ${token}` : '',
					...options.header
				},
				timeout: this.timeout,
				success: (res) => {
					if (res.statusCode === 200) {
						if (res.data.code === 200) {
							resolve(res.data)
						} else if (res.data.code === 401) {
							uni.removeStorageSync(config.TOKEN_KEY)
							uni.removeStorageSync(config.USER_INFO_KEY)
							uni.reLaunch({
								url: '/pages/login/login'
							})
							reject(new Error('登录已过期，请重新登录'))
						} else {
							uni.showToast({
								title: res.data.msg || '请求失败',
								icon: 'none'
							})
							reject(new Error(res.data.msg || '请求失败'))
						}
					} else {
						uni.showToast({
							title: '网络请求失败',
							icon: 'none'
						})
						reject(new Error('网络请求失败'))
					}
				},
				fail: (err) => {
					uni.showToast({
						title: '网络连接失败',
						icon: 'none'
					})
					reject(err)
				}
			})
		})
	}
	
	get(url, data, options = {}) {
		return this.request({
			url,
			method: 'GET',
			data,
			...options
		})
	}
	
	post(url, data, options = {}) {
		return this.request({
			url,
			method: 'POST',
			data,
			...options
		})
	}
	
	put(url, data, options = {}) {
		return this.request({
			url,
			method: 'PUT',
			data,
			...options
		})
	}
	
	delete(url, data, options = {}) {
		return this.request({
			url,
			method: 'DELETE',
			data,
			...options
		})
	}
}

export default new Request()
