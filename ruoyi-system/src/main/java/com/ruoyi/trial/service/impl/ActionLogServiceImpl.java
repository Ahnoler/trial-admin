package com.ruoyi.trial.service.impl;

import com.ruoyi.common.entity.ActionLog;
import com.ruoyi.trial.mapper.ActionLogMapper;
import com.ruoyi.trial.service.ActionLogService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * 计划任务操作日志(ActionLog)表服务实现类
 *
 * @author makejava
 * @since 2023-07-26 20:03:38
 */
@Service("actionLogService")
public class ActionLogServiceImpl implements ActionLogService {
    @Resource
    private ActionLogMapper actionLogMapper;

    /**
     * 通过ID查询单条数据
     *
     * @param id 主键
     * @return 实例对象
     */
    @Override
    public ActionLog queryById(Integer id) {
        return this.actionLogMapper.queryById(id);
    }

    /**
     * 查询多条数据
     *
     * @return 对象列表
     */
    @Override
    public List<ActionLog> queryAllByLimit(ActionLog actionLog) {
        return this.actionLogMapper.queryAll(actionLog);
    }

    /**
     * 新增数据
     *
     * @param actionLog 实例对象
     * @return 实例对象
     */
    @Override
    public ActionLog insert(ActionLog actionLog) {
        this.actionLogMapper.insert(actionLog);
        return actionLog;
    }

    /**
     * 修改数据
     *
     * @param actionLog 实例对象
     * @return 实例对象
     */
    @Override
    public ActionLog update(ActionLog actionLog) {
        this.actionLogMapper.update(actionLog);
        return this.queryById(actionLog.getId());
    }

    /**
     * 通过主键删除数据
     *
     * @param id 主键
     * @return 是否成功
     */
    @Override
    public boolean deleteById(Integer id) {
        return this.actionLogMapper.deleteById(id) > 0;
    }
}