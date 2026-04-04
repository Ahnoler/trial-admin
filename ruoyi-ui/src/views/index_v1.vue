<template>
  <div class="trial-home">
    <el-row :gutter="16" class="mb16">
      <el-col :xs="24" :sm="12" :lg="6">
        <div class="stat-card" @click="goTrialProd()">
          <div class="stat-title">试制任务总数</div>
          <div class="stat-value">{{ stats.total }}</div>
          <div class="stat-sub">点击进入任务管理</div>
        </div>
      </el-col>
      <el-col :xs="24" :sm="12" :lg="6">
        <div class="stat-card" @click="goTrialProd({ status: '0' })">
          <div class="stat-title">正常任务</div>
          <div class="stat-value">{{ stats.normal }}</div>
          <div class="stat-sub">点击查看正常任务</div>
        </div>
      </el-col>
      <el-col :xs="24" :sm="12" :lg="6">
        <div class="stat-card" @click="goTrialProd({ status: '1' })">
          <div class="stat-title">停用任务</div>
          <div class="stat-value">{{ stats.stopped }}</div>
          <div class="stat-sub">点击查看停用任务</div>
        </div>
      </el-col>
      <el-col :xs="24" :sm="12" :lg="6">
        <div class="stat-card" @click="goTrialProd({ status: '2' })">
          <div class="stat-title">完成任务</div>
          <div class="stat-value">{{ stats.finished }}</div>
          <div class="stat-sub">点击查看结束任务</div>
        </div>
      </el-col>
    </el-row>

    <el-row :gutter="16" class="mb16">
      <el-col :xs="24" :lg="8">
        <el-card shadow="never">
          <div slot="header" class="card-header">
            <span>快捷入口</span>
          </div>
          <div class="quick-actions">
            <el-button type="primary" plain icon="el-icon-s-operation" @click="go('/trial/prod')">试制任务管理</el-button>
            <el-button type="success" plain icon="el-icon-document" @click="go('/trial/prodDetail')">流转程序管理</el-button>
            <el-button type="warning" plain icon="el-icon-folder" @click="go('/trial/projects')">项目信息管理</el-button>
          </div>
        </el-card>
        <el-card shadow="never">
          <div slot="header" class="card-header">
            <span>我负责的试制任务</span>
            <el-button type="text" @click="goTrialProd({ onlyMine: 'true' })">更多</el-button>
          </div>
          <el-table v-loading="loadingMine" :data="myTasks" size="mini" @row-click="openTask">
            <el-table-column label="任务编号" prop="taskId" width="90" />
            <el-table-column label="卡片名称" prop="title"/>

            <el-table-column label="状态" prop="status" width="90">
              <template slot-scope="scope">
                <dict-tag :options="dict.type.task_status" :value="scope.row.status" />
              </template>
            </el-table-column>
            <el-table-column label="当前工序" prop="currentSerialName"/>
          </el-table>
        </el-card>
      </el-col>
      <el-col :xs="24" :lg="16">
        <el-card shadow="never" class="stack-card">
          <div slot="header" class="card-header">
            <span>最近试制任务</span>
            <el-button type="text" @click="go('/trial/prod')">更多</el-button>
          </div>
          <el-table v-loading="loadingRecent" :data="recentTasks" size="mini" @row-click="openTask">
            <el-table-column label="任务编号" prop="taskId" width="90" />
            <el-table-column label="卡片名称" prop="title"/>
            <el-table-column label="所属项目" prop="projectName"/>
            <el-table-column label="状态" prop="status" width="140">
              <template slot-scope="scope">
                <dict-tag :options="dict.type.task_status" :value="scope.row.status" />
              </template>
            </el-table-column>
            <el-table-column label="当前工序" prop="currentSerialName" width="0" />
          </el-table>
        </el-card>
      </el-col>
    </el-row>
  </div>
</template>

<script>
import { listProd as listTrialProd } from "@/api/trial/prod";

export default {
  name: 'Index',
  dicts: ["task_status"],
  data() {
    return {
      loadingRecent: false,
      loadingMine: false,
      stats: {
        total: 0,
        normal: 0,
        stopped: 0,
        finished: 0,
      },
      recentTasks: [],
      myTasks: [],
    }
  },
  created() {
    this.loadStats();
    this.loadRecent();
    this.loadMyTasks();
  },
  methods: {
    go(path) {
      this.$router.push({ path });
    },
    goTrialProd(extraQuery = {}) {
      this.$router.push({ path: "/trial/prod", query: { ...extraQuery } });
    },
    openTask(row) {
      // 进入任务管理页并高亮/带入筛选
      this.$router.push({ path: "/trial/prod", query: { taskId: row.taskId } });
    },
    async loadStats() {
      // 利用分页接口的 total 字段统计数量（不依赖额外后端接口）
      const base = { pageNum: 1, pageSize: 1 };
      const [all, normal, stopped, finished] = await Promise.all([
        listTrialProd({ ...base }),
        listTrialProd({ ...base, status: "0" }),
        listTrialProd({ ...base, status: "1" }),
        listTrialProd({ ...base, status: "2" }),
      ]);
      this.stats.total = all.total || 0;
      this.stats.normal = normal.total || 0;
      this.stats.stopped = stopped.total || 0;
      this.stats.finished = finished.total || 0;
    },
    loadRecent() {
      this.loadingRecent = true;
      listTrialProd({
        pageNum: 1,
        pageSize: 8,
        orderByColumn: "createTime",
        isAsc: "desc",
      })
        .then((res) => {
          this.recentTasks = res.rows || [];
        })
        .finally(() => {
          this.loadingRecent = false;
        });
    },
    loadMyTasks() {
      this.loadingMine = true;
      listTrialProd({
        pageNum: 1,
        pageSize: 8,
        onlyMine: true,
        orderByColumn: "createTime",
        isAsc: "desc",
      })
        .then((res) => {
          this.myTasks = res.rows || [];
        })
        .finally(() => {
          this.loadingMine = false;
        });
    },
  },
}
</script>

<style lang="scss" scoped>
.trial-home {
  padding: 24px;
  background-color: rgb(240, 242, 245);
  min-height: calc(100vh - 84px);
}

.mb16 {
  margin-bottom: 16px;
}

.stat-card {
  background: #fff;
  border-radius: 6px;
  padding: 16px;
  cursor: pointer;
  box-shadow: 4px 4px 40px rgba(0, 0, 0, 0.05);
  transition: transform 0.15s ease, box-shadow 0.15s ease;
  user-select: none;
}

.stat-card:hover {
  transform: translateY(-2px);
  box-shadow: 6px 6px 50px rgba(0, 0, 0, 0.06);
}

.stat-title {
  font-size: 14px;
  color: rgba(0, 0, 0, 0.45);
  margin-bottom: 10px;
}

.stat-value {
  font-size: 28px;
  font-weight: 700;
  color: #303133;
  line-height: 1.1;
}

.stat-sub {
  margin-top: 10px;
  font-size: 12px;
  color: #909399;
}

.card-header {
  display: flex;
  align-items: center;
  justify-content: space-between;
}

.quick-actions {
  display: flex;
  flex-wrap: wrap;
  gap: 10px;
}

.stack-card {
  margin-top: 16px;
}
</style>
