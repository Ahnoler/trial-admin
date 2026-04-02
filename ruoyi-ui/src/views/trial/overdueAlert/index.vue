<template>
  <div class="app-container">
    <el-form :model="queryParams" ref="queryForm" size="small" :inline="true" v-show="showSearch" label-width="88px">
      <el-form-item label="预警类型" prop="alertType">
        <el-select v-model="queryParams.alertType" placeholder="请选择预警类型" clearable style="width: 140px">
          <el-option label="项目超期" value="project" />
          <el-option label="流转卡超期" value="task" />
        </el-select>
      </el-form-item>
      <el-form-item label="预警级别" prop="alertLevel">
        <el-select v-model="queryParams.alertLevel" placeholder="请选择预警级别" clearable style="width: 140px">
          <el-option label="警告" value="warning" />
          <el-option label="严重" value="danger" />
        </el-select>
      </el-form-item>
      <el-form-item label="预警状态" prop="alertStatus">
        <el-select v-model="queryParams.alertStatus" placeholder="请选择预警状态" clearable style="width: 140px">
          <el-option label="待处理" value="pending" />
          <el-option label="已处理" value="processed" />
          <el-option label="已忽略" value="ignored" />
        </el-select>
      </el-form-item>
      <el-form-item label="项目名称" prop="projectName">
        <el-input v-model="queryParams.projectName" placeholder="请输入项目名称" clearable
          @keyup.enter.native="handleQuery" />
      </el-form-item>
      <el-form-item>
        <el-button type="primary" icon="el-icon-search" size="mini" @click="handleQuery">搜索</el-button>
        <el-button icon="el-icon-refresh" size="mini" @click="resetQuery">重置</el-button>
      </el-form-item>
    </el-form>

    <el-row :gutter="10" class="mb8">
      <el-col :span="1.5">
        <el-button type="danger" plain icon="el-icon-delete" size="mini" :disabled="multiple" @click="handleDelete"
          v-hasPermi="['trial:overdueAlert:remove']">删除</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button type="warning" plain icon="el-icon-download" size="mini" @click="handleExport"
          v-hasPermi="['trial:overdueAlert:export']">导出</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button type="primary" plain icon="el-icon-refresh" size="mini" @click="handleExecute"
          v-hasPermi="['trial:overdueAlert:execute']">执行检查</el-button>
      </el-col>
      <right-toolbar :showSearch.sync="showSearch" @queryTable="getList"></right-toolbar>
    </el-row>

    <el-table v-loading="loading" :data="alertList" @selection-change="handleSelectionChange">
      <el-table-column type="selection" width="55" align="center" />
      <el-table-column label="预警类型" align="center" prop="alertType" width="100">
        <template slot-scope="scope">
          <el-tag :type="scope.row.alertType === 'project' ? 'primary' : 'success'" size="small">
            {{ scope.row.alertType === 'project' ? '项目超期' : '流转卡超期' }}
          </el-tag>
        </template>
      </el-table-column>
      <el-table-column label="目标名称" align="center" prop="targetName" :show-overflow-tooltip="true" />
      <el-table-column label="项目名称" align="center" prop="projectName" :show-overflow-tooltip="true" />
      <el-table-column label="预警级别" align="center" prop="alertLevel" width="90">
        <template slot-scope="scope">
          <el-tag :type="scope.row.alertLevel === 'danger' ? 'danger' : 'warning'" size="small">
            {{ scope.row.alertLevel === 'danger' ? '严重' : '警告' }}
          </el-tag>
        </template>
      </el-table-column>
      <el-table-column label="预警状态" align="center" prop="alertStatus" width="90">
        <template slot-scope="scope">
          <el-tag :type="getStatusType(scope.row.alertStatus)" size="small">
            {{ getStatusLabel(scope.row.alertStatus) }}
          </el-tag>
        </template>
      </el-table-column>
      <el-table-column label="超期天数" align="center" prop="overdueDays" width="90">
        <template slot-scope="scope">
          <span :class="{ 'text-danger': scope.row.overdueDays >= 14 }">{{ scope.row.overdueDays }} 天</span>
        </template>
      </el-table-column>
      <el-table-column label="预期完成日期" align="center" prop="expectedDate" width="120">
        <template slot-scope="scope">
          <span>{{ parseTime(scope.row.expectedDate, '{y}-{m}-{d}') }}</span>
        </template>
      </el-table-column>
      <el-table-column label="预警内容" align="center" prop="alertContent" :show-overflow-tooltip="true" />
      <el-table-column label="处理人" align="center" prop="handler" width="100" />
      <el-table-column label="处理时间" align="center" prop="handleTime" width="160">
        <template slot-scope="scope">
          <span>{{ parseTime(scope.row.handleTime) }}</span>
        </template>
      </el-table-column>
      <el-table-column label="创建时间" align="center" prop="createTime" width="160">
        <template slot-scope="scope">
          <span>{{ parseTime(scope.row.createTime) }}</span>
        </template>
      </el-table-column>
      <el-table-column label="操作" align="center" class-name="small-padding fixed-width" width="180">
        <template slot-scope="scope">
          <el-button size="mini" type="text" icon="el-icon-view" @click="handleView(scope.row)"
            v-hasPermi="['trial:overdueAlert:query']">详情</el-button>
          <el-button size="mini" type="text" icon="el-icon-check" @click="handleHandleSingle(scope.row)"
            v-if="scope.row.alertStatus === 'pending'" v-hasPermi="['trial:overdueAlert:handle']">处理</el-button>
          <el-button size="mini" type="text" icon="el-icon-close" @click="handleIgnoreSingle(scope.row)"
            v-if="scope.row.alertStatus === 'pending'" v-hasPermi="['trial:overdueAlert:ignore']">忽略</el-button>
        </template>
      </el-table-column>
    </el-table>

    <pagination v-show="total > 0" :total="total" :page.sync="queryParams.pageNum" :limit.sync="queryParams.pageSize"
      @pagination="getList" />

    <el-dialog :title="title" :visible.sync="open" width="600px" append-to-body>
      <el-descriptions :column="2" border>
        <el-descriptions-item label="预警类型">
          <el-tag :type="form.alertType === 'project' ? 'primary' : 'success'" size="small">
            {{ form.alertType === 'project' ? '项目超期' : '流转卡超期' }}
          </el-tag>
        </el-descriptions-item>
        <el-descriptions-item label="预警级别">
          <el-tag :type="form.alertLevel === 'danger' ? 'danger' : 'warning'" size="small">
            {{ form.alertLevel === 'danger' ? '严重' : '警告' }}
          </el-tag>
        </el-descriptions-item>
        <el-descriptions-item label="目标名称" :span="2">{{ form.targetName }}</el-descriptions-item>
        <el-descriptions-item label="项目名称" :span="2">{{ form.projectName }}</el-descriptions-item>
        <el-descriptions-item label="超期天数">{{ form.overdueDays }} 天</el-descriptions-item>
        <el-descriptions-item label="预警状态">
          <el-tag :type="getStatusType(form.alertStatus)" size="small">
            {{ getStatusLabel(form.alertStatus) }}
          </el-tag>
        </el-descriptions-item>
        <el-descriptions-item label="预期完成日期">{{ parseTime(form.expectedDate, '{y}-{m}-{d}') }}</el-descriptions-item>
        <el-descriptions-item label="创建时间">{{ parseTime(form.createTime) }}</el-descriptions-item>
        <el-descriptions-item label="预警内容" :span="2">{{ form.alertContent }}</el-descriptions-item>
        <el-descriptions-item label="处理人" v-if="form.handler">{{ form.handler }}</el-descriptions-item>
        <el-descriptions-item label="处理时间" v-if="form.handleTime">{{ parseTime(form.handleTime) }}</el-descriptions-item>
        <el-descriptions-item label="处理备注" :span="2" v-if="form.handleRemark">{{ form.handleRemark }}</el-descriptions-item>
      </el-descriptions>
      <div slot="footer" class="dialog-footer">
        <el-button @click="cancel">关 闭</el-button>
      </div>
    </el-dialog>

    <el-dialog title="处理预警" :visible.sync="handleOpen" width="500px" append-to-body>
      <el-form ref="handleForm" :model="handleForm" label-width="100px">
        <el-form-item label="处理备注">
          <el-input v-model="handleForm.handleRemark" type="textarea" :rows="3" placeholder="请输入处理备注" />
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button type="primary" @click="submitHandle">确 定</el-button>
        <el-button @click="handleOpen = false">取 消</el-button>
      </div>
    </el-dialog>

    <el-dialog title="忽略预警" :visible.sync="ignoreOpen" width="500px" append-to-body>
      <el-form ref="ignoreForm" :model="ignoreForm" label-width="100px">
        <el-form-item label="忽略原因">
          <el-input v-model="ignoreForm.handleRemark" type="textarea" :rows="3" placeholder="请输入忽略原因（可选）" />
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button type="primary" @click="submitIgnore">确 定</el-button>
        <el-button @click="ignoreOpen = false">取 消</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
