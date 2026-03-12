package com.ruoyi.trial.service.impl;

import java.util.List;
import com.ruoyi.common.utils.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.ruoyi.trial.mapper.CardTypeMapper;
import com.ruoyi.trial.domain.CardType;
import com.ruoyi.trial.service.ICardTypeService;

/**
 * 卡片类型Service业务层处理
 * 
 * @author ruoyi
 * @date 2023-07-13
 */
@Service
public class CardTypeServiceImpl implements ICardTypeService 
{
    @Autowired
    private CardTypeMapper cardTypeMapper;

    /**
     * 查询卡片类型
     * 
     * @param cardId 卡片类型主键
     * @return 卡片类型
     */
    @Override
    public CardType selectCardTypeByCardId(Long cardId)
    {
        return cardTypeMapper.selectCardTypeByCardId(cardId);
    }

    /**
     * 查询卡片类型列表
     * 
     * @param cardType 卡片类型
     * @return 卡片类型
     */
    @Override
    public List<CardType> selectCardTypeList(CardType cardType)
    {
        return cardTypeMapper.selectCardTypeList(cardType);
    }

    /**
     * 新增卡片类型
     * 
     * @param cardType 卡片类型
     * @return 结果
     */
    @Override
    public int insertCardType(CardType cardType)
    {
        cardType.setCreateTime(DateUtils.getNowDate());
        return cardTypeMapper.insertCardType(cardType);
    }

    /**
     * 修改卡片类型
     * 
     * @param cardType 卡片类型
     * @return 结果
     */
    @Override
    public int updateCardType(CardType cardType)
    {
        cardType.setUpdateTime(DateUtils.getNowDate());
        return cardTypeMapper.updateCardType(cardType);
    }

    /**
     * 批量删除卡片类型
     * 
     * @param cardIds 需要删除的卡片类型主键
     * @return 结果
     */
    @Override
    public int deleteCardTypeByCardIds(Long[] cardIds)
    {
        return cardTypeMapper.deleteCardTypeByCardIds(cardIds);
    }

    /**
     * 删除卡片类型信息
     * 
     * @param cardId 卡片类型主键
     * @return 结果
     */
    @Override
    public int deleteCardTypeByCardId(Long cardId)
    {
        return cardTypeMapper.deleteCardTypeByCardId(cardId);
    }
}
