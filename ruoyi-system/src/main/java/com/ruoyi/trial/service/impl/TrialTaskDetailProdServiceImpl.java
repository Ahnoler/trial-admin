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
        //获取当前任务
        TrialTaskProd trialTaskProd =  trialTaskProdMapper.selectTrialTaskProdByTaskId(trialTaskDetailProd.getTaskId());

        //查询当前任务的卡片序号列表
        TrialTaskDetailProd node = new TrialTaskDetailProd();
        node.setTaskId(trialTaskDetailProd.getTaskId());
        List<TrialTaskDetailProd> list = trialTaskDetailProdMapper.selectTrialTaskDetailProdList(node);

        //查询下一个程序的节点
        int nextNode = -1;
        for(int i=0;i<list.size();i++){
            int currentNode = list.get(i).getSerialNo();
            if (currentNode == trialTaskDetailProd.getSerialNo()&&i<list.size()-1){
                nextNode = list.get(i+1).getSerialNo();
            }
        }

        //更新下一个程序的状态为当前节点
        if (nextNode>-1) {
            //获取卡片列表
            TrialTaskDetailProd trialTaskDetailProdNext = new TrialTaskDetailProd();
            trialTaskDetailProdNext.setTaskId(trialTaskDetailProd.getTaskId());
            trialTaskDetailProdNext.setSerialNo(nextNode);
            List<TrialTaskDetailProd> nextList = trialTaskDetailProdMapper.selectTrialTaskDetailProdList(trialTaskDetailProdNext);

            if (nextList.size()>0){
                //如果当前程序为min=1,max=1则需要把当前环节跳转到下一个程序,如果是min=1,max>1则要判断是否属于同一个大环节，相同则修改，不相同则需要新插入一条记录
                int min = cardColumnHeaderMapper.selectCardColumnHeaderByColumnCode(trialTaskDetailProd.getColumnCode()).getColumnMin();
                int max = cardColumnHeaderMapper.selectCardColumnHeaderByColumnCode(trialTaskDetailProd.getColumnCode()).getColumnMax();

                //判断大节点是否一样
                boolean same = nextNode/100 == trialTaskDetailProd.getSerialNo()/100;

                if (min==1&&max==1||min==1&&max>1&&same){

                    //先修改下一个环节
                    TrialTaskDetailProd trialTaskDetailProdUpdate = nextList.get(0);
                    trialTaskDetailProdUpdate.setStatus("1");//正在填报状态
                    trialTaskDetailProdMapper.updateTrialTaskDetailProd(trialTaskDetailProdUpdate);

                    //再修改任务表中的当前环节
                    trialTaskProd.setCurrentSerialNo(trialTaskDetailProdUpdate.getSerialNo());//把任务表的当前程序环节变为下一个环节
                    trialTaskProd.setCurrentSerialName(trialTaskDetailProdUpdate.getProgram());//把任务表的当前程序环节变为下一个环节
                    trialTaskProdMapper.updateTrialTaskProd(trialTaskProd);

                } else {
                    TrialTaskDetailProd trialTaskDetailProdNew = trialTaskDetailProd;
                    trialTaskDetailProdNew.setSerialNo(trialTaskDetailProd.getSerialNo()+1);
                    trialTaskDetailProdNew.setProgram("new");//流转程序
                    trialTaskDetailProdNew.setStatus("1");//正在填报状态
                    trialTaskDetailProdNew.setId(null);
                    trialTaskDetailProdMapper.insertTrialTaskDetailProd(trialTaskDetailProdNew);

                    //再修改任务表中的当前环节
                    trialTaskProd.setCurrentSerialNo(trialTaskDetailProdNew.getSerialNo());//把任务表的当前程序环节变为下一个环节
                    trialTaskProd.setCurrentSerialName(trialTaskDetailProdNew.getProgram());//把任务表的当前程序环节变为下一个环节
                    trialTaskProdMapper.updateTrialTaskProd(trialTaskProd);
                }
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