import {
  listOverdueAlert,
  getOverdueAlert,
  handleOverdueAlert,
  ignoreOverdueAlert,
  delOverdueAlert,
  executeOverdueCheck,
  exportOverdueAlert
} from "@/api/trial/overdueAlert";

export default {
  name: "OverdueAlert",
  data() {
    return {
      loading: true,
      ids: [],
      single: true,
      multiple: true,
      showSearch: true,
      total: 0,
      alertList: [],
      title: "",
      open: false,
      handleOpen: false,
      ignoreOpen: false,
      queryParams: {
        pageNum: 1,
        pageSize: 10,
        alertType: null,
        alertLevel: null,
        alertStatus: null,
        projectName: null
      },
      form: {},
      handleForm: {
        alertIds: [],
        handleRemark: ""
      },
      ignoreForm: {
        alertIds: [],
        handleRemark: ""
      }
    };
  },
  created() {
    this.getList();
  },
  methods: {
    getList() {
      this.loading = true;
      listOverdueAlert(this.queryParams).then(response => {
        this.alertList = response.rows;
        this.total = response.total;
        this.loading = false;
      });
    },
    cancel() {
      this.open = false;
    },
    resetQuery() {
      this.resetForm("queryForm");
      this.handleQuery();
    },
    handleQuery() {
      this.queryParams.pageNum = 1;
      this.getList();
    },
    handleSelectionChange(selection) {
      this.ids = selection.map(item => item.alertId);
      this.single = selection.length !== 1;
      this.multiple = !selection.length;
    },
    getStatusType(status) {
      const map = {
        'pending': 'warning',
        'processed': 'success',
        'ignored': 'info'
      };
      return map[status] || 'info';
    },
    getStatusLabel(status) {
      const map = {
        'pending': '待处理',
        'processed': '已处理',
        'ignored': '已忽略'
      };
      return map[status] || status;
    },
    handleView(row) {
      this.reset();
      const alertId = row.alertId || this.ids;
      getOverdueAlert(alertId).then(response => {
        this.form = response.data;
        this.open = true;
        this.title = "预警详情";
      });
    },
    reset() {
      this.form = {
        alertId: null,
        alertType: null,
        targetId: null,
        targetName: null,
        projectId: null,
        projectName: null,
        alertLevel: null,
        alertStatus: null,
        overdueDays: null,
        expectedDate: null,
        alertContent: null,
        handler: null,
        handleTime: null,
        handleRemark: null
      };
    },
    handleHandleSingle(row) {
      this.handleForm = {
        alertIds: [row.alertId],
        handleRemark: ""
      };
      this.handleOpen = true;
    },
    submitHandle() {
      handleOverdueAlert(this.handleForm.alertIds, this.handleForm.handleRemark).then(response => {
        this.$modal.msgSuccess("处理成功");
        this.handleOpen = false;
        this.getList();
      });
    },
    handleIgnoreSingle(row) {
      this.ignoreForm = {
        alertIds: [row.alertId],
        handleRemark: ""
      };
      this.ignoreOpen = true;
    },
    submitIgnore() {
      ignoreOverdueAlert(this.ignoreForm.alertIds, this.ignoreForm.handleRemark).then(response => {
        this.$modal.msgSuccess("忽略成功");
        this.ignoreOpen = false;
        this.getList();
      });
    },
    handleDelete(row) {
      const alertIds = row.alertId || this.ids;
      this.$modal.confirm('是否确认删除选中的预警记录？').then(function () {
        return delOverdueAlert(alertIds);
      }).then(() => {
        this.getList();
        this.$modal.msgSuccess("删除成功");
      }).catch(() => { });
    },
    handleExport() {
      this.download('trial/overdueAlert/export', {
        ...this.queryParams
      }, `overdue_alert_${new Date().getTime()}.xlsx`);
    },
    handleExecute() {
      this.$modal.confirm('是否立即执行超期预警检查？').then(() => {
        return executeOverdueCheck();
      }).then(response => {
        this.$modal.msgSuccess(response.msg);
        this.getList();
      }).catch(() => { });
    }
  }
};
</script>

<style scoped>
.text-danger {
  color: #f56c6c;
  font-weight: bold;
}
</style>
