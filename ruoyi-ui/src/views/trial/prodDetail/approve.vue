<template>
  <div class="app-container">
    <!-- 添加或修改试制任务程序对话框 -->
    <el-dialog :title="title" :visible.sync="open" width="100%" append-to-body>
      <el-form ref="form" :model="form" :rules="rules" label-width="20%">
        <!-- <el-form-item label="任务编号" prop="taskId">
          <el-input v-model="form.taskId" placeholder="请输入任务编号" />
        </el-form-item>
        <el-form-item label="卡片列头编码" prop="columnCode">
          <el-input v-model="form.columnCode" placeholder="请输入卡片列头编码" />
        </el-form-item>
        <el-form-item label="序号" prop="serialNo">
          <el-input v-model="form.serialNo" placeholder="请输入序号" />
        </el-form-item> -->
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
      <div slot="footer" class="dialog-footer">
        <el-button type="primary" @click="submitForm">确 定</el-button>
        <!-- <el-button @click="cancel">取 消</el-button> -->
      </div>
    </el-dialog>
  </div>
</template>

<script>
import { listProd, getProd, delProd, addProd, updateProd,approveProd,applyProd } from "@/api/trial/prodDetail";

export default {
  name: "ProdDetail",
  dicts: ['card_status', 'sys_normal_disable'],
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
      // 表单参数
      form: {},
      // 表单校验
      rules: {
        taskId: [
          { required: true, message: "任务编号不能为空", trigger: "blur" }
        ],
        serialNo: [
          { required: true, message: "序号不能为空", trigger: "blur" }
        ],
      }
    };
  },
  created() {
		const taskId = this.$route.params && this.$route.params.taskId;
		if (taskId) {
		  this.queryParams.taskId = taskId;
			this.queryParams.status = "2";
		  this.getInfo();
			
		}
    
  },
  methods: {
    /** 查询试制任务程序列表 */
    getInfo() {
      this.loading = true;
      listProd(this.queryParams).then(response => {
        this.prodList = response.rows;
        this.total = response.total;
        this.loading = false;
				if (this.prodList.length>0) this.handleUpdate(this.prodList[0]); 
				else {
					this.$modal.msgSuccess("没有需要填报的卡片!");
				}
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
        remark: null
      };
      this.resetForm("form");
    },
    /** 搜索按钮操作 */
    handleQuery() {
      this.queryParams.pageNum = 1;
      
    },
    /** 重置按钮操作 */
    resetQuery() {
      this.resetForm("queryForm");
      this.handleQuery();
    },
    // 多选框选中数据
    handleSelectionChange(selection) {
      this.ids = selection.map(item => item.id)
      this.single = selection.length!==1
      this.multiple = !selection.length
    },
    /** 新增按钮操作 */
    handleAdd() {
      this.reset();
      this.open = true;
      this.title = "添加试制任务程序";
    },
    /** 修改按钮操作 */
    handleUpdate(row) {
      this.reset();
      const id = row.id
      getProd(id).then(response => {
        this.form = response.data;
        this.open = true;
        this.title = "填报试制任务程序内容";
      });
    },
    /** 提交按钮 */
    submitForm() {
      this.$refs["form"].validate(valid => {
        if (valid) {
          if (this.form.id != null) {
            updateProd(this.form).then(response => {
              this.open = false;
              this.applyProdProcess();
            });
          } else {
            addProd(this.form).then(response => {
              this.$modal.msgSuccess("新增成功");
              this.open = false;
              
            });
          }
        }
      });
    },
	/** 申请按钮操作 */
	handleApply(row) {
		const that = this;
		this.$modal.confirm('是否确认申请试制任务程序的填写内容').then(function() {
		   return that.applyProdProcess();
		}).then(() => {
			this.reset();
			
		  //return that.applyProdProcess();
		}).catch((e ) => {
			//this.$modal.msgSuccess("申请失败"+e);
			return;
		});
	},
	applyProdProcess(){
		  applyProd(this.form).then(responseNode => {
		    this.$modal.msgSuccess("填报成功");
		  });
	},
	/** 审核按钮操作 */
	handleAppove(row) {
		const that = this;
		this.$modal.confirm('是否确认审核试制任务程序的填写内容').then(function() {
		   const id =  that.ids;
		   getProd(id).then(response => {
		     that.form = response.data;
		     approveProd(that.form).then(responseNode => {
		       that.$modal.msgSuccess("审核成功");
		       that.reset();
		       
		     });
		   });
		}).then(() => {
		  this.reset();
		  
		}).catch((e ) => {
			//this.$modal.msgSuccess("审核失败"+e);
			return;
		});
		
		
	  
	},
    /** 删除按钮操作 */
    handleDelete(row) {
      const ids = row.id || this.ids;
      this.$modal.confirm('是否确认删除试制任务程序编号为"' + ids + '"的数据项？').then(function() {
        return delProd(ids);
      }).then(() => {
        
        this.$modal.msgSuccess("删除成功");
      }).catch(() => {});
    },
    /** 导出按钮操作 */
    handleExport() {
      this.download('trial/prod/detail/export', {
        ...this.queryParams
      }, `prod_${new Date().getTime()}.xlsx`)
    }
  }
};
</script>
