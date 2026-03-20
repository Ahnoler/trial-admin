-- =============================================
-- 乐观锁并发控制 - 数据库迁移脚本
-- 版本: V1.0.0
-- 日期: 2026-03-20
-- 说明: 为试制任务相关表添加version字段实现乐观锁
-- =============================================

-- 1. 为 trial_task_prod 表添加 version 字段
ALTER TABLE `trial_task_prod` 
ADD COLUMN `version` INT(11) NOT NULL DEFAULT 0 COMMENT '乐观锁版本号' AFTER `qr_code_url`;

-- 2. 为 trial_task_detail_prod 表添加 version 字段
ALTER TABLE `trial_task_detail_prod` 
ADD COLUMN `version` INT(11) NOT NULL DEFAULT 0 COMMENT '乐观锁版本号' AFTER `remark`;

-- 3. 初始化现有数据的 version 字段为 0
UPDATE `trial_task_prod` SET `version` = 0 WHERE `version` IS NULL;
UPDATE `trial_task_detail_prod` SET `version` = 0 WHERE `version` IS NULL;

-- 4. 为 version 字段创建索引（可选，用于查询优化）
-- CREATE INDEX `idx_version` ON `trial_task_prod` (`version`);
-- CREATE INDEX `idx_version` ON `trial_task_detail_prod` (`version`);

-- =============================================
-- 回滚脚本（如需回滚执行以下语句）
-- =============================================
-- ALTER TABLE `trial_task_prod` DROP COLUMN `version`;
-- ALTER TABLE `trial_task_detail_prod` DROP COLUMN `version`;
