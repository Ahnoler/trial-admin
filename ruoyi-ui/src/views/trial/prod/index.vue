<template>
	<div class="app-container">
		<el-form :model="queryParams" ref="queryForm" size="small" v-show="showSearch" label-width="120px">
			<el-row :gutter="10">
				<el-col :span="4">
					<el-form-item label="所属项目" prop="projectName">
						<el-input v-model="queryParams.projectName" placeholder="请输入项目名称" clearable @keyup.enter.native="handleQuery" />
					</el-form-item>
				</el-col>
				<el-col :span="4">
					<el-form-item label="卡片名称" prop="title">
						<el-input v-model="queryParams.title" placeholder="请输入卡片名称" clearable @keyup.enter.native="handleQuery" />
					</el-form-item>
				</el-col>
				<el-col :span="4">
					<el-form-item label="车型" prop="carType">
						<el-select v-model="queryParams.carType" placeholder="请选择车型" style="width: 100%">
							<el-option label="所用车型" value=""></el-option>
							<el-option v-for="dict in dict.type.car_type" :key="dict.value" :label="dict.label"
								:value="dict.value"></el-option>
						</el-select>
					</el-form-item>
				</el-col>
				<el-col :span="4">
					<el-form-item label="总成名称" prop="assemblyName">
						<el-input v-model="queryParams.assemblyName" placeholder="请输入总成名称" clearable
							@keyup.enter.native="handleQuery" />
					</el-form-item>
				</el-col>
				<el-col :span="4">
					<el-form-item label="总成图号" prop="assemblyFigure">
						<el-input v-model="queryParams.assemblyFigure" placeholder="请输入总成图号" clearable
							@keyup.enter.native="handleQuery" />
					</el-form-item>
				</el-col>
				<el-col :span="4">
					<el-form-item label-width="0">
						<el-button type="primary" icon="el-icon-search" size="mini" @click="handleQuery">搜索</el-button>
						<el-button icon="el-icon-refresh" size="mini" @click="resetQuery">重置</el-button>
					</el-form-item>
				</el-col>
			</el-row>
			<el-row :gutter="10">
				<el-col :span="8">
					<el-form-item label="试制管理员/电话" prop="pm">
						<el-input v-model="queryParams.pm" placeholder="请输入试制管理员/电话" clearable @keyup.enter.native="handleQuery" />
					</el-form-item>
				</el-col>
				<el-col :span="8">
					<el-form-item label="PE姓名/电话" prop="pe">
						<el-input v-model="queryParams.pe" placeholder="请输入PE姓名/电话" clearable @keyup.enter.native="handleQuery" />
					</el-form-item>
				</el-col>
			</el-row>
		</el-form>

		<el-row :gutter="10" class="mb8">
			<el-col :span="1.5">
				<el-button type="primary" plain icon="el-icon-plus" size="mini" @click="handleAdd"
					v-hasPermi="['trial:prod:add']">新增</el-button>
			</el-col>
			<el-col :span="1.5">
				<el-button type="success" plain icon="el-icon-edit" size="mini" :disabled="single" @click="handleUpdate"
					v-hasPermi="['trial:prod:edit']">修改</el-button>
			</el-col>
			<el-col :span="1.5">
				<el-button type="danger" plain icon="el-icon-delete" size="mini" :disabled="multiple" @click="handleDelete"
					v-hasPermi="['trial:prod:remove']">删除</el-button>
			</el-col>
			<el-col :span="1.5">
				<el-button type="warning" plain icon="el-icon-download" size="mini" @click="handleExportExcel"
					v-hasPermi="['trial:prod:export']">导出EXCEL</el-button>
			</el-col>
			<el-col :span="1.5">
				<el-button type="danger" plain icon="el-icon-d-caret" size="mini" @click="openFlowProject" :disabled="single"
					v-hasPermi="['trial:projects:edit']">变更</el-button>
			</el-col>
			<el-col :span="1.5">
				<el-button type="success" plain icon="el-icon-share" size="mini" @click="openForkProject" :disabled="single"
					v-hasPermi="['trial:projects:edit']">分流</el-button>
			</el-col>
			<el-col :span="1.5">
				<el-button type="warning" plain icon="el-icon-switch-button" size="mini" @click="openCloseProject"
					:disabled="single" v-hasPermi="['trial:projects:edit']">结束</el-button>
			</el-col>
			<el-col :span="1.5">
				<el-button type="info" plain icon="el-icon-s-operation" size="mini" @click="openRelatedProject"
					:disabled="single" v-hasPermi="['trial:projects:edit']">关联卡片</el-button>
			</el-col>
			<el-col :span="1.5">
				<el-button type="primary" plain icon="el-icon-printer" size="mini" @click="handlePrint" :disabled="single"
					v-hasPermi="['trial:prod:print']">打印</el-button>
			</el-col>
			<el-col :span="1.5">
				<el-button type="warning" plain icon="el-icon-download" size="mini" @click="handleExportPdf" :disabled="single"
					v-hasPermi="['trial:prod:export']">导出PDF</el-button>
			</el-col>
			<right-toolbar :showSearch.sync="showSearch" @queryTable="getList"></right-toolbar>
		</el-row>

		<el-table v-loading="loading" :data="prodList" @selection-change="handleSelectionChange">
			<el-table-column type="selection" width="55" align="center" />
			<el-table-column label="任务编号" align="center" prop="taskId" />
			<el-table-column label="所属项目" align="center" prop="projectName" />
			<el-table-column label="卡片名称" align="center" prop="title" />
			<el-table-column label="车型" align="center" prop="carType" width="100">
				<template slot-scope="scope">
					<dict-tag :options="dict.type.car_type" :value="scope.row.carType" />
				</template>
			</el-table-column>
			<el-table-column label="总成名称" align="center" prop="assemblyName" />
			<el-table-column label="总成图号" align="center" prop="assemblyFigure" />
			<el-table-column label="试制管理员/电话" align="center" prop="pm" />
			<el-table-column label="PE姓名/电话" align="center" prop="pe" />
			<el-table-column label="备注" align="center" prop="remark" />
			<el-table-column label="状态" align="center" prop="status" width="100">
				<template slot-scope="scope">
					<dict-tag :options="dict.type.task_status" :value="scope.row.status" />
				</template>
			</el-table-column>
			<el-table-column label="当前工序" align="center" prop="currentSerialName" />
		</el-table>

		<pagination v-show="total>0" :total="total" :page.sync="queryParams.pageNum" :limit.sync="queryParams.pageSize"
			@pagination="getList" />

		<!-- 添加或修改试制任务信息对话框 -->
		<el-dialog :title="title" :visible.sync="open" width="500px" append-to-body>
			<el-form ref="form" :model="form" :rules="rules" label-width="120px">
				<el-form-item label="所属项目" prop="projectName">
					<el-input v-model="form.projectId" v-show="false" placeholder="请输入项目管理编号" />
					<el-input v-model="form.projectName" placeholder="请选择所属项目" style="width:60%;float: left;" disabled />
					<el-button type="primary" style="line-height:20px; width:40%;float: right;" plain icon="el-icon-plus"
						size="mini" @click="openSelectProject" v-hasPermi="['trial:projects:add']">请选择所属项目</el-button>

				</el-form-item>
				<el-form-item label="卡片名称" prop="title">
					<el-input v-model="form.title" placeholder="请输入卡片名称" />
				</el-form-item>

				<el-form-item label="车型" prop="carType">
					<el-select v-model="form.carType" placeholder="请选择车型">
						<el-option v-for="dict in dict.type.car_type" :key="dict.value" :label="dict.label"
							:value="dict.value"></el-option>
					</el-select>
				</el-form-item>
				<el-form-item label="总成名称" prop="assemblyName">
					<el-input v-model="form.assemblyName" placeholder="请输入总成名称" />
				</el-form-item>
				<el-form-item label="总成图号" prop="assemblyFigure">
					<el-input v-model="form.assemblyFigure" placeholder="请输入总成图号" />
				</el-form-item>
				<el-form-item label="试制管理员/电话" prop="pm">
					<el-input v-model="form.pm" placeholder="请输入试制管理员/电话" />
				</el-form-item>
				<el-form-item label="PE姓名/电话" prop="pe">
					<el-input v-model="form.pe" placeholder="请输入PE姓名/电话" />
				</el-form-item>
				<el-form-item label="备注" prop="remark">
					<el-input v-model="form.remark" type="textarea" placeholder="请输入内容" />
				</el-form-item>

			</el-form>
			<div slot="footer" class="dialog-footer">
				<el-button type="primary" @click="submitForm">确 定</el-button>
				<el-button @click="cancel">取 消</el-button>
			</div>
		</el-dialog>
		<select-project ref="select" :pm="queryParams.pm" @ok="handleQueryProject" />
		<selectFlow ref="flow" :taskId="taskId" @ok="handleFlowProject" />
		<selectFork ref="fork" :taskId="taskId" @ok="handleForkProject" />
		<selectRelated ref="related" :taskId="taskId" :title="''" />
	</div>
