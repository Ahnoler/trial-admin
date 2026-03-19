<template>
	<div class="app-container">
		<el-form :model="queryParams" ref="queryForm" size="small" :inline="true" v-show="showSearch" label-width="68px">
			<el-form-item label="任务编号" prop="taskId">
				<el-select v-model="queryParams.taskId" placeholder="请选择试制任务" clearable filterable @change="handleQuery">
					<el-option v-for="item in taskList" :key="item.taskId" :label="item.title" :value="item.taskId" />
				</el-select>
			</el-form-item>

			<el-form-item label="图号" prop="figure">
				<el-input v-model="queryParams.figure" placeholder="请输入图号" clearable @keyup.enter.native="handleQuery" />
			</el-form-item>

			<el-form-item label="负责人" prop="director">
				<el-input v-model="queryParams.director" placeholder="请输入负责人" clearable @keyup.enter.native="handleQuery" />
			</el-form-item>

			<el-form-item label="责任ME" prop="meDirector">
				<el-input v-model="queryParams.meDirector" placeholder="请输入责任ME" clearable @keyup.enter.native="handleQuery" />
			</el-form-item>
			<el-form-item>
				<el-button type="primary" icon="el-icon-search" size="mini" @click="handleQuery">搜索</el-button>
				<el-button icon="el-icon-refresh" size="mini" @click="resetQuery">重置</el-button>
			</el-form-item>
		</el-form>

		<el-row :gutter="10" class="mb8">
			<el-col :span="1.5">
				<el-button type="primary" plain icon="el-icon-plus" size="mini" @click="handleAdd"
					v-hasPermi="['trial:prod:detail:add']">新增</el-button>
			</el-col>
			<el-col :span="1.5">
				<el-button type="success" plain icon="el-icon-edit" size="mini" :disabled="single" @click="handleUpdate"
					v-hasPermi="['trial:prod:detail:edit']">修改</el-button>
			</el-col>
			<el-col :span="1.5">
				<el-button type="danger" plain icon="el-icon-delete" size="mini" :disabled="multiple" @click="handleDelete"
					v-hasPermi="['trial:prod:detail:remove']">删除</el-button>
			</el-col>
			<el-col :span="1.5">
				<el-button type="warning" plain icon="el-icon-download" size="mini" @click="handleExport"
					v-hasPermi="['trial:prod:detail:export']">导出</el-button>
				<el-button size="mini" type="success" icon="el-icon-refresh-left" @click="handleApply()" :disabled="single || !canApply"
					v-hasPermi="['trial:prod:detail:edit']">申请</el-button>
				<el-button size="mini" type="info" icon="el-icon-s-check" :disabled="single || !canApprove" @click="handleAppove()"
					v-hasPermi="['trial:prod:detail:edit']">审核</el-button>
			</el-col>
			<right-toolbar :showSearch.sync="showSearch" @queryTable="getList"></right-toolbar>
		</el-row>

		<el-table v-loading="loading" :data="prodList" @selection-change="handleSelectionChange">
			<template slot="empty">
				<div class="empty-tip">请先选择试制任务</div>
			</template>
			<el-table-column type="selection" width="55" align="center" />
			<el-table-column type="expand" width="50">
				<template slot-scope="props">
					<el-form label-position="left" class="demo-table-expand" label-width="120px">
						<el-form-item label="试制数量">
							<span>{{ props.row.trialQuantity }}</span>
						</el-form-item>
						<el-form-item label="送检数量">
							<span>{{ props.row.inspectionQuantity }}</span>
						</el-form-item>
						<el-form-item label="制造区域">
							<span>{{ props.row.manufacturingArea }}</span>
						</el-form-item>
						<el-form-item label="负责人">
							<span>{{ props.row.director }}</span>
						</el-form-item>
						<el-form-item label="负责人电话">
							<span>{{ props.row.directorTel }}</span>
						</el-form-item>
						<el-form-item label="责任ME">
							<span>{{ props.row.meDirector }}</span>
						</el-form-item>
						<el-form-item label="责任ME电话">
							<span>{{ props.row.meDirectorTel }}</span>
						</el-form-item>
					</el-form>
				</template>
			</el-table-column>

			<el-table-column label="任务编号" align="center" prop="taskId" />
			<el-table-column label="序号" align="center" prop="serialNo" />
			<el-table-column label="流转程序" align="center" prop="program" />
			<el-table-column label="名称" align="center" prop="name" />
			<el-table-column label="图号" align="center" prop="figure" />
			<el-table-column label="制造质量" align="center" prop="manufacturingQualityStatus" />
			<el-table-column label="工艺质量" align="center" prop="processQualityStatus" />
			<el-table-column label="备注" align="center" prop="notes" />
			<el-table-column label="状态" align="center" prop="status" width="100">
				<template slot-scope="scope">
					<dict-tag :options="dict.type.card_status" :value="scope.row.status" />
				</template>
			</el-table-column>
		</el-table>



		<!-- 添加或修改试制任务程序对话框 -->
		<el-dialog :title="title" :visible.sync="open" width="500px" append-to-body>
			<el-form ref="form" :model="form" :rules="rules" label-width="80px">
				<!-- <el-form-item label="任务编号" prop="taskId">
          <el-input v-model="form.taskId" placeholder="请输入任务编号" />
        </el-form-item>
        <el-form-item label="卡片列头编码" prop="columnCode">
          <el-input v-model="form.columnCode" placeholder="请输入卡片列头编码" />
        </el-form-item>
        <el-form-item label="序号" prop="serialNo">
          <el-input v-model="form.serialNo" placeholder="请输入序号" />
        </el-form-item> -->
				<el-form-item label="流转程序" prop="program">
					<el-input v-model="form.program" placeholder="请输入流转程序" />
				</el-form-item>
				<el-form-item label="名称" prop="name">
					<el-input v-model="form.name" placeholder="请输入名称" />
				</el-form-item>
				<el-form-item label="图号" prop="figure">
					<el-input v-model="form.figure" placeholder="请输入图号" />
				</el-form-item>
				<el-form-item label="试制数量" prop="trialQuantity">
					<el-input v-model="form.trialQuantity" placeholder="请输入试制数量" />
				</el-form-item>
				<el-form-item label="送检数量" prop="inspectionQuantity">
					<el-input v-model="form.inspectionQuantity" placeholder="请输入送检数量" />
				</el-form-item>
				<el-form-item label="制造区域" prop="manufacturingArea">
					<el-input v-model="form.manufacturingArea" placeholder="请输入制造区域" />
				</el-form-item>
				<el-form-item label="负责人" prop="director">
					<el-input v-model="form.director" placeholder="请输入负责人" />
				</el-form-item>
				<el-form-item label="电话" prop="directorTel">
					<el-input v-model="form.directorTel" placeholder="请输入电话" />
				</el-form-item>
				<el-form-item label="责任ME" prop="meDirector">
					<el-input v-model="form.meDirector" placeholder="请输入责任ME" />
				</el-form-item>
				<el-form-item label="联系电话" prop="meDirectorTel">
					<el-input v-model="form.meDirectorTel" placeholder="请输入联系电话" />
				</el-form-item>
				<el-form-item label="备注" prop="notes">
					<el-input v-model="form.notes" placeholder="请输入备注" />
				</el-form-item>

			</el-form>
			<div slot="footer" class="dialog-footer">
				<el-button type="primary" @click="submitForm">确 定</el-button>
				<el-button @click="cancel">取 消</el-button>
			</div>
		</el-dialog>
	</div>
