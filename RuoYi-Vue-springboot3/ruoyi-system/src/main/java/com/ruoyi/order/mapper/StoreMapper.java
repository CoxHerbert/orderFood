package com.ruoyi.order.mapper;

import java.util.List;
import com.ruoyi.order.domain.Store;

/**
 * 门店Mapper接口
 */
public interface StoreMapper
{
    /**
     * 查询门店
     *
     * @param id 门店主键
     * @return 门店
     */
    Store selectStoreById(Long id);

    /**
     * 查询门店列表
     *
     * @param store 门店
     * @return 门店集合
     */
    List<Store> selectStoreList(Store store);

    /**
     * 新增门店
     *
     * @param store 门店
     * @return 结果
     */
    int insertStore(Store store);

    /**
     * 修改门店
     *
     * @param store 门店
     * @return 结果
     */
    int updateStore(Store store);

    /**
     * 删除门店
     *
     * @param id 门店主键
     * @return 结果
     */
    int deleteStoreById(Long id);

    /**
     * 批量删除门店
     *
     * @param ids 需要删除的数据主键集合
     * @return 结果
     */
    int deleteStoreByIds(Long[] ids);
}
