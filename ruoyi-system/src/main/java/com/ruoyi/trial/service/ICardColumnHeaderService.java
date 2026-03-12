package com.ruoyi.trial.service;

import java.util.List;
import com.ruoyi.trial.domain.CardColumnHeader;

/**
 * 卡片列头模版Service接口
 * 
 * @author ruoyi
 * @date 2023-07-13
 */
public interface ICardColumnHeaderService 
{
    /**
     * 查询卡片列头模版
     * 
     * @param columnCode 卡片列头模版主键
     * @return 卡片列头模版
     */
    public CardColumnHeader selectCardColumnHeaderByColumnCode(Long columnCode);

    /**
     * 查询卡片列头模版列表
     * 
     * @param cardColumnHeader 卡片列头模版
     * @return 卡片列头模版集合
     */
    public List<CardColumnHeader> selectCardColumnHeaderList(CardColumnHeader cardColumnHeader);

    /**
     * 新增卡片列头模版
     * 
     * @param cardColumnHeader 卡片列头模版
     * @return 结果
     */
    public int insertCardColumnHeader(CardColumnHeader cardColumnHeader);

    /**
     * 修改卡片列头模版
     * 
     * @param cardColumnHeader 卡片列头模版
     * @return 结果
     */
    public int updateCardColumnHeader(CardColumnHeader cardColumnHeader);

    /**
     * 批量删除卡片列头模版
     * 
     * @param columnCodes 需要删除的卡片列头模版主键集合
     * @return 结果
     */
    public int deleteCardColumnHeaderByColumnCodes(Long[] columnCodes);

    /**
     * 删除卡片列头模版信息
     * 
     * @param columnCode 卡片列头模版主键
     * @return 结果
     */
    public int deleteCardColumnHeaderByColumnCode(Long columnCode);
}
