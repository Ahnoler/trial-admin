-- 更新projects表结构，添加新增字段
ALTER TABLE projects
ADD COLUMN assembly_name VARCHAR(255) COMMENT '总成名称',
ADD COLUMN assembly_drawing_number VARCHAR(255) COMMENT '总成图号',
ADD COLUMN project_description TEXT COMMENT '项目描述',
ADD COLUMN start_date DATETIME COMMENT '项目开始时间',
ADD COLUMN end_date DATETIME COMMENT '项目结束时间',
ADD COLUMN project_status VARCHAR(50) COMMENT '项目状态',
ADD COLUMN budget DOUBLE COMMENT '项目预算',
ADD COLUMN actual_cost DOUBLE COMMENT '项目实际成本',
ADD COLUMN priority VARCHAR(50) COMMENT '项目优先级',
ADD COLUMN project_leader VARCHAR(100) COMMENT '项目负责人',
ADD COLUMN contact_info VARCHAR(255) COMMENT '联系方式';
