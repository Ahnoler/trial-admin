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
import com.ruoyi.trial.domain.CardModel;
import com.ruoyi.trial.service.ICardModelService;
import com.ruoyi.common.utils.poi.ExcelUtil;
import com.ruoyi.common.core.page.TableDataInfo;

/**
 * 卡片信息模版Controller
 * 
 * @author ruoyi
 * @date 2023-07-13
 */
@RestController
@RequestMapping("/card/model")
public class CardModelController extends BaseController
{
    @Autowired
    private ICardModelService cardModelService;

    /**
     * 查询卡片信息模版列表
     */
    @PreAuthorize("@ss.hasPermi('card:model:list')")
    @GetMapping("/list")
    public TableDataInfo list(CardModel cardModel)
    {
        startPage();
        List<CardModel> list = cardModelService.selectCardModelList(cardModel);
        return getDataTable(list);
    }

    /**
     * 导出卡片信息模版列表
     */
    @PreAuthorize("@ss.hasPermi('card:model:export')")
    @Log(title = "卡片信息模版", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, CardModel cardModel)
    {
        List<CardModel> list = cardModelService.selectCardModelList(cardModel);
        ExcelUtil<CardModel> util = new ExcelUtil<CardModel>(CardModel.class);
        util.exportExcel(response, list, "卡片信息模版数据");
    }

    /**
     * 获取卡片信息模版详细信息
     */
    @PreAuthorize("@ss.hasPermi('card:model:query')")
    @GetMapping(value = "/{cardCode}")
    public AjaxResult getInfo(@PathVariable("cardCode") Long cardCode)
    {
        return success(cardModelService.selectCardModelByCardCode(cardCode));
    }

    /**
     * 新增卡片信息模版
     */
    @PreAuthorize("@ss.hasPermi('card:model:add')")
    @Log(title = "卡片信息模版", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody CardModel cardModel)
    {
        return toAjax(cardModelService.insertCardModel(cardModel));
    }

    /**
     * 修改卡片信息模版
     */
    @PreAuthorize("@ss.hasPermi('card:model:edit')")
    @Log(title = "卡片信息模版", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody CardModel cardModel)
    {
        return toAjax(cardModelService.updateCardModel(cardModel));
    }

    /**
     * 删除卡片信息模版
     */
    @PreAuthorize("@ss.hasPermi('card:model:remove')")
    @Log(title = "卡片信息模版", businessType = BusinessType.DELETE)
	@DeleteMapping("/{cardCodes}")
    public AjaxResult remove(@PathVariable Long[] cardCodes)
    {
        return toAjax(cardModelService.deleteCardModelByCardCodes(cardCodes));
    }

}
