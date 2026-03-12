package com.ruoyi.trial.service.impl;

import java.util.List;
import com.ruoyi.common.utils.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.ruoyi.trial.mapper.CardModelMapper;
import com.ruoyi.trial.domain.CardModel;
import com.ruoyi.trial.service.ICardModelService;

/**
 * 卡片信息模版Service业务层处理
 * 
 * @author ruoyi
 * @date 2023-07-13
 */
@Service
public class CardModelServiceImpl implements ICardModelService 
{
    @Autowired
    private CardModelMapper cardModelMapper;

    /**
     * 查询卡片信息模版
     * 
     * @param cardCode 卡片信息模版主键
     * @return 卡片信息模版
     */
    @Override
    public CardModel selectCardModelByCardCode(Long cardCode)
    {
        return cardModelMapper.selectCardModelByCardCode(cardCode);
    }

    /**
     * 查询卡片信息模版列表
     * 
     * @param cardModel 卡片信息模版
     * @return 卡片信息模版
     */
    @Override
    public List<CardModel> selectCardModelList(CardModel cardModel)
    {
        return cardModelMapper.selectCardModelList(cardModel);
    }

    /**
     * 新增卡片信息模版
     * 
     * @param cardModel 卡片信息模版
     * @return 结果
     */
    @Override
    public int insertCardModel(CardModel cardModel)
    {
        cardModel.setCreateTime(DateUtils.getNowDate());
        return cardModelMapper.insertCardModel(cardModel);
    }

    /**
     * 修改卡片信息模版
     * 
     * @param cardModel 卡片信息模版
     * @return 结果
     */
    @Override
    public int updateCardModel(CardModel cardModel)
    {
        cardModel.setUpdateTime(DateUtils.getNowDate());
        return cardModelMapper.updateCardModel(cardModel);
    }

    /**
     * 批量删除卡片信息模版
     * 
     * @param cardCodes 需要删除的卡片信息模版主键
     * @return 结果
     */
    @Override
    public int deleteCardModelByCardCodes(Long[] cardCodes)
    {
        return cardModelMapper.deleteCardModelByCardCodes(cardCodes);
    }

    /**
     * 删除卡片信息模版信息
     * 
     * @param cardCode 卡片信息模版主键
     * @return 结果
     */
    @Override
    public int deleteCardModelByCardCode(Long cardCode)
    {
        return cardModelMapper.deleteCardModelByCardCode(cardCode);
    }
}
