package com.ruoyi.order.service.impl;

import java.util.ArrayList;
import java.util.List;
import org.springframework.stereotype.Service;
import com.ruoyi.order.domain.Category;
import com.ruoyi.order.domain.ProductSku;
import com.ruoyi.order.domain.ProductSpu;
import com.ruoyi.order.domain.dto.CategoryMenuDTO;
import com.ruoyi.order.domain.dto.ProductDTO;
import com.ruoyi.order.domain.dto.SkuDTO;
import com.ruoyi.order.mapper.CategoryMapper;
import com.ruoyi.order.mapper.ProductSkuMapper;
import com.ruoyi.order.mapper.ProductSpuMapper;
import com.ruoyi.order.service.IUserMenuService;

@Service
public class UserMenuServiceImpl implements IUserMenuService
{
    private final CategoryMapper categoryMapper;

    private final ProductSpuMapper productSpuMapper;

    private final ProductSkuMapper productSkuMapper;

    public UserMenuServiceImpl(CategoryMapper categoryMapper, ProductSpuMapper productSpuMapper, ProductSkuMapper productSkuMapper)
    {
        this.categoryMapper = categoryMapper;
        this.productSpuMapper = productSpuMapper;
        this.productSkuMapper = productSkuMapper;
    }

    @Override
    public List<CategoryMenuDTO> getStoreMenu(Long storeId)
    {
        List<CategoryMenuDTO> result = new ArrayList<>();
        List<Category> categories = categoryMapper.selectByStoreId(storeId);
        for (Category category : categories)
        {
            CategoryMenuDTO dto = new CategoryMenuDTO();
            dto.setCategoryId(category.getId());
            dto.setCategoryName(category.getName());

            List<ProductSpu> spus = productSpuMapper.selectOnSaleByCategoryId(category.getId());
            List<ProductDTO> productDTOS = new ArrayList<>();
            for (ProductSpu spu : spus)
            {
                ProductDTO productDTO = new ProductDTO();
                productDTO.setSpuId(spu.getId());
                productDTO.setName(spu.getName());
                productDTO.setDescription(spu.getDescription());
                productDTO.setImageUrl(spu.getImageUrl());

                List<ProductSku> skus = productSkuMapper.selectAvailableBySpuId(spu.getId());
                List<SkuDTO> skuDTOS = new ArrayList<>();
                for (ProductSku sku : skus)
                {
                    SkuDTO skuDTO = new SkuDTO();
                    skuDTO.setSkuId(sku.getId());
                    skuDTO.setSkuName(sku.getSkuName());
                    skuDTO.setPrice(sku.getPrice());
                    skuDTOS.add(skuDTO);
                }
                productDTO.setSkus(skuDTOS);
                productDTOS.add(productDTO);
            }
            dto.setProducts(productDTOS);
            result.add(dto);
        }
        return result;
    }
}
