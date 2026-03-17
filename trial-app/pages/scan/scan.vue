<template>
	<view class="scan-container">
		<view class="scan-area">
			<camera 
				class="camera" 
				device-position="back" 
				flash="auto" 
				@error="handleCameraError"
			></camera>
			
			<view class="scan-frame">
				<view class="scan-line"></view>
			</view>
			
			<view class="scan-tip">将二维码放入框内，即可自动扫描</view>
		</view>
		
		<view class="scan-actions">
			<view class="action-item" @click="handleScanAgain">
				<text class="action-icon">🔄</text>
				<text class="action-text">重新扫描</text>
			</view>
			
			<view class="action-item" @click="handleManualInput">
				<text class="action-icon">⌨️</text>
				<text class="action-text">手动输入</text>
			</view>
		</view>
		
		<view class="scan-result" v-if="scanResult">
			<view class="result-header">
				<text class="result-title">扫描结果</text>
			</view>
			<view class="result-content">
				<text class="result-text">{{ scanResult }}</text>
			</view>
			<view class="result-actions">
				<button class="confirm-btn" @click="handleConfirm">确认</button>
				<button class="cancel-btn" @click="handleCancel">取消</button>
			</view>
		</view>
	</view>
</template>

<script>
import taskApi from '@/api/task.js'

export default {
	data() {
		return {
			scanResult: '',
			loading: false
		}
	},
	
	onLoad() {
		this.startScan()
	},
	
	onUnload() {
		this.stopScan()
	},
	
	methods: {
		startScan() {
			uni.scanCode({
				success: async (res) => {
					this.scanResult = res.result
					await this.handleScanResult(res.result)
				},
				fail: (err) => {
					console.error('扫码失败:', err)
					uni.showToast({
						title: '扫码失败',
						icon: 'none'
					})
				}
			})
		},
		
		stopScan() {
			// 停止扫码相关操作
		},
		
		async handleScanResult(qrCode) {
			this.loading = true
			
			try {
				uni.showLoading({ title: '识别中...' })
				const result = await taskApi.scanQRCode(qrCode)
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
			} finally {
				this.loading = false
			}
		},
		
		handleScanAgain() {
			this.scanResult = ''
			this.startScan()
		},
		
		handleManualInput() {
			uni.showModal({
				title: '手动输入',
				editable: true,
				placeholderText: '请输入任务ID',
				success: (res) => {
					if (res.confirm && res.content) {
						const taskId = res.content.trim()
						if (taskId) {
							uni.navigateTo({
								url: `/pages/task-detail/task-detail?taskId=${taskId}`
							})
						} else {
							uni.showToast({
								title: '请输入有效的任务ID',
								icon: 'none'
							})
						}
					}
				}
			})
		},
		
		handleCameraError(e) {
			console.error('相机错误:', e)
			uni.showToast({
				title: '相机打开失败',
				icon: 'none'
			})
		},
		
		handleConfirm() {
			if (this.scanResult) {
				this.handleScanResult(this.scanResult)
			}
		},
		
		handleCancel() {
			this.scanResult = ''
			uni.navigateBack()
		}
	}
}
</script>

<style lang="scss" scoped>
.scan-container {
	min-height: 100vh;
	background: #000000;
	display: flex;
	flex-direction: column;
}

.scan-area {
	flex: 1;
	position: relative;
	display: flex;
	flex-direction: column;
	align-items: center;
	justify-content: center;
	
	.camera {
		width: 100%;
		height: 100%;
	}
	
	.scan-frame {
		position: absolute;
		width: 500rpx;
		height: 500rpx;
		border: 4rpx solid #007AFF;
		border-radius: 20rpx;
		
		.scan-line {
			position: absolute;
			top: 0;
			left: 0;
			width: 100%;
			height: 4rpx;
			background: #007AFF;
			animation: scan 2s linear infinite;
		}
	}
	
	.scan-tip {
		position: absolute;
		bottom: 100rpx;
		font-size: 28rpx;
		color: #FFFFFF;
		text-align: center;
		padding: 0 60rpx;
	}
}

@keyframes scan {
	0% {
		top: 0;
	}
	100% {
		top: 100%;
	}
}

.scan-actions {
	background: #FFFFFF;
	padding: 40rpx 30rpx;
	display: flex;
	justify-content: space-around;
	
	.action-item {
		display: flex;
		flex-direction: column;
		align-items: center;
		
		.action-icon {
			font-size: 60rpx;
			margin-bottom: 10rpx;
		}
		
		.action-text {
			font-size: 26rpx;
			color: #666666;
		}
	}
}

.scan-result {
	position: fixed;
	top: 0;
	left: 0;
	right: 0;
	bottom: 0;
	background: rgba(0, 0, 0, 0.8);
	display: flex;
	flex-direction: column;
	align-items: center;
	justify-content: center;
	padding: 60rpx;
	
	.result-header {
		margin-bottom: 40rpx;
		
		.result-title {
			font-size: 36rpx;
			font-weight: bold;
			color: #FFFFFF;
		}
	}
	
	.result-content {
		background: #FFFFFF;
		border-radius: 16rpx;
		padding: 40rpx;
		margin-bottom: 40rpx;
		width: 100%;
		
		.result-text {
			font-size: 28rpx;
			color: #333333;
			word-break: break-all;
		}
	}
	
	.result-actions {
		display: flex;
		gap: 30rpx;
		width: 100%;
		
		.confirm-btn {
			flex: 1;
			height: 88rpx;
			background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
			border-radius: 12rpx;
			color: #FFFFFF;
			font-size: 32rpx;
			border: none;
		}
		
		.cancel-btn {
			flex: 1;
			height: 88rpx;
			background: #FFFFFF;
			border-radius: 12rpx;
			color: #333333;
			font-size: 32rpx;
			border: 2rpx solid #E0E0E0;
		}
	}
}
</style>
