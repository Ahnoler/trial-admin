package com.ruoyi.trial.service;

import java.util.List;
import java.util.Map;
import com.ruoyi.trial.domain.TrialTaskProd;
import net.sf.jasperreports.engine.JRException;

import javax.servlet.http.HttpServletResponse;

/**
 * 试制任务信息Service接口
 * 
 * @author ruoyi
 * @date 2023-07-11
 */
public interface ITrialTaskProdService
{
    /**
     * 查询零件流转卡
     * 
     * @param taskId 零件流转卡主键
     * @return 零件流转卡
     */
    public TrialTaskProd selectTrialTaskProdByTaskId(Long taskId);

    /**
     * 查询零件流转卡列表
     * 
     * @param trialTaskProd 零件流转卡
     * @return 零件流转卡集合
     */
    public List<TrialTaskProd> selectTrialTaskProdList(TrialTaskProd trialTaskProd);

    /**
     * 新增零件流转卡
     * 
     * @param trialTaskProd 零件流转卡
     * @return 结果
     */
    public int insertTrialTaskProd(TrialTaskProd trialTaskProd);

    /**
     * 复制零件流转卡
     *
     * @param trialTaskProd 零件流转卡
     * @return 结果
     */
    public int copyTrialTaskProd(TrialTaskProd trialTaskProd);

    /**
     * 修改零件流转卡
     * 
     * @param trialTaskProd 零件流转卡
     * @return 结果
     */
    public int updateTrialTaskProd(TrialTaskProd trialTaskProd);

    /**
     * 变更零件流转卡
     *
     * @param trialTaskProd 零件流转卡
     * @return 结果
     */
    public int changeTrialTaskProd(TrialTaskProd trialTaskProd);

    /**
     * 分流零件流转卡
     *
     * @param trialTaskProd 零件流转卡  
     * @param oldTaskId 旧任务ID
     * @return 结果
     */
    public int forkTrialTaskProd(TrialTaskProd trialTaskProd,long oldTaskId);

    /**
     * 批量删除零件流转卡
     * 
     * @param taskIds 需要删除的零件流转卡主键集合
     * @return 结果
     */
    public int deleteTrialTaskProdByTaskIds(Long[] taskIds);

    void exportPdf(Long id, HttpServletResponse response) throws Exception;
    
    /**
     * 根据关联任务ID查询相关的零件流转卡列表
     * 
     * @param relatedTaskId 关联任务ID
     * @return 零件流转卡集合
     */
    public List<TrialTaskProd> selectTrialTaskProdByRelatedTaskId(Long relatedTaskId);

    /**
     * 零件流转卡-流转程序变更
     * 
     * @param trialTaskProd 零件流转卡
     * @param userId 用户ID
     * @return 结果
     */
    public int flowTrialTaskProd(TrialTaskProd trialTaskProd, String userId);
    
    /**
     * 零件流转卡分流操作
     * 
     * @param trialTaskProd 零件流转卡
     * @param userId 用户ID
     * @return 结果
     */
    public int forkTrialTaskProdWithLog(TrialTaskProd trialTaskProd, String userId);

    /**
     * 打印零件流转卡详细信息
     * 
     * @param id 任务ID
     * @param printType 打印类型
     * @param userId 用户ID
     * @param response HTTP响应
     * @throws Exception 异常
     */
    public void printTrialTaskProd(Long id, Long printType, String userId, HttpServletResponse response) throws Exception;

    /**
     * 准备打印数据
     * 
     * @param id 任务ID
     * @param printType 打印类型
     * @param userId 用户ID
     * @return 打印数据模型
     */
    public Map<String, Object> preparePrintData(Long id, Long printType, String userId);

    /**
     * 根据二维码标识查询零件流转卡
     * 
     * @param qrCode 二维码标识
     * @return 零件流转卡
     */
    public TrialTaskProd selectTrialTaskProdByQrCode(String qrCode);

    /**
     * 批量为没有二维码的流转卡生成二维码
     * 
     * @return 生成的二维码数量
     */
    public int batchGenerateQrCode();
}
