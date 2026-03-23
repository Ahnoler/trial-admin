<template>
	<view class="password-container">
		<view class="form-section">
			<view class="form-item">
				<text class="form-label">旧密码</text>
				<input 
					class="form-input" 
					v-model="formData.oldPassword" 
					type="password"
					placeholder="请输入旧密码"
					placeholder-class="input-placeholder"
				/>
			</view>
			
			<view class="form-item">
				<text class="form-label">新密码</text>
				<input 
					class="form-input" 
					v-model="formData.newPassword" 
					type="password"
					placeholder="请输入新密码"
					placeholder-class="input-placeholder"
				/>
			</view>
			
			<view class="form-item">
				<text class="form-label">确认密码</text>
				<input 
					class="form-input" 
					v-model="formData.confirmPassword" 
					type="password"
					placeholder="请确认新密码"
					placeholder-class="input-placeholder"
				/>
			</view>
		</view>
		
		<view class="tips-section">
			<view class="tips-title">密码要求：</view>
			<view class="tips-item">• 密码长度为 6-20 个字符</view>
		</view>
		
		<view class="btn-section">
			<button class="submit-btn" @click="handleSubmit" :loading="loading">确认修改</button>
		</view>
	</view>
</template>

<script>
import userApi from '@/api/user.js'

export default {
	data() {
		return {
			formData: {
				oldPassword: '',
				newPassword: '',
				confirmPassword: ''
			},
			loading: false
		}
	},
	
	methods: {
		validateForm() {
			if (!this.formData.oldPassword) {
				uni.showToast({
					title: '请输入旧密码',
					icon: 'none'
				})
				return false
			}
			
			if (!this.formData.newPassword) {
				uni.showToast({
					title: '请输入新密码',
					icon: 'none'
				})
				return false
			}
			
			if (this.formData.newPassword.length < 6 || this.formData.newPassword.length > 20) {
				uni.showToast({
					title: '新密码长度为 6 到 20 个字符',
					icon: 'none'
				})
				return false
			}
			
			if (!this.formData.confirmPassword) {
				uni.showToast({
					title: '请确认新密码',
					icon: 'none'
				})
				return false
			}
			
			if (this.formData.newPassword !== this.formData.confirmPassword) {
				uni.showToast({
					title: '两次输入的密码不一致',
					icon: 'none'
				})
				return false
			}
			
			if (this.formData.oldPassword === this.formData.newPassword) {
				uni.showToast({
					title: '新密码不能与旧密码相同',
					icon: 'none'
				})
				return false
			}
			
			return true
		},
		
		async handleSubmit() {
			if (!this.validateForm()) {
				return
			}
			
			this.loading = true
			
			try {
				await userApi.updatePwd(this.formData.oldPassword, this.formData.newPassword)
				
				uni.showToast({
					title: '修改成功',
					icon: 'success'
				})
				
				setTimeout(() => {
					uni.navigateBack()
				}, 1500)
			} catch (error) {
				console.error('修改密码失败:', error)
				uni.showToast({
					title: error.message || '修改失败',
					icon: 'none'
				})
			} finally {
				this.loading = false
			}
		}
	}
}
</script>

<style lang="scss" scoped>
.password-container {
	min-height: 100vh;
	background: #F5F7FA;
	padding-bottom: 120rpx;
}

.form-section {
	background: #FFFFFF;
	margin: 20rpx 30rpx;
	border-radius: 16rpx;
	overflow: hidden;
}

.form-item {
	display: flex;
	align-items: center;
	padding: 30rpx;
	border-bottom: 1rpx solid #F0F0F0;
	
	&:last-child {
		border-bottom: none;
	}
	
	.form-label {
		font-size: 28rpx;
		color: #333333;
		width: 160rpx;
		flex-shrink: 0;
	}
	
	.form-input {
		flex: 1;
		font-size: 28rpx;
		color: #333333;
	}
	
	.input-placeholder {
		color: #CCCCCC;
	}
}

.tips-section {
	margin: 20rpx 30rpx;
	padding: 24rpx 30rpx;
	background: #FFF7E6;
	border-radius: 12rpx;
	border-left: 4rpx solid #FFA940;
	
	.tips-title {
		font-size: 26rpx;
		color: #FA8C16;
		font-weight: bold;
		margin-bottom: 10rpx;
	}
	
	.tips-item {
		font-size: 24rpx;
		color: #FA8C16;
		line-height: 1.6;
	}
}

.btn-section {
	padding: 40rpx 30rpx;
	
	.submit-btn {
		width: 100%;
		height: 88rpx;
		background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
		border-radius: 12rpx;
		color: #FFFFFF;
		font-size: 32rpx;
		border: none;
		
		&:active {
			opacity: 0.8;
		}
	}
}
</style>
