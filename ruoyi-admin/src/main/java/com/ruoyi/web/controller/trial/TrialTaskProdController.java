package com.ruoyi.web.controller.trial;

import java.util.List;
import java.util.Map;
import javax.servlet.http.HttpServletResponse;

import com.ruoyi.common.core.domain.model.LoginUser;
import com.ruoyi.common.utils.ServletUtils;
import com.ruoyi.common.utils.QRCodeUtil;
import com.ruoyi.common.utils.uuid.UUID;
import com.ruoyi.framework.web.service.TokenService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import com.ruoyi.common.annotation.Log;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.enums.BusinessType;
import com.ruoyi.trial.domain.TrialTaskProd;
import com.ruoyi.trial.service.ITrialTaskProdService;
import com.ruoyi.common.utils.poi.ExcelUtil;
import com.ruoyi.common.core.page.TableDataInfo;

/**
 * 试制任务信息Controller
 *
 * @author ruoyi
 * @date 2023-07-11
 */
@RestController
@RequestMapping("/trial/prod")
@Api(tags = "试制任务-零件流转卡")
public class TrialTaskProdController extends BaseController {
    @Autowired
    private ITrialTaskProdService trialTaskProdService;

    @Autowired
    private TokenService tokenService;

    @PostMapping
    @ApiOperation(value = "新增零件流转卡")
    @PreAuthorize("@ss.hasPermi('trial:prod:add')")
    @Log(title = "试制任务信息", businessType = BusinessType.INSERT)
    public AjaxResult add(@RequestBody TrialTaskProd trialTaskProd) {
        return toAjax(trialTaskProdService.insertTrialTaskProd(trialTaskProd));
    }

    @PutMapping
    @ApiOperation(value = "修改零件流转卡")
    @PreAuthorize("@ss.hasPermi('trial:prod:edit')")
    @Log(title = "试制任务信息", businessType = BusinessType.UPDATE)
    public AjaxResult edit(@RequestBody TrialTaskProd trialTaskProd) {
        TrialTaskProd oldNode = trialTaskProdService.selectTrialTaskProdByTaskId(trialTaskProd.getTaskId());
        if ("2".equals(oldNode.getStatus())) {
            return AjaxResult.error("任务已结束，无法修改");
        }
        return toAjax(trialTaskProdService.updateTrialTaskProd(trialTaskProd));
    }

    @DeleteMapping("/{taskIds}")
    @ApiOperation(value = "删除零件流转卡")
    @PreAuthorize("@ss.hasPermi('trial:prod:remove')")
    @Log(title = "试制任务信息", businessType = BusinessType.DELETE)
    public AjaxResult remove(@PathVariable Long[] taskIds) {
        return toAjax(trialTaskProdService.deleteTrialTaskProdByTaskIds(taskIds));
    }

    @GetMapping("/myTask")
    @ApiOperation(value = "获取我的待办任务列表")
    public TableDataInfo myTask(TrialTaskProd trialTaskProd) {
        startPage();
        List<TrialTaskProd> list = trialTaskProdService.selectTrialTaskProdList(trialTaskProd);
        return getDataTable(list);
    }

    @GetMapping(value = "/{taskId}")
    @ApiOperation(value = "获取零件流转卡详细信息")
    @PreAuthorize("@ss.hasPermi('trial:prod:query')")
    public AjaxResult getInfo(@PathVariable("taskId") Long taskId) {
        return success(trialTaskProdService.selectTrialTaskProdByTaskId(taskId));
    }

    @GetMapping("/list")
    @ApiOperation(value = "获取零件流转卡列表")
    @PreAuthorize("@ss.hasPermi('trial:prod:list')")
    public TableDataInfo list(TrialTaskProd trialTaskProd) {
        startPage();
        List<TrialTaskProd> list = trialTaskProdService.selectTrialTaskProdList(trialTaskProd);
        return getDataTable(list);
    }

    @PostMapping("/exportExcel")
    @PreAuthorize("@ss.hasPermi('trial:prod:export')")
    @ApiOperation(value = "导出零件流转卡列表为Excel")
    @Log(title = "试制任务信息", businessType = BusinessType.EXPORT)
    public void export(HttpServletResponse response, TrialTaskProd trialTaskProd) {
        List<TrialTaskProd> list = trialTaskProdService.selectTrialTaskProdList(trialTaskProd);
        ExcelUtil<TrialTaskProd> util = new ExcelUtil<>(TrialTaskProd.class);
        util.exportExcel(response, list, "试制任务信息数据");
    }

    @PostMapping("/flow")
    @PreAuthorize("@ss.hasPermi('trial:prod:edit')")
    @Log(title = "试制任务信息", businessType = BusinessType.UPDATE)
    @ApiOperation(value = "变更零件流转卡-流转程序")
    public AjaxResult flow(@RequestBody TrialTaskProd trialTaskProd) {
        try {
            LoginUser loginUser = tokenService.getLoginUser(ServletUtils.getRequest());
            String userId = loginUser.getUser().getUserId().toString();
            return toAjax(trialTaskProdService.flowTrialTaskProd(trialTaskProd, userId));
        } catch (RuntimeException e) {
            return AjaxResult.error("变更操作失败！");
        }
    }

