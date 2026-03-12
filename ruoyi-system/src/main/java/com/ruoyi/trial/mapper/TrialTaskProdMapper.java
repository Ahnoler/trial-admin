package com.ruoyi.trial.mapper;

import java.util.List;
import com.ruoyi.trial.domain.TrialTaskProd;

/**
 * 试制任务信息Mapper接口
 * 
 * @author ruoyi
 * @date 2023-07-11
 */
public interface TrialTaskProdMapper
{
    /**
     * 查询试制任务信息
     * 
     * @param taskId 试制任务信息主键
     * @return 试制任务信息
     */
    public TrialTaskProd selectTrialTaskProdByTaskId(Long taskId);

    /**
     * 查询试制任务信息列表
     * 
     * @param trialTaskProd 试制任务信息
     * @return 试制任务信息集合
     */
    public List<TrialTaskProd> selectTrialTaskProdList(TrialTaskProd trialTaskProd);

    /**
     * 新增试制任务信息
     * 
     * @param trialTaskProd 试制任务信息
     * @return 结果
     */
    public int insertTrialTaskProd(TrialTaskProd trialTaskProd);

    /**
     * 修改试制任务信息
     * 
     * @param trialTaskProd 试制任务信息
     * @return 结果
     */
    public int updateTrialTaskProd(TrialTaskProd trialTaskProd);

    /**
     * 删除试制任务信息
     * 
     * @param taskId 试制任务信息主键
     * @return 结果
     */
    public int deleteTrialTaskProdByTaskId(Long taskId);

    /**
     * 批量删除试制任务信息
     * 
     * @param taskIds 需要删除的数据主键集合
     * @return 结果
     */
    public int deleteTrialTaskProdByTaskIds(Long[] taskIds);
    
    /**
     * 根据关联任务ID查询试制任务信息列表
     * 
     * @param relatedTaskId 关联任务ID
     * @return 试制任务信息集合
     */
    public List<TrialTaskProd> selectTrialTaskProdByRelatedTaskId(Long relatedTaskId);
}
