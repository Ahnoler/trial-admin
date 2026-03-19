<template>
	<view class="profile-container">
		<view class="profile-header">
			<view class="avatar-section" @click="handleAvatarClick">
				<image class="avatar-img" :src="avatarUrl" mode="aspectFill"></image>
				<view class="avatar-edit">
					<text class="edit-text">编辑</text>
				</view>
			</view>
			<text class="user-name">{{ userInfo.nickName || userInfo.userName }}</text>
		</view>
		
		<view class="info-section">
			<view class="section-title">基本信息</view>
			
			<view class="info-item">
				<text class="info-label">用户名</text>
				<text class="info-value">{{ userInfo.userName }}</text>
			</view>
			
			<view class="info-item">
				<text class="info-label">用户昵称</text>
				<input 
					class="info-input" 
					v-model="formData.nickName" 
					placeholder="请输入昵称"
				/>
			</view>
			
			<view class="info-item">
				<text class="info-label">手机号码</text>
				<input 
					class="info-input" 
					v-model="formData.phonenumber" 
					type="number"
					placeholder="请输入手机号码"
				/>
			</view>
			
			<view class="info-item">
				<text class="info-label">邮箱</text>
				<input 
					class="info-input" 
					v-model="formData.email" 
					placeholder="请输入邮箱"
				/>
			</view>
			
			<view class="info-item">
				<text class="info-label">性别</text>
				<picker 
					mode="selector" 
					:range="sexOptions" 
					:value="sexIndex"
					@change="handleSexChange"
				>
					<view class="info-picker">
						<text class="picker-text">{{ sexOptions[sexIndex] }}</text>
						<text class="picker-arrow">></text>
					</view>
				</picker>
			</view>
		</view>
		
		<view class="info-section">
			<view class="section-title">其他信息</view>
			
			<view class="info-item">
				<text class="info-label">所属部门</text>
				<text class="info-value">{{ userInfo.dept?.deptName || '暂无' }}</text>
			</view>
			
			<view class="info-item">
				<text class="info-label">角色</text>
				<text class="info-value">{{ roleGroup || '暂无' }}</text>
			</view>
			
			<view class="info-item">
				<text class="info-label">创建时间</text>
				<text class="info-value">{{ userInfo.createTime || '暂无' }}</text>
			</view>
		</view>
		
		<view class="btn-section">
			<button class="save-btn" @click="handleSave" :loading="loading">保存修改</button>
		</view>
	</view>
</template>

<script>
import userApi from '@/api/user.js'
import uploadApi from '@/api/upload.js'
import config from '@/config/index.js'
import { useUserStore } from '@/store/user.js'

