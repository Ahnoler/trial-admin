<template>
	<view class="fill-form-container">
		<view class="form-header">
			<view class="header-item">
				<text class="label">流转程序</text>
				<text class="value">{{ processInfo.program }}</text>
			</view>
			<view class="header-item">
				<text class="label">名称</text>
				<text class="value">{{ processInfo.name }}</text>
			</view>
		</view>
		
		<view class="form-content">
			<view class="form-section">
				<view class="section-title">基本信息</view>
				
				<view class="form-item">
					<text class="item-label required">试制数量</text>
					<input 
						class="item-input" 
						v-model="formData.trialQuantity" 
						type="number"
						placeholder="请输入试制数量"
					/>
				</view>
				
				<view class="form-item">
					<text class="item-label required">送检数量</text>
					<input 
						class="item-input" 
						v-model="formData.inspectionQuantity" 
						type="number"
						placeholder="请输入送检数量"
					/>
				</view>
				
				<view class="form-item">
					<text class="item-label">制造质量状态</text>
					<picker 
						mode="selector" 
						:range="qualityStatusOptions" 
						@change="handleQualityChange"
					>
						<view class="item-picker">
							<text class="picker-text">{{ formData.manufacturingQualityStatus || '请选择' }}</text>
							<text class="picker-icon">></text>
						</view>
					</picker>
				</view>
				
				<view class="form-item">
					<text class="item-label">工艺质量状态</text>
					<picker 
						mode="selector" 
						:range="qualityStatusOptions" 
						@change="handleProcessQualityChange"
					>
						<view class="item-picker">
							<text class="picker-text">{{ formData.processQualityStatus || '请选择' }}</text>
							<text class="picker-icon">></text>
						</view>
					</picker>
				</view>
				
				<view class="form-item">
					<text class="item-label">制造区域</text>
					<input 
						class="item-input" 
						v-model="formData.manufacturingArea" 
						placeholder="请输入制造区域"
					/>
				</view>
			</view>
			
			<view class="form-section">
				<view class="section-title">负责人信息</view>
				
				<view class="form-item">
					<text class="item-label">负责人</text>
					<input 
						class="item-input" 
						v-model="formData.director" 
						placeholder="请输入负责人"
					/>
				</view>
				
				<view class="form-item">
					<text class="item-label">负责人电话</text>
					<input 
						class="item-input" 
						v-model="formData.directorTel" 
						type="number"
						placeholder="请输入负责人电话"
					/>
				</view>
				
				<view class="form-item">
					<text class="item-label">责任ME</text>
					<input 
						class="item-input" 
						v-model="formData.meDirector" 
						placeholder="请输入责任ME"
					/>
				</view>
				
				<view class="form-item">
					<text class="item-label">ME联系电话</text>
					<input 
						class="item-input" 
						v-model="formData.meDirectorTel" 
						type="number"
						placeholder="请输入ME联系电话"
					/>
				</view>
			</view>
			
			<view class="form-section">
				<view class="section-title">其他信息</view>
				
				<view class="form-item">
					<text class="item-label">图号</text>
					<input 
						class="item-input" 
						v-model="formData.figure" 
						placeholder="请输入图号"
					/>
				</view>
				
				<view class="form-item">
					<text class="item-label">备注</text>
					<textarea 
						class="item-textarea" 
						v-model="formData.notes" 
						placeholder="请输入备注"
						:maxlength="500"
					/>
				</view>
				
				<view class="form-item">
					<text class="item-label">附件上传</text>
					<view class="upload-area">
						<view class="upload-btn" @click="handleUpload">
							<text class="upload-icon">+</text>
							<text class="upload-text">上传图片</text>
						</view>
						<view class="image-list">
							<view 
								class="image-item" 
								v-for="(img, index) in imageList" 
								:key="index"
							>
								<image class="preview-image" :src="img" mode="aspectFill"></image>
								<view class="delete-btn" @click="handleDeleteImage(index)">
									<text class="delete-icon">×</text>
								</view>
							</view>
						</view>
					</view>
				</view>
			</view>
		</view>
		
		<view class="form-footer">
			<button class="save-btn" @click="handleSave" :loading="loading">保存</button>
			<button class="submit-btn" @click="handleSubmit" :loading="loading">提交</button>
		</view>
	</view>