    @PostMapping("/fork")
    @PreAuthorize("@ss.hasPermi('trial:prod:edit')")
    @Log(title = "试制任务信息", businessType = BusinessType.UPDATE)
    @ApiOperation(value = "零件流转卡分流操作")
    public AjaxResult change(@RequestBody TrialTaskProd trialTaskProd) {
        try {
            LoginUser loginUser = tokenService.getLoginUser(ServletUtils.getRequest());
            String userId = loginUser.getUser().getUserId().toString();
            return toAjax(trialTaskProdService.forkTrialTaskProdWithLog(trialTaskProd, userId));
        } catch (RuntimeException e) {
            return AjaxResult.error(e.getMessage());
        }
    }

    @PostMapping("/over")
    @PreAuthorize("@ss.hasPermi('trial:prod:edit')")
    @Log(title = "试制任务信息", businessType = BusinessType.UPDATE)
    @ApiOperation(value = "结束零件流转卡")
    public AjaxResult over(@RequestBody TrialTaskProd trialTaskProd) {
        TrialTaskProd oldNode = trialTaskProdService.selectTrialTaskProdByTaskId(trialTaskProd.getTaskId());
        // 把任务置为结束状态
        oldNode.setStatus("2");
        return toAjax(trialTaskProdService.updateTrialTaskProd(oldNode));
    }

    @ApiOperation(value = "导出零件流转卡详细信息为pdf")
    @PreAuthorize("@ss.hasPermi('trial:projects:export')")
    @Log(title = "试制任务信息", businessType = BusinessType.EXPORT)
    @GetMapping(value = "/exportPdf/{id}", produces = "application/pdf")
    public void exportProdPdf(@PathVariable Long id, HttpServletResponse response) throws Exception {
        trialTaskProdService.exportPdf(id, response);
    }

    @GetMapping("/related/{relatedTaskId}")
    @PreAuthorize("@ss.hasPermi('trial:prod:list')")
    @ApiOperation(value = "根据关联任务ID查询相关的零件流转卡列表")
    public TableDataInfo selectByRelatedTaskId(@PathVariable("relatedTaskId") Long relatedTaskId) {
        startPage();
        List<TrialTaskProd> list = trialTaskProdService.selectTrialTaskProdByRelatedTaskId(relatedTaskId);
        return getDataTable(list);
    }

    @GetMapping("/printDetail/{id}")
    @PreAuthorize("@ss.hasPermi('trial:prod:print')")
    @ApiOperation(value = "打印零件流转卡详细信息")
    public ModelAndView print(@PathVariable Long id, 
                        @RequestParam(required = false, defaultValue = "1") Long printType) throws Exception {
        LoginUser loginUser = tokenService.getLoginUser(ServletUtils.getRequest());
        String userId = loginUser.getUser().getUserId().toString();
        
        Map<String, Object> printData = trialTaskProdService.preparePrintData(id, printType, userId);
        
        ModelAndView modelAndView = new ModelAndView("print-trial-task");
        modelAndView.addAllObjects(printData);
        
        return modelAndView;
    }

    @GetMapping("/scan/{qrCode}")
    @ApiOperation(value = "扫码识别-根据二维码获取流转卡信息")
    public AjaxResult scanQrCode(@PathVariable("qrCode") String qrCode) {
        TrialTaskProd task = trialTaskProdService.selectTrialTaskProdByQrCode(qrCode);
        if (task == null) {
            return AjaxResult.error("未找到对应的流转卡，请检查二维码是否正确");
        }
        if ("2".equals(task.getStatus())) {
            return AjaxResult.error("该流转卡已结束，无法进行操作");
        }
        return success(task);
    }

    @PostMapping("/regenerateQrCode/{taskId}")
    @PreAuthorize("@ss.hasPermi('trial:prod:edit')")
    @ApiOperation(value = "重新生成二维码")
    public AjaxResult regenerateQrCode(@PathVariable Long taskId) {
        TrialTaskProd task = trialTaskProdService.selectTrialTaskProdByTaskId(taskId);
        if (task == null) {
            return AjaxResult.error("未找到对应的流转卡");
        }
        String qrCode = "TASK_" + task.getTaskId() + "_" + UUID.randomUUID().toString().substring(0, 8);
        String qrCodeBase64 = QRCodeUtil.generateQRCodeBase64(qrCode, 200, 200);
        task.setQrCode(qrCode);
        task.setQrCodeUrl(qrCodeBase64);
        return toAjax(trialTaskProdService.updateTrialTaskProd(task));
    }

    @PostMapping("/batchGenerateQrCode")
    @PreAuthorize("@ss.hasPermi('trial:prod:edit')")
    @ApiOperation(value = "批量生成二维码")
    public AjaxResult batchGenerateQrCode() {
        int count = trialTaskProdService.batchGenerateQrCode();
        return AjaxResult.success("成功为 " + count + " 个流转卡生成二维码");
    }
}
