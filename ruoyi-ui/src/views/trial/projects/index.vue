<template>
	<div class="app-container">
		<el-form :model="queryParams" ref="queryForm" size="small" :inline="true" v-show="showSearch"
			label-width="68px">
			<el-form-item label="项目名称" prop="projectName">
				<el-input v-model="queryParams.projectName" placeholder="请输入项目名称" clearable
					@keyup.enter.native="handleQuery" />
			</el-form-item>
			<el-form-item label="总成名称" prop="assemblyName">
				<el-input v-model="queryParams.assemblyName" placeholder="请输入总成名称" clearable
					@keyup.enter.native="handleQuery" />
			</el-form-item>
			<el-form-item label="总成图号" prop="assemblyDrawingNumber">
				<el-input v-model="queryParams.assemblyDrawingNumber" placeholder="请输入总成图号" clearable
					@keyup.enter.native="handleQuery" />
			</el-form-item>
			<el-form-item label="项目类型" prop="projectType">
				<el-select v-model="queryParams.projectType" placeholder="项目类型" clearable style="width: 120px">
					<el-option v-for="dict in dict.type.project_type" :key="dict.value" :label="dict.label"
						:value="dict.value" />
				</el-select>
			</el-form-item>
			<el-form-item label="状态" prop="status">
				<el-select v-model="queryParams.status" placeholder="项目状态" clearable style="width: 120px">
					<el-option v-for="dict in dict.type.sys_normal_disable" :key="dict.value" :label="dict.label"
						:value="dict.value" />
				</el-select>
			</el-form-item>
			<el-form-item>
				<el-button type="primary" icon="el-icon-search" size="mini" @click="handleQuery">搜索</el-button>
				<el-button icon="el-icon-refresh" size="mini" @click="resetQuery">重置</el-button>
			</el-form-item>
		</el-form>

		<el-row :gutter="10" class="mb8">
			<el-col :span="1.5">
				<el-button type="primary" plain icon="el-icon-plus" size="mini" @click="handleAdd"
					v-hasPermi="['trial:projects:add']">新增</el-button>
			</el-col>
			<el-col :span="1.5">
				<el-button type="success" plain icon="el-icon-edit" size="mini" :disabled="single" @click="handleUpdate"
					v-hasPermi="['trial:projects:edit']">修改</el-button>
			</el-col>
			<el-col :span="1.5">
				<el-button type="danger" plain icon="el-icon-delete" size="mini" :disabled="multiple"
					@click="handleDelete" v-hasPermi="['trial:projects:remove']">删除</el-button>
			</el-col>
			<el-col :span="1.5">
				<el-button type="warning" plain icon="el-icon-download" size="mini" @click="handleExport"
					v-hasPermi="['trial:projects:export']">导出</el-button>
			</el-col>
			<el-col :span="1.5">
				<el-button type="info" plain icon="el-icon-printer" size="mini" :disabled="single" @click="handlePrint"
					v-hasPermi="['trial:projects:print']">打印</el-button>
			</el-col>
			<el-col :span="1.5">
				<el-button type="primary" plain icon="el-icon-document" size="mini" :disabled="single" @click="handleExportPdf"
					v-hasPermi="['trial:projects:export']">导出PDF</el-button>
			</el-col>
			<el-col :span="1.5">
				<el-button type="success" plain icon="el-icon-s-order" size="mini" :disabled="single" @click="handlePrintAllCards"
					v-hasPermi="['trial:projects:print']">打印所有流转卡</el-button>
			</el-col>
			
			<right-toolbar :showSearch.sync="showSearch" @queryTable="getList"></right-toolbar>
		</el-row>

		<el-table v-loading="loading" :data="projectsList" @selection-change="handleSelectionChange">
			<el-table-column type="selection" width="55" align="center" />
			<el-table-column label="序号" align="center" prop="id" />
			<el-table-column label="项目名称" align="center" prop="projectName" />
			<el-table-column label="总成名称" align="center" prop="assemblyName" />
			<el-table-column label="总成图号" align="center" prop="assemblyDrawingNumber" />
			<el-table-column label="项目类型" align="center" prop="projectType" width="100">
				<template slot-scope="scope">
					<dict-tag :options="dict.type.project_type" :value="scope.row.projectType" />
				</template>
			</el-table-column>
			<el-table-column label="项目状态" align="center" prop="projectStatus" width="100" />
			<el-table-column label="项目负责人" align="center" prop="projectLeader" />
			<el-table-column label="项目管理员" align="center" prop="pmName" />
			<el-table-column label="启用状态" align="center" key="status">
				<template slot-scope="scope">
					<el-switch v-model="scope.row.status" active-value="0" inactive-value="1"
						@change="handleStatusChange(scope.row)"></el-switch>
				</template>
			</el-table-column>
			<el-table-column label="备注" align="center" prop="remark" />
			<el-table-column label="操作" align="center" class-name="small-padding fixed-width">
				<template slot-scope="scope">
					<el-button size="mini" type="text" icon="el-icon-edit" @click="handleUpdate(scope.row)"
						v-hasPermi="['trial:projects:edit']">修改</el-button>
					<el-button size="mini" type="text" icon="el-icon-delete" @click="handleDelete(scope.row)"
						v-hasPermi="['trial:projects:remove']">删除</el-button>
				</template>
			</el-table-column>
		</el-table>

		<pagination v-show="total>0" :total="total" :page.sync="queryParams.pageNum" :limit.sync="queryParams.pageSize"
			@pagination="getList" />

		<!-- 添加或修改项目管理对话框 -->
		<el-dialog :title="title" :visible.sync="open" width="600px" append-to-body>
			<el-form ref="form" :model="form" :rules="rules" label-width="120px">
				<el-form-item label="项目名称" prop="projectName">
					<el-input v-model="form.projectName" placeholder="请输入项目名称" />
				</el-form-item>
				<el-form-item label="项目类型" prop="projectType">
					<el-select v-model="form.projectType" placeholder="请选择项目类型">
						<el-option v-for="dict in dict.type.project_type" :key="dict.value" :label="dict.label"
							:value="dict.value"></el-option>
					</el-select>
				</el-form-item>
				<el-form-item label="总成名称" prop="assemblyName">
					<el-input v-model="form.assemblyName" placeholder="请输入总成名称" />
				</el-form-item>
				<el-form-item label="总成图号" prop="assemblyDrawingNumber">
					<el-input v-model="form.assemblyDrawingNumber" placeholder="请输入总成图号" />
				</el-form-item>
				<el-form-item label="项目描述" prop="projectDescription">
					<el-input v-model="form.projectDescription" type="textarea" placeholder="请输入项目描述" />
				</el-form-item>
				<el-form-item label="开始日期" prop="startDate">
					<el-date-picker v-model="form.startDate" type="date" placeholder="选择开始日期" style="width: 100%;" />
				</el-form-item>
				<el-form-item label="结束日期" prop="endDate">
					<el-date-picker v-model="form.endDate" type="date" placeholder="选择结束日期" style="width: 100%;" />
				</el-form-item>
				<el-form-item label="项目状态" prop="projectStatus">
					<el-select v-model="form.projectStatus" placeholder="请选择项目状态">
						<el-option label="新建" value="新建" />
						<el-option label="进行中" value="进行中" />
						<el-option label="已完成" value="已完成" />
						<el-option label="已取消" value="已取消" />
					</el-select>
				</el-form-item>
				<el-form-item label="预算" prop="budget">
					<el-input v-model="form.budget" type="number" placeholder="请输入预算" />
				</el-form-item>
				<el-form-item label="实际成本" prop="actualCost">
					<el-input v-model="form.actualCost" type="number" placeholder="请输入实际成本" />
				</el-form-item>
				<el-form-item label="优先级" prop="priority">
					<el-select v-model="form.priority" placeholder="请选择优先级">
						<el-option label="高" value="高" />
						<el-option label="中" value="中" />
						<el-option label="低" value="低" />
					</el-select>
				</el-form-item>
				<el-form-item label="项目负责人" prop="projectLeader">
					<el-input v-model="form.projectLeader" placeholder="请输入项目负责人" />
				</el-form-item>
				<el-form-item label="联系信息" prop="contactInfo">
					<el-input v-model="form.contactInfo" placeholder="请输入联系信息" />
				</el-form-item>
				<el-form-item label="项目管理员" prop="pm">
					<el-input v-model="form.pm" v-show="false" placeholder="请输入项目管理编号" />
					<el-input v-model="form.pmName" placeholder="请选择项目管理员" style="width:60%;float: left;" disabled />
					<el-button type="primary" style="line-height:20px; width:40%;float: right;"  plain icon="el-icon-plus" size="mini" @click="openSelectUser"
						v-hasPermi="['trial:projects:add']">指定项目管理员</el-button>

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
		<select-user ref="select" :pm="queryParams.pm" @ok="handleQueryUser" />
	</div>
