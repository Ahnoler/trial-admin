package com.ruoyi.trial.mapper;

import java.util.List;
import com.ruoyi.trial.domain.CardModelDetailProd;

/**
 * 卡片程序模版Mapper接口
 * 
 * @author ruoyi
 * @date 2023-07-13
 */
public interface CardModelDetailProdMapper 
{
    /**
     * 查询卡片程序模版
     * 
     * @param id 卡片程序模版主键
     * @return 卡片程序模版
     */
    public CardModelDetailProd selectCardModelDetailProdById(Long id);

    /**
     * 查询卡片程序模版列表
     * 
     * @param cardModelDetailProd 卡片程序模版
     * @return 卡片程序模版集合
     */
    public List<CardModelDetailProd> selectCardModelDetailProdList(CardModelDetailProd cardModelDetailProd);

    /**
     * 新增卡片程序模版
     * 
     * @param cardModelDetailProd 卡片程序模版
     * @return 结果
     */
    public int insertCardModelDetailProd(CardModelDetailProd cardModelDetailProd);

    /**
     * 修改卡片程序模版
     * 
     * @param cardModelDetailProd 卡片程序模版
     * @return 结果
     */
    public int updateCardModelDetailProd(CardModelDetailProd cardModelDetailProd);

    /**
     * 删除卡片程序模版
     * 
     * @param id 卡片程序模版主键
     * @return 结果
     */
    public int deleteCardModelDetailProdById(Long id);

    /**
     * 批量删除卡片程序模版
     * 
     * @param ids 需要删除的数据主键集合
     * @return 结果
     */
    public int deleteCardModelDetailProdByIds(Long[] ids);
}
