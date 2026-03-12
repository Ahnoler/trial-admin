<template>
	<!-- 关联卡片列表弹窗 -->
	<el-dialog :title="dialogTitle" :visible.sync="visible" width="1200px" top="5vh" append-to-body>
		<el-row>
			<el-table v-loading="loading" :data="relatedProdList" @selection-change="handleSelectionChange">
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
			
			<pagination v-show="total > 0" :total="total" :page.sync="queryParams.pageNum" :limit.sync="queryParams.pageSize"
				@pagination="getList" />
		</el-row>
		<div slot="footer" class="dialog-footer">
			<el-button @click="visible = false">关 闭</el-button>
		</div>
	</el-dialog>
</template>

<script>
	import { listRelatedProd } from "@/api/trial/prod";
	export default {
		dicts: ['car_type', 'task_status'],
		props: {
			taskId: {
				type: [Number, String]
			},
			title: {
				type: String
			}
		},
		data() {
			return {
				// 遮罩层
				visible: false,
				// 遮罩层
				loading: true,
				// 选中数组
				ids: [],
				// 非单个禁用
				single: true,
				// 非多个禁用
				multiple: true,
				// 总条数
				total: 0,
				// 关联卡片表格数据
				relatedProdList: [],
				// 查询参数
				queryParams: {
					pageNum: 1,
					pageSize: 10
				},
				dialogTitle: '',
				currentTaskId: null
			};
		},
		methods: {
			// 显示弹框
			show(taskId, title) {
				this.dialogTitle = title;
				this.currentTaskId = taskId;
				this.queryParams.pageNum = 1;
				this.getList();
				this.visible = true;
			},
			// 多选框选中数据
			handleSelectionChange(selection) {
				this.ids = selection.map(item => item.taskId)
				this.single = selection.length !== 1
				this.multiple = !selection.length
			},
			// 查询关联卡片列表
			getList() {
				if (!this.currentTaskId) return;
				this.loading = true;
				listRelatedProd(this.currentTaskId, this.queryParams).then(response => {
					this.relatedProdList = response.rows;
					this.total = response.total;
					this.loading = false;
				});
			}
		}
	};
</script>