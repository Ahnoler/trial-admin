package com.ruoyi.trial.mapper;

import java.util.List;
import com.ruoyi.trial.domain.PrintLog;

/**
 *  打印记录Mapper接口
 * 
 * @author ruoyi
 * @date 2023-07-11
 */
public interface PrintLogMapper 
{
    /**
     * 查询 打印记录
     * 
     * @param id  打印记录主键
     * @return  打印记录
     */
    public PrintLog selectPrintLogById(Long id);

    /**
     * 查询 打印记录列表
     * 
     * @param printLog  打印记录
     * @return  打印记录集合
     */
    public List<PrintLog> selectPrintLogList(PrintLog printLog);

    /**
     * 新增 打印记录
     * 
     * @param printLog  打印记录
     * @return 结果
     */
    public int insertPrintLog(PrintLog printLog);

    /**
     * 修改 打印记录
     * 
     * @param printLog  打印记录
     * @return 结果
     */
    public int updatePrintLog(PrintLog printLog);

    /**
     * 删除 打印记录
     * 
     * @param id  打印记录主键
     * @return 结果
     */
    public int deletePrintLogById(Long id);

    /**
     * 批量删除 打印记录
     * 
     * @param ids 需要删除的数据主键集合
     * @return 结果
     */
    public int deletePrintLogByIds(Long[] ids);
}
