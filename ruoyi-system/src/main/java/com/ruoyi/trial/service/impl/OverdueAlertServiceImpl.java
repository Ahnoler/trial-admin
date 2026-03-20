package com.ruoyi.trial.service.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import com.ruoyi.common.utils.DateUtils;
import com.ruoyi.trial.domain.OverdueAlert;
import com.ruoyi.trial.domain.Projects;
import com.ruoyi.trial.domain.TrialTaskProd;
import com.ruoyi.trial.mapper.OverdueAlertMapper;
import com.ruoyi.trial.mapper.ProjectsMapper;
import com.ruoyi.trial.mapper.TrialTaskProdMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.ruoyi.trial.service.IOverdueAlertService;
import org.springframework.transaction.annotation.Transactional;

@Service
public class OverdueAlertServiceImpl implements IOverdueAlertService
{
    private static final Logger log = LoggerFactory.getLogger(OverdueAlertServiceImpl.class);

    private static final String ALERT_TYPE_PROJECT = "project";
    private static final String ALERT_TYPE_TASK = "task";
    private static final String ALERT_STATUS_PENDING = "pending";
    private static final String ALERT_STATUS_PROCESSED = "processed";
    private static final String ALERT_STATUS_IGNORED = "ignored";
    private static final String ALERT_LEVEL_WARNING = "warning";
    private static final String ALERT_LEVEL_DANGER = "danger";

    private static final int WARNING_DAYS = 7;
    private static final int DANGER_DAYS = 14;

    @Autowired
    private OverdueAlertMapper overdueAlertMapper;

    @Autowired
    private ProjectsMapper projectsMapper;

    @Autowired
    private TrialTaskProdMapper trialTaskProdMapper;

    @Override
    public OverdueAlert selectOverdueAlertById(Long alertId)
    {
        return overdueAlertMapper.selectOverdueAlertById(alertId);
    }

    @Override
    public List<OverdueAlert> selectOverdueAlertList(OverdueAlert overdueAlert)
    {
        return overdueAlertMapper.selectOverdueAlertList(overdueAlert);
    }

    @Override
    public int insertOverdueAlert(OverdueAlert overdueAlert)
    {
        overdueAlert.setCreateTime(DateUtils.getNowDate());
        return overdueAlertMapper.insertOverdueAlert(overdueAlert);
    }

    @Override
    public int updateOverdueAlert(OverdueAlert overdueAlert)
    {
        overdueAlert.setUpdateTime(DateUtils.getNowDate());
        return overdueAlertMapper.updateOverdueAlert(overdueAlert);
    }

    @Override
    public int deleteOverdueAlertById(Long alertId)
    {
        return overdueAlertMapper.deleteOverdueAlertById(alertId);
    }

