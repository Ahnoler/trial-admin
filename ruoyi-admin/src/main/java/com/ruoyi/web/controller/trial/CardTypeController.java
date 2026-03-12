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
import com.ruoyi.trial.domain.CardType;
import com.ruoyi.trial.service.ICardTypeService;
import com.ruoyi.common.utils.poi.ExcelUtil;
import com.ruoyi.common.core.page.TableDataInfo;

/**
 * 卡片类型Controller
 * 
 * @author ruoyi
 * @date 2023-07-13
 */
@RestController
@RequestMapping("/card/type")
public class CardTypeController extends BaseController
{
    @Autowired
    private ICardTypeService cardTypeService;

    /**
     * 查询卡片类型列表
     */
    @PreAuthorize("@ss.hasPermi('card:type:list')")
    @GetMapping("/list")
    public TableDataInfo list(CardType cardType)
    {
        startPage();
        List<CardType> list = cardTypeService.selectCardTypeList(cardType);
        return getDataTable(list);
    }

    /**
     * 导出卡片类型列表
     */
    @PreAuthorize("@ss.hasPermi('card:type:export')")
    @Log(title = "卡片类型", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, CardType cardType)
    {
        List<CardType> list = cardTypeService.selectCardTypeList(cardType);
        ExcelUtil<CardType> util = new ExcelUtil<CardType>(CardType.class);
        util.exportExcel(response, list, "卡片类型数据");
    }

    /**
     * 获取卡片类型详细信息
     */
    @PreAuthorize("@ss.hasPermi('card:type:query')")
    @GetMapping(value = "/{cardId}")
    public AjaxResult getInfo(@PathVariable("cardId") Long cardId)
    {
        return success(cardTypeService.selectCardTypeByCardId(cardId));
    }

    /**
     * 新增卡片类型
     */
    @PreAuthorize("@ss.hasPermi('card:type:add')")
    @Log(title = "卡片类型", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody CardType cardType)
    {
        return toAjax(cardTypeService.insertCardType(cardType));
    }

    /**
     * 修改卡片类型
     */
    @PreAuthorize("@ss.hasPermi('card:type:edit')")
    @Log(title = "卡片类型", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody CardType cardType)
    {
        return toAjax(cardTypeService.updateCardType(cardType));
    }

    /**
     * 删除卡片类型
     */
    @PreAuthorize("@ss.hasPermi('card:type:remove')")
    @Log(title = "卡片类型", businessType = BusinessType.DELETE)
	@DeleteMapping("/{cardIds}")
    public AjaxResult remove(@PathVariable Long[] cardIds)
    {
        return toAjax(cardTypeService.deleteCardTypeByCardIds(cardIds));
    }
}
