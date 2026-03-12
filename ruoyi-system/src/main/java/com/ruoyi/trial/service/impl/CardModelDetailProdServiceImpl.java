package com.ruoyi.trial.service.impl;

import java.util.List;
import com.ruoyi.common.utils.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.ruoyi.trial.mapper.CardModelDetailProdMapper;
import com.ruoyi.trial.domain.CardModelDetailProd;
import com.ruoyi.trial.service.ICardModelDetailProdService;

/**
 * 卡片程序模版Service业务层处理
 * 
 * @author ruoyi
 * @date 2023-07-13
 */
@Service
public class CardModelDetailProdServiceImpl implements ICardModelDetailProdService 
{
    @Autowired
    private CardModelDetailProdMapper cardModelDetailProdMapper;

    /**
     * 查询卡片程序模版
     * 
     * @param id 卡片程序模版主键
     * @return 卡片程序模版
     */
    @Override
    public CardModelDetailProd selectCardModelDetailProdById(Long id)
    {
        return cardModelDetailProdMapper.selectCardModelDetailProdById(id);
    }

    /**
     * 查询卡片程序模版列表
     * 
     * @param cardModelDetailProd 卡片程序模版
     * @return 卡片程序模版
     */
    @Override
    public List<CardModelDetailProd> selectCardModelDetailProdList(CardModelDetailProd cardModelDetailProd)
    {
        return cardModelDetailProdMapper.selectCardModelDetailProdList(cardModelDetailProd);
    }

    /**
     * 新增卡片程序模版
     * 
     * @param cardModelDetailProd 卡片程序模版
     * @return 结果
     */
    @Override
    public int insertCardModelDetailProd(CardModelDetailProd cardModelDetailProd)
    {
        cardModelDetailProd.setCreateTime(DateUtils.getNowDate());
        return cardModelDetailProdMapper.insertCardModelDetailProd(cardModelDetailProd);
    }

    /**
     * 修改卡片程序模版
     * 
     * @param cardModelDetailProd 卡片程序模版
     * @return 结果
     */
    @Override
    public int updateCardModelDetailProd(CardModelDetailProd cardModelDetailProd)
    {
        cardModelDetailProd.setUpdateTime(DateUtils.getNowDate());
        return cardModelDetailProdMapper.updateCardModelDetailProd(cardModelDetailProd);
    }

    /**
     * 批量删除卡片程序模版
     * 
     * @param ids 需要删除的卡片程序模版主键
     * @return 结果
     */
    @Override
    public int deleteCardModelDetailProdByIds(Long[] ids)
    {
        return cardModelDetailProdMapper.deleteCardModelDetailProdByIds(ids);
    }

    /**
     * 删除卡片程序模版信息
     * 
     * @param id 卡片程序模版主键
     * @return 结果
     */
    @Override
    public int deleteCardModelDetailProdById(Long id)
    {
        return cardModelDetailProdMapper.deleteCardModelDetailProdById(id);
    }
}
