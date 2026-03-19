package com.ruoyi.web.controller.trial;

import java.util.List;
import javax.servlet.http.HttpServletResponse;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.ruoyi.common.annotation.Log;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.enums.BusinessType;
import com.ruoyi.trial.domain.PrintLog;
import com.ruoyi.trial.service.IPrintLogService;
import com.ruoyi.common.utils.poi.ExcelUtil;
import com.ruoyi.common.core.page.TableDataInfo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

/**
 *  打印记录Controller
 * 
 * @author ruoyi
 * @date 2023-07-11
 */
@RestController
@RequestMapping("/trial/log")
@Api(tags = "打印记录")
public class PrintLogController extends BaseController
{
    @Autowired
    private IPrintLogService printLogService;

    /**
     * 查询 打印记录列表
     */
    @PreAuthorize("@ss.hasPermi('trial:log:list')")
    @GetMapping("/list")
    @ApiOperation(value = "查询打印记录列表")
    public TableDataInfo list(PrintLog printLog)
    {
        startPage();
        List<PrintLog> list = printLogService.selectPrintLogList(printLog);
        return getDataTable(list);
    }

    /**
     * 导出 打印记录列表
     */
    @PreAuthorize("@ss.hasPermi('trial:log:export')")
    @Log(title = " 打印记录", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    @ApiOperation(value = "导出打印记录列表")
    public void export(HttpServletResponse response, PrintLog printLog)
    {
        List<PrintLog> list = printLogService.selectPrintLogList(printLog);
        ExcelUtil<PrintLog> util = new ExcelUtil<PrintLog>(PrintLog.class);
        util.exportExcel(response, list, " 打印记录数据");
    }

    /**
     * 获取 打印记录详细信息
     */
    @PreAuthorize("@ss.hasPermi('trial:log:query')")
    @GetMapping(value = "/{id}")
    @ApiOperation(value = "获取打印记录详细信息")
    public AjaxResult getInfo(@PathVariable("id") Long id)
    {
        return success(printLogService.selectPrintLogById(id));
    }

    /**
     * 新增 打印记录
     */
    @PreAuthorize("@ss.hasPermi('trial:log:add')")
    @Log(title = " 打印记录", businessType = BusinessType.INSERT)
    @PostMapping
    @ApiOperation(value = "新增打印记录")
    public AjaxResult add(@RequestBody PrintLog printLog)
    {
        return toAjax(printLogService.insertPrintLog(printLog));
    }

    /**
     * 修改 打印记录
     */
    @PreAuthorize("@ss.hasPermi('trial:log:edit')")
    @Log(title = " 打印记录", businessType = BusinessType.UPDATE)
    @PutMapping
    @ApiOperation(value = "修改打印记录")
    public AjaxResult edit(@RequestBody PrintLog printLog)
    {
        return toAjax(printLogService.updatePrintLog(printLog));
    }

    /**
     * 删除 打印记录
     */
    @PreAuthorize("@ss.hasPermi('trial:log:remove')")
    @Log(title = " 打印记录", businessType = BusinessType.DELETE)
	@DeleteMapping("/{ids}")
    @ApiOperation(value = "删除打印记录")
    public AjaxResult remove(@PathVariable Long[] ids)
    {
        return toAjax(printLogService.deletePrintLogByIds(ids));
    }
}
