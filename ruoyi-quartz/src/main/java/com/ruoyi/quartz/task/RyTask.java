package com.ruoyi.quartz.task;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import com.ruoyi.common.utils.StringUtils;
import com.ruoyi.trial.service.IOverdueAlertService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 定时任务调度测试
 * 
 * @author ruoyi
 */
@Component("ryTask")
public class RyTask
{
    private static final Logger log = LoggerFactory.getLogger(RyTask.class);

    @Autowired
    private IOverdueAlertService overdueAlertService;

    public void ryMultipleParams(String s, Boolean b, Long l, Double d, Integer i)
    {
        System.out.println(StringUtils.format("执行多参方法： 字符串类型{}，布尔类型{}，长整型{}，浮点型{}，整形{}", s, b, l, d, i));
    }

    public void ryParams(String params)
    {
        System.out.println("执行有参方法：" + params);
    }

    public void ryNoParams()
    {
        System.out.println("执行无参方法");
    }

    /**
     * 执行超期预警检查
     * 检查项目和流转卡是否超期，生成预警记录
     */
    public void checkOverdueAlert()
    {
        log.info("开始执行超期预警检查定时任务...");
        try
        {
            int alertCount = overdueAlertService.executeOverdueCheck();
            log.info("超期预警检查完成，新增/更新预警 {} 条", alertCount);
        }
        catch (Exception e)
        {
            log.error("超期预警检查任务执行失败", e);
        }
    }
}
