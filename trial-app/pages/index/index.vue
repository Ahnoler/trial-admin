<template>
	<view class="index-container">
		<view class="scan-section">
			<view class="scan-card" @click="handleScan">
				<view class="scan-icon">
					<text class="iconfont icon-scan"></text>
				</view>
				<text class="scan-text">扫码填报</text>
			</view>
		</view>
		
		<view class="task-section">
			<view class="section-header">
				<text class="title">待办任务</text>
				<text class="more" @click="handleMore">查看更多</text>
			</view>
			
			<view class="task-list">
				<view 
					class="task-item" 
					v-for="item in taskList" 
					:key="item.taskId"
					@click="handleTaskClick(item)"
				>
					<view class="task-header">
						<text class="task-name">{{ item.assemblyName }}</text>
						<text class="task-status" :class="getStatusClass(item.status)">
							{{ getStatusText(item.status) }}
						</text>
					</view>
					
					<view class="task-info">
						<view class="info-item">
							<text class="label">图号：</text>
							<text class="value">{{ item.assemblyFigure }}</text>
						</view>
						<view class="info-item">
							<text class="label">当前环节：</text>
							<text class="value">{{ item.currentSerialName }}</text>
						</view>
						<view class="info-item">
							<text class="label">负责人：</text>
							<text class="value">{{ item.pm }}</text>
						</view>
					</view>
				</view>
				
				<view class="empty" v-if="taskList.length === 0">
					<text class="empty-text">暂无待办任务</text>
				</view>
			</view>
		</view>
	</view>
</template>

<script>
import { useUserStore } from '@/store/user.js'
import taskApi from '@/api/task.js'

export default {
	data() {
		return {
			taskList: [],
			loading: false
		}
	},
	
	onShow() {
		this.checkLogin()
		this.getTaskList()
	},
	
	onPullDownRefresh() {
		this.getTaskList().then(() => {
			uni.stopPullDownRefresh()
		})
	},
	
	methods: {
		checkLogin() {
			const userStore = useUserStore()
			if (!userStore.isLoggedIn) {
				uni.redirectTo({
					url: '/pages/login/login'
				})
			}
		},
		
		async getTaskList() {
			this.loading = true
			try {
				const res = await taskApi.getMyTaskList({ pageNum: 1, pageSize: 10 })
				this.taskList = res.rows || []
			} catch (error) {
				console.error('获取任务列表失败:', error)
			} finally {
				this.loading = false
			}
		},
		
		handleScan() {
			uni.scanCode({
				success: async (res) => {
					try {
						uni.showLoading({ title: '识别中...' })
						const result = await taskApi.scanQRCode(res.result)
						uni.hideLoading()
						
						if (result.data) {
							uni.navigateTo({
								url: `/pages/task-detail/task-detail?taskId=${result.data.taskId}`
							})
						}
					} catch (error) {
						uni.hideLoading()
						uni.showToast({
							title: '二维码识别失败',
							icon: 'none'
						})
					}
				},
				fail: () => {
					uni.showToast({
						title: '扫码取消',
						icon: 'none'
					})
				}
			})
		},
		
		handleTaskClick(task) {
			uni.navigateTo({
				url: `/pages/task-detail/task-detail?taskId=${task.taskId}`
			})
		},
		
		handleMore() {
			uni.switchTab({
				url: '/pages/history/history'
			})
		},
		
		getStatusClass(status) {
			const statusMap = {
				'0': 'status-pending',
				'1': 'status-progress',
				'2': 'status-completed'
			}
			return statusMap[status] || 'status-pending'
		},
		
		getStatusText(status) {
			const statusMap = {
				'0': '未开始',
				'1': '进行中',
				'2': '已完成'
			}
			return statusMap[status] || '未知'
		}
	}
}
</script>

<style lang="scss" scoped>
.index-container {
	min-height: 100vh;
	background: #F5F7FA;
	padding-bottom: 20rpx;
}

.scan-section {
	padding: 40rpx 30rpx;
	background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
}

.scan-card {
	background: #FFFFFF;
	border-radius: 20rpx;
	padding: 60rpx;
	display: flex;
	flex-direction: column;
	align-items: center;
	box-shadow: 0 10rpx 30rpx rgba(0, 0, 0, 0.1);
	
	.scan-icon {
		width: 120rpx;
		height: 120rpx;
		background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
		border-radius: 50%;
		display: flex;
		align-items: center;
		justify-content: center;
		margin-bottom: 20rpx;
		
		.iconfont {
			font-size: 60rpx;
			color: #FFFFFF;
		}
	}
	
	.scan-text {
		font-size: 32rpx;
		font-weight: bold;
		color: #333333;
	}
}

.task-section {
	padding: 30rpx;
}

.section-header {
	display: flex;
	justify-content: space-between;
	align-items: center;
	margin-bottom: 20rpx;
	
	.title {
		font-size: 32rpx;
		font-weight: bold;
		color: #333333;
	}
	
	.more {
		font-size: 26rpx;
		color: #007AFF;
	}
}

.task-list {
	.task-item {
		background: #FFFFFF;
		border-radius: 16rpx;
		padding: 30rpx;
		margin-bottom: 20rpx;
		box-shadow: 0 4rpx 12rpx rgba(0, 0, 0, 0.05);
		
		.task-header {
			display: flex;
			justify-content: space-between;
			align-items: center;
			margin-bottom: 20rpx;
			
			.task-name {
				font-size: 30rpx;
				font-weight: bold;
				color: #333333;
				flex: 1;
				overflow: hidden;
				text-overflow: ellipsis;
				white-space: nowrap;
			}
			
			.task-status {
				font-size: 24rpx;
				padding: 8rpx 16rpx;
				border-radius: 8rpx;
				margin-left: 20rpx;
				
				&.status-pending {
					background: #FFF3E0;
					color: #FF9800;
				}
				
				&.status-progress {
					background: #E3F2FD;
					color: #2196F3;
				}
				
				&.status-completed {
					background: #E8F5E9;
					color: #4CAF50;
				}
			}
		}
		
		.task-info {
			.info-item {
				display: flex;
				margin-bottom: 12rpx;
				
				&:last-child {
					margin-bottom: 0;
				}
				
				.label {
					font-size: 26rpx;
					color: #999999;
					min-width: 140rpx;
				}
				
				.value {
					font-size: 26rpx;
					color: #666666;
					flex: 1;
				}
			}
		}
	}
	
	.empty {
		text-align: center;
		padding: 100rpx 0;
		
		.empty-text {
			font-size: 28rpx;
			color: #999999;
		}
	}
}
</style>
