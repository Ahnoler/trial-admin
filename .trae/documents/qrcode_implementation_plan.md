# 二维码功能实现计划

## 功能概述

为每个流转卡生成唯一的二维码，支持小程序扫码填报功能。

***

## 后端开发

### 第一步：添加数据库字段和实体类属性 ✅ 已完成

* [x] 数据库添加 `qr_code` 和 `qr_code_url` 字段

* [x] 实体类 `TrialTaskProd.java` 添加对应属性

* [x] SQL 文件：`sql/add_qrcode_fields.sql`

### 第二步：添加 ZXing 依赖并创建工具类 ✅ 已完成

* [x] `pom.xml` 添加 ZXing 依赖

* [x] 创建 `QRCodeUtil.java` 工具类

### 第三步：修改服务层，在创建任务时自动生成二维码 ✅ 已完成

* [x] `insertTrialTaskProd` 方法添加二维码生成逻辑

* [x] `copyTrialTaskProd` 方法添加二维码生成逻辑（分流卡片）

* [x] 添加 `batchGenerateQrCode` 批量生成方法

### 第四步：添加控制器接口 ✅ 已完成

* [x] `POST /trial/prod/batchGenerateQrCode` - 批量生成二维码

* [x] `POST /trial/prod/regenerateQrCode/{taskId}` - 重新生成二维码

* [x] `GET /trial/prod/scan/{qrCode}` - 扫码识别接口

### 第五步：修改打印模板，显示二维码 ✅ 已完成

* [x] `exportPdf` 方法传递二维码参数

* [x] `printTrialTaskProd` 方法 HTML 打印显示二维码

* [x] Jasper 模板配置 `qrCodeImage` 参数

### 第六步：测试完整流程 ⏳ 进行中

* [x] 调用批量生成二维码接口

* [ ] 验证数据库二维码数据

* [ ] 测试 PDF 导出显示二维码

* [ ] 测试 HTML 打印显示二维码

* [ ] 测试扫码识别接口

***

## 微信小程序开发

### 已完成功能

#### 页面结构

| 页面   | 路径                                  | 功能           |
| ---- | ----------------------------------- | ------------ |
| 首页   | `pages/index/index.vue`             | 任务列表展示       |
| 登录   | `pages/login/login.vue`             | 用户登录         |
| 扫码   | `pages/scan/scan.vue`               | 扫描二维码        |
| 任务详情 | `pages/task-detail/task-detail.vue` | 查看流转卡详情和流转程序 |
| 填报信息 | `pages/fill-form/fill-form.vue`     | 填报流转程序信息     |
| 历史记录 | `pages/history/history.vue`         | 历史填报记录       |
| 个人中心 | `pages/user/user.vue`               | 用户信息管理       |

#### API 接口封装

| 方法                   | 接口                       | 说明       |
| -------------------- | ------------------------ | -------- |
| `getTaskList`        | `/trial/prod/list`       | 获取任务列表   |
| `getTaskDetail`      | `/trial/prod/{taskId}`   | 获取任务详情   |
| `getTaskProcessList` | `/trial/prodDetail/list` | 获取流转程序列表 |
| `getProcessDetail`   | `/trial/prodDetail/{id}` | 获取程序详情   |
| `updateProcess`      | `/trial/prodDetail`      | 更新程序信息   |
| `scanQRCode`         | `/trial/prod/scan`       | 扫码识别     |
| `getMyTaskList`      | `/trial/prod/myTask`     | 获取我的任务   |

#### 扫码功能

* [x] 调用微信扫码 API

* [x] 扫码结果处理

* [x] 手动输入任务ID

* [x] 跳转到任务详情页

#### 填报功能

* [x] 基本信息填报（试制数量、送检数量等）

* [x] 质量状态选择

* [x] 负责人信息填写

* [x] 附件上传

* [x] 保存和提交

### 待完善功能

#### 扫码接口问题

**问题：** 小程序 API 调用方式与后端接口不匹配

小程序代码 (`api/task.js`)：

```javascript
scanQRCode(qrCode) {
    return request.get('/trial/prod/scan', { qrCode })
}
```

后端接口：

```java
@GetMapping("/scan/{qrCode}")
public AjaxResult scanQrCode(@PathVariable("qrCode") String qrCode)
```

**需要修改：** 小程序 API 应改为路径参数方式：

```javascript
scanQRCode(qrCode) {
    return request.get(`/trial/prod/scan/${qrCode}`)
}
```

#### 待开发功能

* [ ] 修复扫码 API 调用方式

* [ ] 添加扫码结果错误处理优化

* [ ] 添加网络异常处理

* [ ] 添加登录状态过期处理

* [ ] 优化填报表单验证

* [ ] 添加图片预览功能

***

## 文件修改清单

### 后端文件

| 文件路径                            | 修改内容                              |
| ------------------------------- | --------------------------------- |
| `pom.xml`                       | 添加 ZXing 依赖                       |
| `TrialTaskProd.java`            | 添加 qrCode、qrCodeUrl 属性            |
| `QRCodeUtil.java`               | 新建二维码工具类                          |
| `TrialTaskProdMapper.xml`       | 添加二维码字段映射和查询                      |
| `TrialTaskProdMapper.java`      | 添加 selectTrialTaskProdByQrCode 方法 |
| `ITrialTaskProdService.java`    | 添加二维码相关方法接口                       |
| `TrialTaskProdServiceImpl.java` | 实现二维码生成和查询逻辑                      |
| `TrialTaskProdController.java`  | 添加扫码和批量生成接口                       |
| `SwaggerConfig.java`            | 支持 OpenAPI 3 注解                   |
| `prod.jrxml`                    | 添加二维码图片显示                         |
| `add_qrcode_fields.sql`         | 数据库迁移脚本                           |

### 小程序文件

| 文件路径          | 修改内容          |
| ------------- | ------------- |
| `api/task.js` | 修复扫码 API 调用方式 |

***

## 下一步行动

1. **修复小程序扫码 API** - 将查询参数改为路径参数
2. **测试扫码流程** - 完整测试扫码→查看详情→填报流程
3. **验证数据库** - 确认二维码数据正确存储

