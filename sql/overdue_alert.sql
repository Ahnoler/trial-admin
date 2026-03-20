-- =============================================
-- 超期预警记录表
-- =============================================

DROP TABLE IF EXISTS `overdue_alert`;
CREATE TABLE `overdue_alert` (
  `alert_id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '预警ID',
  `alert_type` varchar(20) NOT NULL COMMENT '预警类型（project-项目超期，task-流转卡超期）',
  `target_id` bigint(20) NOT NULL COMMENT '关联ID（项目ID或流转卡ID）',
  `target_name` varchar(200) DEFAULT NULL COMMENT '目标名称',
  `project_id` bigint(20) DEFAULT NULL COMMENT '项目ID',
  `project_name` varchar(200) DEFAULT NULL COMMENT '项目名称',
  `alert_level` varchar(20) DEFAULT 'warning' COMMENT '预警级别（warning-警告，danger-严重）',
  `alert_status` varchar(20) DEFAULT 'pending' COMMENT '预警状态（pending-待处理，processed-已处理，ignored-已忽略）',
  `overdue_days` int(11) DEFAULT 0 COMMENT '超期天数',
  `expected_date` datetime DEFAULT NULL COMMENT '预期完成日期',
  `alert_content` varchar(500) DEFAULT NULL COMMENT '预警内容',
  `handler` varchar(64) DEFAULT NULL COMMENT '处理人',
  `handle_time` datetime DEFAULT NULL COMMENT '处理时间',
  `handle_remark` varchar(500) DEFAULT NULL COMMENT '处理备注',
  `create_by` varchar(64) DEFAULT '' COMMENT '创建者',
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_by` varchar(64) DEFAULT '' COMMENT '更新者',
  `update_time` datetime DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `remark` varchar(500) DEFAULT NULL COMMENT '备注',
  PRIMARY KEY (`alert_id`),
  KEY `idx_alert_type` (`alert_type`),
  KEY `idx_target_id` (`target_id`),
  KEY `idx_project_id` (`project_id`),
  KEY `idx_alert_status` (`alert_status`),
  KEY `idx_create_time` (`create_time`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='超期预警记录表';
