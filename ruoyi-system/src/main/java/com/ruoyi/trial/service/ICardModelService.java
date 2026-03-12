package com.ruoyi.trial.service;

import java.util.List;
import com.ruoyi.trial.domain.CardModel;

/**
 * 卡片信息模版Service接口
 * 
 * @author ruoyi
 * @date 2023-07-13
 */
public interface ICardModelService 
{
    /**
     * 查询卡片信息模版
     * 
     * @param cardCode 卡片信息模版主键
     * @return 卡片信息模版
     */
    public CardModel selectCardModelByCardCode(Long cardCode);

    /**
     * 查询卡片信息模版列表
     * 
     * @param cardModel 卡片信息模版
     * @return 卡片信息模版集合
     */
    public List<CardModel> selectCardModelList(CardModel cardModel);

    /**
     * 新增卡片信息模版
     * 
     * @param cardModel 卡片信息模版
     * @return 结果
     */
    public int insertCardModel(CardModel cardModel);

    /**
     * 修改卡片信息模版
     * 
     * @param cardModel 卡片信息模版
     * @return 结果
     */
    public int updateCardModel(CardModel cardModel);

    /**
     * 批量删除卡片信息模版
     * 
     * @param cardCodes 需要删除的卡片信息模版主键集合
     * @return 结果
     */
    public int deleteCardModelByCardCodes(Long[] cardCodes);

    /**
     * 删除卡片信息模版信息
     * 
     * @param cardCode 卡片信息模版主键
     * @return 结果
     */
    public int deleteCardModelByCardCode(Long cardCode);
}
