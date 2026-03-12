package com.ruoyi.trial.service;

import java.util.List;
import com.ruoyi.trial.domain.CardType;

/**
 * 卡片类型Service接口
 * 
 * @author ruoyi
 * @date 2023-07-13
 */
public interface ICardTypeService 
{
    /**
     * 查询卡片类型
     * 
     * @param cardId 卡片类型主键
     * @return 卡片类型
     */
    public CardType selectCardTypeByCardId(Long cardId);

    /**
     * 查询卡片类型列表
     * 
     * @param cardType 卡片类型
     * @return 卡片类型集合
     */
    public List<CardType> selectCardTypeList(CardType cardType);

    /**
     * 新增卡片类型
     * 
     * @param cardType 卡片类型
     * @return 结果
     */
    public int insertCardType(CardType cardType);

    /**
     * 修改卡片类型
     * 
     * @param cardType 卡片类型
     * @return 结果
     */
    public int updateCardType(CardType cardType);

    /**
     * 批量删除卡片类型
     * 
     * @param cardIds 需要删除的卡片类型主键集合
     * @return 结果
     */
    public int deleteCardTypeByCardIds(Long[] cardIds);

    /**
     * 删除卡片类型信息
     * 
     * @param cardId 卡片类型主键
     * @return 结果
     */
    public int deleteCardTypeByCardId(Long cardId);
}
