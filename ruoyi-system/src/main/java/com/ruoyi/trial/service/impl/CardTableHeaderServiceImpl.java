package com.ruoyi.trial.service.impl;

import java.util.List;
import com.ruoyi.common.utils.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.ruoyi.trial.mapper.CardTableHeaderMapper;
import com.ruoyi.trial.domain.CardTableHeader;
import com.ruoyi.trial.service.ICardTableHeaderService;

/**
 * 卡片头模版Service业务层处理
 * 
 * @author ruoyi
 * @date 2023-07-13
 */
@Service
public class CardTableHeaderServiceImpl implements ICardTableHeaderService 
{
    @Autowired
    private CardTableHeaderMapper cardTableHeaderMapper;

    /**
     * 查询卡片头模版
     * 
     * @param headerCode 卡片头模版主键
     * @return 卡片头模版
     */
    @Override
    public CardTableHeader selectCardTableHeaderByHeaderCode(Long headerCode)
    {
        return cardTableHeaderMapper.selectCardTableHeaderByHeaderCode(headerCode);
    }

    /**
     * 查询卡片头模版列表
     * 
     * @param cardTableHeader 卡片头模版
     * @return 卡片头模版
     */
    @Override
    public List<CardTableHeader> selectCardTableHeaderList(CardTableHeader cardTableHeader)
    {
        return cardTableHeaderMapper.selectCardTableHeaderList(cardTableHeader);
    }

    /**
     * 新增卡片头模版
     * 
     * @param cardTableHeader 卡片头模版
     * @return 结果
     */
    @Override
    public int insertCardTableHeader(CardTableHeader cardTableHeader)
    {
        cardTableHeader.setCreateTime(DateUtils.getNowDate());
        return cardTableHeaderMapper.insertCardTableHeader(cardTableHeader);
    }

    /**
     * 修改卡片头模版
     * 
     * @param cardTableHeader 卡片头模版
     * @return 结果
     */
    @Override
    public int updateCardTableHeader(CardTableHeader cardTableHeader)
    {
        cardTableHeader.setUpdateTime(DateUtils.getNowDate());
        return cardTableHeaderMapper.updateCardTableHeader(cardTableHeader);
    }

    /**
     * 批量删除卡片头模版
     * 
     * @param headerCodes 需要删除的卡片头模版主键
     * @return 结果
     */
    @Override
    public int deleteCardTableHeaderByHeaderCodes(Long[] headerCodes)
    {
        return cardTableHeaderMapper.deleteCardTableHeaderByHeaderCodes(headerCodes);
    }

    /**
     * 删除卡片头模版信息
     * 
     * @param headerCode 卡片头模版主键
     * @return 结果
     */
    @Override
    public int deleteCardTableHeaderByHeaderCode(Long headerCode)
    {
        return cardTableHeaderMapper.deleteCardTableHeaderByHeaderCode(headerCode);
    }
}
