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
					<el-form-item label="试制管理员" prop="pm">
						<el-input v-model="queryParams.pm" placeholder="姓名或电话，支持模糊查询" clearable @keyup.enter.native="handleQuery" />
					</el-form-item>
				</el-col>
				<el-col :span="8">
					<el-form-item label="PE姓名" prop="pe">
						<el-input v-model="queryParams.pe" placeholder="姓名或电话，支持模糊查询" clearable @keyup.enter.native="handleQuery" />
					</el-form-item>
				</el-col>
				<el-col :span="8">
					<el-form-item label="任务状态" prop="status">
						<el-select v-model="queryParams.status" placeholder="请选择任务状态" clearable style="width: 100%">
							<el-option v-for="dict in dict.type.task_status" :key="dict.value" :label="dict.label"
								:value="dict.value"></el-option>
						</el-select>
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
				<el-button type="success" plain icon="el-icon-edit" size="mini" :disabled="single || isStoppedOrFinishedSelected" @click="handleUpdate"
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
				<el-button type="danger" plain icon="el-icon-d-caret" size="mini" @click="openFlowProject" :disabled="single || isStoppedOrFinishedSelected"
					v-hasPermi="['trial:projects:edit']">变更</el-button>
			</el-col>
			<el-col :span="1.5">
				<el-button type="success" plain icon="el-icon-share" size="mini" @click="openForkProject" :disabled="single || isStoppedOrFinishedSelected"
					v-hasPermi="['trial:projects:edit']">分流</el-button>
			</el-col>
			<el-col :span="1.5">
				<el-button type="warning" plain icon="el-icon-close" size="mini" @click="openDisableProject"
					:disabled="disableBtnDisabled" v-hasPermi="['trial:prod:edit']">停用</el-button>
			</el-col>
			<el-col :span="1.5">
				<el-button type="success" plain icon="el-icon-check" size="mini" @click="openEnableProject"
					:disabled="enableBtnDisabled" v-hasPermi="['trial:prod:edit']">启用</el-button>
			</el-col>
			<el-col :span="1.5">
				<el-button type="warning" plain icon="el-icon-switch-button" size="mini" @click="openCloseProject"
					:disabled="single || isStoppedOrFinishedSelected" v-hasPermi="['trial:projects:edit']">结束</el-button>
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

		<el-table v-loading="loading" :data="prodList" row-key="taskId" @selection-change="handleSelectionChange">
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
			<el-table-column label="试制管理员" align="center" min-width="100">
				<template slot-scope="scope">
					<el-button
						v-if="scope.row.pm"
						type="text"
						class="contact-link"
						@click.stop="openContactDialog('试制管理员/电话', scope.row.pm)"
					>{{ contactDisplayName(scope.row.pm) }}</el-button>
					<span v-else>—</span>
				</template>
			</el-table-column>
			<el-table-column label="PE姓名" align="center" min-width="100">
				<template slot-scope="scope">
					<el-button
						v-if="scope.row.pe"
						type="text"
						class="contact-link"
						@click.stop="openContactDialog('PE姓名/电话', scope.row.pe)"
					>{{ contactDisplayName(scope.row.pe) }}</el-button>
					<span v-else>—</span>
				</template>
			</el-table-column>
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
		<select-project ref="select" :pm="queryParams.pm" @ok="handleQueryProject" />
		<selectRelated ref="related" :taskId="taskId" :title="''" />

		<el-dialog :title="contactDialogTitle" :visible.sync="contactDialogVisible" width="480px" append-to-body @close="contactDialogText = ''">
			<div class="contact-dialog-body">{{ contactDialogText }}</div>
			<div slot="footer" class="dialog-footer">
				<el-button type="primary" @click="contactDialogVisible = false">关 闭</el-button>
			</div>
		</el-dialog>
	</div>
</template>

<script>
	import {
		listProd,
		delProd,
		forkProd,
		flowProd,
		overProd,
		disableProd,
		enableProd,
		exportProdPdf,
		printDetail
	} from "@/api/trial/prod";
	import selectProject from "./selectProject";
