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
import com.ruoyi.trial.domain.CardModelDetailProd;
import com.ruoyi.trial.service.ICardModelDetailProdService;
import com.ruoyi.common.utils.poi.ExcelUtil;
import com.ruoyi.common.core.page.TableDataInfo;

/**
 * 卡片程序模版Controller
 * 
 * @author ruoyi
 * @date 2023-07-13
 */
@RestController
@RequestMapping("/card/prod")
public class CardModelDetailProdController extends BaseController
{
    @Autowired
    private ICardModelDetailProdService cardModelDetailProdService;

    /**
     * 查询卡片程序模版列表
     */
    @PreAuthorize("@ss.hasPermi('card:prod:list')")
    @GetMapping("/list")
    public TableDataInfo list(CardModelDetailProd cardModelDetailProd)
    {
        startPage();
        List<CardModelDetailProd> list = cardModelDetailProdService.selectCardModelDetailProdList(cardModelDetailProd);
        return getDataTable(list);
    }

    /**
     * 导出卡片程序模版列表
     */
    @PreAuthorize("@ss.hasPermi('card:prod:export')")
    @Log(title = "卡片程序模版", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, CardModelDetailProd cardModelDetailProd)
    {
        List<CardModelDetailProd> list = cardModelDetailProdService.selectCardModelDetailProdList(cardModelDetailProd);
        ExcelUtil<CardModelDetailProd> util = new ExcelUtil<CardModelDetailProd>(CardModelDetailProd.class);
        util.exportExcel(response, list, "卡片程序模版数据");
    }

    /**
     * 获取卡片程序模版详细信息
     */
    @PreAuthorize("@ss.hasPermi('card:prod:query')")
    @GetMapping(value = "/{id}")
    public AjaxResult getInfo(@PathVariable("id") Long id)
    {
        return success(cardModelDetailProdService.selectCardModelDetailProdById(id));
    }

    /**
     * 新增卡片程序模版
     */
    @PreAuthorize("@ss.hasPermi('card:prod:add')")
    @Log(title = "卡片程序模版", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody CardModelDetailProd cardModelDetailProd)
    {
        return toAjax(cardModelDetailProdService.insertCardModelDetailProd(cardModelDetailProd));
    }

    /**
     * 修改卡片程序模版
     */
    @PreAuthorize("@ss.hasPermi('card:prod:edit')")
    @Log(title = "卡片程序模版", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody CardModelDetailProd cardModelDetailProd)
    {
        return toAjax(cardModelDetailProdService.updateCardModelDetailProd(cardModelDetailProd));
    }

    /**
     * 删除卡片程序模版
     */
    @PreAuthorize("@ss.hasPermi('card:prod:remove')")
    @Log(title = "卡片程序模版", businessType = BusinessType.DELETE)
	@DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable Long[] ids)
    {
        return toAjax(cardModelDetailProdService.deleteCardModelDetailProdByIds(ids));
    }
}
