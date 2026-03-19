# Swagger注解统一为Swagger2的实施计划

## 一、现状分析

### 控制器文件扫描结果

| 文件名 | 当前状态 | 需要操作 |
|--------|----------|----------|
| ActionLogController.java | 无swagger注解 | 添加Swagger2注解 |
| CardColumnHeaderController.java | 无swagger注解 | 添加Swagger2注解 |
| CardModelController.java | 无swagger注解 | 添加Swagger2注解 |
| CardModelDetailProdController.java | 无swagger注解 | 添加Swagger2注解 |
| CardTableHeaderController.java | 无swagger注解 | 添加Swagger2注解 |
| CardTypeController.java | 无swagger注解 | 添加Swagger2注解 |
| HomePageController.java | 无swagger注解 | 添加Swagger2注解 |
| PrintLogController.java | 无swagger注解 | 添加Swagger2注解 |
| ProjectsController.java | 无swagger注解 | 添加Swagger2注解 |
| TrialTaskDetailProdController.java | Swagger3.x注解 | 改为Swagger2注解 |
| TrialTaskProdController.java | Swagger3.x注解 | 改为Swagger2注解 |

### Swagger3.x注解使用情况

**TrialTaskDetailProdController.java:**
- `@Tag(name = "试制任务-零件流转卡-流转程序", description = "...")` (类级别)
- `@Operation(summary = "...")` (方法级别)

**TrialTaskProdController.java:**
- `@Tag(name = "试制任务-零件流转卡", description = "...")` (类级别)
- `@Operation(summary = "...")` (方法级别)

## 二、Swagger2注解映射规则

| Swagger3.x注解 | Swagger2注解 | 说明 |
|----------------|--------------|------|
| `@Tag` | `@Api(tags = "...")` | 类级别，描述API分组 |
| `@Operation` | `@ApiOperation(value = "...")` | 方法级别，描述接口功能 |

## 三、实施步骤

### 步骤1：修改SwaggerConfig.java

**文件路径:** `d:\毕业设计\trial-admin\ruoyi-admin\src\main\java\com\ruoyi\web\core\config\SwaggerConfig.java`

**修改内容:**
- 移除对 `io.swagger.v3.oas.annotations.Operation` 的导入
- 移除对swagger3.x注解的扫描条件 `.or(RequestHandlerSelectors.withMethodAnnotation(Operation.class))`
- 只保留对 `io.swagger.annotations.ApiOperation` 的扫描

### 步骤2：为无注解控制器添加Swagger2注解

需要为以下9个控制器添加注解：

#### 2.1 ActionLogController.java
- 添加 `@Api(tags = "计划任务操作日志")` 类注解
- 为每个方法添加 `@ApiOperation` 注解

#### 2.2 CardColumnHeaderController.java
- 添加 `@Api(tags = "卡片列头模版")` 类注解
- 为每个方法添加 `@ApiOperation` 注解

#### 2.3 CardModelController.java
- 添加 `@Api(tags = "卡片信息模版")` 类注解
- 为每个方法添加 `@ApiOperation` 注解

#### 2.4 CardModelDetailProdController.java
- 添加 `@Api(tags = "卡片程序模版")` 类注解
- 为每个方法添加 `@ApiOperation` 注解

#### 2.5 CardTableHeaderController.java
- 添加 `@Api(tags = "卡片头模版")` 类注解
- 为每个方法添加 `@ApiOperation` 注解

#### 2.6 CardTypeController.java
- 添加 `@Api(tags = "卡片类型")` 类注解
- 为每个方法添加 `@ApiOperation` 注解

#### 2.7 HomePageController.java
- 添加 `@Api(tags = "首页信息")` 类注解
- 为每个方法添加 `@ApiOperation` 注解

#### 2.8 PrintLogController.java
- 添加 `@Api(tags = "打印记录")` 类注解
- 为每个方法添加 `@ApiOperation` 注解

