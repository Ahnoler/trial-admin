<template>
  <div class="app-container">
    <el-card shadow="never">
      <div slot="header" class="clearfix">
        <span>{{ title }}</span>
      </div>

      <el-form ref="form" :model="form" :rules="rules" label-width="120px">
        <el-form-item label="所属项目" prop="projectId">
          <el-input v-model="form.projectId" v-show="false" placeholder="请输入项目管理编号" />
          <div style="display: flex; align-items: center; gap: 10px;">
            <el-input
              v-model="form.projectName"
              size="mini"
              placeholder="请选择所属项目"
              style="width: 200px;"
              disabled
            />
            <el-button
              type="primary"
              style="white-space: nowrap;"
              plain
              :icon="form.projectName ? 'el-icon-refresh' : 'el-icon-plus'"
              size="mini"
              @click="openSelectProject"
              v-hasPermi="['trial:projects:add']"
            >{{ form.projectName ? '更换项目' : '选择项目' }}</el-button>
          </div>
        </el-form-item>

        <el-form-item label="卡片名称" prop="title">
          <el-input v-model="form.title" placeholder="请输入卡片名称" />
        </el-form-item>

        <el-form-item label="车型" prop="carType">
          <el-select v-model="form.carType" placeholder="请选择车型" style="width: 100%;">
            <el-option
              v-for="dict in dict.type.car_type"
              :key="dict.value"
              :label="dict.label"
              :value="dict.value"
            ></el-option>
          </el-select>
        </el-form-item>

        <el-form-item label="总成名称" prop="assemblyName">
          <el-input v-model="form.assemblyName" placeholder="请输入总成名称" />
        </el-form-item>

        <el-form-item label="总成图号" prop="assemblyFigure">
          <el-input v-model="form.assemblyFigure" placeholder="请输入总成图号" />
        </el-form-item>

        <el-form-item label="试制管理员/电话" prop="pm">
          <el-input v-model="form.pm" placeholder="请输入试制管理员/电话" />
        </el-form-item>

        <el-form-item label="PE姓名/电话" prop="pe">
          <el-input v-model="form.pe" placeholder="请输入PE姓名/电话" />
        </el-form-item>

        <el-form-item label="备注" prop="remark">
          <el-input v-model="form.remark" type="textarea" placeholder="请输入内容" />
        </el-form-item>
      </el-form>

      <div style="text-align: right;">
        <el-button type="primary" @click="submitForm">保 存</el-button>
        <el-button @click="close">返 回</el-button>
      </div>
    </el-card>

    <select-project ref="select" :pm="queryPm" @ok="handleQueryProject" />
  </div>
</template>

<script>
import { addProd, getProd, updateProd } from "@/api/trial/prod";
import selectProject from "./selectProject";

export default {
  name: "TrialProdEditPage",
  dicts: ["car_type", "sys_normal_disable", "task_status"],
  components: { selectProject },
  data() {
    return {
      loading: false,
      queryPm: null,
      form: {},
      rules: {
        projectId: [{ required: true, message: "项目编号不能为空", trigger: "blur" }],
      },
    };
  },
  computed: {
    taskId() {
      return this.$route.params && this.$route.params.taskId ? this.$route.params.taskId : null;
    },
    title() {
      return this.taskId ? "修改试制任务信息" : "添加试制任务信息";
    },
  },
  created() {
    this.reset();
    if (this.taskId) {
      this.loading = true;
      getProd(this.taskId)
        .then((res) => {
          this.form = res.data || {};
        })
        .finally(() => {
          this.loading = false;
        });
    }
  },
  methods: {
    openSelectProject() {
      this.$refs.select.show();
    },
    handleQueryProject(payload) {
      this.form.projectId = payload.projectIds;
      this.form.projectName = payload.projectNames;
    },
    reset() {
      this.form = {
        taskId: null,
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
        createBy: null,
        createTime: null,
        updateBy: null,
        updateTime: null,
        remark: null,
        relatedTaskId: null,
      };
      this.resetForm("form");
    },
    submitForm() {
      this.$refs["form"].validate((valid) => {
        if (!valid) return;
        const req = this.form.taskId != null ? updateProd(this.form) : addProd(this.form);
        req.then(() => {
          this.$modal.msgSuccess(this.form.taskId != null ? "修改成功" : "新增成功");
          this.close(true);
        });
      });
    },
    close() {
      const obj = { path: "/trial/prod", query: { t: Date.now() } };
      this.$tab.closeOpenPage(obj);
    },
  },
};
</script>