</template>

<script>
import taskApi from '@/api/task.js'
import uploadApi from '@/api/upload.js'

export default {
	data() {
		return {
			processId: null,
			taskId: null,
			processInfo: {},
			formData: {
				trialQuantity: '',
				inspectionQuantity: '',
				manufacturingQualityStatus: '',
				processQualityStatus: '',
				manufacturingArea: '',
				director: '',
				directorTel: '',
				meDirector: '',
				meDirectorTel: '',
				figure: '',
				notes: ''
			},
			imageList: [],
			qualityStatusOptions: ['合格', '不合格', '待定'],
			loading: false
		}
	},
	
	onLoad(options) {
		this.processId = options.id
		this.taskId = options.taskId
		this.getProcessDetail()
	},
	
	methods: {
		async getProcessDetail() {
			try {
				const res = await taskApi.getProcessDetail(this.processId)
				this.processInfo = res.data || {}
				
				this.formData = {
					trialQuantity: this.processInfo.trialQuantity || '',
					inspectionQuantity: this.processInfo.inspectionQuantity || '',
					manufacturingQualityStatus: this.processInfo.manufacturingQualityStatus || '',
					processQualityStatus: this.processInfo.processQualityStatus || '',
					manufacturingArea: this.processInfo.manufacturingArea || '',
					director: this.processInfo.director || '',
					directorTel: this.processInfo.directorTel || '',
					meDirector: this.processInfo.meDirector || '',
					meDirectorTel: this.processInfo.meDirectorTel || '',
					figure: this.processInfo.figure || '',
					notes: this.processInfo.notes || ''
				}
			} catch (error) {
				console.error('获取程序详情失败:', error)
			}
		},
		
		handleQualityChange(e) {
			this.formData.manufacturingQualityStatus = this.qualityStatusOptions[e.detail.value]
		},
		
		handleProcessQualityChange(e) {
			this.formData.processQualityStatus = this.qualityStatusOptions[e.detail.value]
		},
		
		async handleUpload() {
			try {
				const results = await uploadApi.chooseAndUploadImage(3 - this.imageList.length)
				results.forEach(result => {
					this.imageList.push(result.fileName)
				})
				
				uni.showToast({
					title: '上传成功',
					icon: 'success'
				})
			} catch (error) {
				console.error('上传失败:', error)
			}
		},
		
		handleDeleteImage(index) {
			this.imageList.splice(index, 1)
		},
		
		validateForm() {
			if (!this.formData.trialQuantity) {
				uni.showToast({
					title: '请输入试制数量',
					icon: 'none'
				})
				return false
			}
			
			if (!this.formData.inspectionQuantity) {
				uni.showToast({
					title: '请输入送检数量',
					icon: 'none'
				})
				return false
			}
			
			return true
		},
		
		async handleSave() {
			if (!this.validateForm()) {
				return
			}
			
			this.loading = true
			
			try {
				await taskApi.updateProcess({
					id: this.processId,
					taskId: this.taskId,
					...this.formData
				})
				
				uni.showToast({
					title: '保存成功',
					icon: 'success'
				})
			} catch (error) {
				console.error('保存失败:', error)
			} finally {
				this.loading = false
			}
		},
		
		async handleSubmit() {
			if (!this.validateForm()) {
				return
			}
			
			uni.showModal({
				title: '提示',
				content: '确认提交填报信息吗？',
				success: async (res) => {
					if (res.confirm) {
						this.loading = true
						
						try {
							await taskApi.updateProcess({
								id: this.processId,
								taskId: this.taskId,
								...this.formData,
								status: '2'
							})
							
							uni.showToast({
								title: '提交成功',
								icon: 'success'
							})
							
							setTimeout(() => {
								uni.navigateBack()
							}, 1000)
						} catch (error) {
							console.error('提交失败:', error)
						} finally {
							this.loading = false
						}
					}
				}
			})
		}
	}
}
</script>

