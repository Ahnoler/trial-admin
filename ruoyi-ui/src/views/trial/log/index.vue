<template>
  <div class="app-container">
    <el-form :model="queryParams" ref="queryForm" size="small" :inline="true" v-show="showSearch" label-width="68px">
      <el-form-item label=" 打印单据编号" prop="taskId">
        <el-input
          v-model="queryParams.taskId"
          placeholder="请输入 打印单据编号"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="机构编号" prop="orgnno">
        <el-input
          v-model="queryParams.orgnno"
          placeholder="请输入机构编号"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item>
        <el-button type="primary" icon="el-icon-search" size="mini" @click="handleQuery">搜索</el-button>
        <el-button icon="el-icon-refresh" size="mini" @click="resetQuery">重置</el-button>
      </el-form-item>
    </el-form>

    <el-row :gutter="10" class="mb8">
      <el-col :span="1.5">
        <el-button
          type="primary"
          plain
          icon="el-icon-plus"
          size="mini"
          @click="handleAdd"
          v-hasPermi="['trial:log:add']"
        >新增</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button
          type="success"
          plain
          icon="el-icon-edit"
          size="mini"
          :disabled="single"
          @click="handleUpdate"
          v-hasPermi="['trial:log:edit']"
        >修改</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button
          type="danger"
          plain
          icon="el-icon-delete"
          size="mini"
          :disabled="multiple"
          @click="handleDelete"
          v-hasPermi="['trial:log:remove']"
        >删除</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button
          type="warning"
          plain
          icon="el-icon-download"
          size="mini"
          @click="handleExport"
          v-hasPermi="['trial:log:export']"
        >导出</el-button>
      </el-col>
      <right-toolbar :showSearch.sync="showSearch" @queryTable="getList"></right-toolbar>
    </el-row>

    <el-table v-loading="loading" :data="logList" @selection-change="handleSelectionChange">
      <el-table-column type="selection" width="55" align="center" />
      <el-table-column label="打印记录" align="center" prop="id">
        <template slot-scope="scope">
          <el-button type="text" @click="handleViewDetail(scope.row)">查看详情</el-button>
        </template>
      </el-table-column>
      <el-table-column label="打印单据编号" align="center" prop="taskId" />
      <el-table-column label="打印卡片类型" align="center" prop="cardtype" />
      <el-table-column label="机构编号" align="center" prop="orgnno" />
      <el-table-column label="打印类型" align="center" prop="printtype">
        <template slot-scope="scope">
          <dict-tag :options="dict.type.print_type" :value="scope.row.printtype" />
        </template>
      </el-table-column>
      <el-table-column label="状态" align="center" prop="status" />
      <el-table-column label="备注" align="center" prop="remark" />
      <el-table-column label="操作" align="center" class-name="small-padding fixed-width">
        <template slot-scope="scope">
          <el-button
            size="mini"
            type="text"
            icon="el-icon-edit"
            @click="handleUpdate(scope.row)"
            v-hasPermi="['trial:log:edit']"
          >修改</el-button>
          <el-button
            size="mini"
            type="text"
            icon="el-icon-delete"
            @click="handleDelete(scope.row)"
            v-hasPermi="['trial:log:remove']"
          >删除</el-button>
        </template>
      </el-table-column>
    </el-table>
    
    <pagination
      v-show="total>0"
      :total="total"
      :page.sync="queryParams.pageNum"
      :limit.sync="queryParams.pageSize"
      @pagination="getList"
    />

    <!-- 添加或修改 打印记录对话框 -->
    <el-dialog :title="title" :visible.sync="open" width="500px" append-to-body>
      <el-form ref="form" :model="form" :rules="rules" label-width="80px">
        <el-form-item label=" 打印单据编号" prop="taskId">
          <el-input v-model="form.taskId" placeholder="请输入 打印单据编号" />
        </el-form-item>
        <el-form-item label=" 打印内容">
          <editor v-model="form.content" :min-height="192"/>
        </el-form-item>
        <el-form-item label="机构编号" prop="orgnno">
          <el-input v-model="form.orgnno" placeholder="请输入机构编号" />
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

    <!-- 查看详情弹窗 -->
    <el-dialog title="打印内容详情" :visible.sync="detailOpen" width="90%" append-to-body>
      <!-- 基本信息 -->
      <div class="info-section" v-if="detailMap">
        <div class="info-row">
          <div class="info-item">
            <span class="info-label">车型：</span>
            <span class="info-value">{{ detailMap.carType }}</span>
          </div>
          <div class="info-item">
            <span class="info-label">PE</span>
            <span class="info-value">{{ detailMap.PE }}</span>
          </div>
          <div class="info-item">
            <span class="info-label">试制管理员</span>
            <span class="info-value">{{ detailMap.trial_production }}</span>
          </div>
          <div class="info-item">
            <span class="info-label">总成名称</span>
            <span class="info-value">{{ detailMap.assembly_name }}</span>
          </div>
          <div class="info-item">
            <span class="info-label">总成图号</span>
            <span class="info-value">{{ detailMap.assembly_drawing_number }}</span>
          </div>
        </div>
      </div>
      <!-- 流转程序详情 -->
      <div v-for="(group, groupIndex) in detailData" :key="groupIndex" class="detail-group">
        <h4 class="group-title">环节 {{ group.left_index }}</h4>
        <el-table :data="group.listInfo" border size="small" style="margin-bottom: 20px;">
          <el-table-column label="序号" align="center" prop="index" width="60" />
          <el-table-column label="流转程序" align="center" prop="procedure" />
          <el-table-column label="名称" align="center" prop="name" />
          <el-table-column label="图号" align="center" prop="drawing_no" />
          <el-table-column label="试制数量" align="center" prop="trial_num" />
          <el-table-column label="送检数量" align="center" prop="inspection_num" />
          <el-table-column label="制造区域" align="center" prop="area" />
          <el-table-column label="负责人" align="center" prop="head" />
          <el-table-column label="负责人电话" align="center" prop="phone" />
          <el-table-column label="责任ME" align="center" prop="me_name" />
          <el-table-column label="ME电话" align="center" prop="telephone" />
        </el-table>
      </div>
      <div v-if="detailData.length === 0" style="text-align: center; color: #909399; padding: 20px;">
        暂无打印内容
      </div>
      <div slot="footer" class="dialog-footer">
        <el-button @click="detailOpen = false">关 闭</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
