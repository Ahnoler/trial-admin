package com.ruoyi.web.controller.trial;


import java.util.*;
import javax.servlet.http.HttpServletResponse;

import com.ruoyi.trial.domain.TrialTaskProd;
import com.ruoyi.trial.service.ITrialTaskProdService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
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
import com.ruoyi.trial.domain.TrialTaskDetailProd;
import com.ruoyi.trial.service.ITrialTaskDetailProdService;
import com.ruoyi.common.utils.poi.ExcelUtil;
import com.ruoyi.common.core.page.TableDataInfo;
import com.ruoyi.trial.domain.TaskStatus;
import com.ruoyi.trial.domain.ProcessStatus;

/**
 * 试制任务程序Controller
 *
 * @author ruoyi
 * @date 2023-07-11
 */
@RestController
@RequestMapping("/trial/prod/detail")
@Api(tags = "试制任务-零件流转卡-流转程序")
public class TrialTaskDetailProdController extends BaseController {
    @Autowired
    private ITrialTaskDetailProdService trialTaskDetailProdService;

    @Autowired
    private ITrialTaskProdService trialTaskProdService;

    @PostMapping("/add")
    @ApiOperation(value = "新增流转程序")
    public AjaxResult add(@RequestBody TrialTaskDetailProd trialTaskDetailProd) {
        return toAjax(trialTaskDetailProdService.insertTrialTaskDetailProd(trialTaskDetailProd));
    }

    @PostMapping("/edit")
    @ApiOperation(value = "修改流转程序")
    public AjaxResult edit(@RequestBody TrialTaskDetailProd trialTaskDetailProd) {
        TrialTaskProd taskProd = trialTaskProdService.selectTrialTaskProdByTaskId(trialTaskDetailProd.getTaskId());
        if (TaskStatus.FINISHED.getCode().equals(taskProd.getStatus())) {
            return AjaxResult.error("任务已结束，无法修改");
        }
        return toAjax(trialTaskDetailProdService.updateTrialTaskDetailProd(trialTaskDetailProd));
    }

    @DeleteMapping("/{ids}")
    @ApiOperation(value = "删除流转程序")
    @PreAuthorize("@ss.hasPermi('trial:prod:remove')")
    @Log(title = "试制任务程序", businessType = BusinessType.DELETE)
    public AjaxResult remove(@PathVariable Long[] ids) {
        return toAjax(trialTaskDetailProdService.deleteTrialTaskDetailProdByIds(ids));
    }

    @GetMapping("/list")
    @ApiOperation(value = "获取试制任务-零件流转卡-流转程序列表")
    public TableDataInfo list(TrialTaskDetailProd trialTaskDetailProd) {
        startPage();
        List<TrialTaskDetailProd> list = trialTaskDetailProdService.selectTrialTaskDetailProdList(trialTaskDetailProd);
        return getDataTable(list);
    }

    @PostMapping("/export")
    @PreAuthorize("@ss.hasPermi('trial:prod:export')")
    @ApiOperation(value = "导出试制任务-零件流转卡-流转程序列表为Excel")
    @Log(title = "试制任务程序", businessType = BusinessType.EXPORT)
    public void export(HttpServletResponse response, TrialTaskDetailProd trialTaskDetailProd) {
        List<TrialTaskDetailProd> list = trialTaskDetailProdService.selectTrialTaskDetailProdList(trialTaskDetailProd);
        ExcelUtil<TrialTaskDetailProd> util = new ExcelUtil<TrialTaskDetailProd>(TrialTaskDetailProd.class);
        util.exportExcel(response, list, "试制任务程序数据");
    }

    @GetMapping(value = "/{id}")
    @PreAuthorize("@ss.hasPermi('trial:prod:query')")
    @ApiOperation(value = "获取试制任务-零件流转卡-流转程序详细信息")
    public AjaxResult getInfo(@PathVariable("id") Long id) {
        return success(trialTaskDetailProdService.selectTrialTaskDetailProdById(id));
    }

    @PostMapping("/apply")
    @ApiOperation(value = "申请试制任务-零件流转卡-流转程序")
    public AjaxResult apply(@RequestBody TrialTaskDetailProd trialTaskDetailProd) {
        if (trialTaskDetailProd.getId() == null) {
            return AjaxResult.error("ID不能为空");
        }
        TrialTaskDetailProd currentProd = trialTaskDetailProdService.selectTrialTaskDetailProdById(trialTaskDetailProd.getId());
        if (currentProd == null) {
            return AjaxResult.error("未找到该流转程序");
        }
        if (!ProcessStatus.FILLING.getCode().equals(currentProd.getStatus())) {
            return AjaxResult.error("只有正在填报状态的流转程序才能申请");
        }
        TrialTaskProd taskProd = trialTaskProdService.selectTrialTaskProdByTaskId(currentProd.getTaskId());
        if (taskProd != null && TaskStatus.FINISHED.getCode().equals(taskProd.getStatus())) {
            return AjaxResult.error("任务已结束，无法申请");
        }
        currentProd.setStatus(ProcessStatus.SUBMITTED.getCode());
        return toAjax(trialTaskDetailProdService.updateTrialTaskDetailProd(currentProd));
    }


    @PostMapping("/approve")
    @ApiOperation(value = "审核试制任务-零件流转卡-流转程序")
    @PreAuthorize("@ss.hasPermi('trial:prod:edit')")
    @Log(title = "试制任务程序", businessType = BusinessType.UPDATE)
    public AjaxResult approve(@RequestBody TrialTaskDetailProd trialTaskDetailProd) {
        if (trialTaskDetailProd.getId() == null) {
            return AjaxResult.error("ID不能为空");
        }
        TrialTaskDetailProd currentProd = trialTaskDetailProdService.selectTrialTaskDetailProdById(trialTaskDetailProd.getId());
        if (currentProd == null) {
            return AjaxResult.error("未找到该流转程序");
        }
        TrialTaskProd taskProd = trialTaskProdService.selectTrialTaskProdByTaskId(currentProd.getTaskId());
        if (taskProd != null && TaskStatus.FINISHED.getCode().equals(taskProd.getStatus())) {
            return AjaxResult.error("任务已结束，无法审核");
        }
        return toAjax(trialTaskDetailProdService.approveTrialTaskDetailProdById(trialTaskDetailProd.getId()));
    }
}
