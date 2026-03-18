package com.ruoyi.web.controller.trial;

import java.util.List;
import javax.servlet.http.HttpServletResponse;

import com.ruoyi.common.core.domain.model.LoginUser;
import com.ruoyi.common.utils.ServletUtils;
import com.ruoyi.common.utils.QRCodeUtil;
import com.ruoyi.common.utils.uuid.UUID;
import com.ruoyi.framework.web.service.TokenService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
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
@Tag(name = "试制任务-零件流转卡", description = "管理零件流转卡，以及零件流转卡的分流与当前流转程序变更")
public class TrialTaskProdController extends BaseController {
    @Autowired
    private ITrialTaskProdService trialTaskProdService;

    @Autowired
    private TokenService tokenService;

    @PostMapping
    @Operation(summary = "新增零件流转卡")
    @PreAuthorize("@ss.hasPermi('trial:prod:add')")
    @Log(title = "试制任务信息", businessType = BusinessType.INSERT)
    public AjaxResult add(@RequestBody TrialTaskProd trialTaskProd) {
        return toAjax(trialTaskProdService.insertTrialTaskProd(trialTaskProd));
    }

    @PutMapping
    @Operation(summary = "修改零件流转卡")
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
    @Operation(summary = "删除零件流转卡")
    @PreAuthorize("@ss.hasPermi('trial:prod:remove')")
    @Log(title = "试制任务信息", businessType = BusinessType.DELETE)
    public AjaxResult remove(@PathVariable Long[] taskIds) {
        return toAjax(trialTaskProdService.deleteTrialTaskProdByTaskIds(taskIds));
    }

    @GetMapping(value = "/{taskId}")
    @Operation(summary = "获取零件流转卡详细信息")
    @PreAuthorize("@ss.hasPermi('trial:prod:query')")
    public AjaxResult getInfo(@PathVariable("taskId") Long taskId) {
        return success(trialTaskProdService.selectTrialTaskProdByTaskId(taskId));
    }

    @GetMapping("/list")
    @Operation(summary = "获取零件流转卡列表")
    @PreAuthorize("@ss.hasPermi('trial:prod:list')")
    public TableDataInfo list(TrialTaskProd trialTaskProd) {
        startPage();
        List<TrialTaskProd> list = trialTaskProdService.selectTrialTaskProdList(trialTaskProd);
        return getDataTable(list);
    }

    @PostMapping("/exportExcel")
    @PreAuthorize("@ss.hasPermi('trial:prod:export')")
    @Operation(summary = "导出零件流转卡列表为Excel")
    @Log(title = "试制任务信息", businessType = BusinessType.EXPORT)
    public void export(HttpServletResponse response, TrialTaskProd trialTaskProd) {
        List<TrialTaskProd> list = trialTaskProdService.selectTrialTaskProdList(trialTaskProd);
        ExcelUtil<TrialTaskProd> util = new ExcelUtil<>(TrialTaskProd.class);
        util.exportExcel(response, list, "试制任务信息数据");
    }

    @PostMapping("/flow")
    @PreAuthorize("@ss.hasPermi('trial:prod:edit')")
    @Log(title = "试制任务信息", businessType = BusinessType.UPDATE)
    @Operation(summary = "变更零件流转卡-流转程序", description = "将当前工序状态改为1-正在填报，向前是3-已审核，向后是0-未填报，2-已申请还没有做")
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
    @Operation(summary = "零件流转卡分流操作", description = "为传进来的taskId和当前分流改动的工序列表 新建零件流转卡，并且设置关联id")
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
    @Operation(summary = "结束零件流转卡", description = "判断是否可以结束，由前端完成")
    public AjaxResult over(@RequestBody TrialTaskProd trialTaskProd) {
        TrialTaskProd oldNode = trialTaskProdService.selectTrialTaskProdByTaskId(trialTaskProd.getTaskId());
        // 把任务置为结束状态
        oldNode.setStatus("2");
        return toAjax(trialTaskProdService.updateTrialTaskProd(oldNode));
    }

    @Operation(summary = "导出零件流转卡详细信息为pdf")
    @PreAuthorize("@ss.hasPermi('trial:projects:export')")
    @Log(title = "试制任务信息", businessType = BusinessType.EXPORT)
    @GetMapping(value = "/exportPdf/{id}", produces = "application/pdf")
    public void exportProdPdf(@PathVariable Long id, HttpServletResponse response) throws Exception {
        trialTaskProdService.exportPdf(id, response);
    }

    @GetMapping("/related/{relatedTaskId}")
    @PreAuthorize("@ss.hasPermi('trial:prod:list')")
    @Operation(summary = "根据关联任务ID查询相关的零件流转卡列表", description = "单次查询操作交由前端完成")
    public TableDataInfo selectByRelatedTaskId(@PathVariable("relatedTaskId") Long relatedTaskId) {
        startPage();
        List<TrialTaskProd> list = trialTaskProdService.selectTrialTaskProdByRelatedTaskId(relatedTaskId);
        return getDataTable(list);
    }

    @GetMapping("/printDetail/{id}")
    @PreAuthorize("@ss.hasPermi('trial:prod:print')")
    @Operation(summary = "打印零件流转卡详细信息为pdf", description = "在网页上完成打印")
    public void print(@PathVariable Long id, @RequestParam(required = false, defaultValue = "1") Long printType, HttpServletResponse response) throws Exception {
        LoginUser loginUser = tokenService.getLoginUser(ServletUtils.getRequest());
        String userId = loginUser.getUser().getUserId().toString();
        trialTaskProdService.printTrialTaskProd(id, printType, userId, response);
    }

    @GetMapping("/scan/{qrCode}")
    @Operation(summary = "扫码识别-根据二维码获取流转卡信息", description = "小程序端扫描二维码后调用此接口获取流转卡详情")
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
    @Operation(summary = "重新生成二维码", description = "为指定流转卡重新生成二维码")
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
    @Operation(summary = "批量生成二维码", description = "为所有没有二维码的流转卡批量生成二维码")
    public AjaxResult batchGenerateQrCode() {
        int count = trialTaskProdService.batchGenerateQrCode();
        return AjaxResult.success("成功为 " + count + " 个流转卡生成二维码");
    }
}
