-- =============================================
-- 超期预警菜单权限配置
-- =============================================

-- 查询父菜单ID（试制管理）
SET @parent_id = (SELECT menu_id FROM sys_menu WHERE menu_name = '试制管理' LIMIT 1);

-- 如果找不到试制管理菜单，使用默认值
SET @parent_id = IFNULL(@parent_id, 2000);

-- 插入超期预警菜单
INSERT INTO sys_menu (menu_name, parent_id, order_num, path, component, is_frame, is_cache, menu_type, visible, status, perms, icon, create_by, create_time, remark)
VALUES ('超期预警', @parent_id, 6, 'overdueAlert', 'trial/overdueAlert/index', 1, 0, 'C', '0', '0', 'trial:overdueAlert:list', 'warning', 'admin', sysdate(), '超期预警菜单');

-- 获取刚插入的菜单ID
SET @menu_id = LAST_INSERT_ID();

-- 插入超期预警按钮权限
INSERT INTO sys_menu (menu_name, parent_id, order_num, path, component, is_frame, is_cache, menu_type, visible, status, perms, icon, create_by, create_time, remark) VALUES
('超期预警查询', @menu_id, 1, '#', '', 1, 0, 'F', '0', '0', 'trial:overdueAlert:query', '#', 'admin', sysdate(), ''),
('超期预警导出', @menu_id, 2, '#', '', 1, 0, 'F', '0', '0', 'trial:overdueAlert:export', '#', 'admin', sysdate(), ''),
('超期预警处理', @menu_id, 3, '#', '', 1, 0, 'F', '0', '0', 'trial:overdueAlert:handle', '#', 'admin', sysdate(), ''),
('超期预警忽略', @menu_id, 4, '#', '', 1, 0, 'F', '0', '0', 'trial:overdueAlert:ignore', '#', 'admin', sysdate(), ''),
('超期预警删除', @menu_id, 5, '#', '', 1, 0, 'F', '0', '0', 'trial:overdueAlert:remove', '#', 'admin', sysdate(), ''),
('执行预警检查', @menu_id, 6, '#', '', 1, 0, 'F', '0', '0', 'trial:overdueAlert:execute', '#', 'admin', sysdate(), '');

-- =============================================
-- 定时任务配置
-- =============================================

-- 插入超期预警定时任务（每天凌晨1点执行）
INSERT INTO sys_job (job_name, job_group, invoke_target, cron_expression, misfire_policy, concurrent, status, create_by, create_time, remark)
VALUES ('超期预警检查', 'DEFAULT', 'ryTask.checkOverdueAlert()', '0 0 1 * * ?', '2', '1', '0', 'admin', sysdate(), '每天凌晨1点执行超期预警检查，检查项目和流转卡是否超期');
