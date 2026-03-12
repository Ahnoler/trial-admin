<template>
  <!-- 授权项目 -->
  <el-dialog title="选择项目" :visible.sync="visible" width="800px" top="5vh" append-to-body>
    <el-form :model="queryParams" ref="queryForm" size="small" :inline="true">
      <el-form-item label="项目名称" prop="projectName">
      	<el-input v-model="queryParams.projectName" placeholder="请输入项目名称" clearable
      		@keyup.enter.native="handleQuery" />
      </el-form-item>
      <el-form-item label="项目类型" prop="status">
      	<el-select v-model="queryParams.projectType" placeholder="项目状态" clearable style="width: 240px">
      		<el-option v-for="dict in dict.type.project_type" :key="dict.value" :label="dict.label"
      			:value="dict.value" />
      	</el-select>
      </el-form-item>
      <el-form-item label="状态" prop="status">
      	<el-select v-model="queryParams.status" placeholder="项目状态" clearable style="width: 240px">
      		<el-option v-for="dict in dict.type.sys_normal_disable" :key="dict.value" :label="dict.label"
      			:value="dict.value" />
      	</el-select>
      </el-form-item>
      <el-form-item>
      	<el-button type="primary" icon="el-icon-search" size="mini" @click="handleQuery">搜索</el-button>
      	<el-button icon="el-icon-refresh" size="mini" @click="resetQuery">重置</el-button>
      </el-form-item>
      
    </el-form>
    <el-row>
      <el-table @row-click="clickRow" ref="table" :data="projectList" @selection-change="handleSelectionChange" height="260px">
        <el-table-column type="selection" width="55" align="center" />
        <el-table-column label="序号" align="center" prop="id" />
        <el-table-column label="项目名称" align="center" prop="projectName" />
        <el-table-column label="项目类型" align="center" prop="status" width="100">
        	<template slot-scope="scope">
        		<dict-tag :options="dict.type.project_type" :value="scope.row.projectType" />
        	</template>
        </el-table-column>
        <el-table-column label="项目管理员" align="center" prop="pmName" />
        <el-table-column label="项目状态" align="center" key="status">
        	<template slot-scope="scope">
        		<el-switch v-model="scope.row.status" active-value="0" inactive-value="1"
        			@change="handleStatusChange(scope.row)"></el-switch>
        	</template>
        </el-table-column>
        <el-table-column label="备注" align="center" prop="remark" />
      </el-table>
      <pagination
        v-show="total>0"
        :total="total"
        :page.sync="queryParams.pageNum"
        :limit.sync="queryParams.pageSize"
        @pagination="getList"
      />
    </el-row>
    <div slot="footer" class="dialog-footer">
      <el-button type="primary" @click="handleSelectProject">确 定</el-button>
      <el-button @click="visible = false">取 消</el-button>
    </div>
  </el-dialog>
</template>

<script>
import { listProjects} from "@/api/trial/projects";
export default {
  dicts: ['project_type', 'sys_normal_disable'],
  props: {
    // 项目管理员
    pm: {
      type: [Number, String]
    }
  },
  data() {
    return {
      // 遮罩层
      visible: false,
      // 选中数组值
      projectIds: [],
	  projectNames: [],
      // 总条数
      total: 0,
      // 未授权项目数据
      projectList: [],
      // 查询参数
      queryParams: {
        pageNum: 1,
        pageSize: 10,
        roleId: undefined,
        ProjectName: undefined,
        phonenumber: undefined
      },
	  
    };
  },
  methods: {
    // 显示弹框
    show() {
      this.queryParams.projectId = this.pm;
      this.getList();
      this.visible = true;
    },
    clickRow(row) {
      this.$refs.table.toggleRowSelection(row);
    },
    // 多选框选中数据
    handleSelectionChange(selection) {
      this.projectIds = selection.map(item => item.id);
	  this.projectNames = selection.map(item => item.projectName);
    },
    // 查询表数据
    getList() {
      listProjects(this.queryParams).then(res => {
        this.projectList = res.rows;
        this.total = res.total;
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
    /** 选择授权项目操作 */
    handleSelectProject() {
      const roleId = this.queryParams.roleId;
      const projectIds = this.projectIds.join(",");
	  const projectNames = this.projectNames.join(",");
	  
	  const pm = {
		  projectIds:projectIds,
		  projectNames:projectNames
	  }
	  
      if (projectIds == "") {
        this.$modal.msgError("请选择要分配的项目");
        return;
      }
	  
	  this.visible = false;
	  this.$emit("ok",pm);
	  
      // authProjectSelectAll({ roleId: roleId, ProjectIds: ProjectIds }).then(res => {
      //   this.$modal.msgSuccess(res.msg);
      //   if (res.code === 200) {
      //     this.visible = false;
      //     this.$emit("ok");
      //   }
      // });
    }
  }
};
</script>
