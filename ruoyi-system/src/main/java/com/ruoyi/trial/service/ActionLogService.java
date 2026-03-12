package com.ruoyi.trial.service;

import com.ruoyi.common.entity.ActionLog;
import java.util.List;

/**
 * 计划任务操作日志(ActionLog)表服务接口
 *
 * @author makejava
 * @since 2023-07-26 20:03:38
 */
public interface ActionLogService {

    /**
     * 通过ID查询单条数据
     *
     * @param id 主键
     * @return 实例对象
     */
    ActionLog queryById(Integer id);

    /**
     * 查询多条数据
     *
     * @return 对象列表
     */
    List<ActionLog> queryAllByLimit(ActionLog actionLog);

    /**
     * 新增数据
     *
     * @param actionLog 实例对象
     * @return 实例对象
     */
    ActionLog insert(ActionLog actionLog);

    /**
     * 修改数据
     *
     * @param actionLog 实例对象
     * @return 实例对象
     */
    ActionLog update(ActionLog actionLog);

    /**
     * 通过主键删除数据
     *
     * @param id 主键
     * @return 是否成功
     */
    boolean deleteById(Integer id);

}