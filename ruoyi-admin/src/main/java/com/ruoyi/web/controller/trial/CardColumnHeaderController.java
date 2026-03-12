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
import com.ruoyi.trial.domain.CardColumnHeader;
import com.ruoyi.trial.service.ICardColumnHeaderService;
import com.ruoyi.common.utils.poi.ExcelUtil;
import com.ruoyi.common.core.page.TableDataInfo;

/**
 * 卡片列头模版Controller
 * 
 * @author ruoyi
 * @date 2023-07-13
 */
@RestController
@RequestMapping("/card/column/header")
public class CardColumnHeaderController extends BaseController
{
    @Autowired
    private ICardColumnHeaderService cardColumnHeaderService;

    /**
     * 查询卡片列头模版列表
     */
    @PreAuthorize("@ss.hasPermi('card:header:list')")
    @GetMapping("/list")
    public TableDataInfo list(CardColumnHeader cardColumnHeader)
    {
        startPage();
        List<CardColumnHeader> list = cardColumnHeaderService.selectCardColumnHeaderList(cardColumnHeader);
        return getDataTable(list);
    }

    /**
     * 导出卡片列头模版列表
     */
    @PreAuthorize("@ss.hasPermi('card:header:export')")
    @Log(title = "卡片列头模版", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, CardColumnHeader cardColumnHeader)
    {
        List<CardColumnHeader> list = cardColumnHeaderService.selectCardColumnHeaderList(cardColumnHeader);
        ExcelUtil<CardColumnHeader> util = new ExcelUtil<CardColumnHeader>(CardColumnHeader.class);
        util.exportExcel(response, list, "卡片列头模版数据");
    }

    /**
     * 获取卡片列头模版详细信息
     */
    @PreAuthorize("@ss.hasPermi('card:header:query')")
    @GetMapping(value = "/{columnCode}")
    public AjaxResult getInfo(@PathVariable("columnCode") Long columnCode)
    {
        return success(cardColumnHeaderService.selectCardColumnHeaderByColumnCode(columnCode));
    }

    /**
     * 新增卡片列头模版
     */
    @PreAuthorize("@ss.hasPermi('card:header:add')")
    @Log(title = "卡片列头模版", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody CardColumnHeader cardColumnHeader)
    {
        return toAjax(cardColumnHeaderService.insertCardColumnHeader(cardColumnHeader));
    }

    /**
     * 修改卡片列头模版
     */
    @PreAuthorize("@ss.hasPermi('card:header:edit')")
    @Log(title = "卡片列头模版", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody CardColumnHeader cardColumnHeader)
    {
        return toAjax(cardColumnHeaderService.updateCardColumnHeader(cardColumnHeader));
    }

    /**
     * 删除卡片列头模版
     */
    @PreAuthorize("@ss.hasPermi('card:header:remove')")
    @Log(title = "卡片列头模版", businessType = BusinessType.DELETE)
	@DeleteMapping("/{columnCodes}")
    public AjaxResult remove(@PathVariable Long[] columnCodes)
    {
        return toAjax(cardColumnHeaderService.deleteCardColumnHeaderByColumnCodes(columnCodes));
    }
}
