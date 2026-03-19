package com.ruoyi.trial.service.impl;

import java.util.List;
import com.ruoyi.common.utils.DateUtils;
import com.ruoyi.trial.domain.TrialTaskProd;
import com.ruoyi.trial.mapper.CardColumnHeaderMapper;
import com.ruoyi.trial.mapper.TrialTaskProdMapper;
import com.ruoyi.trial.service.ICardColumnHeaderService;
import com.ruoyi.trial.service.ITrialTaskProdService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.ruoyi.trial.mapper.TrialTaskDetailProdMapper;
import com.ruoyi.trial.domain.TrialTaskDetailProd;
import com.ruoyi.trial.service.ITrialTaskDetailProdService;

/**
 * 试制任务程序Service业务层处理
 * 
 * @author ruoyi
 * @date 2023-07-11
 */
@Service
public class TrialTaskDetailProdServiceImpl implements ITrialTaskDetailProdService
{
    @Autowired
    private TrialTaskDetailProdMapper trialTaskDetailProdMapper;

    @Autowired
    private TrialTaskProdMapper trialTaskProdMapper;

    @Autowired
    private CardColumnHeaderMapper cardColumnHeaderMapper;

    /**
     * 查询试制任务程序
     * 
     * @param id 试制任务程序主键
     * @return 试制任务程序
     */
    @Override
    public TrialTaskDetailProd selectTrialTaskDetailProdById(Long id)
    {
        return trialTaskDetailProdMapper.selectTrialTaskDetailProdById(id);
    }

    /**
     * 查询试制任务程序列表
     * 
     * @param trialTaskDetailProd 试制任务程序
     * @return 试制任务程序
     */
    @Override
    public List<TrialTaskDetailProd> selectTrialTaskDetailProdList(TrialTaskDetailProd trialTaskDetailProd)
    {
        return trialTaskDetailProdMapper.selectTrialTaskDetailProdList(trialTaskDetailProd);
    }

    /**
     * 新增试制任务程序
     * 
     * @param trialTaskDetailProd 试制任务程序
     * @return 结果
     */
    @Override
    public int insertTrialTaskDetailProd(TrialTaskDetailProd trialTaskDetailProd)
    {
        trialTaskDetailProd.setCreateTime(DateUtils.getNowDate());
        return trialTaskDetailProdMapper.insertTrialTaskDetailProd(trialTaskDetailProd);
    }

    /**
     * 修改试制任务程序
     * 
     * @param trialTaskDetailProd 试制任务程序
     * @return 结果
     */
    @Override
    public int updateTrialTaskDetailProd(TrialTaskDetailProd trialTaskDetailProd)
    {
        trialTaskDetailProd.setUpdateTime(DateUtils.getNowDate());
        return trialTaskDetailProdMapper.updateTrialTaskDetailProd(trialTaskDetailProd);
    }

    /**
     * 审核试制任务程序
     *
     * @param trialTaskDetailProd 试制任务程序
     * @return 结果
     */
    @Override
    public int approveTrialTaskDetailProd(TrialTaskDetailProd trialTaskDetailProd){
        return approveTrialTaskDetailProdById(trialTaskDetailProd.getId());
    }

    /**
     * 根据ID审核试制任务程序
     *
     * @param id 试制任务程序ID
     * @return 结果
     */
    @Override
    public int approveTrialTaskDetailProdById(Long id){
        TrialTaskDetailProd currentProd = trialTaskDetailProdMapper.selectTrialTaskDetailProdById(id);
        if (currentProd == null) {
            return 0;
        }
        if (!"2".equals(currentProd.getStatus())) {
            return 0;
        }
        currentProd.setStatus("3");
        trialTaskDetailProdMapper.updateTrialTaskDetailProd(currentProd);

        TrialTaskDetailProd queryParam = new TrialTaskDetailProd();
        queryParam.setTaskId(currentProd.getTaskId());
        List<TrialTaskDetailProd> list = trialTaskDetailProdMapper.selectTrialTaskDetailProdList(queryParam);
        list.sort((a, b) -> a.getSerialNo().compareTo(b.getSerialNo()));

        for (int i = 0; i < list.size(); i++) {
            if (list.get(i).getId().equals(id) && i < list.size() - 1) {
                TrialTaskDetailProd nextProd = list.get(i + 1);
                nextProd.setStatus("1");
                trialTaskDetailProdMapper.updateTrialTaskDetailProd(nextProd);

                TrialTaskProd trialTaskProd = trialTaskProdMapper.selectTrialTaskProdByTaskId(currentProd.getTaskId());
                if (trialTaskProd != null) {
                    trialTaskProd.setCurrentSerialNo(nextProd.getSerialNo());
                    trialTaskProd.setCurrentSerialName(nextProd.getProgram());
                    trialTaskProdMapper.updateTrialTaskProd(trialTaskProd);
                }
                break;
            }
        }
        return 1;
    }

    /**
     * 批量删除试制任务程序
     * 
     * @param ids 需要删除的试制任务程序主键
     * @return 结果
     */
    @Override
    public int deleteTrialTaskDetailProdByIds(Long[] ids)
    {
        return trialTaskDetailProdMapper.deleteTrialTaskDetailProdByIds(ids);
    }

    /**
     * 删除试制任务程序信息
     * 
     * @param id 试制任务程序主键
     * @return 结果
     */
    @Override
    public int deleteTrialTaskDetailProdById(Long id)
    {
        return trialTaskDetailProdMapper.deleteTrialTaskDetailProdById(id);
    }
}