</template>

<script>
	import {
		listProd,
		getProd,
		delProd,
		addProd,
		updateProd,
		approveProd,
		applyProd
	} from "@/api/trial/prodDetail";
	import { listProd as listTrialTask } from "@/api/trial/prod";

	export default {
		name: "ProdDetail",
		dicts: ['card_status', 'sys_normal_disable'],
		data() {
			return {
				// 遮罩层
				loading: false,
				// 选中数组
				ids: [],
				// 非单个禁用
				single: true,
				// 非多个禁用
				multiple: true,
				// 是否可以申请
				canApply: false,
				// 是否可以审核
				canApprove: false,
				// 显示搜索条件
				showSearch: true,
				// 总条数
				total: 0,
				// 试制任务程序表格数据
				prodList: [],
				// 试制任务列表
				taskList: [],
				// 弹出层标题
				title: "",
				// 是否显示弹出层
				open: false,
				// 查询参数
				queryParams: {
					taskId: null,
					figure: null,
					director: null,
					meDirector: null,
				},
				// 表单参数
				form: {},
				// 表单校验
				rules: {
					taskId: [{
						required: true,
						message: "任务编号不能为空",
						trigger: "blur"
					}],
					serialNo: [{
						required: true,
						message: "序号不能为空",
						trigger: "blur"
					}],
				}
			};
		},
		created() {
			this.getTaskList();
		},
		methods: {
			/** 查询试制任务列表 */
			getTaskList() {
				listTrialTask().then(response => {
					this.taskList = response.rows || [];
				});
			},
			/** 查询试制任务程序列表 */
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
					id: null,
					taskId: null,
					cardType: null,
					columnCode: null,
					serialNo: null,
					program: null,
					name: null,
					figure: null,
					trialQuantity: null,
					inspectionQuantity: null,
					manufacturingQualityStatus: null,
					manufacturingArea: null,
					director: null,
					directorTel: null,
					processQualityStatus: null,
					meDirector: null,
					meDirectorTel: null,
					notes: null,
					status: null,
					createBy: null,
					createTime: null,
					updateBy: null,
					updateTime: null,
					remark: null
				};
				this.resetForm("form");
			},
			/** 搜索按钮操作 */
			handleQuery() {
				if (!this.queryParams.taskId) {
					this.$modal.msgWarning("请先选择试制任务");
					return;
				}
				this.getList();
			},
			/** 重置按钮操作 */
			resetQuery() {
				this.resetForm("queryForm");
				this.handleQuery();
			},
			// 多选框选中数据
			handleSelectionChange(selection) {
				this.ids = selection.map(item => item.id)
				this.single = selection.length !== 1
				this.multiple = !selection.length
				this.canApply = selection.length === 1 && selection[0].status === '1'
				this.canApprove = selection.length === 1 && selection[0].status === '2'
			},
			/** 新增按钮操作 */
			handleAdd() {
				this.reset();
				this.open = true;
				this.title = "添加试制任务程序";
			},
			/** 修改按钮操作 */
			handleUpdate(row) {
				this.reset();
				const id = row.id || this.ids[0];
				getProd(id).then(response => {
					this.form = response.data;
					this.open = true;
					this.title = "修改试制任务程序";
				});
			},
			/** 提交按钮 */
			submitForm() {
				this.$refs["form"].validate(valid => {
					if (valid) {
						if (this.form.id != null) {
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
			/** 申请按钮操作 */
			handleApply(row) {
				const id = this.ids[0];
				this.$modal.confirm('是否确认申请试制任务程序的填写内容').then(() => {
					applyProd({ id: id }).then(response => {
						this.$modal.msgSuccess("申请成功");
						this.reset();
						this.getList();
					});
				}).catch(() => {});
			},
			/** 审核按钮操作 */
			handleAppove(row) {
				const id = this.ids[0];
				this.$modal.confirm('是否确认审核试制任务程序的填写内容').then(() => {
					approveProd({ id: id }).then(response => {
						this.$modal.msgSuccess("审核成功");
						this.reset();
						this.getList();
					});
				}).catch(() => {});
			},
			/** 删除按钮操作 */
			handleDelete(row) {
				const ids = row.id || this.ids;
				this.$modal.confirm('是否确认删除试制任务程序编号为"' + ids + '"的数据项？').then(function() {
					return delProd(ids);
				}).then(() => {
					this.getList();
					this.$modal.msgSuccess("删除成功");
				}).catch(() => {});
			},
			/** 导出按钮操作 */
			handleExport() {
				this.download('trial/prod/detail/export', {
					...this.queryParams
				}, `prod_${new Date().getTime()}.xlsx`)
			}
		}
	};
</script>

<style scoped>
.demo-table-expand {
  margin-left: 105px;
  width: calc(100% - 105px);
  padding: 8px 0;
}

.demo-table-expand .el-form-item {
  width: 100%;
  margin-bottom: 16px;
  margin-right: 0;
}

.empty-tip {
  font-size: 18px;
  color: #909399;
  padding: 40px 0;
}
</style>