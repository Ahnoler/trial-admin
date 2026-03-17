import config from '@/config/index.js'

export default {
	uploadImage(filePath) {
		return new Promise((resolve, reject) => {
			const token = uni.getStorageSync(config.TOKEN_KEY)
			
			uni.uploadFile({
				url: config.BASE_URL + '/common/upload',
				filePath: filePath,
				name: 'file',
				header: {
					'Authorization': token ? `Bearer ${token}` : ''
				},
				success: (res) => {
					const data = JSON.parse(res.data)
					if (data.code === 200) {
						resolve(data)
					} else {
						uni.showToast({
							title: data.msg || '上传失败',
							icon: 'none'
						})
						reject(new Error(data.msg || '上传失败'))
					}
				},
				fail: (err) => {
					uni.showToast({
						title: '上传失败',
						icon: 'none'
					})
					reject(err)
				}
			})
		})
	},
	
	chooseAndUploadImage(count = 1) {
		return new Promise((resolve, reject) => {
			uni.chooseImage({
				count: count,
				sizeType: ['compressed'],
				sourceType: ['album', 'camera'],
				success: async (res) => {
					const uploadPromises = res.tempFilePaths.map(filePath => {
						return this.uploadImage(filePath)
					})
					
					try {
						const results = await Promise.all(uploadPromises)
						resolve(results)
					} catch (error) {
						reject(error)
					}
				},
				fail: (err) => {
					reject(err)
				}
			})
		})
	}
}