import selectRelated from "./selectRelated";

	export default {
		name: "Prod",
		dicts: ['car_type', 'sys_normal_disable', 'task_status'],
		components: {
		selectProject,
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
					onlyMine: null,
				},
				// 当前选中任务状态（0正常 1停用 2完成）
				selectedStatus: null,
				contactDialogVisible: false,
				contactDialogTitle: "",
				contactDialogText: "",
			};
		},
		computed: {
			isStoppedOrFinishedSelected() {
				// 只有“单选”时才有意义；多选/未选中时保持 false
				if (this.selectedStatus === null || this.selectedStatus === undefined) return false;
				return String(this.selectedStatus) === "1" || String(this.selectedStatus) === "2";
			},
			// 停用/启用按钮禁用规则（0正常 1停用 2完成）
			disableBtnDisabled() {
				// 只有单选时允许操作；仅 0 -> 1 可停用
				if (this.single) return true;
				return String(this.selectedStatus) !== "0";
			},
			enableBtnDisabled() {
				// 只有单选时允许操作；仅 1 -> 0 可启用
				if (this.single) return true;
				return String(this.selectedStatus) !== "1";
			},
		},
		watch: {
			$route: {
				handler(to, from) {
					if (to.path !== "/trial/prod") {
						return;
					}
					if (from && to.fullPath === from.fullPath) {
						return;
					}
					this.applyRouteQuery();
					if (from) {
						this.queryParams.pageNum = 1;
					}
					this.getList();
				},
				immediate: true,
			},
		},
		methods: {
			/** 从「姓名/电话」类字符串中取展示用姓名 */
			contactDisplayName(val) {
				if (val == null || val === "") {
					return "—";
				}
				const s = String(val).trim();
				const bySlash = s.split(/[/|｜]/);
				if (bySlash.length > 1) {
					return bySlash[0].trim() || s;
				}
				const m = s.match(/^(.+?)[\s,，]+([\d\s\-+()（）]{6,})$/);
				if (m) {
					return m[1].trim() || s;
				}
				return s;
			},
			openContactDialog(title, fullText) {
				if (!fullText) {
					return;
				}
				this.contactDialogTitle = title;
				this.contactDialogText = String(fullText).trim();
				this.contactDialogVisible = true;
			},
			applyRouteQuery() {
				const q = (this.$route && this.$route.query) || {};
				// 路由里 status=0 时 query 为字符串 '0'，需与字典 el-option 的 value 类型一致
				if (q.status !== undefined && q.status !== null && String(q.status).trim() !== "") {
					this.queryParams.status = String(q.status);
				} else {
					this.queryParams.status = null;
				}
				if (q.onlyMine === "true" || q.onlyMine === true) {
					this.queryParams.onlyMine = true;
				} else {
					this.queryParams.onlyMine = null;
				}
			},
			/** 打开变更表弹窗 */
			openFlowProject() {
				this.taskId = this.ids[0];
				console.log("openFlowProject" + this.taskId);
				this.$router.push({ path: "/trial/prod-selectFlow/index", query: { taskId: this.taskId } });
			},
			/** 打开分流表弹窗 */
			openForkProject() {
				this.taskId = this.ids[0];
				console.log("openForkProject" + this.taskId);
				this.$router.push({ path: "/trial/prod-selectFork/index", query: { taskId: this.taskId } });
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
			/** 停用任务 */
			openDisableProject() {
				this.taskId = this.ids[0];
				const row = { taskId: this.taskId };
				this.$modal.confirm('是否确认停用试制任务编号为"' + this.taskId + '"的数据项？').then(() => {
					return disableProd(row);
				}).then(() => {
					this.$modal.msgSuccess("停用成功");
					this.getList();
				}).catch(() => {});
			},
			/** 启用任务 */
			openEnableProject() {
				this.taskId = this.ids[0];
				const row = { taskId: this.taskId };
				this.$modal.confirm('是否确认启用试制任务编号为"' + this.taskId + '"的数据项？').then(() => {
					return enableProd(row);
				}).then(() => {
					this.$modal.msgSuccess("启用成功");
					this.getList();
				}).catch(() => {});
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
				const params = { ...this.queryParams };
				if (!params.onlyMine) {
					delete params.onlyMine;
				}
				listProd(params).then(response => {
					this.prodList = response.rows;
					this.total = response.total;
					this.loading = false;
				});
			},
			/** 搜索按钮操作 */
			handleQuery() {
				this.queryParams.pageNum = 1;
				this.getList();
			},
			/** 搜索项目按钮操作 */
			handleQueryProject(payload) {
				console.log(payload)
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
				this.queryParams.onlyMine = null;
				this.$router.replace({ path: this.$route.path, query: {} }).catch(() => {});
				this.handleQuery();
			},
			// 多选框选中数据
			handleSelectionChange(selection) {
				this.ids = selection.map(item => item.taskId)
				this.single = selection.length !== 1
				this.multiple = !selection.length
				this.selectedStatus = selection.length === 1 ? selection[0].status : null;
			},
			/** 新增按钮操作 */
			handleAdd() {
				this.$router.push({ path: "/trial/prod-add/index" });
			},
			/** 修改按钮操作 */
			handleUpdate(row) {
				const taskId = row && row.taskId ? row.taskId : (this.ids && this.ids.length ? this.ids[0] : null);
				if (!taskId) return;
				this.$router.push({ path: `/trial/prod-add/index/${taskId}` });
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

<style lang="scss" scoped>
.contact-link {
	padding: 0;
}
.contact-dialog-body {
	white-space: pre-wrap;
	word-break: break-all;
	line-height: 1.6;
}
</style>