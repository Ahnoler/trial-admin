<script>
import { useUserStore } from '@/store/user.js'

export default {
	onLaunch: function() {
		console.log('App Launch')
		
		// 检查登录状态
		const userStore = useUserStore()
		if (!userStore.isLoggedIn) {
			uni.redirectTo({
				url: '/pages/login/login'
			})
		}
	},
	onShow: function() {
		console.log('App Show')
	},
	onHide: function() {
		console.log('App Hide')
	}
}
</script>

<style>
/* 全局样式 */
page {
	background-color: #F5F7FA;
	font-family: -apple-system, BlinkMacSystemFont, 'Segoe UI', 'Roboto', 'Oxygen', 'Ubuntu', 'Cantarell', 'Fira Sans', 'Droid Sans', 'Helvetica Neue', sans-serif;
}

/* 清除默认样式 */
button {
	margin: 0;
	padding: 0;
	background: none;
	border: none;
}

button::after {
	border: none;
}

/* 全局字体图标 */
.iconfont {
	font-family: "iconfont" !important;
	font-size: 16px;
	font-style: normal;
	-webkit-font-smoothing: antialiased;
	-moz-osx-font-smoothing: grayscale;
}

/* 图标类 */
.icon-scan::before {
	content: "\e600";
}

.icon-pending::before {
	content: "\e601";
}

.icon-progress::before {
	content: "\e602";
}

.icon-review::before {
	content: "\e603";
}

.icon-completed::before {
	content: "\e604";
}

/* 通用工具类 */
.text-center {
	text-align: center;
}

.text-left {
	text-align: left;
}

.text-right {
	text-align: right;
}

.flex {
	display: flex;
}

.flex-column {
	flex-direction: column;
}

.flex-center {
	justify-content: center;
	align-items: center;
}

.flex-between {
	justify-content: space-between;
}

.flex-around {
	justify-content: space-around;
}

.flex-wrap {
	flex-wrap: wrap;
}

.flex-1 {
	flex: 1;
}

/* 间距工具类 */
.mt-10 { margin-top: 10rpx; }
.mt-20 { margin-top: 20rpx; }
.mt-30 { margin-top: 30rpx; }
.mb-10 { margin-bottom: 10rpx; }
.mb-20 { margin-bottom: 20rpx; }
.mb-30 { margin-bottom: 30rpx; }
.ml-10 { margin-left: 10rpx; }
.ml-20 { margin-left: 20rpx; }
.mr-10 { margin-right: 10rpx; }
.mr-20 { margin-right: 20rpx; }
.pt-10 { padding-top: 10rpx; }
.pt-20 { padding-top: 20rpx; }
.pb-10 { padding-bottom: 10rpx; }
.pb-20 { padding-bottom: 20rpx; }
.pl-10 { padding-left: 10rpx; }
.pl-20 { padding-left: 20rpx; }
.pr-10 { padding-right: 10rpx; }
.pr-20 { padding-right: 20rpx; }

/* 字体大小 */
.font-24 { font-size: 24rpx; }
.font-26 { font-size: 26rpx; }
.font-28 { font-size: 28rpx; }
.font-30 { font-size: 30rpx; }
.font-32 { font-size: 32rpx; }
.font-36 { font-size: 36rpx; }

/* 颜色 */
.text-primary { color: #007AFF; }
.text-success { color: #4CAF50; }
.text-warning { color: #FF9800; }
.text-danger { color: #FF4D4F; }
.text-muted { color: #999999; }

/* 背景色 */
.bg-primary { background-color: #007AFF; }
.bg-success { background-color: #4CAF50; }
.bg-warning { background-color: #FF9800; }
.bg-danger { background-color: #FF4D4F; }
.bg-white { background-color: #FFFFFF; }

/* 边框 */
.border { border: 1rpx solid #E0E0E0; }
.border-top { border-top: 1rpx solid #E0E0E0; }
.border-bottom { border-bottom: 1rpx solid #E0E0E0; }

/* 圆角 */
.rounded { border-radius: 8rpx; }
.rounded-lg { border-radius: 16rpx; }
.rounded-full { border-radius: 9999rpx; }

/* 阴影 */
.shadow { box-shadow: 0 4rpx 12rpx rgba(0, 0, 0, 0.05); }
.shadow-lg { box-shadow: 0 10rpx 30rpx rgba(0, 0, 0, 0.1); }
</style>
