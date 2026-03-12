<template>
	<!-- 流转程序变更 -->
	<el-dialog title="选择需要变更到的环节" :visible.sync="visible" width="1600px" top="5vh" append-to-body>
		<el-row>
			<el-table v-loading="loading" :data="prodList" @selection-change="handleSelectionChange">
				<el-table-column label="序号" align="center" prop="serialNo" />
				<el-table-column label="流转程序" align="center" prop="program" />
				<el-table-column label="名称" align="center" prop="name" />
				<el-table-column label="图号" align="center" prop="figure" />
				<el-table-column label="试制数量" align="center" prop="trialQuantity" />
				<el-table-column label="送检数量" align="center" prop="inspectionQuantity" />
				<el-table-column label="质量状态" align="center" prop="manufacturingQualityStatus" />
				<el-table-column label="制造区域" align="center" prop="manufacturingArea" />
				<el-table-column label="负责人" align="center" prop="director" />
				<el-table-column label="电话" align="center" prop="directorTel" />
				<el-table-column label="质量状态" align="center" prop="processQualityStatus" />
				<el-table-column label="责任ME" align="center" prop="meDirector" />
				<el-table-column label="联系电话" align="center" prop="meDirectorTel" />
				<el-table-column label="备注" align="center" prop="notes" />
				<el-table-column label="状态" align="center" prop="status" width="100">
					<template slot-scope="scope">
						<dict-tag :options="dict.type.card_status" :value="scope.row.status" />
					</template>
				</el-table-column>
				<el-table-column type="selection" width="55" align="center" />
			</el-table>
			<pagination v-show="total>0" :total="total" :page.sync="queryParams.pageNum" :limit.sync="queryParams.pageSize"
				@pagination="getList" />
		</el-row>
		<div slot="footer" class="dialog-footer">
			<el-button type="primary" @click="handleSelectFlow" :disabled="single">确 定</el-button>
			<el-button @click="visible = false">取 消</el-button>
		</div>
	</el-dialog>
</template>

<script>
	import {
		listProd
	} from "@/api/trial/prodDetail";

	export default {
		dicts: ['card_status', 'sys_normal_disable'],
		props: {
			// 项目管理员
			taskId: {
				type: [Number, String]
			}
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
				showSearch: false,
				// 总条数

				// 遮罩层
				visible: false,
				// 选中数组值
				projectIds: [],
				projectNames: [],
				currentSerialNo: [],
				currentSerialName: [],
				// 总条数
				total: 0,
				// 试制任务程序表格数据
				prodList: [],
				// 弹出层标题
				title: "",
				// 是否显示弹出层
				open: false,
				// 查询参数
				queryParams: {
					pageNum: 1,
					pageSize: 10,
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
				},

			};
		},
		methods: {
			// 显示弹框
			show(taskId) {
				console.log("flow:" + taskId);
				this.queryParams.taskId = taskId;
				this.getList();
				this.visible = true;
			},
			clickRow(row) {
				this.$refs.table.toggleRowSelection(row);
			},
			// 多选框选中数据
			handleSelectionChange(selection) {
				this.ids = selection.map(item => item.taskId)
				this.single = selection.length !== 1
				this.multiple = !selection.length
				this.projectIds = selection.map(item => item.id);
				this.projectNames = selection.map(item => item.program);
				this.currentSerialNo = selection.map(item => item.serialNo);
				this.currentSerialName = selection.map(item => item.program);
			},
			// 查询表数据
			getList() {
				this.loading = true;
				listProd(this.queryParams).then(response => {
					this.prodList = response.rows;
					this.total = response.total;
					this.loading = false;
				});
			},
			/** 变更操作 */
			handleSelectFlow() {
				const taskId = this.queryParams.taskId;
				const currentSerialNo = this.currentSerialNo.join(",");
				const currentSerialName = this.currentSerialName.join(",");

				const pm = {
					taskId: taskId,
					currentSerialNo: currentSerialNo,
					currentSerialName: currentSerialName
				}

				this.visible = false;
				this.$emit("ok", pm);
			}
		}
	};
</script>