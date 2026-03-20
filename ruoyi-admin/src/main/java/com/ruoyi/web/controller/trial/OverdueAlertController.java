package com.ruoyi.web.controller.trial;

import java.util.List;
import javax.servlet.http.HttpServletResponse;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.ruoyi.common.annotation.Log;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.enums.BusinessType;
import com.ruoyi.trial.domain.OverdueAlert;
import com.ruoyi.trial.service.IOverdueAlertService;
import com.ruoyi.common.utils.poi.ExcelUtil;
import com.ruoyi.common.core.page.TableDataInfo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

@RestController
@RequestMapping("/trial/overdueAlert")
@Api(tags = "超期预警管理")
public class OverdueAlertController extends BaseController
{
    @Autowired
    private IOverdueAlertService overdueAlertService;

    @PreAuthorize("@ss.hasPermi('trial:overdueAlert:list')")
    @GetMapping("/list")
    @ApiOperation("查询超期预警列表")
    public TableDataInfo list(OverdueAlert overdueAlert)
    {
        startPage();
        List<OverdueAlert> list = overdueAlertService.selectOverdueAlertList(overdueAlert);
        return getDataTable(list);
    }

    @PreAuthorize("@ss.hasPermi('trial:overdueAlert:export')")
    @Log(title = "超期预警", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    @ApiOperation("导出超期预警列表")
    public void export(HttpServletResponse response, OverdueAlert overdueAlert)
    {
        List<OverdueAlert> list = overdueAlertService.selectOverdueAlertList(overdueAlert);
        ExcelUtil<OverdueAlert> util = new ExcelUtil<OverdueAlert>(OverdueAlert.class);
        util.exportExcel(response, list, "超期预警数据");
    }

    @PreAuthorize("@ss.hasPermi('trial:overdueAlert:query')")
    @GetMapping(value = "/{alertId}")
    @ApiOperation("获取超期预警详细信息")
    public AjaxResult getInfo(@PathVariable("alertId") Long alertId)
    {
        return success(overdueAlertService.selectOverdueAlertById(alertId));
    }

    @PreAuthorize("@ss.hasPermi('trial:overdueAlert:handle')")
    @Log(title = "超期预警处理", businessType = BusinessType.UPDATE)
    @PostMapping("/handle")
    @ApiOperation("处理超期预警")
    public AjaxResult handle(@RequestBody Long[] alertIds, String handleRemark)
    {
        String handler = getLoginUser().getUsername();
        return toAjax(overdueAlertService.handleAlerts(alertIds, handler, handleRemark));
    }

    @PreAuthorize("@ss.hasPermi('trial:overdueAlert:ignore')")
    @Log(title = "超期预警忽略", businessType = BusinessType.UPDATE)
    @PostMapping("/ignore")
    @ApiOperation("忽略超期预警")
    public AjaxResult ignore(@RequestBody Long[] alertIds, String handleRemark)
    {
        String handler = getLoginUser().getUsername();
        return toAjax(overdueAlertService.ignoreAlerts(alertIds, handler, handleRemark));
    }

    @PreAuthorize("@ss.hasPermi('trial:overdueAlert:remove')")
    @Log(title = "超期预警", businessType = BusinessType.DELETE)
    @DeleteMapping("/{alertIds}")
    @ApiOperation("删除超期预警")
    public AjaxResult remove(@PathVariable Long[] alertIds)
    {
        return toAjax(overdueAlertService.deleteOverdueAlertByIds(alertIds));
    }

    @GetMapping("/countPending")
    @ApiOperation("获取待处理预警数量")
    public AjaxResult countPending()
    {
        return success(overdueAlertService.countPendingAlerts());
    }

    @PreAuthorize("@ss.hasPermi('trial:overdueAlert:execute')")
    @Log(title = "执行超期预警检查", businessType = BusinessType.OTHER)
    @PostMapping("/execute")
    @ApiOperation("手动执行超期预警检查")
    public AjaxResult execute()
    {
        return success("检查完成，新增预警 " + overdueAlertService.executeOverdueCheck() + " 条");
    }
}
