package com.ruoyi.trial.service;

import java.util.List;
import com.ruoyi.trial.domain.CardTableHeader;

/**
 * 卡片头模版Service接口
 * 
 * @author ruoyi
 * @date 2023-07-13
 */
public interface ICardTableHeaderService 
{
    /**
     * 查询卡片头模版
     * 
     * @param headerCode 卡片头模版主键
     * @return 卡片头模版
     */
    public CardTableHeader selectCardTableHeaderByHeaderCode(Long headerCode);

    /**
     * 查询卡片头模版列表
     * 
     * @param cardTableHeader 卡片头模版
     * @return 卡片头模版集合
     */
    public List<CardTableHeader> selectCardTableHeaderList(CardTableHeader cardTableHeader);

    /**
     * 新增卡片头模版
     * 
     * @param cardTableHeader 卡片头模版
     * @return 结果
     */
    public int insertCardTableHeader(CardTableHeader cardTableHeader);

    /**
     * 修改卡片头模版
     * 
     * @param cardTableHeader 卡片头模版
     * @return 结果
     */
    public int updateCardTableHeader(CardTableHeader cardTableHeader);

    /**
     * 批量删除卡片头模版
     * 
     * @param headerCodes 需要删除的卡片头模版主键集合
     * @return 结果
     */
    public int deleteCardTableHeaderByHeaderCodes(Long[] headerCodes);

    /**
     * 删除卡片头模版信息
     * 
     * @param headerCode 卡片头模版主键
     * @return 结果
     */
    public int deleteCardTableHeaderByHeaderCode(Long headerCode);
}