#### 2.9 ProjectsController.java
- 添加 `@Api(tags = "项目管理")` 类注解
- 为每个方法添加 `@ApiOperation` 注解

### 步骤3：将Swagger3.x注解改为Swagger2注解

#### 3.1 TrialTaskDetailProdController.java
- 移除 `import io.swagger.v3.oas.annotations.Operation;`
- 移除 `import io.swagger.v3.oas.annotations.tags.Tag;`
- 添加 `import io.swagger.annotations.Api;`
- 添加 `import io.swagger.annotations.ApiOperation;`
- 将 `@Tag(name = "...", description = "...")` 改为 `@Api(tags = "...")`
- 将所有 `@Operation(summary = "...")` 改为 `@ApiOperation(value = "...")`

#### 3.2 TrialTaskProdController.java
- 移除 `import io.swagger.v3.oas.annotations.Operation;`
- 移除 `import io.swagger.v3.oas.annotations.tags.Tag;`
- 添加 `import io.swagger.annotations.Api;`
- 添加 `import io.swagger.annotations.ApiOperation;`
- 将 `@Tag(name = "...", description = "...")` 改为 `@Api(tags = "...")`
- 将所有 `@Operation(summary = "...")` 改为 `@ApiOperation(value = "...")`

## 四、注解详情对照表

### 各控制器方法注解详情

#### ActionLogController
| 方法 | @ApiOperation value |
|------|---------------------|
| selectOne | 通过主键查询单条数据 |
| add | 新增操作日志 |
| getList | 查询操作日志列表 |

#### CardColumnHeaderController
| 方法 | @ApiOperation value |
|------|---------------------|
| list | 查询卡片列头模版列表 |
| export | 导出卡片列头模版列表 |
| getInfo | 获取卡片列头模版详细信息 |
| add | 新增卡片列头模版 |
| edit | 修改卡片列头模版 |
| remove | 删除卡片列头模版 |

#### CardModelController
| 方法 | @ApiOperation value |
|------|---------------------|
| list | 查询卡片信息模版列表 |
| export | 导出卡片信息模版列表 |
| getInfo | 获取卡片信息模版详细信息 |
| add | 新增卡片信息模版 |
| edit | 修改卡片信息模版 |
| remove | 删除卡片信息模版 |

#### CardModelDetailProdController
| 方法 | @ApiOperation value |
|------|---------------------|
| list | 查询卡片程序模版列表 |
| export | 导出卡片程序模版列表 |
| getInfo | 获取卡片程序模版详细信息 |
| add | 新增卡片程序模版 |
| edit | 修改卡片程序模版 |
| remove | 删除卡片程序模版 |

#### CardTableHeaderController
| 方法 | @ApiOperation value |
|------|---------------------|
| list | 查询卡片头模版列表 |
| export | 导出卡片头模版列表 |
| getInfo | 获取卡片头模版详细信息 |
| add | 新增卡片头模版 |
| edit | 修改卡片头模版 |
| remove | 删除卡片头模版 |

#### CardTypeController
| 方法 | @ApiOperation value |
|------|---------------------|
| list | 查询卡片类型列表 |
| export | 导出卡片类型列表 |
| getInfo | 获取卡片类型详细信息 |
| add | 新增卡片类型 |
| edit | 修改卡片类型 |
| remove | 删除卡片类型 |

#### HomePageController
| 方法 | @ApiOperation value |
|------|---------------------|
| selectHomeInfo | 查询首页信息 |
| selectExamineTask | 查询审核任务列表 |

#### PrintLogController
| 方法 | @ApiOperation value |
|------|---------------------|
| list | 查询打印记录列表 |
| export | 导出打印记录列表 |
| getInfo | 获取打印记录详细信息 |
| add | 新增打印记录 |
| edit | 修改打印记录 |
| remove | 删除打印记录 |