</template>

<script>
	import {
		listProd,
		getProd,
		delProd,
		addProd,
		updateProd,
		forkProd,
		flowProd,
		overProd,
		listRelatedProd,
		exportProdPdf,
		printDetail
	} from "@/api/trial/prod";
	import selectProject from "./selectProject";
import selectFlow from "./selectFlow";
import selectFork from "./selectFork";
import selectRelated from "./selectRelated";

	export default {
		name: "Prod",
		dicts: ['car_type', 'sys_normal_disable', 'task_status'],
		components: {
		selectProject,
		selectFlow,
		selectFork,
		selectRelated
	},
		data() {
			return {
				taskId: null,
				// 遮罩层
				loading: true,
				// 选中数组
				ids: [],
				// 非单个禁用
				single: true,
				// 非多个禁用
				multiple: true,
				// 显示搜索条件
				showSearch: true,
				// 总条数
				total: 0,
				// 试制任务信息表格数据
				prodList: [],
				// 弹出层标题
				title: "",
				// 是否显示弹出层
				open: false,
				// 查询参数
				queryParams: {
					pageNum: 1,
					pageSize: 10,
					projectId: null,
					projectName: null,
					cardType: null,
					title: null,
					carType: null,
					assemblyName: null,
					assemblyFigure: null,
					pm: null,
					pe: null,
					status: null,
				},
				// 表单参数
				form: {},
				// 表单校验
				rules: {
					projectId: [{
						required: true,
						message: "项目编号不能为空",
						trigger: "blur"
					}],
				}
			};
		},
		created() {
			this.getList();
		},
		methods: {
			/** 打开所属项目表弹窗 */
			openSelectProject() {
				this.$refs.select.show();
			},
			/** 打开变更表弹窗 */
			openFlowProject() {
				this.taskId = this.ids[0];
				console.log("openFlowProject" + this.taskId);
				this.$refs.flow.show(this.taskId);
			},
			/** 打开分流表弹窗 */
			openForkProject() {
				this.taskId = this.ids[0];
				console.log("openForkProject" + this.taskId);
				this.$refs.fork.show(this.taskId);
			},
			/** 结束任务 */
			openCloseProject() {
				this.taskId = this.ids[0];
				var row = {
					taskId: this.taskId
				}
				this.reset();
				overProd(row).then(response => {
					this.$modal.msgSuccess("结束成功");
					this.reset();
					this.getList();
				});
			},
			/** 查询关联卡片 */
		openRelatedProject() {
			this.taskId = this.ids[0];
			const selectedRow = this.prodList.find(row => row.taskId === this.taskId);
			const title = selectedRow ? `${selectedRow.taskId} ${selectedRow.title}` : this.taskId;
			this.$refs.related.show(this.taskId, title + "的关联卡片");
		},
			/** 打印任务详情 */
			handlePrint() {
				this.taskId = this.ids[0];
				this.$prompt('请选择打印类型', '打印设置', {
					confirmButtonText: '确定',
					cancelButtonText: '取消',
					inputPlaceholder: '1:正常打印, 2:丢失补打, 3:分流打印',
					inputValue: '1',
					inputValidator: (value) => {
						if (!value || (value != 1 && value != 2 && value != 3)) {
							return '请输入正确的打印类型（1-3）';
						}
					}
				}).then(({
					value
				}) => {
					// 第一步：同步打开窗口
					const printWindow = window.open('', '_blank');
					if (!printWindow) {
					  alert('浏览器拦截了弹窗，请允许本站点的弹窗后重试');
					  return;
					}
					// 先显示加载提示，避免用户以为白屏
					printWindow.document.write(`
					  <html>
					    <head><meta charset="UTF-8"><title>流转卡打印</title></head>
					    <body><div style="text-align:center;margin-top:50px;font-size:16px;">正在加载打印内容...</div></body>
					  </html>
					`);
					console.log("开始读取");
					// 第二步：发起请求
					printDetail(this.taskId).then(htmlText => {
					  // 清空原有内容，写入完整HTML
						console.log(htmlText);
					  printWindow.document.open();
					  printWindow.document.write(htmlText);
					  printWindow.document.close(); // 必须关闭写入流，确保自动打印脚本执行
					}).catch(error => {
					  console.error('流转卡打印加载失败：', error);
					  printWindow.document.write(`
					    <html>
					      <head><meta charset="UTF-8"><title>加载失败</title></head>
					      <body><div style="text-align:center;margin-top:50px;color:red;font-size:16px;">加载失败，请关闭页面后重试</div></body>
					    </html>
					  `);
					  alert('流转卡打印加载失败，请稍后重试');
					});
				}).catch(() => {
					// 取消操作
				});
			},
			/** 导出任务信息为pdf */
			handleExportPdf() {
				this.taskId = this.ids[0];
				exportProdPdf(this.taskId).then(blobData => {
					// 直接使用返回的Blob对象，无需再取.data
					const blob = new Blob([blobData], {type: 'application/pdf'});
					// 执行下载逻辑
					const url = URL.createObjectURL(blob);
					const link = document.createElement('a');
					link.href = url;
					link.download = `任务详情_${new Date().getTime()}.pdf`;
					document.body.appendChild(link);
					link.click();
					// 清理内存资源，避免内存泄漏
					document.body.removeChild(link);
					URL.revokeObjectURL(url);
				})
			},
			/** 查询试制任务信息列表 */
			getList() {
				this.loading = true;
				listProd(this.queryParams).then(response => {
					this.prodList = response.rows;
					this.total = response.total;
					this.loading = false;
				});
			},
			// 取消按钮
			cancel() {
				this.open = false;
				this.reset();
			},
			// 表单重置
			reset() {
				this.form = {
					taskId: null,
					projectId: null,
					projectName: null,
					cardType: null,
					title: null,
					carType: null,
					assemblyName: null,
					assemblyFigure: null,
					pm: null,
					pe: null,
					status: null,
					createBy: null,
					createTime: null,
					updateBy: null,
					updateTime: null,
					remark: null,
					relatedTaskId: null
				};
				this.resetForm("form");
			},
			/** 搜索按钮操作 */
			handleQuery() {
				this.queryParams.pageNum = 1;
				this.getList();
			},
			/** 搜索项目按钮操作 */
			handleQueryProject(payload) {
				console.log(payload)
				this.form.projectId = payload.projectIds;
				this.form.projectName = payload.projectNames;
				//this.queryParams.pageNum = 1;
				//this.getList();
			},
			/** 变更项目按钮操作 */
			handleFlowProject(payload) {
				console.log("变更项目按钮操作",payload)
				// this.form.projectId = payload.projectIds;
				// this.form.projectName = payload.projectNames;
				this.handleFlow(payload);

			},
			/** 分流项目按钮操作 */
			handleForkProject(payload) {
				console.log(payload)
				this.handleFork(payload);

			},
			/** 重置按钮操作 */
			resetQuery() {
				this.resetForm("queryForm");
				this.handleQuery();
			},
			// 多选框选中数据
			handleSelectionChange(selection) {
				this.ids = selection.map(item => item.taskId)
				this.single = selection.length !== 1
				this.multiple = !selection.length
			},
			/** 新增按钮操作 */
			handleAdd() {
				this.reset();
				this.open = true;
				this.title = "添加试制任务信息";
			},
			/** 修改按钮操作 */
			handleUpdate(row) {
				this.reset();
				const taskId = row.taskId || this.ids
				getProd(taskId).then(response => {
					this.form = response.data;
					this.open = true;
					this.title = "修改试制任务信息";
				});
			},

			/** 变更按钮操作 flowProd*/
			handleFlow(row) {
				this.reset();
				const taskId = row.taskId
				flowProd(row).then(response => {
					this.$modal.msgSuccess("变更成功");
					this.handleQuery();
				});
			},
			/** 分流按钮操 forkProd, */
			handleFork(row) {
				this.reset();
				const taskId = row.taskId
				forkProd(row).then(response => {
					this.$modal.msgSuccess("分流成功");
					this.handleQuery();
				});
			},
			/** 提交按钮 */
			submitForm() {
				this.$refs["form"].validate(valid => {
					if (valid) {
						if (this.form.taskId != null) {
							updateProd(this.form).then(response => {
								this.$modal.msgSuccess("修改成功");
								this.open = false;
								this.getList();
							});
						} else {
							addProd(this.form).then(response => {
								this.$modal.msgSuccess("新增成功");
								this.open = false;
								this.getList();
							});
						}
					}
				});
			},
			/** 删除按钮操作 */
			handleDelete(row) {
				const taskIds = row.taskId || this.ids;
				this.$modal.confirm('是否确认删除试制任务信息编号为"' + taskIds + '"的数据项？').then(function() {
					return delProd(taskIds);
				}).then(() => {
					this.getList();
					this.$modal.msgSuccess("删除成功");
				}).catch(() => {});
			},
			/** 导出EXCEL按钮操作 */
			handleExportExcel() {
				this.download('trial/prod/exportExcel', {
					...this.queryParams
				}, `prod_${new Date().getTime()}.xlsx`)
			}
		}
	};
</script>