    @Override
    public int deleteOverdueAlertByIds(Long[] alertIds)
    {
        return overdueAlertMapper.deleteOverdueAlertByIds(alertIds);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int handleAlerts(Long[] alertIds, String handler, String handleRemark)
    {
        List<Long> alertIdList = new ArrayList<>();
        for (Long alertId : alertIds)
        {
            alertIdList.add(alertId);
        }
        return overdueAlertMapper.updateAlertStatusBatch(alertIdList, ALERT_STATUS_PROCESSED, handler, handleRemark);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int ignoreAlerts(Long[] alertIds, String handler, String handleRemark)
    {
        List<Long> alertIdList = new ArrayList<>();
        for (Long alertId : alertIds)
        {
            alertIdList.add(alertId);
        }
        return overdueAlertMapper.updateAlertStatusBatch(alertIdList, ALERT_STATUS_IGNORED, handler, handleRemark);
    }

    @Override
    public int countPendingAlerts()
    {
        return overdueAlertMapper.countPendingAlerts();
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int executeOverdueCheck()
    {
        log.info("开始执行超期预警检查任务...");
        int alertCount = 0;

        List<Projects> overdueProjects = projectsMapper.selectOverdueProjects();
        log.info("发现 {} 个超期项目", overdueProjects.size());

        for (Projects project : overdueProjects)
        {
            if (createProjectAlert(project))
            {
                alertCount++;
            }
        }

        List<TrialTaskProd> overdueTasks = trialTaskProdMapper.selectOverdueTrialTaskProd();
        log.info("发现 {} 个超期流转卡", overdueTasks.size());

        for (TrialTaskProd task : overdueTasks)
        {
            if (createTaskAlert(task))
            {
                alertCount++;
            }
        }

        log.info("超期预警检查完成，新增预警 {} 条", alertCount);
        return alertCount;
    }

    private boolean createProjectAlert(Projects project)
    {
        OverdueAlert existingAlert = overdueAlertMapper.selectExistingAlert(
            ALERT_TYPE_PROJECT, project.getId(), ALERT_STATUS_PENDING);
        
        if (existingAlert != null)
        {
            int overdueDays = calculateOverdueDays(project.getEndDate());
            existingAlert.setOverdueDays(overdueDays);
            existingAlert.setAlertLevel(determineAlertLevel(overdueDays));
            existingAlert.setAlertContent(buildProjectAlertContent(project, overdueDays));
            existingAlert.setUpdateTime(new Date());
            overdueAlertMapper.updateOverdueAlert(existingAlert);
            return false;
        }

        int overdueDays = calculateOverdueDays(project.getEndDate());
        OverdueAlert alert = new OverdueAlert();
        alert.setAlertType(ALERT_TYPE_PROJECT);
        alert.setTargetId(project.getId());
        alert.setTargetName(project.getProjectName());
        alert.setProjectId(project.getId());
        alert.setProjectName(project.getProjectName());
        alert.setAlertLevel(determineAlertLevel(overdueDays));
        alert.setAlertStatus(ALERT_STATUS_PENDING);
        alert.setOverdueDays(overdueDays);
        alert.setExpectedDate(project.getEndDate());
        alert.setAlertContent(buildProjectAlertContent(project, overdueDays));
        alert.setCreateBy("system");
        alert.setCreateTime(new Date());
        
        return overdueAlertMapper.insertOverdueAlert(alert) > 0;
    }

    private boolean createTaskAlert(TrialTaskProd task)
    {
        OverdueAlert existingAlert = overdueAlertMapper.selectExistingAlert(
            ALERT_TYPE_TASK, task.getTaskId(), ALERT_STATUS_PENDING);
        
        Projects project = projectsMapper.selectProjectsById(task.getProjectId());
        String projectName = project != null ? project.getProjectName() : "";
        Date expectedDate = project != null ? project.getEndDate() : task.getCreateTime();

        int overdueDays = calculateOverdueDays(expectedDate);

        if (existingAlert != null)
        {
            existingAlert.setOverdueDays(overdueDays);
            existingAlert.setAlertLevel(determineAlertLevel(overdueDays));
            existingAlert.setAlertContent(buildTaskAlertContent(task, overdueDays));
            existingAlert.setUpdateTime(new Date());
            overdueAlertMapper.updateOverdueAlert(existingAlert);
            return false;
        }

        OverdueAlert alert = new OverdueAlert();
        alert.setAlertType(ALERT_TYPE_TASK);
        alert.setTargetId(task.getTaskId());
        alert.setTargetName(task.getTitle());
        alert.setProjectId(task.getProjectId());
        alert.setProjectName(projectName);
        alert.setAlertLevel(determineAlertLevel(overdueDays));
        alert.setAlertStatus(ALERT_STATUS_PENDING);
        alert.setOverdueDays(overdueDays);
        alert.setExpectedDate(expectedDate);
        alert.setAlertContent(buildTaskAlertContent(task, overdueDays));
        alert.setCreateBy("system");
        alert.setCreateTime(new Date());
        
        return overdueAlertMapper.insertOverdueAlert(alert) > 0;
    }

    private int calculateOverdueDays(Date expectedDate)
    {
        if (expectedDate == null)
        {
            return 0;
        }
        long diff = new Date().getTime() - expectedDate.getTime();
        return (int) (diff / (1000 * 60 * 60 * 24));
    }

    private String determineAlertLevel(int overdueDays)
    {
        if (overdueDays >= DANGER_DAYS)
        {
            return ALERT_LEVEL_DANGER;
        }
        return ALERT_LEVEL_WARNING;
    }

    private String buildProjectAlertContent(Projects project, int overdueDays)
    {
        StringBuilder content = new StringBuilder();
        content.append("项目【").append(project.getProjectName()).append("】");
        content.append("已超期 ").append(overdueDays).append(" 天，");
        content.append("计划完成日期：").append(DateUtils.parseDateToStr("yyyy-MM-dd", project.getEndDate()));
        if (project.getPmName() != null)
        {
            content.append("，项目负责人：").append(project.getPmName());
        }
        return content.toString();
    }

    private String buildTaskAlertContent(TrialTaskProd task, int overdueDays)
    {
        StringBuilder content = new StringBuilder();
        content.append("流转卡【").append(task.getTitle()).append("】");
        content.append("已超期 ").append(overdueDays).append(" 天，");
        content.append("当前程序：").append(task.getCurrentSerialName() != null ? task.getCurrentSerialName() : "未知");
        if (task.getPm() != null)
        {
            content.append("，试制管理员：").append(task.getPm());
        }
        return content.toString();
    }
}