#### ProjectsController
| 方法 | @ApiOperation value |
|------|---------------------|
| list | 查询项目管理列表 |
| export | 导出项目管理列表 |
| getInfo | 获取项目管理详细信息 |
| add | 新增项目管理 |
| edit | 修改项目管理 |
| remove | 删除项目管理 |
| print | 打印项目基本信息 |
| exportPdf | 导出项目为PDF |
| printAllCards | 打印项目所有零件的电子流转卡 |

#### TrialTaskDetailProdController (Swagger3.x → Swagger2)
| 方法 | 原@Operation summary | 新@ApiOperation value |
|------|---------------------|----------------------|
| add | 新增流转程序 | 新增流转程序 |
| edit | 修改流转程序 | 修改流转程序 |
| remove | 删除流转程序 | 删除流转程序 |
| list | 获取试制任务-零件流转卡-流转程序列表 | 获取试制任务-零件流转卡-流转程序列表 |
| export | 导出试制任务-零件流转卡-流转程序列表为Excel | 导出试制任务-零件流转卡-流转程序列表为Excel |
| getInfo | 获取试制任务-零件流转卡-流转程序详细信息 | 获取试制任务-零件流转卡-流转程序详细信息 |
| apply | 申请试制任务-零件流转卡-流转程序 | 申请试制任务-零件流转卡-流转程序 |
| approve | 审核试制任务-零件流转卡-流转程序 | 审核试制任务-零件流转卡-流转程序 |

#### TrialTaskProdController (Swagger3.x → Swagger2)
| 方法 | 原@Operation summary | 新@ApiOperation value |
|------|---------------------|----------------------|
| add | 新增零件流转卡 | 新增零件流转卡 |
| edit | 修改零件流转卡 | 修改零件流转卡 |
| remove | 删除零件流转卡 | 删除零件流转卡 |
| getInfo | 获取零件流转卡详细信息 | 获取零件流转卡详细信息 |
| list | 获取零件流转卡列表 | 获取零件流转卡列表 |
| export | 导出零件流转卡列表为Excel | 导出零件流转卡列表为Excel |
| flow | 变更零件流转卡-流转程序 | 变更零件流转卡-流转程序 |
| change | 零件流转卡分流操作 | 零件流转卡分流操作 |
| over | 结束零件流转卡 | 结束零件流转卡 |
| exportProdPdf | 导出零件流转卡详细信息为pdf | 导出零件流转卡详细信息为pdf |
| selectByRelatedTaskId | 根据关联任务ID查询相关的零件流转卡列表 | 根据关联任务ID查询相关的零件流转卡列表 |
| print | 打印零件流转卡详细信息为pdf | 打印零件流转卡详细信息为pdf |
| scanQrCode | 扫码识别-根据二维码获取流转卡信息 | 扫码识别-根据二维码获取流转卡信息 |
| regenerateQrCode | 重新生成二维码 | 重新生成二维码 |
| batchGenerateQrCode | 批量生成二维码 | 批量生成二维码 |

## 五、执行顺序

1. 修改 SwaggerConfig.java（移除swagger3.x扫描）
2. 修改 TrialTaskDetailProdController.java（swagger3.x → swagger2）
3. 修改 TrialTaskProdController.java（swagger3.x → swagger2）
4. 修改 ActionLogController.java（添加swagger2注解）
5. 修改 CardColumnHeaderController.java（添加swagger2注解）
6. 修改 CardModelController.java（添加swagger2注解）
7. 修改 CardModelDetailProdController.java（添加swagger2注解）
8. 修改 CardTableHeaderController.java（添加swagger2注解）
9. 修改 CardTypeController.java（添加swagger2注解）
10. 修改 HomePageController.java（添加swagger2注解）
11. 修改 PrintLogController.java（添加swagger2注解）
12. 修改 ProjectsController.java（添加swagger2注解）

## 六、预期结果

- 所有控制器统一使用Swagger2注解
- SwaggerConfig只扫描Swagger2的 `@ApiOperation` 注解
- 所有接口都能在Swagger文档中正确显示
