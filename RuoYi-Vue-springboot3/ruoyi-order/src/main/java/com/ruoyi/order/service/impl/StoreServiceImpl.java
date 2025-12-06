package com.ruoyi.order.service.impl;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.ruoyi.order.mapper.StoreMapper;
import com.ruoyi.order.domain.Store;
import com.ruoyi.order.service.IStoreService;

/**
 * 门店Service业务层处理
 */
@Service
public class StoreServiceImpl implements IStoreService
{
    @Autowired
    private StoreMapper storeMapper;

    @Override
    public Store selectStoreById(Long id)
    {
        return storeMapper.selectStoreById(id);
    }

    @Override
    public List<Store> selectStoreList(Store store)
    {
        return storeMapper.selectStoreList(store);
    }

    @Override
    public int insertStore(Store store)
    {
        return storeMapper.insertStore(store);
    }

    @Override
    public int updateStore(Store store)
    {
        return storeMapper.updateStore(store);
    }

    @Override
    public int deleteStoreByIds(Long[] ids)
    {
        return storeMapper.deleteStoreByIds(ids);
    }

    @Override
    public int deleteStoreById(Long id)
    {
        return storeMapper.deleteStoreById(id);
    }
}
