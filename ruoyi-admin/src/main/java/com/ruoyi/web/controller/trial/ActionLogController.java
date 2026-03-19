package com.ruoyi.web.controller.trial;

import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.entity.ActionLog;
import com.ruoyi.trial.service.ActionLogService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

/**
 * 计划任务操作日志(ActionLog)表控制层
 *
 * @author makejava
 * @since 2023-07-26 20:03:40
 */
@RestController
@RequestMapping("actionLog")
@Api(tags = "计划任务操作日志")
public class ActionLogController extends BaseController {
    /**
     * 服务对象
     */
    @Resource
    private ActionLogService actionLogService;

    /**
     * 通过主键查询单条数据
     *
     * @param id 主键
     * @return 单条数据
     */
    @GetMapping("selectOne")
    @ApiOperation(value = "通过主键查询单条数据")
    public ActionLog selectOne(Integer id) {
        return this.actionLogService.queryById(id);
    }

    @PostMapping("add")
    @ApiOperation(value = "新增操作日志")
    public AjaxResult add(@RequestBody ActionLog actionLog) {
        return AjaxResult.success(actionLogService.insert(actionLog));
    }
    @GetMapping("list")
    @ApiOperation(value = "查询操作日志列表")
    public AjaxResult getList(ActionLog actionLog) {
        startPage();
        return AjaxResult.success(getDataTable(actionLogService.queryAllByLimit(actionLog)));
    }
}