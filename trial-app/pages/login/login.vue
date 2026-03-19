<template>
	<view class="login-container">
		<view class="login-header">
			<image class="logo" src="/static/logo.png" mode="aspectFit"></image>
			<text class="title">试制任务管理系统</text>
		</view>
		
		<view class="login-form">
			<view class="form-item">
				<text class="label">用户名</text>
				<input 
					class="input" 
					v-model="loginForm.username" 
					placeholder="请输入用户名"
					type="text"
				/>
			</view>
			
			<view class="form-item">
				<text class="label">密码</text>
				<input 
					class="input" 
					v-model="loginForm.password" 
					placeholder="请输入密码"
					type="password"
				/>
			</view>
			
			<view class="form-item">
				<text class="label">验证码</text>
				<view class="code-input">
					<input 
						class="input" 
						v-model="loginForm.code" 
						placeholder="请输入验证码"
						type="text"
					/>
					<image 
						class="code-image" 
						:src="codeUrl" 
						@click="getCode"
						mode="aspectFit"
					></image>
				</view>
			</view>
			
			<view class="form-item">
				<checkbox-group @change="rememberChange">
					<label class="remember-label">
						<checkbox :checked="loginForm.rememberMe" color="#007AFF"/>
						<text>记住密码</text>
					</label>
				</checkbox-group>
			</view>
			
			<button class="login-btn" @click="handleLogin" :loading="loading">登录</button>
		</view>
	</view>
</template>

<script>
import { useUserStore } from '@/store/user.js'

export default {
	data() {
		return {
			loginForm: {
				username: '',
				password: '',
				code: '',
				uuid: '',
				rememberMe: false
			},
			codeUrl: '',
			loading: false
		}
	},
	
	onLoad() {
		this.getCode()
		this.getRememberedInfo()
	},
	
	methods: {
		async getCode() {
			try {
				const res = await uni.request({
					url: 'http://localhost:8080/captchaImage',
					method: 'GET'
				})
				
				if (res.data.code === 200) {
					this.codeUrl = 'data:image/gif;base64,' + res.data.img
					this.loginForm.uuid = res.data.uuid
				}
			} catch (error) {
				console.error('获取验证码失败:', error)
			}
		},
		
		getRememberedInfo() {
			const rememberedUsername = uni.getStorageSync('remembered_username')
			const rememberedPassword = uni.getStorageSync('remembered_password')
			
			if (rememberedUsername && rememberedPassword) {
				this.loginForm.username = rememberedUsername
				this.loginForm.password = rememberedPassword
				this.loginForm.rememberMe = true
			}
		},
		
		rememberChange(e) {
			this.loginForm.rememberMe = e.detail.value.length > 0
		},
		
		async handleLogin() {
			if (!this.validateForm()) {
				return
			}
			
			this.loading = true
			
			try {
				const userStore = useUserStore()
				await userStore.login(this.loginForm)
				
				if (this.loginForm.rememberMe) {
					uni.setStorageSync('remembered_username', this.loginForm.username)
					uni.setStorageSync('remembered_password', this.loginForm.password)
				} else {
					uni.removeStorageSync('remembered_username')
					uni.removeStorageSync('remembered_password')
				}
				
				uni.showToast({
					title: '登录成功',
					icon: 'success'
				})
				
				setTimeout(() => {
					uni.switchTab({
						url: '/pages/index/index'
					})
				}, 1000)
			} catch (error) {
				this.getCode()
			} finally {
				this.loading = false
			}
		},
		
		validateForm() {
			if (!this.loginForm.username) {
				uni.showToast({
					title: '请输入用户名',
					icon: 'none'
				})
				return false
			}
			
			if (!this.loginForm.password) {
				uni.showToast({
					title: '请输入密码',
					icon: 'none'
				})
				return false
			}
			
			if (!this.loginForm.code) {
				uni.showToast({
					title: '请输入验证码',
					icon: 'none'
				})
				return false
			}
			
			return true
		}
	}
}
</script>

<style lang="scss" scoped>
.login-container {
	min-height: 100vh;
	background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
	padding: 100rpx 60rpx;
}

.login-header {
	text-align: center;
	margin-bottom: 80rpx;
	
	.logo {
		width: 180rpx;
		height: 180rpx;
		margin-bottom: 30rpx;
	}
	
	.title {
		display: block;
		font-size: 48rpx;
		font-weight: bold;
		color: #FFFFFF;
	}
}

.login-form {
	background: #FFFFFF;
	border-radius: 20rpx;
	padding: 60rpx 40rpx;
	box-shadow: 0 10rpx 30rpx rgba(0, 0, 0, 0.1);
}

.form-item {
	margin-bottom: 40rpx;
	
	.label {
		display: block;
		font-size: 28rpx;
		color: #333333;
		margin-bottom: 16rpx;
		font-weight: 500;
	}
	
	.input {
		width: 90%;
		height: 88rpx;
		background: #F5F7FA;
		border-radius: 12rpx;
		padding: 0 30rpx;
		font-size: 28rpx;
	}
	
	.code-input {
		display: flex;
		align-items: center;
		
		.input {
			flex: 1;
			margin-right: 20rpx;
		}
		
		.code-image {
			width: 200rpx;
			height: 88rpx;
			border-radius: 12rpx;
		}
	}
	
	.remember-label {
		display: flex;
		align-items: center;
		font-size: 28rpx;
		color: #666666;
		
		checkbox {
			margin-right: 10rpx;
		}
	}
}

.login-btn {
	width: 100%;
	height: 88rpx;
	background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
	border-radius: 12rpx;
	color: #FFFFFF;
	font-size: 32rpx;
	font-weight: bold;
	border: none;
	margin-top: 40rpx;
	
	&:active {
		opacity: 0.8;
	}
}
</style>
