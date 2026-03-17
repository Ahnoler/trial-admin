-- =============================================
-- 为流转卡添加二维码功能
-- 执行时间：2026-03-17
-- =============================================

-- 1. 添加二维码标识字段
ALTER TABLE trial_task_prod ADD COLUMN qr_code VARCHAR(100) COMMENT '二维码标识';

-- 2. 添加二维码图片URL字段
ALTER TABLE trial_task_prod ADD COLUMN qr_code_url VARCHAR(500) COMMENT '二维码图片URL';

-- 3. 为二维码标识字段添加索引（提高查询效率）
CREATE INDEX idx_qr_code ON trial_task_prod(qr_code);

-- 4. 为已有数据生成二维码标识（可选，如果需要为现有数据生成二维码）
-- UPDATE trial_task_prod 
-- SET qr_code = CONCAT('TASK_', task_id, '_', DATE_FORMAT(NOW(), '%Y%m%d%H%i%s'))
-- WHERE qr_code IS NULL;

-- =============================================
-- 说明：
-- 1. qr_code: 存储二维码的唯一标识，格式为 TASK_时间戳_UUID
-- 2. qr_code_url: 存储二维码图片的Base64编码或URL
-- 3. 索引可以提高扫码查询的效率
-- =============================================
