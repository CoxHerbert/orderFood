package com.ruoyi.order.mapper;

import java.util.List;
import com.ruoyi.order.domain.ProductSku;

/**
 * 菜品SKU Mapper接口
 */
public interface ProductSkuMapper
{
    ProductSku selectProductSkuById(Long id);

    List<ProductSku> selectAvailableBySpuId(Long spuId);
}
