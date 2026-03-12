package com.ruoyi.trial.service.impl;

import java.util.List;
import com.ruoyi.common.utils.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.ruoyi.trial.mapper.PrintLogMapper;
import com.ruoyi.trial.domain.PrintLog;
import com.ruoyi.trial.service.IPrintLogService;

/**
 *  打印记录Service业务层处理
 * 
 * @author ruoyi
 * @date 2023-07-11
 */
@Service
public class PrintLogServiceImpl implements IPrintLogService 
{
    @Autowired
    private PrintLogMapper printLogMapper;

    /**
     * 查询 打印记录
     * 
     * @param id  打印记录主键
     * @return  打印记录
     */
    @Override
    public PrintLog selectPrintLogById(Long id)
    {
        return printLogMapper.selectPrintLogById(id);
    }

    /**
     * 查询 打印记录列表
     * 
     * @param printLog  打印记录
     * @return  打印记录
     */
    @Override
    public List<PrintLog> selectPrintLogList(PrintLog printLog)
    {
        return printLogMapper.selectPrintLogList(printLog);
    }

    /**
     * 新增 打印记录
     * 
     * @param printLog  打印记录
     * @return 结果
     */
    @Override
    public int insertPrintLog(PrintLog printLog)
    {
        printLog.setCreateTime(DateUtils.getNowDate());
        return printLogMapper.insertPrintLog(printLog);
    }

    /**
     * 修改 打印记录
     * 
     * @param printLog  打印记录
     * @return 结果
     */
    @Override
    public int updatePrintLog(PrintLog printLog)
    {
        printLog.setUpdateTime(DateUtils.getNowDate());
        return printLogMapper.updatePrintLog(printLog);
    }

    /**
     * 批量删除 打印记录
     * 
     * @param ids 需要删除的 打印记录主键
     * @return 结果
     */
    @Override
    public int deletePrintLogByIds(Long[] ids)
    {
        return printLogMapper.deletePrintLogByIds(ids);
    }

    /**
     * 删除 打印记录信息
     * 
     * @param id  打印记录主键
     * @return 结果
     */
    @Override
    public int deletePrintLogById(Long id)
    {
        return printLogMapper.deletePrintLogById(id);
    }
}
