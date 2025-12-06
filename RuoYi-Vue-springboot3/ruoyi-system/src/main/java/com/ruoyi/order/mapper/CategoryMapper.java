package com.ruoyi.order.mapper;

import java.util.List;
import com.ruoyi.order.domain.Category;

/**
 * 分类Mapper接口
 */
public interface CategoryMapper
{
    List<Category> selectByStoreId(Long storeId);
}
