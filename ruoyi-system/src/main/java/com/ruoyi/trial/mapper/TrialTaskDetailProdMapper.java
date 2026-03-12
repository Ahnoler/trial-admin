package com.ruoyi.trial.mapper;

import java.util.List;

import com.ruoyi.trial.domain.TrialTaskDetailProd;
import com.ruoyi.trial.domain.DTO.TrialTaskDetailProdPdfDTO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

/**
 * 试制任务程序Mapper接口
 * 
 * @author ruoyi
 * @date 2023-07-11
 */
@Mapper
public interface TrialTaskDetailProdMapper
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

    public List<TrialTaskDetailProd> selectTrialTaskDetailProdByTaskId(Long taskId);

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
     * 删除试制任务程序
     * 
     * @param id 试制任务程序主键
     * @return 结果
     */
    public int deleteTrialTaskDetailProdById(Long id);

    /**
     * 批量删除试制任务程序
     * 
     * @param ids 需要删除的数据主键集合
     * @return 结果
     */
    public int deleteTrialTaskDetailProdByIds(Long[] ids);

    public int deleteTrialtaskDetailProdByTaskId(@Param("taskIds") Long [] taskId);

    /**
     * 批量插入试制卡片程序
     *
     * @param trialTaskDetailProdList 需要批量插入数据主键集合
     * @return 结果
     */
    public int batchTrialTaskDetailProd(List<TrialTaskDetailProd> trialTaskDetailProdList);

    public List<TrialTaskDetailProd> selectTrialTaskDetailProdByUser(Long userId);

    /**
     * 根据任务ID查询试制任务产品详情Pdf数据
     * 
     * @param taskId 任务ID
     * @return 试制任务产品详情Pdf数据集合
     */
    public List<TrialTaskDetailProdPdfDTO> selectTrialTaskDetailProdPdfDTOByTaskId(Long taskId);
}
