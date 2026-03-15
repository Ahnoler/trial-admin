<template>
	<!-- 分流卡片：根据母卡片信息，创建新卡片 -->
	<el-dialog title="选择需要分流的流转程序" :visible.sync="visible" width="1600px" top="5vh" append-to-body>
		<el-row>
			<el-table ref="table" v-loading="loading" :data="prodList" @selection-change="handleSelectionChange">
				<el-table-column label="序号" align="center" prop="serialNo" />
				<el-table-column type="expand" width="50">
					<template slot-scope="props">
						<el-form label-position="left" class="demo-table-expand" label-width="120px">
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
				<el-table-column label="流转程序" align="center" prop="program" />
				<el-table-column label="名称" align="center" prop="name" />
				<el-table-column label="图号" align="center" prop="figure" />
				<el-table-column label="试制数量" align="center" prop="trialQuantity" />
				<el-table-column label="分流数量" align="center" width="150">
					<template slot-scope="scope">
						<el-input-number v-model="scope.row.shuntQty" :min="0" :max="parseInt(scope.row.trialQuantity)" size="mini" :disabled="!isSelected(scope.row) || scope.row.status !== '1'"></el-input-number>
					</template>
				</el-table-column>
				<el-table-column label="送检数量" align="center" prop="inspectionQuantity" />
				<el-table-column label="制造质量" align="center" prop="manufacturingQualityStatus" />
				<el-table-column label="工艺质量" align="center" prop="processQualityStatus" />
				<el-table-column label="备注" align="center" prop="notes" />
				<el-table-column label="状态" align="center" prop="status" width="100">
					<template slot-scope="scope">
						<dict-tag :options="dict.type.card_status" :value="scope.row.status" />
					</template>
				</el-table-column>
				<el-table-column label="备注" align="center" prop="remark" />
				<el-table-column type="selection" width="55" align="center" :selectable="checkSelectable" />
			</el-table>
			
			<pagination v-show="total > 0" :total="total" :page.sync="queryParams.pageNum" :limit.sync="queryParams.pageSize"
				@pagination="getList" />
		</el-row>
		<div slot="footer" class="dialog-footer">
			<el-button type="primary" @click="handleSelectProject" :disabled="single">确 定</el-button>
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
				visible: false,
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
			// 判断行是否可选
			checkSelectable(row) {
				return row.status === '1';
			},
			// 判断行是否被选中
			isSelected(row) {
				return this.$refs.table.selection.some(item => item.id === row.id);
			},
			// 显示弹框
			show(taskId) {
				console.log("fork:" + taskId);
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
				this.projectNames = selection.map(item => item.projectName);
				this.currentSerialNo = selection.map(item => item.serialNo);
				this.currentSerialName = selection.map(item => item.program);
				// 更新选中行的shuntQty状态
				this.prodList.forEach(row => {
					const isSelected = selection.some(item => item.id === row.id);
					if (!isSelected) {
						row.shuntQty = undefined;
					}
				});
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
			/** 搜索按钮操作 */
			handleQuery() {
				this.queryParams.pageNum = 1;
				this.getList();
			},
			/** 重置按钮操作 */
			resetQuery() {
				this.resetForm("queryForm");
				this.handleQuery();
			},
			/** 零件流转卡的分流操作 */
			handleSelectProject() {
				const taskId = this.queryParams.taskId;
				const selectedRows = this.$refs.table.selection;

				// 验证分流数量
				for (const row of selectedRows) {
					if (row.shuntQty === undefined || row.shuntQty === null || row.shuntQty < 0) {
						this.$message.warning('请填写有效的分流数量');
						return;
					}
					if (row.shuntQty > parseInt(row.trialQuantity)) {
						this.$message.warning('分流数量不能大于试制数量');
						return;
					}
				}

				const pm = {
					taskId: taskId,
					listStr: JSON.stringify(selectedRows)
				}

				console.log(pm)
				this.visible = false;
				this.$emit("ok", pm);
			}
		}
	};
</script>