</template>

<script>
	import {
		listProjects,
		getProjects,
		delProjects,
		addProjects,
		updateProjects,
		printProjects,
		exportProjectPdf,
		printAllProjectsCards
	} from "@/api/trial/projects";

	import selectUser from "./selectUser";

	export default {
		name: "Projects",
		dicts: ['project_type', 'sys_normal_disable'],
		components: {
			selectUser
		},
		data() {
			return {
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
				// 项目管理表格数据
				projectsList: [],
				// 弹出层标题
				title: "",
				// 是否显示弹出层
				open: false,
				// 查询参数
				queryParams: {
					pageNum: 1,
					pageSize: 10,
					projectName: null,
					projectType: null,
					assemblyName: null,
					assemblyDrawingNumber: null,
					status: null,
				},
				// 表单参数
				form: {},
				// 表单校验
				rules: {}
			};
		},
		created() {
			this.getList();
		},
		methods: {
			/** 打开授权用户表弹窗 */
			openSelectUser() {
				this.$refs.select.show();
			},
			/** 查询项目管理列表 */
			getList() {
				this.loading = true;
				listProjects(this.queryParams).then(response => {
					this.projectsList = response.rows;
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
					projectName: null,
					projectType: null,
					assemblyName: null,
					assemblyDrawingNumber: null,
					projectDescription: null,
					startDate: null,
					endDate: null,
					projectStatus: null,
					budget: null,
					actualCost: null,
					priority: null,
					projectLeader: null,
					contactInfo: null,
					pm:null,
					pmName: null,
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
				// console.log(payload)
				// this.form.pm = payload.userIds;
				// this.form.pmName = payload.userNames;
				this.queryParams.pageNum = 1;
				this.getList();
			},
			/** 搜索用户按钮操作 */
			handleQueryUser(payload) {
				console.log(payload)
				this.form.pm = payload.userIds;
				this.form.pmName = payload.userNames;
				//this.queryParams.pageNum = 1;
				//this.getList();
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
			},
			/** 新增按钮操作 */
			handleAdd() {
				this.reset();
				this.open = true;
				this.title = "添加项目管理";
			},
			/** 修改按钮操作 */
			handleUpdate(row) {
				this.reset();
				const id = row.id || this.ids
				getProjects(id).then(response => {
					this.form = response.data;
					this.open = true;
					this.title = "修改项目管理";
				});
			},
			/** 提交按钮 */
			submitForm() {
				this.$refs["form"].validate(valid => {
					if (valid) {
						if (this.form.id != null) {
							updateProjects(this.form).then(response => {
								this.$modal.msgSuccess("修改成功");
								this.open = false;
								this.getList();
							});
						} else {
							addProjects(this.form).then(response => {
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
				const ids = row.id || this.ids;
				this.$modal.confirm('是否确认删除项目管理编号为"' + ids + '"的数据项？').then(function() {
					return delProjects(ids);
				}).then(() => {
					this.getList();
					this.$modal.msgSuccess("删除成功");
				}).catch(() => {});
			},
			/** 导出按钮操作 */
			handleExport() {
				this.download('trial/projects/export', {
					...this.queryParams
				}, `projects_${new Date().getTime()}.xlsx`)
			},
			/** 打印按钮操作 */
			handlePrint() {
			  const id = this.ids[0];
			  printProjects(id).then(blobData => {
			    // 兜底拦截：后端返回JSON错误信息时，提前提示
			    if (blobData.type === 'application/json') {
			      const errorText = new Response(blobData).text();
			      const errorInfo = JSON.parse(errorText);
			      alert(errorInfo.msg || '打印预览加载失败');
			      return;
			    }
			
			    const blob = new Blob([blobData], { type: 'application/pdf' });
			    const url = URL.createObjectURL(blob);
			    // 新窗口打开PDF预览，用户可直接在预览页触发打印
			    window.open(url, '_blank');
			  }).catch(error => {
			    console.error('打印预览加载失败：', error);
			    alert('打印预览加载失败，请稍后重试');
			  });
			},
			/** 导出PDF按钮操作 */
			handleExportPdf() {
			  const id = this.ids[0];
			  exportProjectPdf(id).then(blobData => {
			    // 直接使用返回的Blob对象，无需再取.data
			    const blob = new Blob([blobData], { type: 'application/pdf' });
			    // 执行下载逻辑
			    const url = URL.createObjectURL(blob);
			    const link = document.createElement('a');
			    link.href = url;
			    link.download = `项目信息_${new Date().getTime()}.pdf`;
			    document.body.appendChild(link);
			    link.click();
			    // 清理内存资源，避免内存泄漏
			    document.body.removeChild(link);
			    URL.revokeObjectURL(url);
			
			  }).catch(error => {
			    alert('导出PDF失败，请稍后重试');
			  });
			},
			/** 打印所有流转卡按钮操作 */
			handlePrintAllCards() {
			  const id = this.ids[0];
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
			  // 第二步：发起请求
			  printAllProjectsCards(id).then(htmlText => {
			    // 清空原有内容，写入完整HTML
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
			}
		}
	};
</script>