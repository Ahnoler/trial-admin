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
import com.ruoyi.trial.domain.CardTableHeader;
import com.ruoyi.trial.service.ICardTableHeaderService;
import com.ruoyi.common.utils.poi.ExcelUtil;
import com.ruoyi.common.core.page.TableDataInfo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

/**
 * 卡片头模版Controller
 * 
 * @author ruoyi
 * @date 2023-07-13
 */
@RestController
@RequestMapping("/card/header")
@Api(tags = "卡片头模版")
public class CardTableHeaderController extends BaseController
{
    @Autowired
    private ICardTableHeaderService cardTableHeaderService;

    /**
     * 查询卡片头模版列表
     */
    @PreAuthorize("@ss.hasPermi('card:header:list')")
    @GetMapping("/list")
    @ApiOperation(value = "查询卡片头模版列表")
    public TableDataInfo list(CardTableHeader cardTableHeader)
    {
        startPage();
        List<CardTableHeader> list = cardTableHeaderService.selectCardTableHeaderList(cardTableHeader);
        return getDataTable(list);
    }

    /**
     * 导出卡片头模版列表
     */
    @PreAuthorize("@ss.hasPermi('card:header:export')")
    @Log(title = "卡片头模版", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    @ApiOperation(value = "导出卡片头模版列表")
    public void export(HttpServletResponse response, CardTableHeader cardTableHeader)
    {
        List<CardTableHeader> list = cardTableHeaderService.selectCardTableHeaderList(cardTableHeader);
        ExcelUtil<CardTableHeader> util = new ExcelUtil<CardTableHeader>(CardTableHeader.class);
        util.exportExcel(response, list, "卡片头模版数据");
    }

    /**
     * 获取卡片头模版详细信息
     */
    @PreAuthorize("@ss.hasPermi('card:header:query')")
    @GetMapping(value = "/{headerCode}")
    @ApiOperation(value = "获取卡片头模版详细信息")
    public AjaxResult getInfo(@PathVariable("headerCode") Long headerCode)
    {
        return success(cardTableHeaderService.selectCardTableHeaderByHeaderCode(headerCode));
    }

    /**
     * 新增卡片头模版
     */
    @PreAuthorize("@ss.hasPermi('card:header:add')")
    @Log(title = "卡片头模版", businessType = BusinessType.INSERT)
    @PostMapping
    @ApiOperation(value = "新增卡片头模版")
    public AjaxResult add(@RequestBody CardTableHeader cardTableHeader)
    {
        return toAjax(cardTableHeaderService.insertCardTableHeader(cardTableHeader));
    }

    /**
     * 修改卡片头模版
     */
    @PreAuthorize("@ss.hasPermi('card:header:edit')")
    @Log(title = "卡片头模版", businessType = BusinessType.UPDATE)
    @PutMapping
    @ApiOperation(value = "修改卡片头模版")
    public AjaxResult edit(@RequestBody CardTableHeader cardTableHeader)
    {
        return toAjax(cardTableHeaderService.updateCardTableHeader(cardTableHeader));
    }

    /**
     * 删除卡片头模版
     */
    @PreAuthorize("@ss.hasPermi('card:header:remove')")
    @Log(title = "卡片头模版", businessType = BusinessType.DELETE)
	@DeleteMapping("/{headerCodes}")
    @ApiOperation(value = "删除卡片头模版")
    public AjaxResult remove(@PathVariable Long[] headerCodes)
    {
        return toAjax(cardTableHeaderService.deleteCardTableHeaderByHeaderCodes(headerCodes));
    }
}
