<template>
  <div class="app-container">
    <el-card shadow="never">
      <div slot="header" class="clearfix">
        <span>{{ title }}</span>
      </div>

      <el-form ref="form" :model="form" :rules="rules" label-width="100px">
        <el-form-item label="流转程序" prop="program">
          <el-input v-model="form.program" placeholder="请输入流转程序" />
        </el-form-item>
        <el-form-item label="名称" prop="name">
          <el-input v-model="form.name" placeholder="请输入名称" />
        </el-form-item>
        <el-form-item label="图号" prop="figure">
          <el-input v-model="form.figure" placeholder="请输入图号" />
        </el-form-item>
        <el-form-item label="试制数量" prop="trialQuantity">
          <el-input v-model="form.trialQuantity" placeholder="请输入试制数量" />
        </el-form-item>
        <el-form-item label="送检数量" prop="inspectionQuantity">
          <el-input v-model="form.inspectionQuantity" placeholder="请输入送检数量" />
        </el-form-item>
        <el-form-item label="制造区域" prop="manufacturingArea">
          <el-input v-model="form.manufacturingArea" placeholder="请输入制造区域" />
        </el-form-item>
        <el-form-item label="负责人" prop="director">
          <el-input v-model="form.director" placeholder="请输入负责人" />
        </el-form-item>
        <el-form-item label="电话" prop="directorTel">
          <el-input v-model="form.directorTel" placeholder="请输入电话" />
        </el-form-item>
        <el-form-item label="责任ME" prop="meDirector">
          <el-input v-model="form.meDirector" placeholder="请输入责任ME" />
        </el-form-item>
        <el-form-item label="联系电话" prop="meDirectorTel">
          <el-input v-model="form.meDirectorTel" placeholder="请输入联系电话" />
        </el-form-item>
        <el-form-item label="备注" prop="notes">
          <el-input v-model="form.notes" placeholder="请输入备注" />
        </el-form-item>
      </el-form>

      <div style="text-align: right;">
        <el-button type="primary" @click="submitForm">保 存</el-button>
        <el-button @click="close">返 回</el-button>
      </div>
    </el-card>
  </div>
</template>

<script>
import { addProd, getProd, updateProd } from "@/api/trial/prodDetail";

export default {
  name: "AddProdDetail",
  dicts: ["card_status", "sys_normal_disable"],
  data() {
    return {
      loading: false,
      form: {},
      rules: {},
    };
  },
  computed: {
    id() {
      return this.$route.params && this.$route.params.id ? this.$route.params.id : null;
    },
    title() {
      return this.id ? "修改试制任务程序" : "添加试制任务程序";
    },
    taskIdFromQuery() {
      return this.$route.query && this.$route.query.taskId ? this.$route.query.taskId : null;
    },
  },
  created() {
    this.reset();
    if (this.id) {
      this.loading = true;
      getProd(this.id)
        .then((res) => {
          this.form = res.data || {};
        })
        .finally(() => {
          this.loading = false;
        });
    } else if (this.taskIdFromQuery) {
      this.form.taskId = this.taskIdFromQuery;
    }
  },
  methods: {
    reset() {
      this.form = {
        id: null,
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
        createBy: null,
        createTime: null,
        updateBy: null,
        updateTime: null,
        remark: null,
      };
      this.resetForm("form");
    },
    submitForm() {
      this.$refs["form"].validate((valid) => {
        if (!valid) return;
        const req = this.form.id != null ? updateProd(this.form) : addProd(this.form);
        req.then(() => {
          this.$modal.msgSuccess(this.form.id != null ? "修改成功" : "新增成功");
          this.close();
        });
      });
    },
    close() {
      const obj = {
        path: "/trial/prodDetail",
        query: { t: Date.now(), taskId: this.form.taskId || this.taskIdFromQuery || undefined },
      };
      this.$tab.closeOpenPage(obj);
    },
  },
};
</script>