import { listLog, getLog, delLog, addLog, updateLog } from "@/api/trial/log";

export default {
  name: "Log",
  dicts: ['print_type'],
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
      //  打印记录表格数据
      logList: [],
      // 弹出层标题
      title: "",
      // 是否显示弹出层
      open: false,
      // 详情弹窗
      detailOpen: false,
      // 详情数据
      detailData: [],
      // 详情Map信息
      detailMap: null,
      // 查询参数
      queryParams: {
        pageNum: 1,
        pageSize: 10,
        taskId: null,
        content: null,
        cardtype: null,
        orgnno: null,
        printtype: null,
        status: null,
      },
      // 表单参数
      form: {},
      // 表单校验
      rules: {
      }
    };
  },
  created() {
    this.getList();
  },
  methods: {
    /** 查询 打印记录列表 */
    getList() {
      this.loading = true;
      listLog(this.queryParams).then(response => {
        this.logList = response.rows;
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
        content: null,
        cardtype: null,
        orgnno: null,
        printtype: null,
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
      this.queryParams.pageNum = 1;
      this.getList();
    },
    /** 重置按钮操作 */
    resetQuery() {
      this.resetForm("queryForm");
      this.handleQuery();
    },
    // 多选框选中数据
    handleSelectionChange(selection) {
      this.ids = selection.map(item => item.id);
      this.single = selection.length !== 1;
      this.multiple = !selection.length;
    },
    /** 新增按钮操作 */
    handleAdd() {
      this.reset();
      this.open = true;
      this.title = "添加 打印记录";
    },
    /** 修改按钮操作 */
    handleUpdate(row) {
      this.reset();
      const id = row.id || this.ids[0];
      getLog(id).then(response => {
        this.form = response.data;
        this.open = true;
        this.title = "修改 打印记录";
      });
    },
    /** 提交按钮 */
    submitForm() {
      this.$refs["form"].validate(valid => {
        if (valid) {
          if (this.form.id != null) {
            updateLog(this.form).then(response => {
              this.$modal.msgSuccess("修改成功");
              this.open = false;
              this.getList();
            });
          } else {
            addLog(this.form).then(response => {
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
      this.$modal.confirm('是否确认删除打印记录编号为"' + ids + '"的数据项？').then(function() {
        return delLog(ids);
      }).then(() => {
        this.getList();
        this.$modal.msgSuccess("删除成功");
      }).catch(() => {});
    },
    /** 导出按钮操作 */
    handleExport() {
      this.download('trial/log/export', {
        ...this.queryParams
      }, `log_${new Date().getTime()}.xlsx`)
    },
    /** 查看详情 */
    handleViewDetail(row) {
      this.detailData = [];
      this.detailMap = null;
      if (row.content) {
        try {
          const parsed = JSON.parse(row.content);
          if (parsed.list) {
            this.detailData = parsed.list;
          }
          if (parsed.map) {
            this.detailMap = parsed.map;
          }
        } catch (e) {
          this.$modal.msgError("打印内容格式错误，无法解析");
        }
      }
      this.detailOpen = true;
    }
  }
};
</script>

<style scoped>
.detail-group {
  margin-bottom: 10px;
}
.group-title {
  margin: 10px 0;
  padding: 8px 12px;
  background-color: #f5f7fa;
  border-left: 3px solid #409EFF;
  font-size: 14px;
  color: #303133;
}
.info-section {
  background-color: #f5f7fa;
  padding: 15px;
  margin-bottom: 20px;
  border-radius: 4px;
}
.info-row {
  display: flex;
  flex-wrap: wrap;
  gap: 20px;
}
.info-item {
  flex: 1;
  min-width: 200px;
}
.info-label {
  color: #909399;
  font-size: 13px;
  margin-right: 8px;
}
.info-value {
  color: #303133;
  font-size: 14px;
}
</style>
