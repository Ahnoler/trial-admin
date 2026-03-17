<template>
	<view class="task-detail-container">
		<view class="task-header">
			<view class="header-item">
				<text class="label">总成名称</text>
				<text class="value">{{ taskInfo.assemblyName }}</text>
			</view>
			<view class="header-item">
				<text class="label">总成图号</text>
				<text class="value">{{ taskInfo.assemblyFigure }}</text>
			</view>
			<view class="header-item">
				<text class="label">项目负责人</text>
				<text class="value">{{ taskInfo.pm }}</text>
			</view>
			<view class="header-item">
				<text class="label">当前环节</text>
				<text class="value highlight">{{ taskInfo.currentSerialName }}</text>
			</view>
		</view>
		
		<view class="process-section">
			<view class="section-title">流转程序</view>
			
			<view class="process-list">
				<view 
					class="process-item" 
					v-for="(item, index) in processList" 
					:key="item.id"
					:class="{
						'current': item.serialNo === taskInfo.currentSerialNo,
						'completed': item.status === '3',
						'pending': item.status === '0'
					}"
					@click="handleProcessClick(item)"
				>
					<view class="process-left">
						<view class="process-icon">
							<text class="iconfont" :class="getProcessIcon(item.status)"></text>
						</view>
						<view class="process-line" v-if="index < processList.length - 1"></view>
					</view>
					
					<view class="process-content">
						<view class="process-header">
							<text class="process-name">{{ item.program }}</text>
							<text class="process-status" :class="'status-' + item.status">
								{{ getStatusText(item.status) }}
							</text>
						</view>
						
						<view class="process-info">
							<text class="info-text">名称：{{ item.name }}</text>
							<text class="info-text">试制数量：{{ item.trialQuantity }}</text>
							<text class="info-text">送检数量：{{ item.inspectionQuantity }}</text>
						</view>
						
						<view class="process-action" v-if="item.status === '1'">
							<button class="action-btn" @click.stop="handleFill(item)">立即填报</button>
						</view>
					</view>
				</view>
			</view>
		</view>
	</view>
</template>

<script>
import taskApi from '@/api/task.js'

export default {
	data() {
		return {
			taskId: null,
			taskInfo: {},
			processList: [],
			loading: false
		}
	},
	
	onLoad(options) {
		this.taskId = options.taskId
		this.getTaskDetail()
		this.getProcessList()
	},
	
	onPullDownRefresh() {
		Promise.all([
			this.getTaskDetail(),
			this.getProcessList()
		]).then(() => {
			uni.stopPullDownRefresh()
		})
	},
	
	methods: {
		async getTaskDetail() {
			try {
				const res = await taskApi.getTaskDetail(this.taskId)
				this.taskInfo = res.data || {}
				
				this.scrollToCurrentProcess()
			} catch (error) {
				console.error('获取任务详情失败:', error)
			}
		},
		
		async getProcessList() {
			try {
				const res = await taskApi.getTaskProcessList(this.taskId)
				this.processList = res.rows || []
			} catch (error) {
				console.error('获取流转程序失败:', error)
			}
		},
		
		scrollToCurrentProcess() {
			this.$nextTick(() => {
				const currentIndex = this.processList.findIndex(
					item => item.serialNo === this.taskInfo.currentSerialNo
				)
				
				if (currentIndex > 0) {
					uni.pageScrollTo({
						scrollTop: currentIndex * 200,
						duration: 300
					})
				}
			})
		},
		
		handleProcessClick(process) {
			if (process.status === '1') {
				this.handleFill(process)
			}
		},
		
		handleFill(process) {
			uni.navigateTo({
				url: `/pages/fill-form/fill-form?id=${process.id}&taskId=${this.taskId}`
			})
		},
		
		getProcessIcon(status) {
			const iconMap = {
				'0': 'icon-pending',
				'1': 'icon-progress',
				'2': 'icon-review',
				'3': 'icon-completed'
			}
			return iconMap[status] || 'icon-pending'
		},
		
		getStatusText(status) {
			const statusMap = {
				'0': '未填报',
				'1': '正在填报',
				'2': '待审核',
				'3': '已审核'
			}
			return statusMap[status] || '未知'
		}
	}
}
</script>

<style lang="scss" scoped>
.task-detail-container {
	min-height: 100vh;
	background: #F5F7FA;
	padding-bottom: 30rpx;
}

.task-header {
	background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
	padding: 40rpx 30rpx;
	
	.header-item {
		display: flex;
		justify-content: space-between;
		align-items: center;
		margin-bottom: 20rpx;
		
		&:last-child {
			margin-bottom: 0;
		}
		
		.label {
			font-size: 28rpx;
			color: rgba(255, 255, 255, 0.8);
		}
		
		.value {
			font-size: 28rpx;
			color: #FFFFFF;
			font-weight: 500;
			
			&.highlight {
				color: #FFD700;
				font-weight: bold;
			}
		}
	}
}

.process-section {
	padding: 30rpx;
}

.section-title {
	font-size: 32rpx;
	font-weight: bold;
	color: #333333;
	margin-bottom: 30rpx;
}

.process-list {
	.process-item {
		display: flex;
		margin-bottom: 30rpx;
		
		&.current {
			.process-icon {
				background: #007AFF;
				color: #FFFFFF;
			}
		}
		
		&.completed {
			.process-icon {
				background: #4CAF50;
				color: #FFFFFF;
			}
		}
		
		&.pending {
			.process-icon {
				background: #E0E0E0;
				color: #999999;
			}
		}
	}
	
	.process-left {
		display: flex;
		flex-direction: column;
		align-items: center;
		margin-right: 20rpx;
		
		.process-icon {
			width: 60rpx;
			height: 60rpx;
			border-radius: 50%;
			display: flex;
			align-items: center;
			justify-content: center;
			background: #007AFF;
			color: #FFFFFF;
			
			.iconfont {
				font-size: 32rpx;
			}
		}
		
		.process-line {
			width: 2rpx;
			height: 100rpx;
			background: #E0E0E0;
			margin-top: 10rpx;
		}
	}
	
	.process-content {
		flex: 1;
		background: #FFFFFF;
		border-radius: 16rpx;
		padding: 30rpx;
		box-shadow: 0 4rpx 12rpx rgba(0, 0, 0, 0.05);
		
		.process-header {
			display: flex;
			justify-content: space-between;
			align-items: center;
			margin-bottom: 20rpx;
			
			.process-name {
				font-size: 30rpx;
				font-weight: bold;
				color: #333333;
			}
			
			.process-status {
				font-size: 24rpx;
				padding: 6rpx 16rpx;
				border-radius: 8rpx;
				
				&.status-0 {
					background: #F5F5F5;
					color: #999999;
				}
				
				&.status-1 {
					background: #E3F2FD;
					color: #2196F3;
				}
				
				&.status-2 {
					background: #FFF3E0;
					color: #FF9800;
				}
				
				&.status-3 {
					background: #E8F5E9;
					color: #4CAF50;
				}
			}
		}
		
		.process-info {
			margin-bottom: 20rpx;
			
			.info-text {
				display: block;
				font-size: 26rpx;
				color: #666666;
				margin-bottom: 8rpx;
			}
		}
		
		.process-action {
			.action-btn {
				width: 100%;
				height: 70rpx;
				background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
				border-radius: 12rpx;
				color: #FFFFFF;
				font-size: 28rpx;
				border: none;
				
				&:active {
					opacity: 0.8;
				}
			}
		}
	}
}
</style>
