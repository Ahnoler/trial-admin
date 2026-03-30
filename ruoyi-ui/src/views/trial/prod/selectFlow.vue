<template>
	<div class="app-container">
		<el-card shadow="never">
			<div slot="header" class="clearfix">
				<span>选择需要变更到的环节</span>
				<div style="float: right;">
					<el-button @click="close">返 回</el-button>
				</div>
			</div>

			<el-table ref="table" v-loading="loading" :data="prodList" @selection-change="handleSelectionChange">
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

			<pagination
				v-show="total>0"
				:total="total"
				:page.sync="queryParams.pageNum"
				:limit.sync="queryParams.pageSize"
				@pagination="getList"
			/>

			<div style="text-align: right; margin-top: 16px;">
				<el-button type="primary" @click="handleSelectFlow" :disabled="single">确 定</el-button>
				<el-button @click="close">取 消</el-button>
			</div>
		</el-card>
	</div>
</template>

<script>
import { listProd } from "@/api/trial/prodDetail";
import { flowProd } from "@/api/trial/prod";

export default {
	dicts: ["card_status", "sys_normal_disable"],
	data() {
		return {
			// 遮罩层
			loading: true,
			// 选中数组
			ids: [],
			// 是否恰好选中一条
			single: true,
			multiple: true,
			// 总条数
			total: 0,
			// 表格数据
			prodList: [],
			// 当前选择的字段
			currentSerialNo: [],
			currentSerialName: [],
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
		// 多选框选中数据
		handleSelectionChange(selection) {
			this.ids = selection.map((item) => item.taskId);
			this.single = selection.length !== 1;
			this.multiple = !selection.length;
			this.currentSerialNo = selection.map((item) => item.serialNo);
			this.currentSerialName = selection.map((item) => item.program);
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
		/** 变更操作 */
		handleSelectFlow() {
			const taskId = this.queryParams.taskId;
			const currentSerialNo = this.currentSerialNo.join(",");
			const currentSerialName = this.currentSerialName.join(",");

			const pm = {
				taskId: taskId,
				currentSerialNo: currentSerialNo,
				currentSerialName: currentSerialName,
			};

			flowProd(pm).then(() => {
				this.$modal.msgSuccess("变更成功");
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