package com.ruoyi.order.mapper;

import java.util.List;
import com.ruoyi.order.domain.ProductSpu;

/**
 * 菜品SPU Mapper接口
 */
public interface ProductSpuMapper
{
    ProductSpu selectProductSpuById(Long id);

    List<ProductSpu> selectOnSaleByCategoryId(Long categoryId);
}
