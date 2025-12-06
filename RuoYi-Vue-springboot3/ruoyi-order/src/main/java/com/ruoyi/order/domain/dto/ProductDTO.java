package com.ruoyi.order.domain.dto;

import java.util.List;

public class ProductDTO
{
    private Long spuId;

    private String name;

    private String description;

    private String imageUrl;

    private List<SkuDTO> skus;

    public Long getSpuId()
    {
        return spuId;
    }

    public void setSpuId(Long spuId)
    {
        this.spuId = spuId;
    }

    public String getName()
    {
        return name;
    }

    public void setName(String name)
    {
        this.name = name;
    }

    public String getDescription()
    {
        return description;
    }

    public void setDescription(String description)
    {
        this.description = description;
    }

    public String getImageUrl()
    {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl)
    {
        this.imageUrl = imageUrl;
    }

    public List<SkuDTO> getSkus()
    {
        return skus;
    }

    public void setSkus(List<SkuDTO> skus)
    {
        this.skus = skus;
    }
}
