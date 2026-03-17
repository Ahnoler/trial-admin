import { defineStore } from 'pinia'
import userApi from '@/api/user.js'
import config from '@/config/index.js'

export const useUserStore = defineStore('user', {
	state: () => ({
		token: uni.getStorageSync(config.TOKEN_KEY) || '',
		userInfo: uni.getStorageSync(config.USER_INFO_KEY) || null
	}),
	
	getters: {
		isLoggedIn: (state) => !!state.token,
		userName: (state) => state.userInfo?.userName || '',
		userId: (state) => state.userInfo?.userId || ''
	},
	
	actions: {
		async login(loginForm) {
			try {
				const res = await userApi.login(loginForm)
				this.token = res.token
				uni.setStorageSync(config.TOKEN_KEY, res.token)
				
				await this.getUserInfo()
				
				return res
			} catch (error) {
				throw error
			}
		},
		
		async getUserInfo() {
			try {
				const res = await userApi.getUserInfo()
				this.userInfo = res.user
				uni.setStorageSync(config.USER_INFO_KEY, res.user)
				return res
			} catch (error) {
				throw error
			}
		},
		
		async logout() {
			try {
				await userApi.logout()
			} catch (error) {
				console.error('登出失败:', error)
			} finally {
				this.token = ''
				this.userInfo = null
				uni.removeStorageSync(config.TOKEN_KEY)
				uni.removeStorageSync(config.USER_INFO_KEY)
			}
		},
		
		updateUserInfo(userInfo) {
			this.userInfo = { ...this.userInfo, ...userInfo }
			uni.setStorageSync(config.USER_INFO_KEY, this.userInfo)
		}
	}
})
