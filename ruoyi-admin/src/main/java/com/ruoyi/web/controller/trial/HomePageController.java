package com.ruoyi.web.controller.trial;

import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.core.domain.model.LoginUser;
import com.ruoyi.trial.service.HomePageService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/homePage")
@AllArgsConstructor
@Api(tags = "首页信息")
public class HomePageController extends BaseController {
    private final HomePageService homePageService;

    @GetMapping("selectHomeInfo")
    @ApiOperation(value = "查询首页信息")
    public AjaxResult selectHomeInfo(String year, String month){
        LoginUser loginUser = getLoginUser();
        return AjaxResult.success(homePageService.selectHomePage(loginUser,year,month));
    }
    @GetMapping("selectExamineTask")
    @ApiOperation(value = "查询审核任务列表")
    public AjaxResult selectExamineTask(String year, String month){
        LoginUser loginUser = getLoginUser();
        startPage();
        return AjaxResult.success(getDataTable(homePageService.selectExamineTask(loginUser,year,month)));
    }
}
