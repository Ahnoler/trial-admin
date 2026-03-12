package com.ruoyi.trial.service.impl;

import java.util.List;
import com.ruoyi.common.utils.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.ruoyi.trial.mapper.CardColumnHeaderMapper;
import com.ruoyi.trial.domain.CardColumnHeader;
import com.ruoyi.trial.service.ICardColumnHeaderService;

/**
 * 卡片列头模版Service业务层处理
 * 
 * @author ruoyi
 * @date 2023-07-13
 */
@Service
public class CardColumnHeaderServiceImpl implements ICardColumnHeaderService 
{
    @Autowired
    private CardColumnHeaderMapper cardColumnHeaderMapper;

    /**
     * 查询卡片列头模版
     * 
     * @param columnCode 卡片列头模版主键
     * @return 卡片列头模版
     */
    @Override
    public CardColumnHeader selectCardColumnHeaderByColumnCode(Long columnCode)
    {
        return cardColumnHeaderMapper.selectCardColumnHeaderByColumnCode(columnCode);
    }

    /**
     * 查询卡片列头模版列表
     * 
     * @param cardColumnHeader 卡片列头模版
     * @return 卡片列头模版
     */
    @Override
    public List<CardColumnHeader> selectCardColumnHeaderList(CardColumnHeader cardColumnHeader)
    {
        return cardColumnHeaderMapper.selectCardColumnHeaderList(cardColumnHeader);
    }

    /**
     * 新增卡片列头模版
     * 
     * @param cardColumnHeader 卡片列头模版
     * @return 结果
     */
    @Override
    public int insertCardColumnHeader(CardColumnHeader cardColumnHeader)
    {
        cardColumnHeader.setCreateTime(DateUtils.getNowDate());
        return cardColumnHeaderMapper.insertCardColumnHeader(cardColumnHeader);
    }

    /**
     * 修改卡片列头模版
     * 
     * @param cardColumnHeader 卡片列头模版
     * @return 结果
     */
    @Override
    public int updateCardColumnHeader(CardColumnHeader cardColumnHeader)
    {
        cardColumnHeader.setUpdateTime(DateUtils.getNowDate());
        return cardColumnHeaderMapper.updateCardColumnHeader(cardColumnHeader);
    }

    /**
     * 批量删除卡片列头模版
     * 
     * @param columnCodes 需要删除的卡片列头模版主键
     * @return 结果
     */
    @Override
    public int deleteCardColumnHeaderByColumnCodes(Long[] columnCodes)
    {
        return cardColumnHeaderMapper.deleteCardColumnHeaderByColumnCodes(columnCodes);
    }

    /**
     * 删除卡片列头模版信息
     * 
     * @param columnCode 卡片列头模版主键
     * @return 结果
     */
    @Override
    public int deleteCardColumnHeaderByColumnCode(Long columnCode)
    {
        return cardColumnHeaderMapper.deleteCardColumnHeaderByColumnCode(columnCode);
    }
}
