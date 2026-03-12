package com.ruoyi.trial.mapper;

import com.ruoyi.common.entity.ActionLog;
import org.apache.ibatis.annotations.Param;
import java.util.List;

/**
 * 计划任务操作日志(ActionLog)表数据库访问层
 *
 * @author makejava
 * @since 2023-07-26 20:03:37
 */
public interface ActionLogMapper {

    /**
     * 通过ID查询单条数据
     *
     * @param id 主键
     * @return 实例对象
     */
    ActionLog queryById(Integer id);

    /**
     * 查询指定行数据
     *
     * @param offset 查询起始位置
     * @param limit 查询条数
     * @return 对象列表
     */
    List<ActionLog> queryAllByLimit(@Param("offset") int offset, @Param("limit") int limit);


    /**
     * 通过实体作为筛选条件查询
     *
     * @param actionLog 实例对象
     * @return 对象列表
     */
    List<ActionLog> queryAll(ActionLog actionLog);

    /**
     * 新增数据
     *
     * @param actionLog 实例对象
     * @return 影响行数
     */
    int insert(ActionLog actionLog);

    /**
     * 修改数据
     *
     * @param actionLog 实例对象
     * @return 影响行数
     */
    int update(ActionLog actionLog);

    /**
     * 通过主键删除数据
     *
     * @param id 主键
     * @return 影响行数
     */
    int deleteById(Integer id);

}