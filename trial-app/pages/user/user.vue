<template>
	<view class="user-container">
		<view class="user-header">
			<view class="user-avatar">
				<image class="avatar-img" :src="avatarUrl" mode="aspectFill"></image>
			</view>
			<text class="user-name">{{ userInfo.userName || '未登录' }}</text>
			<text class="user-role">{{ userInfo.dept?.deptName || '暂无部门' }}</text>
		</view>
		
		<view class="user-stats">
			<view class="stat-item">
				<text class="stat-value">{{ stats.total }}</text>
				<text class="stat-label">总任务</text>
			</view>
			<view class="stat-item">
				<text class="stat-value">{{ stats.progress }}</text>
				<text class="stat-label">进行中</text>
			</view>
			<view class="stat-item">
				<text class="stat-value">{{ stats.completed }}</text>
				<text class="stat-label">已完成</text>
			</view>
		</view>
		
		<view class="menu-section">
			<view class="menu-item" @click="handleMenuClick('profile')">
				<view class="menu-left">
					<text class="menu-icon">👤</text>
					<text class="menu-text">个人信息</text>
				</view>
				<text class="menu-arrow">></text>
			</view>
			
			<view class="menu-item" @click="handleMenuClick('password')">
				<view class="menu-left">
					<text class="menu-icon">🔒</text>
					<text class="menu-text">修改密码</text>
				</view>
				<text class="menu-arrow">></text>
			</view>
			
			<view class="menu-item" @click="handleMenuClick('feedback')">
				<view class="menu-left">
					<text class="menu-icon">💬</text>
					<text class="menu-text">意见反馈</text>
				</view>
				<text class="menu-arrow">></text>
			</view>
			
			<view class="menu-item" @click="handleMenuClick('about')">
				<view class="menu-left">
					<text class="menu-icon">ℹ️</text>
					<text class="menu-text">关于我们</text>
				</view>
				<text class="menu-arrow">></text>
			</view>
		</view>
		
		<view class="logout-section">
			<button class="logout-btn" @click="handleLogout">退出登录</button>
		</view>
		
		<view class="version-info">
			<text class="version-text">版本：1.0.0</text>
		</view>
	</view>
</template>

<script>
import { useUserStore } from '@/store/user.js'
import taskApi from '@/api/task.js'
import config from '@/config/index.js'

export default {
	data() {
		return {
			userInfo: {},
			stats: {
				total: 0,
				progress: 0,
				completed: 0
			}
		}
	},
	
	computed: {
		avatarUrl() {
			if (this.userInfo.avatar) {
				return config.BASE_URL + this.userInfo.avatar
			}
			return '/static/logo.png'
		}
	},
	
	onShow() {
		this.getUserInfo()
		this.getStats()
	},
	
	methods: {
		getUserInfo() {
			const userStore = useUserStore()
			this.userInfo = userStore.userInfo || {}
		},
		
		async getStats() {
			try {
				const res = await taskApi.getTaskList({ pageNum: 1, pageSize: 1000 })
				const tasks = res.rows || []
				
				this.stats = {
					total: tasks.length,
					progress: tasks.filter(t => t.status === '1').length,
					completed: tasks.filter(t => t.status === '2').length
				}
			} catch (error) {
				console.error('获取统计数据失败:', error)
			}
		},
		
		handleMenuClick(type) {
			switch (type) {
				case 'profile':
					uni.navigateTo({
						url: '/pages/profile/profile'
					})
					break
				case 'password':
					uni.navigateTo({
						url: '/pages/password/password'
					})
					break
				case 'feedback':
					uni.showToast({
						title: '功能开发中',
						icon: 'none'
					})
					break
				case 'about':
					uni.showModal({
						title: '关于我们',
						content: '试制任务管理系统 v1.0.0\n\n用于管理试制任务的质量信息流转卡填报工作。',
						showCancel: false
					})
					break
			}
		},
		
		handleLogout() {
			uni.showModal({
				title: '提示',
				content: '确定要退出登录吗？',
				success: async (res) => {
					if (res.confirm) {
						const userStore = useUserStore()
						await userStore.logout()
						
						uni.reLaunch({
							url: '/pages/login/login'
						})
					}
				}
			})
		}
	}
}
</script>

<style lang="scss" scoped>
.user-container {
	min-height: 100vh;
	background: #F5F7FA;
}

.user-header {
	background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
	padding: 80rpx 30rpx 60rpx;
	display: flex;
	flex-direction: column;
	align-items: center;
	
	.user-avatar {
		width: 160rpx;
		height: 160rpx;
		border-radius: 50%;
		overflow: hidden;
		border: 6rpx solid rgba(255, 255, 255, 0.3);
		margin-bottom: 30rpx;
		
		.avatar-img {
			width: 100%;
			height: 100%;
		}
	}
	
	.user-name {
		font-size: 36rpx;
		font-weight: bold;
		color: #FFFFFF;
		margin-bottom: 10rpx;
	}
	
	.user-role {
		font-size: 26rpx;
		color: rgba(255, 255, 255, 0.8);
	}
}

.user-stats {
	background: #FFFFFF;
	margin: -40rpx 30rpx 30rpx;
	border-radius: 16rpx;
	padding: 40rpx;
	display: flex;
	box-shadow: 0 10rpx 30rpx rgba(0, 0, 0, 0.1);
	
	.stat-item {
		flex: 1;
		text-align: center;
		
		.stat-value {
			display: block;
			font-size: 48rpx;
			font-weight: bold;
			color: #333333;
			margin-bottom: 10rpx;
		}
		
		.stat-label {
			display: block;
			font-size: 26rpx;
			color: #999999;
		}
	}
}

.menu-section {
	background: #FFFFFF;
	margin: 0 30rpx 30rpx;
	border-radius: 16rpx;
	overflow: hidden;
	
	.menu-item {
		display: flex;
		justify-content: space-between;
		align-items: center;
		padding: 30rpx;
		border-bottom: 1rpx solid #F0F0F0;
		
		&:last-child {
			border-bottom: none;
		}
		
		.menu-left {
			display: flex;
			align-items: center;
			
			.menu-icon {
				font-size: 40rpx;
				margin-right: 20rpx;
			}
			
			.menu-text {
				font-size: 30rpx;
				color: #333333;
			}
		}
		
		.menu-arrow {
			font-size: 28rpx;
			color: #999999;
		}
	}
}

.logout-section {
	padding: 60rpx 30rpx 30rpx;
	
	.logout-btn {
		width: 100%;
		height: 88rpx;
		background: #FFFFFF;
		border: 2rpx solid #FF4D4F;
		border-radius: 12rpx;
		color: #FF4D4F;
		font-size: 32rpx;
	}
}

.version-info {
	text-align: center;
	padding: 30rpx 0;
	
	.version-text {
		font-size: 24rpx;
		color: #999999;
	}
}
</style>
