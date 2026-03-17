<template>
	<view class="history-container">
		<view class="search-bar">
			<view class="search-input">
				<text class="search-icon">🔍</text>
				<input 
					v-model="searchForm.keyword" 
					placeholder="搜索总成名称或图号"
					@confirm="handleSearch"
				/>
			</view>
			<button class="search-btn" @click="handleSearch">搜索</button>
		</view>
		
		<view class="filter-bar">
			<picker mode="selector" :range="statusOptions" @change="handleStatusChange">
				<view class="filter-item">
					<text>{{ statusOptions[searchForm.statusIndex] || '全部状态' }}</text>
					<text class="arrow">▼</text>
				</view>
			</picker>
			
			<picker mode="date" @change="handleDateChange">
				<view class="filter-item">
					<text>{{ searchForm.date || '选择日期' }}</text>
					<text class="arrow">▼</text>
				</view>
			</picker>
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
					<view class="info-item">
						<text class="label">更新时间：</text>
						<text class="value">{{ item.updateTime }}</text>
					</view>
				</view>
			</view>
			
			<view class="empty" v-if="taskList.length === 0 && !loading">
				<text class="empty-text">暂无历史记录</text>
			</view>
			
			<view class="loading" v-if="loading">
				<text class="loading-text">加载中...</text>
			</view>
		</view>
	</view>
</template>

<script>
import taskApi from '@/api/task.js'

export default {
	data() {
		return {
			searchForm: {
				keyword: '',
				statusIndex: 0,
				date: ''
			},
			statusOptions: ['全部状态', '未开始', '进行中', '已完成'],
			taskList: [],
			loading: false,
			pageNum: 1,
			pageSize: 10,
			hasMore: true
		}
	},
	
	onShow() {
		this.getTaskList()
	},
	
	onPullDownRefresh() {
		this.pageNum = 1
		this.hasMore = true
		this.getTaskList().then(() => {
			uni.stopPullDownRefresh()
		})
	},
	
	onReachBottom() {
		if (this.hasMore && !this.loading) {
			this.pageNum++
			this.getTaskList()
		}
	},
	
	methods: {
		async getTaskList() {
			this.loading = true
			
			try {
				const params = {
					pageNum: this.pageNum,
					pageSize: this.pageSize,
					keyword: this.searchForm.keyword
				}
				
				if (this.searchForm.statusIndex > 0) {
					params.status = this.searchForm.statusIndex - 1
				}
				
				if (this.searchForm.date) {
					params.date = this.searchForm.date
				}
				
				const res = await taskApi.getTaskList(params)
				
				if (this.pageNum === 1) {
					this.taskList = res.rows || []
				} else {
					this.taskList = [...this.taskList, ...(res.rows || [])]
				}
				
				this.hasMore = this.taskList.length < res.total
			} catch (error) {
				console.error('获取任务列表失败:', error)
			} finally {
				this.loading = false
			}
		},
		
		handleSearch() {
			this.pageNum = 1
			this.hasMore = true
			this.getTaskList()
		},
		
		handleStatusChange(e) {
			this.searchForm.statusIndex = e.detail.value
			this.handleSearch()
		},
		
		handleDateChange(e) {
			this.searchForm.date = e.detail.value
			this.handleSearch()
		},
		
		handleTaskClick(task) {
			uni.navigateTo({
				url: `/pages/task-detail/task-detail?taskId=${task.taskId}`
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
.history-container {
	min-height: 100vh;
	background: #F5F7FA;
}

.search-bar {
	background: #FFFFFF;
	padding: 20rpx 30rpx;
	display: flex;
	align-items: center;
	gap: 20rpx;
	
	.search-input {
		flex: 1;
		display: flex;
		align-items: center;
		background: #F5F7FA;
		border-radius: 40rpx;
		padding: 0 30rpx;
		height: 70rpx;
		
		.search-icon {
			font-size: 28rpx;
			margin-right: 10rpx;
		}
		
		input {
			flex: 1;
			font-size: 28rpx;
		}
	}
	
	.search-btn {
		width: 140rpx;
		height: 70rpx;
		background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
		border-radius: 40rpx;
		color: #FFFFFF;
		font-size: 28rpx;
		border: none;
		line-height: 70rpx;
		padding: 0;
	}
}

.filter-bar {
	background: #FFFFFF;
	padding: 20rpx 30rpx;
	display: flex;
	gap: 20rpx;
	border-top: 1rpx solid #F0F0F0;
	
	.filter-item {
		flex: 1;
		display: flex;
		justify-content: space-between;
		align-items: center;
		background: #F5F7FA;
		border-radius: 12rpx;
		padding: 16rpx 24rpx;
		font-size: 26rpx;
		color: #666666;
		
		.arrow {
			font-size: 20rpx;
			color: #999999;
		}
	}
}

.task-list {
	padding: 30rpx;
	
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
	
	.loading {
		text-align: center;
		padding: 30rpx 0;
		
		.loading-text {
			font-size: 28rpx;
			color: #999999;
		}
	}
}
</style>
