<template>
	<div class="app-container">
		<el-card shadow="never">
			<div slot="header" class="clearfix">
				<span>选择需要分流的流转程序</span>
				<div style="float: right;">
					<el-button @click="close">返 回</el-button>
				</div>
			</div>

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
						<el-input-number
							v-model="scope.row.shuntQty"
							:min="0"
							:max="parseInt(scope.row.trialQuantity)"
							size="mini"
							:disabled="!isSelected(scope.row) || scope.row.status !== '1'"
						></el-input-number>
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

			<pagination
				v-show="total > 0"
				:total="total"
				:page.sync="queryParams.pageNum"
				:limit.sync="queryParams.pageSize"
				@pagination="getList"
			/>

			<div style="text-align: right; margin-top: 16px;">
				<el-button type="primary" @click="handleSelectProject" :disabled="single">确 定</el-button>
				<el-button @click="close">取 消</el-button>
			</div>
		</el-card>
	</div>
</template>

<script>
import { listProd } from "@/api/trial/prodDetail";
import { forkProd } from "@/api/trial/prod";

export default {
	dicts: ["card_status", "sys_normal_disable"],
	data() {
		return {
			loading: true,
			ids: [],
			single: true,
			multiple: true,
			total: 0,
			prodList: [],
			currentSerialNo: [],
			currentSerialName: [],
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
	created() {
		const taskId = this.$route.query && this.$route.query.taskId ? Number(this.$route.query.taskId) : null;
		this.queryParams.taskId = taskId;
		this.getList();
	},
	activated() {
		const taskId = this.$route.query && this.$route.query.taskId ? Number(this.$route.query.taskId) : null;
		if (taskId && taskId !== this.queryParams.taskId) {
			this.queryParams.taskId = taskId;
			this.getList();
		}
	},
	methods: {
		// 判断行是否可选
		checkSelectable(row) {
			return row.status === "1";
		},
		// 判断行是否被选中
		isSelected(row) {
			return this.$refs.table.selection.some((item) => item.id === row.id);
		},
		// 多选框选中数据
		handleSelectionChange(selection) {
			this.ids = selection.map((item) => item.taskId);
			this.single = selection.length !== 1;
			this.multiple = !selection.length;

			this.currentSerialNo = selection.map((item) => item.serialNo);
			this.currentSerialName = selection.map((item) => item.program);

			// 未选中的行不保留分流数量，避免下次确认带错数据
			this.prodList.forEach((row) => {
				const isSelected = selection.some((item) => item.id === row.id);
				if (!isSelected) row.shuntQty = undefined;
			});
		},
		// 查询表数据
		getList() {
			if (!this.queryParams.taskId) {
				this.loading = false;
				return;
			}
			this.loading = true;
			listProd(this.queryParams).then((response) => {
				this.prodList = response.rows;
				this.total = response.total;
				this.loading = false;
			});
		},
		/** 零件流转卡的分流操作 */
		handleSelectProject() {
			const taskId = this.queryParams.taskId;
			const selectedRows = this.$refs.table.selection;

			// 验证分流数量
			for (const row of selectedRows) {
				if (row.shuntQty === undefined || row.shuntQty === null || row.shuntQty < 0) {
					this.$message.warning("请填写有效的分流数量");
					return;
				}
				if (row.shuntQty > parseInt(row.trialQuantity)) {
					this.$message.warning("分流数量不能大于试制数量");
					return;
				}
			}

			const pm = {
				taskId: taskId,
				listStr: JSON.stringify(selectedRows),
			};

			forkProd(pm).then(() => {
				this.$modal.msgSuccess("分流成功");
				this.close();
			});
		},
		close() {
			const obj = { path: "/trial/prod", query: { t: Date.now() } };
			this.$tab.closeOpenPage(obj);
		},
	},
};
</script>