<style lang="scss" scoped>
.fill-form-container {
	min-height: 100vh;
	background: #F5F7FA;
	padding-bottom: 150rpx;
}

.form-header {
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
		}
	}
}

.form-content {
	padding: 30rpx;
}

.form-section {
	background: #FFFFFF;
	border-radius: 16rpx;
	padding: 30rpx;
	margin-bottom: 30rpx;
	
	.section-title {
		font-size: 30rpx;
		font-weight: bold;
		color: #333333;
		margin-bottom: 30rpx;
		padding-bottom: 20rpx;
		border-bottom: 1rpx solid #F0F0F0;
	}
}

.form-item {
	margin-bottom: 30rpx;
	
	&:last-child {
		margin-bottom: 0;
	}
	
	.item-label {
		display: block;
		font-size: 28rpx;
		color: #333333;
		margin-bottom: 16rpx;
		font-weight: 500;
		
		&.required::before {
			content: '*';
			color: #FF4D4F;
			margin-right: 8rpx;
		}
	}
	
	.item-input {
		width: 100%;
		height: 80rpx;
		background: #F5F7FA;
		border-radius: 12rpx;
		padding: 0 30rpx;
		font-size: 28rpx;
	}
	
	.item-picker {
		width: 100%;
		height: 80rpx;
		background: #F5F7FA;
		border-radius: 12rpx;
		padding: 0 30rpx;
		display: flex;
		justify-content: space-between;
		align-items: center;
		
		.picker-text {
			font-size: 28rpx;
			color: #333333;
		}
		
		.picker-icon {
			font-size: 28rpx;
			color: #999999;
		}
	}
	
	.item-textarea {
		width: 100%;
		min-height: 200rpx;
		background: #F5F7FA;
		border-radius: 12rpx;
		padding: 20rpx 30rpx;
		font-size: 28rpx;
	}
	
	.upload-area {
		.upload-btn {
			width: 200rpx;
			height: 200rpx;
			background: #F5F7FA;
			border: 2rpx dashed #D9D9D9;
			border-radius: 12rpx;
			display: flex;
			flex-direction: column;
			align-items: center;
			justify-content: center;
			
			.upload-icon {
				font-size: 60rpx;
				color: #999999;
				margin-bottom: 10rpx;
			}
			
			.upload-text {
				font-size: 24rpx;
				color: #999999;
			}
		}
		
		.image-list {
			display: flex;
			flex-wrap: wrap;
			margin-top: 20rpx;
			
			.image-item {
				position: relative;
				width: 200rpx;
				height: 200rpx;
				margin-right: 20rpx;
				margin-bottom: 20rpx;
				
				.preview-image {
					width: 100%;
					height: 100%;
					border-radius: 12rpx;
				}
				
				.delete-btn {
					position: absolute;
					top: -10rpx;
					right: -10rpx;
					width: 40rpx;
					height: 40rpx;
					background: #FF4D4F;
					border-radius: 50%;
					display: flex;
					align-items: center;
					justify-content: center;
					
					.delete-icon {
						color: #FFFFFF;
						font-size: 32rpx;
					}
				}
			}
		}
	}
}

.form-footer {
	position: fixed;
	bottom: 0;
	left: 0;
	right: 0;
	background: #FFFFFF;
	padding: 20rpx 30rpx;
	display: flex;
	gap: 20rpx;
	box-shadow: 0 -4rpx 12rpx rgba(0, 0, 0, 0.05);
	
	.save-btn {
		flex: 1;
		height: 88rpx;
		background: #FFFFFF;
		border: 2rpx solid #007AFF;
		border-radius: 12rpx;
		color: #007AFF;
		font-size: 32rpx;
	}
	
	.submit-btn {
		flex: 1;
		height: 88rpx;
		background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
		border-radius: 12rpx;
		color: #FFFFFF;
		font-size: 32rpx;
		border: none;
	}
}
</style>