export default {
	data() {
		return {
			userInfo: {},
			roleGroup: '',
			formData: {
				nickName: '',
				phonenumber: '',
				email: '',
				sex: '0'
			},
			sexOptions: ['男', '女'],
			loading: false
		}
	},
	
	computed: {
		sexIndex: {
			get() {
				return this.formData.sex === '1' ? 1 : 0
			},
			set(val) {
				this.formData.sex = val === 1 ? '1' : '0'
			}
		},
		avatarUrl() {
			if (this.userInfo.avatar) {
				return config.BASE_URL + this.userInfo.avatar
			}
			return '/static/logo.png'
		}
	},
	
	onLoad() {
		this.getProfile()
	},
	
	methods: {
		async getProfile() {
			try {
				const res = await userApi.getProfile()
				this.userInfo = res.data || {}
				this.roleGroup = res.roleGroup || ''
				
				this.formData = {
					userId: this.userInfo.userId,
					nickName: this.userInfo.nickName || '',
					phonenumber: this.userInfo.phonenumber || '',
					email: this.userInfo.email || '',
					sex: this.userInfo.sex || '0'
				}
			} catch (error) {
				console.error('获取个人信息失败:', error)
			}
		},
		
		handleSexChange(e) {
			this.sexIndex = parseInt(e.detail.value)
		},
		
		handleAvatarClick() {
			uni.showActionSheet({
				itemList: ['拍照', '从相册选择'],
				success: async (res) => {
					const sourceType = res.tapIndex === 0 ? ['camera'] : ['album']
					
					uni.chooseImage({
						count: 1,
						sizeType: ['compressed'],
						sourceType: sourceType,
						success: async (chooseRes) => {
							try {
								uni.showLoading({ title: '上传中...' })
								
								const uploadRes = await this.uploadAvatar(chooseRes.tempFilePaths[0])
								
								this.userInfo.avatar = uploadRes.imgUrl
								
								const userStore = useUserStore()
								userStore.setUserInfo(this.userInfo)
								
								uni.hideLoading()
								uni.showToast({
									title: '头像更新成功',
									icon: 'success'
								})
							} catch (error) {
								uni.hideLoading()
								console.error('上传头像失败:', error)
							}
						}
					})
				}
			})
		},
		
		uploadAvatar(filePath) {
			return new Promise((resolve, reject) => {
				const token = uni.getStorageSync(config.TOKEN_KEY)
				
				uni.uploadFile({
					url: config.BASE_URL + '/system/user/profile/avatar',
					filePath: filePath,
					name: 'avatarfile',
					header: {
						'Authorization': token ? `Bearer ${token}` : ''
					},
					success: (res) => {
						const data = JSON.parse(res.data)
						if (data.code === 200) {
							resolve(data)
						} else {
							reject(new Error(data.msg || '上传失败'))
						}
					},
					fail: reject
				})
			})
		},
		
		async handleSave() {
			if (!this.formData.nickName) {
				uni.showToast({
					title: '请输入昵称',
					icon: 'none'
				})
				return
			}
			
			this.loading = true
			
			try {
				await userApi.updateProfile(this.formData)
				
				this.userInfo.nickName = this.formData.nickName
				this.userInfo.phonenumber = this.formData.phonenumber
				this.userInfo.email = this.formData.email
				this.userInfo.sex = this.formData.sex
				
				const userStore = useUserStore()
				userStore.setUserInfo(this.userInfo)
				
				uni.showToast({
					title: '保存成功',
					icon: 'success'
				})
			} catch (error) {
				console.error('保存失败:', error)
			} finally {
				this.loading = false
			}
		}
	}
}
</script>

<style lang="scss" scoped>
.profile-container {
	min-height: 100vh;
	background: #F5F7FA;
	padding-bottom: 120rpx;
}

.profile-header {
	background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
	padding: 60rpx 30rpx;
	display: flex;
	flex-direction: column;
	align-items: center;
	
	.avatar-section {
		position: relative;
		width: 160rpx;
		height: 160rpx;
		margin-bottom: 20rpx;
		
		.avatar-img {
			width: 100%;
			height: 100%;
			border-radius: 50%;
			border: 4rpx solid rgba(255, 255, 255, 0.3);
		}
		
		.avatar-edit {
			position: absolute;
			bottom: 0;
			left: 0;
			right: 0;
			height: 50rpx;
			background: rgba(0, 0, 0, 0.5);
			border-radius: 0 0 80rpx 80rpx;
			display: flex;
			align-items: center;
			justify-content: center;
			
			.edit-text {
				font-size: 22rpx;
				color: #FFFFFF;
			}
		}
	}
	
	.user-name {
		font-size: 34rpx;
		font-weight: bold;
		color: #FFFFFF;
	}
}

.info-section {
	background: #FFFFFF;
	margin: 20rpx 30rpx;
	border-radius: 16rpx;
	overflow: hidden;
	
	.section-title {
		padding: 24rpx 30rpx;
		font-size: 28rpx;
		color: #999999;
		border-bottom: 1rpx solid #F0F0F0;
	}
}

.info-item {
	display: flex;
	justify-content: space-between;
	align-items: center;
	padding: 28rpx 30rpx;
	border-bottom: 1rpx solid #F0F0F0;
	
	&:last-child {
		border-bottom: none;
	}
	
	.info-label {
		font-size: 28rpx;
		color: #333333;
		width: 160rpx;
	}
	
	.info-value {
		flex: 1;
		font-size: 28rpx;
		color: #666666;
		text-align: right;
	}
	
	.info-input {
		flex: 1;
		font-size: 28rpx;
		text-align: right;
		color: #333333;
	}
	
	.info-picker {
		flex: 1;
		display: flex;
		justify-content: flex-end;
		align-items: center;
		
		.picker-text {
			font-size: 28rpx;
			color: #333333;
		}
		
		.picker-arrow {
			font-size: 24rpx;
			color: #999999;
			margin-left: 10rpx;
		}
	}
}

.btn-section {
	padding: 40rpx 30rpx;
	
	.save-btn {
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
