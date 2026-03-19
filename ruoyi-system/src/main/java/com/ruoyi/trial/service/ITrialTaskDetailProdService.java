package com.ruoyi.trial.service;

import java.util.List;
import com.ruoyi.trial.domain.TrialTaskDetailProd;

/**
 * 试制任务程序Service接口
 * 
 * @author ruoyi
 * @date 2023-07-11
 */
public interface ITrialTaskDetailProdService
{
    /**
     * 查询试制任务程序
     * 
     * @param id 试制任务程序主键
     * @return 试制任务程序
     */
    public TrialTaskDetailProd selectTrialTaskDetailProdById(Long id);

    /**
     * 查询试制任务程序列表
     * 
     * @param trialTaskDetailProd 试制任务程序
     * @return 试制任务程序集合
     */
    public List<TrialTaskDetailProd> selectTrialTaskDetailProdList(TrialTaskDetailProd trialTaskDetailProd);

    /**
     * 新增试制任务程序
     * 
     * @param trialTaskDetailProd 试制任务程序
     * @return 结果
     */
    public int insertTrialTaskDetailProd(TrialTaskDetailProd trialTaskDetailProd);

    /**
     * 修改试制任务程序
     * 
     * @param trialTaskDetailProd 试制任务程序
     * @return 结果
     */
    public int updateTrialTaskDetailProd(TrialTaskDetailProd trialTaskDetailProd);


    /**
     * 审核试制任务程序
     *
     * @param trialTaskDetailProd 试制任务程序
     * @return 结果
     */
    public int approveTrialTaskDetailProd(TrialTaskDetailProd trialTaskDetailProd);

    /**
     * 根据ID审核试制任务程序
     *
     * @param id 试制任务程序ID
     * @return 结果
     */
    public int approveTrialTaskDetailProdById(Long id);

    /**
     * 批量删除试制任务程序
     * 
     * @param ids 需要删除的试制任务程序主键集合
     * @return 结果
     */
    public int deleteTrialTaskDetailProdByIds(Long[] ids);

    /**
     * 删除试制任务程序信息
     * 
     * @param id 试制任务程序主键
     * @return 结果
     */
    public int deleteTrialTaskDetailProdById(Long id);
}
