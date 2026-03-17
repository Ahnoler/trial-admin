# 试制任务管理系统 - 微信小程序端

## 项目简介

本项目是试制任务管理系统的微信小程序端，基于 uni-app 框架开发，用于实现质量信息流转卡的扫码填报功能。

## 技术栈

- **前端框架**: uni-app (Vue 3)
- **状态管理**: Pinia
- **UI设计**: 自定义组件 + SCSS
- **网络请求**: 基于 uni.request 封装

## 项目结构

```
trial-app/
├── api/                  # API接口
│   ├── task.js          # 任务相关API
│   ├── upload.js        # 文件上传API
│   └── user.js          # 用户相关API
├── config/              # 配置文件
│   └── index.js         # 全局配置
├── pages/               # 页面文件
│   ├── index/           # 首页
│   ├── login/           # 登录页
│   ├── scan/            # 扫码页
│   ├── task-detail/     # 任务详情页
│   ├── fill-form/       # 填报表单页
│   ├── history/         # 历史记录页
│   └── user/            # 个人中心页
├── static/              # 静态资源
├── store/               # 状态管理
│   ├── index.js         # Store入口
│   └── user.js          # 用户状态管理
├── utils/               # 工具函数
│   └── request.js       # 网络请求封装
├── App.vue              # 应用入口
├── main.js              # 主入口文件
├── manifest.json        # 应用配置
├── pages.json           # 页面配置
└── package.json         # 项目依赖

```

## 功能模块

### 1. 用户认证
- 用户登录
- 记住密码
- 自动登录
- 退出登录

### 2. 扫码填报
- 微信扫码
- 二维码识别
- 任务自动跳转
- 手动输入任务ID

### 3. 任务管理
- 待办任务列表
- 任务详情查看
- 流转程序展示
- 当前环节定位

### 4. 表单填报
- 动态表单生成
- 默认数据填充
- 表单验证
- 数据提交
- 图片上传

### 5. 历史记录
- 任务历史查看
- 搜索筛选
- 下拉刷新
- 上拉加载

### 6. 个人中心
- 用户信息展示
- 任务统计
- 系统设置

## 开发指南

### 安装依赖

```bash
npm install
```

### 开发运行

#### 微信小程序
```bash
npm run dev:mp-weixin
```

#### H5
```bash
npm run dev:h5
```

### 生产构建

#### 微信小程序
```bash
npm run build:mp-weixin
```

#### H5
```bash
npm run build:h5
```

### 配置说明

#### 1. 后端接口配置
修改 `config/index.js` 文件中的 `BASE_URL`：

```javascript
const BASE_URL = 'http://localhost:8080'  // 修改为实际的后端地址
```

#### 2. 微信小程序 AppID 配置
修改 `manifest.json` 文件中的 `mp-weixin.appid`：

```json
"mp-weixin": {
  "appid": "your-appid",  // 修改为实际的微信小程序AppID
  "setting": {
    "urlCheck": true
  }
}
```

#### 3. 权限配置
在 `manifest.json` 中配置所需权限：

```json
"mp-weixin": {
  "permission": {
    "scope.userLocation": {
      "desc": "您的位置信息将用于定位"
    }
  }
}
```

## 开发注意事项

### 1. 网络请求
- 所有网络请求已统一封装在 `utils/request.js`
- 自动添加 Token 认证
- 统一错误处理
- 401 自动跳转登录

### 2. 状态管理
- 使用 Pinia 进行状态管理
- 用户信息存储在 `store/user.js`
- 支持 localStorage 持久化

### 3. 样式规范
- 使用 SCSS 预处理器
- 尺寸单位使用 rpx（响应式像素）
- 遵循 BEM 命名规范

### 4. 图标使用
- 使用 iconfont 字体图标
- 在 App.vue 中定义图标类

### 5. 兼容性
- 支持 Vue 3 语法
- 兼容微信小程序
- 支持 H5 端

## 部署说明

### 微信小程序部署

1. 构建项目
```bash
npm run build:mp-weixin
```

2. 使用微信开发者工具打开 `dist/build/mp-weixin` 目录

3. 上传代码并提交审核

### H5 部署

1. 构建项目
```bash
npm run build:h5
```

2. 将 `dist/build/h5` 目录部署到服务器

## 后端接口要求

### 1. 登录接口
```
POST /login
Request: { username, password, code, uuid }
Response: { code: 200, token: "xxx" }
```

### 2. 获取用户信息
```
GET /getInfo
Response: { code: 200, user: {...} }
```

### 3. 任务列表
```
GET /trial/prod/list
Response: { code: 200, rows: [...], total: 100 }
```

### 4. 任务详情
```
GET /trial/prod/{taskId}
Response: { code: 200, data: {...} }
```

### 5. 更新程序
```
PUT /trial/prodDetail
Request: { id, taskId, ... }
Response: { code: 200, msg: "success" }
```

### 6. 文件上传
```
POST /common/upload
Request: FormData
Response: { code: 200, fileName: "xxx" }
```

## 常见问题

### 1. 登录失败
- 检查后端接口地址是否正确
- 检查验证码是否正确
- 检查用户名密码是否正确

### 2. 扫码失败
- 检查是否有相机权限
- 检查二维码格式是否正确
- 检查后端扫码接口是否正常

### 3. 网络请求失败
- 检查网络连接
- 检查后端服务是否启动
- 检查接口地址是否正确

### 4. 样式显示异常
- 检查 rpx 单位是否正确
- 检查 SCSS 语法是否正确
- 检查兼容性问题

## 更新日志

### v1.0.0 (2026-03-12)
- 初始版本发布
- 实现基础功能模块
- 完成用户认证
- 实现扫码填报
- 完成表单填报功能

## 技术支持

如有问题，请联系开发团队。

## 许可证

MIT License
